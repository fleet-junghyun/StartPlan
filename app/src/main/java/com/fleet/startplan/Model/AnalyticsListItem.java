package com.fleet.startplan.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class AnalyticsListItem {

    int scId;
    String scEmoji;
    String scContents;
    String scCategory;
    int scComplete;
    String scCompletedTime;
    int scListPosition;
    int todoStartHour;
    int todoStartMinute;
    int todoEndHour;
    int todoEndMinute;
    String dDayStartDate;
    String dDayEndDate;
    int dDayCompleted; //0이면 할 수 있음, 1이면 지난것들
    String scRegisteredDate;
    String scDeletedDate;
    String scStandardDate;

    public AnalyticsListItem(int scId, String scEmoji, String scContents, String scCategory, int scComplete, String scCompletedTime, int scListPosition,
                             int todoStartHour, int todoStartMinute, int todoEndHour, int todoEndMinute,
                             String dDayStartDate, String dDayEndDate, int dDayCompleted, String scRegisteredDate, String scDeletedDate, String scStandardDate) {
        this.scId = scId;
        this.scEmoji = scEmoji;
        this.scContents = scContents;
        this.scCategory = scCategory;
        this.scComplete = scComplete;
        this.scCompletedTime = scCompletedTime;
        this.scListPosition = scListPosition;
        this.todoStartHour = todoStartHour;
        this.todoStartMinute = todoStartMinute;
        this.todoEndHour = todoEndHour;
        this.todoEndMinute = todoEndMinute;
        this.dDayStartDate = dDayStartDate;
        this.dDayEndDate = dDayEndDate;
        this.dDayCompleted = dDayCompleted;
        this.scRegisteredDate = scRegisteredDate;
        this.scDeletedDate = scDeletedDate;
        this.scStandardDate = scStandardDate;
    }

    public static Comparator<AnalyticsListItem> setAnalyticsItemList = new Comparator<AnalyticsListItem>() {
        @Override
        public int compare(AnalyticsListItem p1, AnalyticsListItem p2) {

            String p1Category = p1.getScCategory();
            String p2Category = p2.getScCategory();

            if (p1Category.equals("dDay") && p2Category.equals("dDay")) {
                return p2.getCountDay() - p1.getCountDay();
            } else if (p1Category.equals("dDay") && p2Category.equals("startDay")) {
                return p2.getCountDay() - p1.getCountDay();
            } else if (p1Category.equals("startDay") && p2Category.equals("startDay")) {
                return p2.calculateStartDay() - p1.calculateStartDay();
            } else if (p1Category.equals("startDay") && p2Category.equals("dDay")) {
                return p2.getCountDay() - p1.getCountDay();
            }
            return p1.getScListPosition() - p2.getScListPosition();

        }
    };

    public int getDday() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date FirstDate = format.parse(dDayStartDate);
            Date SecondDate = format.parse(dDayEndDate);
            long calDate = SecondDate.getTime() - FirstDate.getTime();
            long calDateDays = calDate / (24 * 60 * 60 * 1000);
            return (int) (calDateDays);
        } catch (ParseException e) {
            return 0;
        }
    }


    public String getStartTime() {
        if (todoStartHour > 12) {
            String pm = "PM";
            return String.format("%02d:%02d %s", todoStartHour - 12, todoStartMinute, pm);
        } else if (todoStartHour == 12) {
            String pm = "PM";
            return String.format("%02d:%02d %s", todoStartHour, todoStartMinute, pm);
        } else {
            String am = "AM";
            return String.format("%02d:%02d %s", todoStartHour, todoStartMinute, am);
        }
    }


    public String getEndTime() {
        if (todoEndHour > 12) {
            String pm = "PM";
            return String.format("%02d:%02d %s", todoEndHour - 12, todoEndMinute, pm);
        } else if (todoEndHour == 12) {
            String pm = "PM";
            return String.format("%02d:%02d %s", todoEndHour, todoEndMinute, pm);
        } else {
            String am = "AM";
            return String.format("%02d:%02d %s", todoEndHour, todoEndMinute, am);
        }
    }


    public int getCountDay() {
        if (getdDayEndDate().equals("pin")) {
            return 999999999;
        } else if (getScCategory().equals(Information.CATEGORY_START_DAY)) {
            return 999999990;
        } else {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date FirstDate = format.parse(scStandardDate);
                Date SecondDate = format.parse(dDayEndDate);
                long calDate = SecondDate.getTime() - FirstDate.getTime();
                long calDateDays = calDate / (24 * 60 * 60 * 1000);
                return (int) (calDateDays);
            } catch (ParseException e) {
                return 0;
            }
        }
    }

    public int calculateStartDay() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date FirstDate = format.parse(scStandardDate);
            Date SecondDate = format.parse(scRegisteredDate);
            long calDate = FirstDate.getTime() - SecondDate.getTime();
            long calDateDays = calDate / (24 * 60 * 60 * 1000);
            return (int) (calDateDays);
        } catch (ParseException e) {
            return 0;
        }
    }


    public int getScId() {
        return scId;
    }

    public void setScId(int scId) {
        this.scId = scId;
    }

    public String getScEmoji() {
        return scEmoji;
    }

    public void setScEmoji(String scEmoji) {
        this.scEmoji = scEmoji;
    }

    public String getScContents() {
        return scContents;
    }

    public void setScContents(String scContents) {
        this.scContents = scContents;
    }

    public String getScCategory() {
        return scCategory;
    }

    public void setScCategory(String scCategory) {
        this.scCategory = scCategory;
    }

    public int getScComplete() {
        return scComplete;
    }

    public void setScComplete(int scComplete) {
        this.scComplete = scComplete;
    }

    public String getScCompletedTime() {
        return scCompletedTime;
    }

    public void setScCompletedTime(String scCompletedTime) {
        this.scCompletedTime = scCompletedTime;
    }

    public int getScListPosition() {
        return scListPosition;
    }

    public void setScListPosition(int scListPosition) {
        this.scListPosition = scListPosition;
    }

    public int getTodoStartHour() {
        return todoStartHour;
    }

    public void setTodoStartHour(int todoStartHour) {
        this.todoStartHour = todoStartHour;
    }

    public int getTodoStartMinute() {
        return todoStartMinute;
    }

    public void setTodoStartMinute(int todoStartMinute) {
        this.todoStartMinute = todoStartMinute;
    }

    public int getTodoEndHour() {
        return todoEndHour;
    }

    public void setTodoEndHour(int todoEndHour) {
        this.todoEndHour = todoEndHour;
    }

    public int getTodoEndMinute() {
        return todoEndMinute;
    }

    public void setTodoEndMinute(int todoEndMinute) {
        this.todoEndMinute = todoEndMinute;
    }

    public String getdDayStartDate() {
        return dDayStartDate;
    }

    public void setdDayStartDate(String dDayStartDate) {
        this.dDayStartDate = dDayStartDate;
    }

    public String getdDayEndDate() {
        return dDayEndDate;
    }

    public void setdDayEndDate(String dDayEndDate) {
        this.dDayEndDate = dDayEndDate;
    }

    public int getdDayCompleted() {
        return dDayCompleted;
    }

    public void setdDayCompleted(int dDayCompleted) {
        this.dDayCompleted = dDayCompleted;
    }

    public String getScRegisteredDate() {
        return scRegisteredDate;
    }

    public void setScRegisteredDate(String scRegisteredDate) {
        this.scRegisteredDate = scRegisteredDate;
    }

    public String getScDeletedDate() {
        return scDeletedDate;
    }

    public void setScDeletedDate(String scDeletedDate) {
        this.scDeletedDate = scDeletedDate;
    }

    public String getScStandardDate() {
        return scStandardDate;
    }

    public void setScStandardDate(String scStandardDate) {
        this.scStandardDate = scStandardDate;
    }

}
