package com.fleet.startplan.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.fleet.startplan.Copy.CopyItem;
import com.fleet.startplan.Copy.PickDateItem;
import com.fleet.startplan.Model.AnalyticsItem;
import com.fleet.startplan.Model.AnalyticsListItem;
import com.fleet.startplan.Model.CompleteItem;
import com.fleet.startplan.Model.Information;
import com.fleet.startplan.Routine.StorageRoutineTodoItem;
import com.fleet.startplan.Routine.RoutineItem;
import com.fleet.startplan.Model.ScheduleItem;
import com.fleet.startplan.Model.StorageItem;
import com.fleet.startplan.SuperFocus.LockItem;
import com.fleet.startplan.SuperFocus.LockRoutineItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SPDBHelper extends SQLiteOpenHelper {

    private static SPDBHelper sInstance;

    private static final int DATABASE_VERSION = 2;

    private static final String DB_NAME = "SP.db";

    // TBL_SCHEDULE
    private static final String SQL_CREATE_SCHEDULE_ENTRIES =
            String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s INTEGER,  %s INTEGER,  %s INTEGER,  %s INTEGER,  %s INTEGER, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT)",
                    SPContract.ScheduleEntry.NAME_SCHEDULE,
                    SPContract.ScheduleEntry._ID,
                    SPContract.ScheduleEntry.COLUMN_SCHEDULE_EMOJI, // string
                    SPContract.ScheduleEntry.COLUMN_SCHEDULE_CONTENTS, // string
                    SPContract.ScheduleEntry.COLUMN_SCHEDULE_CATEGORY, // string
                    SPContract.ScheduleEntry.COLUMN_SCHEDULE_COMPLETE, // integer
                    SPContract.ScheduleEntry.COLUMN_SCHEDULE_COMPLETED_TIME, // string
                    SPContract.ScheduleEntry.COLUMN_SCHEDULE_LIST_POSITION, // integer
                    SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_START_HOUR, // integer
                    SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_START_MINUTE, // integer
                    SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_END_HOUR, // integer
                    SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_END_MINUTE, // integer
                    SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_START_DATE, // string
                    SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_END_DATE, // string
                    SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_COMPLETED, // integer
                    SPContract.ScheduleEntry.COLUMN_SCHEDULE_REGISTERED_DATE, // string
                    SPContract.ScheduleEntry.COLUMN_SCHEDULE_DELETED_DATE // string
            );

    //TBL_STORAGE
    private static final String SQL_CREATE_STORAGE_ENTRIES =
            String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s INTEGER )",
                    SPContract.StorageEntry.NAME_STORAGE,
                    SPContract.StorageEntry._ID,
                    SPContract.StorageEntry.COLUMN_STORAGE_CATEGORY, //string
                    SPContract.StorageEntry.COLUMN_STORAGE_CONTENTS, // string
                    SPContract.StorageEntry.COLUMN_STORAGE_EMOJI, // string
                    SPContract.StorageEntry.COLUMN_STORAGE_REGISTERED_DATE, // string
                    SPContract.StorageEntry.COLUMN_STORAGE_DELETED_DATE, // string
                    SPContract.StorageEntry.COLUMN_STORAGE_LIST_POSITION,  // integer
                    SPContract.StorageEntry.COLUMN_STORAGE_ADD_COUNT // integer
            );

    //TBL_ROUTINE_TODO
    private static final String SQL_CREATE_ROUTINE_TODO_ENTRIES =
            String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT )",
                    SPContract.RoutineTodoEntry.NAME_ROUTINE_TODO,
                    SPContract.RoutineTodoEntry._ID,
                    SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_ITEM_ID,
                    SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_CATEGORY,
                    SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_POSITION,
                    SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_COMPLETE,
                    SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_CONTENTS,
                    SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_REGISTER_DATE
            );

    //TBL_STORAGE_ROUTINE_TODO
    private static final String SQL_CREATE_STORAGE_ROUTINE_TODO_ENTRIES =
            String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT )",
                    SPContract.StorageRoutineTodoEntry.NAME_STORAGE_ROUTINE_TODO,
                    SPContract.StorageRoutineTodoEntry._ID,
                    SPContract.StorageRoutineTodoEntry.COLUMN_STORAGE_ROUTINE_TODO_CONTENTS, //string
                    SPContract.StorageRoutineTodoEntry.COLUMN_STORAGE_ROUTINE_TODO_LIST_POSITION, //int
                    SPContract.StorageRoutineTodoEntry.COLUMN_STORAGE_PARENTS_ID, //int
                    SPContract.StorageRoutineTodoEntry.COLUMN_STORAGE_PARENTS_CONTENTS, //string
                    SPContract.StorageRoutineTodoEntry.COLUMN_STORAGE_PARENTS_REGISTER_DATE //string
            );


    public static SPDBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SPDBHelper(context);
        }
        return sInstance;
    }

    public SPDBHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SCHEDULE_ENTRIES);
        db.execSQL(SQL_CREATE_STORAGE_ENTRIES);
        db.execSQL(SQL_CREATE_ROUTINE_TODO_ENTRIES);
        db.execSQL(SQL_CREATE_STORAGE_ROUTINE_TODO_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL(SQL_CREATE_ROUTINE_TODO_ENTRIES);
                db.execSQL(SQL_CREATE_STORAGE_ROUTINE_TODO_ENTRIES);
            case 2:
        }

    }


    // model class 연결 부분

    public ArrayList<ScheduleItem> getScheduleItems() {
        String sql = "select * from  " + SPContract.ScheduleEntry.NAME_SCHEDULE;

        SQLiteDatabase db = getReadableDatabase();

        ArrayList<ScheduleItem> scheduleItems = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int scId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry._ID)));
                @SuppressLint("Range") String scEmoji = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_EMOJI));
                @SuppressLint("Range") String scContents = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CONTENTS));
                @SuppressLint("Range") String scCategory = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CATEGORY));
                @SuppressLint("Range") int scComplete = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_COMPLETE)));
                @SuppressLint("Range") String scCompletedTime = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_COMPLETED_TIME));
                @SuppressLint("Range") int scListPosition = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_LIST_POSITION)));
                @SuppressLint("Range") int todoStartHour = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_START_HOUR)));
                @SuppressLint("Range") int todoStartMinute = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_START_MINUTE)));
                @SuppressLint("Range") int todoEndHour = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_END_HOUR)));
                @SuppressLint("Range") int todoEndMinute = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_END_MINUTE)));
                @SuppressLint("Range") String dDayStartDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_START_DATE));
                @SuppressLint("Range") String dDayEndDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_END_DATE));
                @SuppressLint("Range") int dDayCompleted = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_COMPLETED)));
                @SuppressLint("Range") String scRegisteredDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_REGISTERED_DATE));
                @SuppressLint("Range") String scDeletedDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_DELETED_DATE));
                @SuppressLint("Range") String scStandardDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_START_DATE));
                scheduleItems.add(new ScheduleItem(scId, scEmoji, scContents, scCategory, scComplete, scCompletedTime, scListPosition,
                        todoStartHour, todoStartMinute, todoEndHour, todoEndMinute, dDayStartDate, dDayEndDate, dDayCompleted, scRegisteredDate, scDeletedDate, scStandardDate));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return scheduleItems;
    }

    // model storage 랑 연결 되는 코드
    public ArrayList<StorageItem> getStorageItems() {
        String sql = "select * from " + SPContract.StorageEntry.NAME_STORAGE + " order by " + SPContract.StorageEntry.COLUMN_STORAGE_LIST_POSITION + " desc";
        SQLiteDatabase db = getReadableDatabase();

        ArrayList<StorageItem> storageItems = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int stId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.StorageEntry._ID)));
                @SuppressLint("Range") String stCategory = cursor.getString(cursor.getColumnIndex(SPContract.StorageEntry.COLUMN_STORAGE_CATEGORY));
                @SuppressLint("Range") String stContents = cursor.getString(cursor.getColumnIndex(SPContract.StorageEntry.COLUMN_STORAGE_CONTENTS));
                @SuppressLint("Range") String stEmoji = cursor.getString(cursor.getColumnIndex(SPContract.StorageEntry.COLUMN_STORAGE_EMOJI));
                @SuppressLint("Range") String stRegisteredDate = cursor.getString(cursor.getColumnIndex(SPContract.StorageEntry.COLUMN_STORAGE_REGISTERED_DATE));
                @SuppressLint("Range") String stDeletedDate = cursor.getString(cursor.getColumnIndex(SPContract.StorageEntry.COLUMN_STORAGE_REGISTERED_DATE));
                @SuppressLint("Range") int stListPosition = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.StorageEntry.COLUMN_STORAGE_LIST_POSITION)));
                @SuppressLint("Range") int stAddCount = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.StorageEntry.COLUMN_STORAGE_ADD_COUNT)));

                storageItems.add(new StorageItem(stId, stCategory, stContents, stEmoji, stRegisteredDate, stDeletedDate, stListPosition, stAddCount));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return storageItems;
    }

    public ArrayList<ScheduleItem> getScheduleInfo(int itemID) {
        String sql = "select * from " + SPContract.ScheduleEntry.NAME_SCHEDULE + " where " + SPContract.ScheduleEntry._ID + "=?";

        SQLiteDatabase db = getReadableDatabase();

        ArrayList<ScheduleItem> scheduleItems = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(itemID)});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int scId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry._ID)));
                @SuppressLint("Range") String scEmoji = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_EMOJI));
                @SuppressLint("Range") String scContents = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CONTENTS));
                @SuppressLint("Range") String scCategory = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CATEGORY));
                @SuppressLint("Range") int scComplete = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_COMPLETE)));
                @SuppressLint("Range") String scCompletedTime = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_COMPLETED_TIME));
                @SuppressLint("Range") int scListPosition = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_LIST_POSITION)));
                @SuppressLint("Range") int todoStartHour = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_START_HOUR)));
                @SuppressLint("Range") int todoStartMinute = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_START_MINUTE)));
                @SuppressLint("Range") int todoEndHour = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_END_HOUR)));
                @SuppressLint("Range") int todoEndMinute = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_END_MINUTE)));
                @SuppressLint("Range") String dDayStartDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_START_DATE));
                @SuppressLint("Range") String dDayEndDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_END_DATE));
                @SuppressLint("Range") int dDayCompleted = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_COMPLETED)));
                @SuppressLint("Range") String scRegisteredDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_REGISTERED_DATE));
                @SuppressLint("Range") String scDeletedDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_DELETED_DATE));
                @SuppressLint("Range") String scStandardDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_START_DATE));
                scheduleItems.add(new ScheduleItem(scId, scEmoji, scContents, scCategory, scComplete, scCompletedTime, scListPosition,
                        todoStartHour, todoStartMinute, todoEndHour, todoEndMinute, dDayStartDate, dDayEndDate, dDayCompleted, scRegisteredDate, scDeletedDate, scStandardDate));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return scheduleItems;
    }

    //routine Item 을 MODEL CLASS 에 담는 코드
    public ArrayList<RoutineItem> getRoutineTodoItems(int itemID) {
        String sql = "";
        Cursor cursor;
        sql = "select * from  " + SPContract.RoutineTodoEntry.NAME_ROUTINE_TODO + " where " + SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_ITEM_ID + "=?"
                + " order by " + SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_POSITION;
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.rawQuery(sql, new String[]{String.valueOf(itemID)});

        ArrayList<RoutineItem> routineItems = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int rTodoId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry._ID)));
                @SuppressLint("Range") int rTodoItemId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_ITEM_ID)));
                @SuppressLint("Range") String rTodoCategory = cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_CATEGORY));
                @SuppressLint("Range") int rTodoPos = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_POSITION)));
                @SuppressLint("Range") int rTodoComplete = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_COMPLETE)));
                @SuppressLint("Range") String rTodoContents = cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_CONTENTS));
                @SuppressLint("Range") String rTodoRegisterDate = cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_REGISTER_DATE));
                routineItems.add(new RoutineItem(rTodoId, rTodoItemId, rTodoCategory, rTodoPos, rTodoComplete, rTodoContents, rTodoRegisterDate));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return routineItems;
    }

    //routine Item 을 MODEL CLASS 에 담는 코드
    public ArrayList<LockRoutineItem> getLockRoutineItems(int itemID) {
        String sql = "";
        Cursor cursor;
        sql = "select * from  " + SPContract.RoutineTodoEntry.NAME_ROUTINE_TODO + " where " + SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_ITEM_ID + "=?"
                + " order by " + SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_POSITION;
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.rawQuery(sql, new String[]{String.valueOf(itemID)});

        ArrayList<LockRoutineItem> lockRoutineItems = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int rTodoId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry._ID)));
                @SuppressLint("Range") int rTodoItemId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_ITEM_ID)));
                @SuppressLint("Range") String rTodoCategory = cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_CATEGORY));
                @SuppressLint("Range") int rTodoPos = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_POSITION)));
                @SuppressLint("Range") int rTodoComplete = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_COMPLETE)));
                @SuppressLint("Range") String rTodoContents = cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_CONTENTS));
                @SuppressLint("Range") String rTodoRegisterDate = cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_REGISTER_DATE));
                lockRoutineItems.add(new LockRoutineItem(rTodoId, rTodoItemId, rTodoCategory, rTodoPos, rTodoComplete, rTodoContents, rTodoRegisterDate));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lockRoutineItems;
    }


    public ArrayList<RoutineItem> getAllRoutineTodo() {
        String sql = "";
        Cursor cursor;
        sql = "select * from  " + SPContract.RoutineTodoEntry.NAME_ROUTINE_TODO;
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.rawQuery(sql, null);

        ArrayList<RoutineItem> routineItems = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int rTodoId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry._ID)));
                @SuppressLint("Range") int rTodoItemId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_ITEM_ID)));
                @SuppressLint("Range") String rTodoCategory = cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_CATEGORY));
                @SuppressLint("Range") int rTodoPos = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_POSITION)));
                @SuppressLint("Range") int rTodoComplete = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_COMPLETE)));
                @SuppressLint("Range") String rTodoContents = cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_CONTENTS));
                @SuppressLint("Range") String rTodoRegisterDate = cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_REGISTER_DATE));
                routineItems.add(new RoutineItem(rTodoId, rTodoItemId, rTodoCategory, rTodoPos, rTodoComplete, rTodoContents, rTodoRegisterDate));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return routineItems;
    }


    public void sendTomorrowRoutineData(int ID, int routineID) {
        String sql = "";
        Cursor cursor;
        sql = "select * from  " + SPContract.RoutineTodoEntry.NAME_ROUTINE_TODO +
                " where " + SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_ITEM_ID + "=?";
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.rawQuery(sql, new String[]{String.valueOf(ID)});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String rTodoCategory = cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_CATEGORY));
                @SuppressLint("Range") int rTodoPos = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_POSITION)));
                @SuppressLint("Range") String rTodoContents = cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_CONTENTS));
                @SuppressLint("Range") String rTodoRegisterDate = cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_REGISTER_DATE));
                insertRoutine(routineID, rTodoCategory, rTodoPos, 0, rTodoContents, rTodoRegisterDate);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void copyRoutine(int ID, int routineID) {
        String sql = "";
        Cursor cursor;
        sql = "select * from  " + SPContract.RoutineTodoEntry.NAME_ROUTINE_TODO +
                " where " + SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_ITEM_ID + "=?";
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.rawQuery(sql, new String[]{String.valueOf(ID)});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String rTodoCategory = cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_CATEGORY));
                @SuppressLint("Range") int rTodoPos = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_POSITION)));
                @SuppressLint("Range") String rTodoContents = cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_CONTENTS));
                @SuppressLint("Range") String rTodoRegisterDate = cursor.getString(cursor.getColumnIndex(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_REGISTER_DATE));
                insertBringRoutine(routineID, rTodoCategory, rTodoPos, 0, rTodoContents, rTodoRegisterDate);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public long insertBringRoutine(int rTodoItemId, String rTodoCategory, int rTodoPos, int rTodoComplete, String rTodoContents, String rTodoRegisterDate) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_ITEM_ID, rTodoItemId);
        values.put(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_CATEGORY, rTodoCategory);
        values.put(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_POSITION, rTodoPos);
        values.put(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_COMPLETE, rTodoComplete);
        values.put(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_CONTENTS, rTodoContents);
        values.put(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_REGISTER_DATE, rTodoRegisterDate);

        long id = db.insert(SPContract.RoutineTodoEntry.NAME_ROUTINE_TODO, null, values);
        return id;
    }


    public long insertCopySchedule(ArrayList<CopyItem> copyItems, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        long id = 0;
        for (int i = 0; i < copyItems.size(); i++) {
            int listPos = 0;
            ArrayList<ScheduleItem> sci = getScheduleItems();
            if (sci.isEmpty()) {
                listPos = 1;
            } else {
                ScheduleItem item = sci.get(getScheduleItems().size() - 1);
                listPos = item.getScId() + 1;
            }

            CopyItem item = copyItems.get(i);
            values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_EMOJI, item.getBrEmoji());
            values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CONTENTS, item.getBrContents());
            values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CATEGORY, item.getBrCategory());
            if (item.getBrCategory().equals(Information.CATEGORY_ROUTINE)) {
                copyRoutine(item.getBrId(), listPos);
            }
            values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_COMPLETE, 0);
            values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_COMPLETED_TIME, "");
            values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_LIST_POSITION, listPos);
            values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_START_HOUR, item.getBrTodoStartHour());
            values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_START_MINUTE, item.getBrTodoStartMinute());
            values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_END_HOUR, item.getBrTodoEndHour());
            values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_END_MINUTE, item.getBrTodoEndMinute());
            values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_START_DATE, "");
            values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_END_DATE, "");
            values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_COMPLETED, 0);
            values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_REGISTERED_DATE, date);
            values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_DELETED_DATE, "");
            id = db.insert(SPContract.ScheduleEntry.NAME_SCHEDULE, null, values);
        }
        db.close();

        return id;
    }


    //Storage Routine TodoItem
    public ArrayList<StorageRoutineTodoItem> getStorageRoutineTodoItems() {
        String sql = "select * from " + SPContract.StorageRoutineTodoEntry.NAME_STORAGE_ROUTINE_TODO + " order by " + SPContract.StorageRoutineTodoEntry.COLUMN_STORAGE_ROUTINE_TODO_LIST_POSITION + " desc";
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<StorageRoutineTodoItem> storageRoutineTodoItems = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int stRtId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.StorageRoutineTodoEntry._ID)));
                @SuppressLint("Range") String stRtContents = cursor.getString(cursor.getColumnIndex(SPContract.StorageRoutineTodoEntry.COLUMN_STORAGE_ROUTINE_TODO_CONTENTS));
                @SuppressLint("Range") int stRtListPos = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.StorageRoutineTodoEntry.COLUMN_STORAGE_ROUTINE_TODO_LIST_POSITION)));
                @SuppressLint("Range") int stRtParentId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.StorageRoutineTodoEntry.COLUMN_STORAGE_PARENTS_ID)));
                @SuppressLint("Range") String stRtParentContents = cursor.getString(cursor.getColumnIndex(SPContract.StorageRoutineTodoEntry.COLUMN_STORAGE_PARENTS_CONTENTS));
                @SuppressLint("Range") String stRtParentRegisterDate = cursor.getString(cursor.getColumnIndex(SPContract.StorageRoutineTodoEntry.COLUMN_STORAGE_PARENTS_REGISTER_DATE));
                storageRoutineTodoItems.add(new StorageRoutineTodoItem(stRtId, stRtContents, stRtListPos, stRtParentId, stRtParentContents, stRtParentRegisterDate));
            } while (cursor.moveToNext());

        }
        cursor.close();
        return storageRoutineTodoItems;
    }

    //Search 로 스토리지 해당 ID의 루틴 아이템 찾기
    public ArrayList<StorageRoutineTodoItem> searchStorageRoutineTodo(int ID) {

        String sql = "select * from " + SPContract.StorageRoutineTodoEntry.NAME_STORAGE_ROUTINE_TODO +
                " where " + SPContract.StorageRoutineTodoEntry._ID + "=?";
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<StorageRoutineTodoItem> storageRoutineTodoItems = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(ID)});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int stRtId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.StorageRoutineTodoEntry._ID)));
                @SuppressLint("Range") String stRtContents = cursor.getString(cursor.getColumnIndex(SPContract.StorageRoutineTodoEntry.COLUMN_STORAGE_ROUTINE_TODO_CONTENTS));
                @SuppressLint("Range") int stRtListPos = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.StorageRoutineTodoEntry.COLUMN_STORAGE_ROUTINE_TODO_LIST_POSITION)));
                @SuppressLint("Range") int stRtParentId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.StorageRoutineTodoEntry.COLUMN_STORAGE_PARENTS_ID)));
                @SuppressLint("Range") String stRtParentContents = cursor.getString(cursor.getColumnIndex(SPContract.StorageRoutineTodoEntry.COLUMN_STORAGE_PARENTS_CONTENTS));
                @SuppressLint("Range") String stRtParentRegisterDate = cursor.getString(cursor.getColumnIndex(SPContract.StorageRoutineTodoEntry.COLUMN_STORAGE_PARENTS_REGISTER_DATE));
                storageRoutineTodoItems.add(new StorageRoutineTodoItem(stRtId, stRtContents, stRtListPos, stRtParentId, stRtParentContents, stRtParentRegisterDate));
            } while (cursor.moveToNext());

        }
        cursor.close();
        return storageRoutineTodoItems;
    }

    // Analytics 에서 DB 불러오기

    public ArrayList<AnalyticsItem> getScheduleFromAnalytics() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<AnalyticsItem> analyticsItems = new ArrayList<>();
        Cursor cursor = db.query(true, SPContract.ScheduleEntry.NAME_SCHEDULE,
                new String[]{SPContract.ScheduleEntry.COLUMN_SCHEDULE_REGISTERED_DATE}, null,
                null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String registerDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_REGISTERED_DATE));
                analyticsItems.add(new AnalyticsItem(registerDate));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return analyticsItems;
    }

    // Analytics 에서 날짜 별 complete 가져오기

    public ArrayList<CompleteItem> searchCompleteInAnalytics(String registerDate) {

        String sql = "select * from " + SPContract.ScheduleEntry.NAME_SCHEDULE +
                " where " + SPContract.ScheduleEntry.COLUMN_SCHEDULE_REGISTERED_DATE + "=?";
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<CompleteItem> completeItems = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, new String[]{registerDate});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String _category = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CATEGORY));
                if (_category.equals(Information.CATEGORY_TODO) || _category.equals(Information.CATEGORY_TIME) || _category.equals(Information.CATEGORY_ROUTINE)) {
                    @SuppressLint("Range") int _complete = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_COMPLETE)));
                    completeItems.add(new CompleteItem(_complete));
                }
            } while (cursor.moveToNext());

        }
        cursor.close();
        return completeItems;
    }


    // TABLE SCHEDULE 에 저장
    public long insertSchedule(String scEmoji, String scContents, String scCategory, int scComplete, String scCompletedTime, int scListPosition, int todoStartHour,
                               int todoStartMinute, int todoEndHour, int todoEndMinute, String dDayStartDate,
                               String dDayEndDate, int dDayCompleted, String scRegisteredDate, String scDeletedDate) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_EMOJI, scEmoji);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CONTENTS, scContents);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CATEGORY, scCategory);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_COMPLETE, scComplete);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_COMPLETED_TIME, scCompletedTime);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_LIST_POSITION, scListPosition);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_START_HOUR, todoStartHour);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_START_MINUTE, todoStartMinute);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_END_HOUR, todoEndHour);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_END_MINUTE, todoEndMinute);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_START_DATE, dDayStartDate);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_END_DATE, dDayEndDate);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_COMPLETED, dDayCompleted);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_REGISTERED_DATE, scRegisteredDate);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_DELETED_DATE, scDeletedDate);

        long id = db.insert(SPContract.ScheduleEntry.NAME_SCHEDULE, null, values);

        db.close();
        return id;
    }


    public long insertRoutine(int rTodoItemId, String rTodoCategory, int rTodoPos, int rTodoComplete, String rTodoContents, String rTodoRegisterDate) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_ITEM_ID, rTodoItemId);
        values.put(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_CATEGORY, rTodoCategory);
        values.put(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_POSITION, rTodoPos);
        values.put(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_COMPLETE, rTodoComplete);
        values.put(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_CONTENTS, rTodoContents);
        values.put(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_REGISTER_DATE, rTodoRegisterDate);

        long id = db.insert(SPContract.RoutineTodoEntry.NAME_ROUTINE_TODO, null, values);
        db.close();
        return id;

    }

    public long insertStorageRoutine(String contents, int pos, int parentsID, String parentsContents, String parentsRegisterDate) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SPContract.StorageRoutineTodoEntry.COLUMN_STORAGE_ROUTINE_TODO_CONTENTS, contents);
        values.put(SPContract.StorageRoutineTodoEntry.COLUMN_STORAGE_ROUTINE_TODO_LIST_POSITION, pos);
        values.put(SPContract.StorageRoutineTodoEntry.COLUMN_STORAGE_PARENTS_ID, parentsID);
        values.put(SPContract.StorageRoutineTodoEntry.COLUMN_STORAGE_PARENTS_CONTENTS, parentsContents);
        values.put(SPContract.StorageRoutineTodoEntry.COLUMN_STORAGE_PARENTS_REGISTER_DATE, parentsRegisterDate);

        long id = db.insert(SPContract.StorageRoutineTodoEntry.NAME_STORAGE_ROUTINE_TODO, null, values);
        db.close();
        return id;

    }


    // TABLE STORAGE에 저장
    public long insertStorage(String stCategory, String stContents, String stEmoji, String stRegisteredDate, String stDeletedDate, int stListPosition, int stAddCount) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SPContract.StorageEntry.COLUMN_STORAGE_CATEGORY, stCategory);
        values.put(SPContract.StorageEntry.COLUMN_STORAGE_CONTENTS, stContents);
        values.put(SPContract.StorageEntry.COLUMN_STORAGE_EMOJI, stEmoji);
        values.put(SPContract.StorageEntry.COLUMN_STORAGE_REGISTERED_DATE, stRegisteredDate);
        values.put(SPContract.StorageEntry.COLUMN_STORAGE_DELETED_DATE, stDeletedDate);
        values.put(SPContract.StorageEntry.COLUMN_STORAGE_LIST_POSITION, stListPosition);
        values.put(SPContract.StorageEntry.COLUMN_STORAGE_ADD_COUNT, stAddCount);

        long id = db.insert(SPContract.StorageEntry.NAME_STORAGE, null, values);
        db.close();
        return id;
    }

    // delete id
    public void deleteListStorage(int stId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(SPContract.StorageEntry.NAME_STORAGE, SPContract.StorageEntry._ID + "=?", new String[]{String.valueOf(stId)});
        db.close();
    }


    // delete id
    public void deleteSchedule(int scId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(SPContract.ScheduleEntry.NAME_SCHEDULE, SPContract.ScheduleEntry._ID + "=?", new String[]{String.valueOf(scId)});
        db.close();
    }

    //해당 아이템이 삭제되면 안에 있는 루틴에 관한 db 도 전부 삭제
    public void deleteRoutineInSchedule(int ID, int size) {
        SQLiteDatabase db = getWritableDatabase();
        int i = 0;
        do {
            db.delete(SPContract.RoutineTodoEntry.NAME_ROUTINE_TODO, SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_ITEM_ID + "=?", new String[]{String.valueOf(ID)});
            i++;
        } while (i < size);
        db.close();
    }


    //Delete routineTodo
    public void deleteRoutineTodoItem(int rtID) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(SPContract.RoutineTodoEntry.NAME_ROUTINE_TODO, SPContract.RoutineTodoEntry._ID + "=?", new String[]{String.valueOf(rtID)});
        db.close();
    }

    //Delete Storage Routine todo
    public void deleteStorageRoutineTodo(int stRtID) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(SPContract.StorageRoutineTodoEntry.NAME_STORAGE_ROUTINE_TODO, SPContract.StorageRoutineTodoEntry._ID + "=?", new String[]{String.valueOf(stRtID)});
        db.close();
    }


    public void swapItem(StorageItem from, StorageItem to) {
        updateStListPosition(to.getStId(), from.getStListPosition());
        updateStListPosition(from.getStId(), to.getStListPosition());
    }

    public void swapRoutineTodoItem(RoutineItem from, RoutineItem to) {
        updateRtTodoListPosition(to.getrTodoId(), from.getrTodoPos());
        updateRtTodoListPosition(from.getrTodoId(), to.getrTodoPos());
    }

    public void swapLockRoutineItem(LockRoutineItem from, LockRoutineItem to) {
        updateRtTodoListPosition(to.getrTodoId(), from.getrTodoPos());
        updateRtTodoListPosition(from.getrTodoId(), to.getrTodoPos());
    }

    public void swapStorageRoutineTodoItem(StorageRoutineTodoItem from, StorageRoutineTodoItem to) {
        updateStRtListPosition(to.getStRtId(), from.getStRtListPos());
        updateStRtListPosition(from.getStRtId(), to.getStRtListPos());
    }


    //update stListPosition
    public long updateStListPosition(long stId, long stListPosition) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SPContract.StorageEntry.COLUMN_STORAGE_LIST_POSITION, stListPosition);         // 바꿀값
        long id = db.update(SPContract.StorageEntry.NAME_STORAGE, values, SPContract.StorageEntry._ID + "=?", new String[]{Long.toString(stId)});
        db.close();
        return id;
    }

    //update rtListPosition
    public long updateRtTodoListPosition(long rtID, long rtPos) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_POSITION, rtPos);
        long id = db.update(SPContract.RoutineTodoEntry.NAME_ROUTINE_TODO, values, SPContract.RoutineTodoEntry._ID + "=?", new String[]{Long.toString(rtID)});
        db.close();
        return id;
    }

    //update stRtListPosition
    public long updateStRtListPosition(long stRtID, long stRtPos) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SPContract.StorageRoutineTodoEntry.COLUMN_STORAGE_ROUTINE_TODO_LIST_POSITION, stRtPos);
        long id = db.update(SPContract.StorageRoutineTodoEntry.NAME_STORAGE_ROUTINE_TODO, values, SPContract.StorageRoutineTodoEntry._ID + "=?", new String[]{Long.toString(stRtID)});
        db.close();
        return id;
    }


    //update stListPosition
    public long upDateComplete(long scId, long scItemComplete) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_COMPLETE, scItemComplete);         // 바꿀값
        long id = db.update(SPContract.ScheduleEntry.NAME_SCHEDULE, values, SPContract.ScheduleEntry._ID + "=?", new String[]{Long.toString(scId)});
        db.close();
        return id;
    }

    public long updateTodoEditItem(long editId, String emoji, String contents) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_EMOJI, emoji);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CONTENTS, contents);
        long id = db.update(SPContract.ScheduleEntry.NAME_SCHEDULE, values, SPContract.ScheduleEntry._ID + "=?", new String[]{Long.toString(editId)});
        db.close();
        return id;
    }

    public long updateTimeEditItem(long editId, String emoji, String contents, int startHour, int startMinute, int endHour, int endMinute) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_EMOJI, emoji);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CONTENTS, contents);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CONTENTS, contents);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_START_HOUR, startHour);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_START_MINUTE, startMinute);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_END_HOUR, endHour);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_END_MINUTE, endMinute);
        long id = db.update(SPContract.ScheduleEntry.NAME_SCHEDULE, values, SPContract.ScheduleEntry._ID + "=?", new String[]{Long.toString(editId)});
        db.close();
        return id;

    }


    public long updatePinEditItem(long editId, String emoji, String contents) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_EMOJI, emoji);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CONTENTS, contents);
        long id = db.update(SPContract.ScheduleEntry.NAME_SCHEDULE, values, SPContract.ScheduleEntry._ID + "=?", new String[]{Long.toString(editId)});
        db.close();
        return id;
    }

    public long updateDdayEditItem(long editId, String emoji, String contents, String endDate) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_EMOJI, emoji);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CONTENTS, contents);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_END_DATE, endDate);
        long id = db.update(SPContract.ScheduleEntry.NAME_SCHEDULE, values, SPContract.ScheduleEntry._ID + "=?", new String[]{Long.toString(editId)});
        db.close();
        return id;
    }

    public long updateStartDayEditItem(long editId, String emoji, String contents) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_EMOJI, emoji);
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CONTENTS, contents);
        long id = db.update(SPContract.ScheduleEntry.NAME_SCHEDULE, values, SPContract.ScheduleEntry._ID + "=?", new String[]{Long.toString(editId)});
        db.close();
        return id;
    }


    public void scheduleSwapItem(ScheduleItem from, ScheduleItem to) {
        updateScListPosition(to.getScId(), from.getScListPosition());
        updateScListPosition(from.getScId(), to.getScListPosition());
    }

    public void lockSwapItem(LockItem from, LockItem to) {
        updateScListPosition(to.getScId(), from.getScListPosition());
        updateScListPosition(from.getScId(), to.getScListPosition());
    }


    //update stListPosition
    public long updateScListPosition(long scId, long scListPosition) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_LIST_POSITION, scListPosition);         // 바꿀값
        long id = db.update(SPContract.ScheduleEntry.NAME_SCHEDULE, values, SPContract.ScheduleEntry._ID + "=?", new String[]{Long.toString(scId)});

        db.close();

        return id;
    }

    public long updateRoutineTodoComplete(long rtID, long complete) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SPContract.RoutineTodoEntry.COLUMN_ROUTINE_TODO_COMPLETE, complete);
        long id = db.update(SPContract.RoutineTodoEntry.NAME_ROUTINE_TODO, values, SPContract.RoutineTodoEntry._ID + "=?", new String[]{Long.toString(rtID)});
        db.close();
        return id;
    }

    // update fragment list item todo or routine percentage
    public long changeCategoryInRoutine(long ID, String category) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CATEGORY, category);
        long id = db.update(SPContract.ScheduleEntry.NAME_SCHEDULE, values, SPContract.ScheduleEntry._ID + "=?", new String[]{Long.toString(ID)});
        db.close();
        return id;
    }


    // model class 연결 부분

    public ArrayList<ScheduleItem> getScheduleData(String date, String category, String secondCategory) {
        String sql = "select * from " + SPContract.ScheduleEntry.NAME_SCHEDULE + " where " + SPContract.ScheduleEntry.COLUMN_SCHEDULE_REGISTERED_DATE + "=?"
                + "OR " + SPContract.ScheduleEntry.COLUMN_SCHEDULE_CATEGORY + "=?" + "OR " + SPContract.ScheduleEntry.COLUMN_SCHEDULE_CATEGORY + "=?";
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<ScheduleItem> scheduleItems = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, new String[]{date, category, secondCategory});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int scId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry._ID)));
                @SuppressLint("Range") String scEmoji = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_EMOJI));
                @SuppressLint("Range") String scContents = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CONTENTS));
                @SuppressLint("Range") String scCategory = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CATEGORY));
                @SuppressLint("Range") int scComplete = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_COMPLETE)));
                @SuppressLint("Range") String scCompletedTime = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_COMPLETED_TIME));
                @SuppressLint("Range") int scListPosition = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_LIST_POSITION)));
                @SuppressLint("Range") int todoStartHour = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_START_HOUR)));
                @SuppressLint("Range") int todoStartMinute = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_START_MINUTE)));
                @SuppressLint("Range") int todoEndHour = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_END_HOUR)));
                @SuppressLint("Range") int todoEndMinute = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_END_MINUTE)));
                @SuppressLint("Range") String dDayStartDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_START_DATE));
                @SuppressLint("Range") String dDayEndDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_END_DATE));
                @SuppressLint("Range") int dDayCompleted = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_COMPLETED)));
                @SuppressLint("Range") String scRegisteredDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_REGISTERED_DATE));
                @SuppressLint("Range") String scDeletedDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_DELETED_DATE));
                String scStandardDate = date;

                if (scCategory.equals(Information.CATEGORY_TODO) || scCategory.equals(Information.CATEGORY_TIME)) {
                    scheduleItems.add(new ScheduleItem(scId, scEmoji, scContents, scCategory,
                            scComplete, scCompletedTime, scListPosition, todoStartHour, todoStartMinute,
                            todoEndHour, todoEndMinute, dDayStartDate,
                            dDayEndDate, dDayCompleted, scRegisteredDate, scDeletedDate, scStandardDate));
                } else if (scCategory.equals(Information.CATEGORY_START_DAY)) {
                    if (visibilitySchedule(date, scRegisteredDate) >= 0) {
                        scheduleItems.add(new ScheduleItem(scId, scEmoji, scContents, scCategory,
                                scComplete, scCompletedTime, scListPosition, todoStartHour, todoStartMinute,
                                todoEndHour, todoEndMinute, dDayStartDate,
                                dDayEndDate, dDayCompleted, scRegisteredDate, scDeletedDate, scStandardDate));
                    }
                } else {
                    if (dDayEndDate.equals(Information.CATEGORY_PIN)) {
                        scheduleItems.add(new ScheduleItem(scId, scEmoji, scContents, scCategory,
                                scComplete, scCompletedTime, scListPosition, todoStartHour, todoStartMinute,
                                todoEndHour, todoEndMinute, dDayStartDate,
                                dDayEndDate, dDayCompleted, scRegisteredDate, scDeletedDate, scStandardDate));
                    } else {
                        if (visibilitySchedule(date, scRegisteredDate) >= 0) {
                            if (visibilitySchedule(date, dDayEndDate) <= 0) {
                                scheduleItems.add(new ScheduleItem(scId, scEmoji, scContents, scCategory,
                                        scComplete, scCompletedTime, scListPosition, todoStartHour, todoStartMinute,
                                        todoEndHour, todoEndMinute, dDayStartDate,
                                        dDayEndDate, dDayCompleted, scRegisteredDate, scDeletedDate, scStandardDate));
                            }
                        }
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return scheduleItems;
    }

    //get lock screen data

    public ArrayList<LockItem> getLockData(String date, String category, String secondCategory) {
        String sql = "select * from " + SPContract.ScheduleEntry.NAME_SCHEDULE + " where " + SPContract.ScheduleEntry.COLUMN_SCHEDULE_REGISTERED_DATE + "=?"
                + "OR " + SPContract.ScheduleEntry.COLUMN_SCHEDULE_CATEGORY + "=?" + "OR " + SPContract.ScheduleEntry.COLUMN_SCHEDULE_CATEGORY + "=?";
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<LockItem> lockItems = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, new String[]{date, category, secondCategory});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int scId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry._ID)));
                @SuppressLint("Range") String scEmoji = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_EMOJI));
                @SuppressLint("Range") String scContents = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CONTENTS));
                @SuppressLint("Range") String scCategory = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CATEGORY));
                @SuppressLint("Range") int scComplete = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_COMPLETE)));
                @SuppressLint("Range") String scCompletedTime = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_COMPLETED_TIME));
                @SuppressLint("Range") int scListPosition = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_LIST_POSITION)));
                @SuppressLint("Range") int todoStartHour = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_START_HOUR)));
                @SuppressLint("Range") int todoStartMinute = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_START_MINUTE)));
                @SuppressLint("Range") int todoEndHour = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_END_HOUR)));
                @SuppressLint("Range") int todoEndMinute = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_END_MINUTE)));
                @SuppressLint("Range") String dDayStartDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_START_DATE));
                @SuppressLint("Range") String dDayEndDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_END_DATE));
                @SuppressLint("Range") int dDayCompleted = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_COMPLETED)));
                @SuppressLint("Range") String scRegisteredDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_REGISTERED_DATE));
                @SuppressLint("Range") String scDeletedDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_DELETED_DATE));
                String scStandardDate = date;

                if (scCategory.equals(Information.CATEGORY_TODO) || scCategory.equals(Information.CATEGORY_TIME)) {
                    lockItems.add(new LockItem(scId, scEmoji, scContents, scCategory,
                            scComplete, scCompletedTime, scListPosition, todoStartHour, todoStartMinute,
                            todoEndHour, todoEndMinute, dDayStartDate,
                            dDayEndDate, dDayCompleted, scRegisteredDate, scDeletedDate, scStandardDate));
                } else if (scCategory.equals(Information.CATEGORY_START_DAY)) {
                    if (visibilitySchedule(date, scRegisteredDate) >= 0) {
                        lockItems.add(new LockItem(scId, scEmoji, scContents, scCategory,
                                scComplete, scCompletedTime, scListPosition, todoStartHour, todoStartMinute,
                                todoEndHour, todoEndMinute, dDayStartDate,
                                dDayEndDate, dDayCompleted, scRegisteredDate, scDeletedDate, scStandardDate));
                    }
                } else {
                    if (dDayEndDate.equals(Information.CATEGORY_PIN)) {
                        lockItems.add(new LockItem(scId, scEmoji, scContents, scCategory,
                                scComplete, scCompletedTime, scListPosition, todoStartHour, todoStartMinute,
                                todoEndHour, todoEndMinute, dDayStartDate,
                                dDayEndDate, dDayCompleted, scRegisteredDate, scDeletedDate, scStandardDate));
                    } else {
                        if (visibilitySchedule(date, scRegisteredDate) >= 0) {
                            if (visibilitySchedule(date, dDayEndDate) <= 0) {
                                lockItems.add(new LockItem(scId, scEmoji, scContents, scCategory,
                                        scComplete, scCompletedTime, scListPosition, todoStartHour, todoStartMinute,
                                        todoEndHour, todoEndMinute, dDayStartDate,
                                        dDayEndDate, dDayCompleted, scRegisteredDate, scDeletedDate, scStandardDate));
                            }
                        }
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lockItems;
    }


    public ArrayList<AnalyticsListItem> getAnalyticsList(String date, String category, String secondCategory) {
        String sql = "select * from " + SPContract.ScheduleEntry.NAME_SCHEDULE + " where " + SPContract.ScheduleEntry.COLUMN_SCHEDULE_REGISTERED_DATE + "=?"
                + "OR " + SPContract.ScheduleEntry.COLUMN_SCHEDULE_CATEGORY + "=?" + "OR " + SPContract.ScheduleEntry.COLUMN_SCHEDULE_CATEGORY + "=?";
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<AnalyticsListItem> analyticsListItems = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, new String[]{date, category, secondCategory});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int scId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry._ID)));
                @SuppressLint("Range") String scEmoji = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_EMOJI));
                @SuppressLint("Range") String scContents = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CONTENTS));
                @SuppressLint("Range") String scCategory = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CATEGORY));
                @SuppressLint("Range") int scComplete = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_COMPLETE)));
                @SuppressLint("Range") String scCompletedTime = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_COMPLETED_TIME));
                @SuppressLint("Range") int scListPosition = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_LIST_POSITION)));
                @SuppressLint("Range") int todoStartHour = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_START_HOUR)));
                @SuppressLint("Range") int todoStartMinute = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_START_MINUTE)));
                @SuppressLint("Range") int todoEndHour = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_END_HOUR)));
                @SuppressLint("Range") int todoEndMinute = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_END_MINUTE)));
                @SuppressLint("Range") String dDayStartDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_START_DATE));
                @SuppressLint("Range") String dDayEndDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_END_DATE));
                @SuppressLint("Range") int dDayCompleted = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_COMPLETED)));
                @SuppressLint("Range") String scRegisteredDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_REGISTERED_DATE));
                @SuppressLint("Range") String scDeletedDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_DELETED_DATE));
                String scStandardDate = date;

                if (scCategory.equals(Information.CATEGORY_TODO) || scCategory.equals(Information.CATEGORY_TIME) ||
                        scCategory.equals(Information.CATEGORY_DIVIDING_LINE_AM_PM) || scCategory.equals(Information.CATEGORY_DIVIDING_LINE_WHEN)) {
                    analyticsListItems.add(new AnalyticsListItem(scId, scEmoji, scContents, scCategory,
                            scComplete, scCompletedTime, scListPosition, todoStartHour, todoStartMinute,
                            todoEndHour, todoEndMinute, dDayStartDate,
                            dDayEndDate, dDayCompleted, scRegisteredDate, scDeletedDate, scStandardDate));
                } else if (scCategory.equals(Information.CATEGORY_START_DAY)) {
                    if (visibilitySchedule(date, scRegisteredDate) >= 0) {
                        analyticsListItems.add(new AnalyticsListItem(scId, scEmoji, scContents, scCategory,
                                scComplete, scCompletedTime, scListPosition, todoStartHour, todoStartMinute,
                                todoEndHour, todoEndMinute, dDayStartDate,
                                dDayEndDate, dDayCompleted, scRegisteredDate, scDeletedDate, scStandardDate));
                    }
                } else {
                    if (dDayEndDate.equals(Information.CATEGORY_PIN)) {
                    } else {
                        if (visibilitySchedule(date, scRegisteredDate) >= 0) {
                            if (visibilitySchedule(date, dDayEndDate) <= 0) {
                                analyticsListItems.add(new AnalyticsListItem(scId, scEmoji, scContents, scCategory,
                                        scComplete, scCompletedTime, scListPosition, todoStartHour, todoStartMinute,
                                        todoEndHour, todoEndMinute, dDayStartDate,
                                        dDayEndDate, dDayCompleted, scRegisteredDate, scDeletedDate, scStandardDate));
                            }
                        }
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return analyticsListItems;
    }

    public ArrayList<PickDateItem> getPickDateList(String date) {
        String sql = "select * from " + SPContract.ScheduleEntry.NAME_SCHEDULE + " where " + SPContract.ScheduleEntry.COLUMN_SCHEDULE_REGISTERED_DATE + "=?";
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<PickDateItem> pickDateItems = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, new String[]{date});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int scId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry._ID)));
                @SuppressLint("Range") String scEmoji = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_EMOJI));
                @SuppressLint("Range") String scContents = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CONTENTS));
                @SuppressLint("Range") String scCategory = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CATEGORY));
                @SuppressLint("Range") int scComplete = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_COMPLETE)));
                @SuppressLint("Range") String scCompletedTime = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_COMPLETED_TIME));
                @SuppressLint("Range") int scListPosition = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_LIST_POSITION)));
                @SuppressLint("Range") int todoStartHour = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_START_HOUR)));
                @SuppressLint("Range") int todoStartMinute = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_START_MINUTE)));
                @SuppressLint("Range") int todoEndHour = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_END_HOUR)));
                @SuppressLint("Range") int todoEndMinute = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_END_MINUTE)));
                @SuppressLint("Range") String dDayStartDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_START_DATE));
                @SuppressLint("Range") String dDayEndDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_END_DATE));
                @SuppressLint("Range") int dDayCompleted = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_D_DAY_COMPLETED)));
                @SuppressLint("Range") String scRegisteredDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_REGISTERED_DATE));
                @SuppressLint("Range") String scDeletedDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_DELETED_DATE));

                if (scCategory.equals(Information.CATEGORY_TODO) || scCategory.equals(Information.CATEGORY_TIME) || scCategory.equals(Information.CATEGORY_ROUTINE) ||
                        scCategory.equals(Information.CATEGORY_DIVIDING_LINE_WHEN) || scCategory.equals(Information.CATEGORY_DIVIDING_LINE_AM_PM)) {
                    pickDateItems.add(new PickDateItem(scId, scEmoji, scContents, scCategory,
                            scComplete, scCompletedTime, scListPosition, todoStartHour, todoStartMinute,
                            todoEndHour, todoEndMinute, dDayStartDate,
                            dDayEndDate, dDayCompleted, scRegisteredDate, scDeletedDate));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return pickDateItems;
    }


    public ArrayList<CopyItem> getBringData(String date) {
        String sql = "select * from " + SPContract.ScheduleEntry.NAME_SCHEDULE + " where " + SPContract.ScheduleEntry.COLUMN_SCHEDULE_REGISTERED_DATE + "=?";
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<CopyItem> copyItems = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, new String[]{date});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int brId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry._ID)));
                @SuppressLint("Range") String brEmoji = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_EMOJI));
                @SuppressLint("Range") String brContents = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CONTENTS));
                @SuppressLint("Range") String brCategory = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_CATEGORY));
                @SuppressLint("Range") int brListPosition = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_LIST_POSITION)));
                @SuppressLint("Range") int brTodoStartHour = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_START_HOUR)));
                @SuppressLint("Range") int brTodoStartMinute = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_START_MINUTE)));
                @SuppressLint("Range") int brTodoEndHour = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_END_HOUR)));
                @SuppressLint("Range") int brTodoEndMinute = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_TODO_END_MINUTE)));
                @SuppressLint("Range") String brScRegisteredDate = cursor.getString(cursor.getColumnIndex(SPContract.ScheduleEntry.COLUMN_SCHEDULE_REGISTERED_DATE));

                if (brCategory.equals(Information.CATEGORY_TODO) || brCategory.equals(Information.CATEGORY_TIME) || brCategory.equals(Information.CATEGORY_ROUTINE)
                        || brCategory.equals(Information.CATEGORY_DIVIDING_LINE_AM_PM) || brCategory.equals(Information.CATEGORY_DIVIDING_LINE_WHEN)) {
                    copyItems.add(new CopyItem(brId, brEmoji, brContents, brCategory, 0, "",
                            brListPosition, brTodoStartHour, brTodoStartMinute, brTodoEndHour, brTodoEndMinute, "", "", 0, brScRegisteredDate, ""));
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        return copyItems;
    }

    private int visibilitySchedule(String sDate, String eDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date FirstDate = format.parse(sDate);
            Date SecondDate = format.parse(eDate);
            long calDate = FirstDate.getTime() - SecondDate.getTime();
            long calDateDays = calDate / (24 * 60 * 60 * 1000);
            return (int) (calDateDays);
        } catch (ParseException e) {
            return 0;
        }
    }

}
