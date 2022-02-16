package com.fleet.startplan.TouchHelperAdapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.fleet.startplan.R;


enum ScheduleButtonsState {GONE, LEFT_VISIBLE, RIGHT_VISIBLE}

public class ScheduleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ScheduleItemTouchHelperListener listener;
    private boolean swipeBack = false;
    private ScheduleButtonsState scheduleButtonsState = ScheduleButtonsState.GONE;
    private float buttonWidth = 300;
    private RectF buttonInstance = null;
    private RecyclerView.ViewHolder currentItemViewHolder = null;

    public ScheduleItemTouchHelperCallback(ScheduleItemTouchHelperListener listener) {
        this.listener = listener;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int drag_flags;
        int swipe_flags;

        int st = viewHolder.getItemViewType();

        drag_flags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        if (st == 2 || st == 3 || st == 4 || st == 5 || st == 6) {
            swipe_flags = ItemTouchHelper.END;
        } else {
            swipe_flags = ItemTouchHelper.START | ItemTouchHelper.END;
        }

        return makeMovementFlags(drag_flags, swipe_flags);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onItemSwipe(viewHolder.getAdapterPosition());
    }

//    @Override
//    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
//        super.onSelectedChanged(viewHolder, actionState);
//
//    }

    //아이템을 터치하거나 스와이프하거나 뷰에 변화가 생길경우 불러오는 함수
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //아이템이 스와이프 됐을경우 버튼을 그려주기 위해서 스와이프가 됐는지 확인

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (scheduleButtonsState != ScheduleButtonsState.GONE) {
                if (scheduleButtonsState == ScheduleButtonsState.LEFT_VISIBLE)
                    dX = Math.max(dX, buttonWidth);
                if (scheduleButtonsState == ScheduleButtonsState.RIGHT_VISIBLE)
                    dX = Math.min(dX, -buttonWidth);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            } else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            if (scheduleButtonsState == ScheduleButtonsState.GONE) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
        currentItemViewHolder = viewHolder;
        //버튼을 그려주는 함수
        drawButtons(c, currentItemViewHolder);
    }

    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
        float buttonWidthWithOutPadding = buttonWidth + 10;
        float corners = 5;
        View itemView = viewHolder.itemView;
        Paint p = new Paint();
        buttonInstance = null;
        //오른쪽으로 스와이프 했을때 (왼쪽에 버튼이 보여지게 될 경우)
        if (scheduleButtonsState == ScheduleButtonsState.LEFT_VISIBLE) {

            RectF leftButton = new RectF();
            leftButton.set(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + buttonWidthWithOutPadding, itemView.getBottom());
            p.setColor(Color.parseColor("#ffffffff"));
            c.drawRoundRect(leftButton, corners, corners, p);
            Bitmap bt = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                bt = getBitmap(itemView.getContext(), R.drawable.ic_main_swipe_menu_edit_24dp);
            }
            int w = bt.getWidth();
            int h = bt.getHeight();
            c.drawBitmap(bt, leftButton.centerX() - (w / 2), leftButton.centerY() - (h / 2), p);
            buttonInstance = leftButton;
        } else if (scheduleButtonsState == ScheduleButtonsState.RIGHT_VISIBLE) {
            RectF rightButton = new RectF();

            rightButton.set(itemView.getRight() - buttonWidthWithOutPadding, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            p.setColor(Color.parseColor("#ffffffff"));
            c.drawRoundRect(rightButton, corners, corners, p);
            Bitmap bt = null;
            bt = getBitmap(itemView.getContext(), R.drawable.ic_main_swipe_menu_completed_24dp);
            int w = bt.getWidth();
            int h = bt.getHeight();
            c.drawBitmap(bt, rightButton.centerX() - (w / 2), rightButton.centerY() - (h / 2), p);
            buttonInstance = rightButton;
        }


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static Bitmap getBitmap(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable instanceof BitmapDrawable) {
            return BitmapFactory.decodeResource(context.getResources(), drawableId);
        } else if (drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = false;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }


    private void setTouchListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                if (swipeBack) {
                    if (dX < -buttonWidth)
                        scheduleButtonsState = ScheduleButtonsState.RIGHT_VISIBLE;
                    else if (dX > buttonWidth)
                        scheduleButtonsState = ScheduleButtonsState.LEFT_VISIBLE;
                    if (scheduleButtonsState != ScheduleButtonsState.GONE) {
                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemsClickable(recyclerView, false);
                    }
                }
                return false;
            }
        });
    }

    private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
                return false;
            }
        });
    }

    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ScheduleItemTouchHelperCallback.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                recyclerView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
                setItemsClickable(recyclerView, true);
                swipeBack = false;
                if (listener != null && buttonInstance != null && buttonInstance.contains(event.getX(), event.getY())) {
                    if (scheduleButtonsState == ScheduleButtonsState.LEFT_VISIBLE) {
                        listener.onLeftClick(viewHolder.getAdapterPosition(), viewHolder);
                    } else if (scheduleButtonsState == ScheduleButtonsState.RIGHT_VISIBLE) {
                        listener.onRightClick(viewHolder.getAdapterPosition(), viewHolder);
                    }
                }
                scheduleButtonsState = ScheduleButtonsState.GONE;
                currentItemViewHolder = null;
                return false;
            }
        });
    }

    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }

}
