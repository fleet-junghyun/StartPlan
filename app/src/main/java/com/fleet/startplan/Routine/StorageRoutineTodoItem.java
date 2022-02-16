package com.fleet.startplan.Routine;

import java.util.Comparator;

public class StorageRoutineTodoItem {

    int stRtId;
    String stRtContents;
    int stRtListPos;
    int stRtParentsId;
    String stRtParentContents;
    String stRtParentRegisterDate;

    public StorageRoutineTodoItem(int stRtId, String stRtContents, int stRtListPos, int stRtParentsId, String stRtParentContents, String stRtParentRegisterDate) {
        this.stRtId = stRtId;
        this.stRtContents = stRtContents;
        this.stRtListPos = stRtListPos;
        this.stRtParentsId = stRtParentsId;
        this.stRtParentContents = stRtParentContents;
        this.stRtParentRegisterDate = stRtParentRegisterDate;
    }

    public static Comparator<StorageRoutineTodoItem> setStorageRoutineTodoItem = new Comparator<StorageRoutineTodoItem>() {
        @Override
        public int compare(StorageRoutineTodoItem p1, StorageRoutineTodoItem p2) {
            return p2.getStRtListPos() - p1.getStRtListPos();
        }
    };


    public int getStRtId() {
        return stRtId;
    }

    public void setStRtId(int stRtId) {
        this.stRtId = stRtId;
    }

    public String getStRtContents() {
        return stRtContents;
    }

    public void setStRtContents(String stRtContents) {
        this.stRtContents = stRtContents;
    }

    public int getStRtListPos() {
        return stRtListPos;
    }

    public void setStRtListPos(int stRtListPos) {
        this.stRtListPos = stRtListPos;
    }

    public int getStRtParentsId() {
        return stRtParentsId;
    }

    public void setStRtParentsId(int stRtParentsId) {
        this.stRtParentsId = stRtParentsId;
    }

    public String getStRtParentContents() {
        return stRtParentContents;
    }

    public void setStRtParentContents(String stRtParentContents) {
        this.stRtParentContents = stRtParentContents;
    }

    public String getStRtParentRegisterDate() {
        return stRtParentRegisterDate;
    }

    public void setStRtParentRegisterDate(String stRtParentRegisterDate) {
        this.stRtParentRegisterDate = stRtParentRegisterDate;
    }
}
