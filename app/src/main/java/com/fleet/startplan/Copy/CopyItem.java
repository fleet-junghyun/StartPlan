package com.fleet.startplan.Copy;

import java.util.Comparator;

public class CopyItem {

    int brId;
    String brEmoji;
    String brContents;
    String brCategory;
    int brComplete;
    String brCompletedTime;
    int brListPosition;
    int brTodoStartHour;
    int brTodoStartMinute;
    int brTodoEndHour;
    int brTodoEndMinute;
    String brDDayStartDate;
    String brDDayEndDate;
    int brDDayCompleted;
    String brScRegisteredDate;
    String brScDeletedDate;

    public CopyItem(int brId, String brEmoji, String brContents, String brCategory, int brComplete, String brCompletedTime,
                    int brListPosition, int brTodoStartHour, int brTodoStartMinute, int brTodoEndHour, int brTodoEndMinute,
                    String brDDayStartDate, String brDDayEndDate, int brDDayCompleted, String brScRegisteredDate, String brScDeletedDate) {
        this.brId = brId;
        this.brEmoji = brEmoji;
        this.brContents = brContents;
        this.brCategory = brCategory;
        this.brComplete = brComplete;
        this.brCompletedTime = brCompletedTime;
        this.brListPosition = brListPosition;
        this.brTodoStartHour = brTodoStartHour;
        this.brTodoStartMinute = brTodoStartMinute;
        this.brTodoEndHour = brTodoEndHour;
        this.brTodoEndMinute = brTodoEndMinute;
        this.brDDayStartDate = brDDayStartDate;
        this.brDDayEndDate = brDDayEndDate;
        this.brDDayCompleted = brDDayCompleted;
        this.brScRegisteredDate = brScRegisteredDate;
        this.brScDeletedDate = brScDeletedDate;
    }

    public static Comparator<CopyItem> setCopyItemList = new Comparator<CopyItem>() {
        @Override
        public int compare(CopyItem p1, CopyItem p2) {
            return p1.getBrListPosition() - p2.getBrListPosition();
        }
    };



    public String getStartTime() {
        if (brTodoStartHour > 12) {
            String pm = "PM";
            return String.format("%02d:%02d %s", brTodoStartHour - 12, brTodoStartMinute, pm);
        } else if (brTodoStartHour == 12) {
            String pm = "PM";
            return String.format("%02d:%02d %s", brTodoStartHour, brTodoStartMinute, pm);
        } else {
            String am = "AM";
            return String.format("%02d:%02d %s", brTodoStartHour, brTodoStartMinute, am);
        }
    }


    public String getEndTime() {
        if (brTodoEndHour > 12) {
            String pm = "PM";
            return String.format("%02d:%02d %s", brTodoEndHour - 12, brTodoEndMinute, pm);
        } else if (brTodoEndHour == 12) {
            String pm = "PM";
            return String.format("%02d:%02d %s", brTodoEndHour, brTodoEndMinute, pm);
        } else {
            String am = "AM";
            return String.format("%02d:%02d %s", brTodoEndHour, brTodoEndMinute, am);
        }
    }


    public int getBrId() {
        return brId;
    }

    public void setBrId(int brId) {
        this.brId = brId;
    }

    public String getBrEmoji() {
        return brEmoji;
    }

    public void setBrEmoji(String brEmoji) {
        this.brEmoji = brEmoji;
    }

    public String getBrContents() {
        return brContents;
    }

    public void setBrContents(String brContents) {
        this.brContents = brContents;
    }

    public String getBrCategory() {
        return brCategory;
    }

    public void setBrCategory(String brCategory) {
        this.brCategory = brCategory;
    }

    public int getBrComplete() {
        return brComplete;
    }

    public void setBrComplete(int brComplete) {
        this.brComplete = brComplete;
    }

    public String getBrCompletedTime() {
        return brCompletedTime;
    }

    public void setBrCompletedTime(String brCompletedTime) {
        this.brCompletedTime = brCompletedTime;
    }

    public int getBrListPosition() {
        return brListPosition;
    }

    public void setBrListPosition(int brListPosition) {
        this.brListPosition = brListPosition;
    }

    public int getBrTodoStartHour() {
        return brTodoStartHour;
    }

    public void setBrTodoStartHour(int brTodoStartHour) {
        this.brTodoStartHour = brTodoStartHour;
    }

    public int getBrTodoStartMinute() {
        return brTodoStartMinute;
    }

    public void setBrTodoStartMinute(int brTodoStartMinute) {
        this.brTodoStartMinute = brTodoStartMinute;
    }

    public int getBrTodoEndHour() {
        return brTodoEndHour;
    }

    public void setBrTodoEndHour(int brTodoEndHour) {
        this.brTodoEndHour = brTodoEndHour;
    }

    public int getBrTodoEndMinute() {
        return brTodoEndMinute;
    }

    public void setBrTodoEndMinute(int brTodoEndMinute) {
        this.brTodoEndMinute = brTodoEndMinute;
    }

    public String getBrDDayStartDate() {
        return brDDayStartDate;
    }

    public void setBrDDayStartDate(String brDDayStartDate) {
        this.brDDayStartDate = brDDayStartDate;
    }

    public String getBrDDayEndDate() {
        return brDDayEndDate;
    }

    public void setBrDDayEndDate(String brDDayEndDate) {
        this.brDDayEndDate = brDDayEndDate;
    }

    public int getBrDDayCompleted() {
        return brDDayCompleted;
    }

    public void setBrDDayCompleted(int brDDayCompleted) {
        this.brDDayCompleted = brDDayCompleted;
    }

    public String getBrScRegisteredDate() {
        return brScRegisteredDate;
    }

    public void setBrScRegisteredDate(String brScRegisteredDate) {
        this.brScRegisteredDate = brScRegisteredDate;
    }

    public String getBrScDeletedDate() {
        return brScDeletedDate;
    }

    public void setBrScDeletedDate(String brScDeletedDate) {
        this.brScDeletedDate = brScDeletedDate;
    }
}
