package com.fleet.startplan.DB;

import android.provider.BaseColumns;

public class SPContract {

    // 인스턴스화 금지
    private SPContract() {
    }

    // 테이블 정보를 내부 클래스로 정의

    public static class ScheduleEntry implements BaseColumns {
        public static final String NAME_SCHEDULE = "ScheduleTable";
        public static final String COLUMN_SCHEDULE_EMOJI = "ScheduleEmoji";
        public static final String COLUMN_SCHEDULE_CONTENTS = "ScheduleContents";
        public static final String COLUMN_SCHEDULE_CATEGORY = "ScheduleCategory";
        public static final String COLUMN_SCHEDULE_COMPLETE = "ScheduleComplete";
        public static final String COLUMN_SCHEDULE_COMPLETED_TIME = "ScheduleCompletedTime";
        public static final String COLUMN_SCHEDULE_LIST_POSITION = "ScheduleListPosition";
        //TO DO 관련 COLUMN
        public static final String COLUMN_SCHEDULE_TODO_START_HOUR = "StartHour";
        public static final String COLUMN_SCHEDULE_TODO_START_MINUTE = "StartMinute";
        public static final String COLUMN_SCHEDULE_TODO_END_HOUR = "EndHour";
        public static final String COLUMN_SCHEDULE_TODO_END_MINUTE = "EndMinute";
        //D-DAY 관련 COLUMN
        public static final String COLUMN_SCHEDULE_D_DAY_START_DATE = "StartDate";
        public static final String COLUMN_SCHEDULE_D_DAY_END_DATE = "EndDate";
        public static final String COLUMN_SCHEDULE_D_DAY_COMPLETED = "CompleteDate";
        public static final String COLUMN_SCHEDULE_REGISTERED_DATE = "RegisterDate";
        public static final String COLUMN_SCHEDULE_DELETED_DATE = "DeletedDate";
    }

    public static class StorageEntry implements BaseColumns {
        public static final String NAME_STORAGE = "StorageTable";
        public static final String COLUMN_STORAGE_CATEGORY = "StorageCategory";
        public static final String COLUMN_STORAGE_CONTENTS = "StorageContents";
        public static final String COLUMN_STORAGE_EMOJI = "StorageEmoji";
        public static final String COLUMN_STORAGE_REGISTERED_DATE = "StorageRegisteredDate";
        public static final String COLUMN_STORAGE_DELETED_DATE = "DeletedDate";
        public static final String COLUMN_STORAGE_LIST_POSITION = "StoragePosition";
        public static final String COLUMN_STORAGE_ADD_COUNT = "StorageAddCount";
    }


    public static class RoutineTodoEntry implements BaseColumns{
        public static final String NAME_ROUTINE_TODO = "RoutineTodoTable";
        public static final String COLUMN_ROUTINE_TODO_ITEM_ID = "RoutineTodoItemID";
        public static final String COLUMN_ROUTINE_TODO_CATEGORY = "RoutineTodoCategory";
        public static final String COLUMN_ROUTINE_TODO_POSITION = "RoutineTodoPosition";
        public static final String COLUMN_ROUTINE_TODO_COMPLETE = "RoutineTodoComplete";
        public static final String COLUMN_ROUTINE_TODO_CONTENTS = "RoutineTodoContents";
        public static final String COLUMN_ROUTINE_TODO_REGISTER_DATE = "RoutineTodoRegisterDate";
    }

    public static class StorageRoutineTodoEntry implements BaseColumns{
        public static final String NAME_STORAGE_ROUTINE_TODO = "StorageRoutineTodoTable";
        public static final String COLUMN_STORAGE_ROUTINE_TODO_CONTENTS = "StorageRoutineTodoContents";
        public static final String COLUMN_STORAGE_ROUTINE_TODO_LIST_POSITION = "StorageRoutineTodoPosition";
        public static final String COLUMN_STORAGE_PARENTS_ID = "StorageRoutineTodoParentsID";
        public static final String COLUMN_STORAGE_PARENTS_CONTENTS= "StorageRoutineTodoParentsContents";
        public static final String COLUMN_STORAGE_PARENTS_REGISTER_DATE = "StorageRoutineTodoParentsRegisterDate";
    }






}
