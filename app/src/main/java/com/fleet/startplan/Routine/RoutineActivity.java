package com.fleet.startplan.Routine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fleet.startplan.Adapter.RoutineAdapter;
import com.fleet.startplan.DB.SPDBHelper;
import com.fleet.startplan.Dialog.DialogAdd;
import com.fleet.startplan.Dialog.DialogPremium;
import com.fleet.startplan.Model.Information;
import com.fleet.startplan.Model.Products;
import com.fleet.startplan.Schedule.ScheduleList;
import com.fleet.startplan.Model.ScheduleItem;
import com.fleet.startplan.R;
import com.fleet.startplan.SharedPreference.PreferenceManager;
import com.fleet.startplan.TouchHelperAdapter.OnSingleClickListener;
import com.fleet.startplan.TouchHelperAdapter.RoutineItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.Collections;

public class RoutineActivity extends AppCompatActivity {

    private TextView mTopBarEmoji, mTopBarContents;
    private int routineItemID;
    private SPDBHelper mDBHelper;
    private String mRegisterDate = "";
    private RoutineAdapter mRoutineAdapter;
    private EditText mRoutineEditText;
    private ImageView mRoutineComplete;
    private static ArrayList<RoutineItem> routineItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);
        mDBHelper = new SPDBHelper(getApplicationContext().getApplicationContext());

        Intent intent = getIntent();
        if (intent != null) {
            routineItemID = intent.getIntExtra(ScheduleList.ROUTINE_TODO_ITEM_ID, 0);
        }

        ImageView mTopBarExitBtn = findViewById(R.id.iv_routine_exit);
        ImageView mRoutineStorage = findViewById(R.id.iv_routine_storage);

        mTopBarEmoji = findViewById(R.id.tv_routine_title_emoji);
        mTopBarContents = findViewById(R.id.tv_routine_title_contents);
        mRoutineEditText = findViewById(R.id.et_routine_input);
        mRoutineComplete = findViewById(R.id.iv_routine_complete);
        RecyclerView mRecyclerView = findViewById(R.id.rv_routine);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        routineItems = mDBHelper.getRoutineTodoItems(routineItemID);
        Collections.sort(routineItems, RoutineItem.setRoutineTodoItemList);
        mRoutineAdapter = new RoutineAdapter(routineItems);
        mRecyclerView.setAdapter(mRoutineAdapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new RoutineItemTouchHelperCallback(mRoutineAdapter));
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        setTopBarText();
        changeCompleteButton();
        checkCompleteBtn();

        mTopBarExitBtn.setOnClickListener(v -> {
            finish();
        });

        mRoutineStorage.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                initRoutineTodoStorage();
            }
        });
    }

    private void setTopBarText() {
        ArrayList<ScheduleItem> scItems = mDBHelper.getScheduleInfo(routineItemID);
        ScheduleItem scheduleItem = scItems.get(0);
        String emoji = scheduleItem.getScEmoji();
        String contents = scheduleItem.getScContents();
        mRegisterDate = scheduleItem.getScRegisteredDate();
        mTopBarEmoji.setText(emoji);
        mTopBarContents.setText(contents);
    }

    private void initRoutineTodoStorage() {
        DialogRoutineStorage dialogRoutineStorage = new DialogRoutineStorage();
        dialogRoutineStorage.setStyle(DialogAdd.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        dialogRoutineStorage.show(getSupportFragmentManager(), "RoutineTodoStorage");
        dialogRoutineStorage.setOnStorageRoutineTodoAddListener(new DialogRoutineStorage.OnStorageRoutineTodoAddListener() {
            @Override
            public void clickAdd() {
                addData();
            }
        });
        Bundle bundle = new Bundle();
        bundle.putInt(ScheduleList.ROUTINE_TODO_ITEM_ID, routineItemID);
        try {
            dialogRoutineStorage.setArguments(bundle);
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "오류가 발생했어요. 죄송합니다. 다시 실행해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkCompleteBtn() {
        mRoutineComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRoutineEditText.getText().toString().equals("") || mRoutineEditText.getText() == null) {
                    Toast.makeText(getApplicationContext(), "세부계획을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    String mRoutineContents = "";
                    if (mRoutineEditText != null) {
                        mRoutineContents = mRoutineEditText.getText().toString();
                    }
                    int lastID = 0;
                    ArrayList<RoutineItem> rtItems = mDBHelper.getAllRoutineTodo();
                    if (rtItems.isEmpty()) {
                        lastID = 1;
                    } else {
                        RoutineItem item = rtItems.get(mDBHelper.getAllRoutineTodo().size() - 1);
                        lastID = item.getrTodoId() + 1;
                    }
                    boolean checkPremiumUser = PreferenceManager.getBoolean(getApplicationContext(), Products.PREMIUM_USER);
                    if (!checkPremiumUser) {
                        ArrayList<RoutineItem> routineItems = mDBHelper.getRoutineTodoItems(routineItemID);

                        if (routineItems.size() > 4) {
                            mRoutineEditText.clearFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mRoutineEditText.getWindowToken(), 0);
                            DialogPremium dialogPremium = new DialogPremium();
                            dialogPremium.setStyle(DialogAdd.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
                            dialogPremium.show(getSupportFragmentManager(), "dialogPremium");

                        } else {
                            save(lastID, mRoutineContents);
                        }
                    } else {
                        save(lastID, mRoutineContents);
                    }
                }
            }
        });
    }

    private void changeCompleteButton() {

        mRoutineEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mRoutineComplete.setImageResource(R.drawable.ic_routine_todo_complete_on_28dp);
                } else {
                    mRoutineComplete.setImageResource(R.drawable.ic_routine_todo_complete_off_28dp);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mRoutineComplete.setImageResource(R.drawable.ic_routine_todo_complete_on_28dp);
                } else {
                    mRoutineComplete.setImageResource(R.drawable.ic_routine_todo_complete_off_28dp);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    private void save(int lastID, String routineContents) {
        String parentContents = String.valueOf(mTopBarContents);
        saveRoutine(routineItemID, Information.ROUTINE_CATEGORY_TODO, lastID, 0, routineContents, mRegisterDate);
        saveStorageRoutine(routineContents, lastID, routineItemID, parentContents, mRegisterDate);
        Toast.makeText(getApplicationContext(), "세부계획이 추가됐어요.", Toast.LENGTH_SHORT).show();
        mRoutineEditText.setText(null);
    }


    private void saveRoutine(int itemID, String category, int pos, int complete, String contents, String registerDate) {
        mDBHelper.insertRoutine(itemID, category, pos, complete, contents, registerDate);
        addData();
    }

    private void saveStorageRoutine(String contents, int pos, int parentID, String parentContents, String parentRegisterDate) {
        mDBHelper.insertStorageRoutine(contents, pos, parentID, parentContents, parentRegisterDate);
        addData();
    }

    private void addData() {
        ArrayList<RoutineItem> routineItems = mDBHelper.getRoutineTodoItems(routineItemID);
        mRoutineAdapter.setItems(routineItems);
        mRoutineAdapter.notifyItemInserted(routineItems.size() + 1);
        mDBHelper.changeCategoryInRoutine(routineItemID, Information.CATEGORY_ROUTINE);
    }

}

