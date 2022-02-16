package com.fleet.startplan.Routine;

import java.util.Comparator;

public class RoutineItem {

    int rTodoId;
    int rTodoItemId;
    String rTodoCategory;
    int rTodoPos;
    int rTodoComplete;
    String rTodoContents;
    String rTodoRegisterDate;


    public static Comparator<RoutineItem> setRoutineTodoItemList = new Comparator<RoutineItem>() {
        @Override
        public int compare(RoutineItem p1, RoutineItem p2) {
            return p1.getrTodoPos() - p2.getrTodoPos();
        }
    };


    public RoutineItem(int rTodoId, int rTodoItemId, String rTodoCategory, int rTodoPos, int rTodoComplete, String rTodoContents, String rTodoRegisterDate) {
        this.rTodoId = rTodoId;
        this.rTodoItemId = rTodoItemId;
        this.rTodoCategory = rTodoCategory;
        this.rTodoPos = rTodoPos;
        this.rTodoComplete = rTodoComplete;
        this.rTodoContents = rTodoContents;
        this.rTodoRegisterDate = rTodoRegisterDate;
    }




    public int getrTodoId() {
        return rTodoId;
    }

    public void setrTodoId(int rTodoId) {
        this.rTodoId = rTodoId;
    }

    public int getrTodoItemId() {
        return rTodoItemId;
    }

    public void setrTodoItemId(int rTodoItemId) {
        this.rTodoItemId = rTodoItemId;
    }

    public String getrTodoCategory() {
        return rTodoCategory;
    }

    public void setrTodoCategory(String rTodoCategory) {
        this.rTodoCategory = rTodoCategory;
    }

    public int getrTodoPos() {
        return rTodoPos;
    }

    public void setrTodoPos(int rTodoPos) {
        this.rTodoPos = rTodoPos;
    }

    public int getrTodoComplete() {
        return rTodoComplete;
    }

    public void setrTodoComplete(int rTodoComplete) {
        this.rTodoComplete = rTodoComplete;
    }

    public String getrTodoContents() {
        return rTodoContents;
    }

    public void setrTodoContents(String rTodoContents) {
        this.rTodoContents = rTodoContents;
    }

    public String getrTodoRegisterDate() {
        return rTodoRegisterDate;
    }

    public void setrTodoRegisterDate(String rTodoRegisterDate) {
        this.rTodoRegisterDate = rTodoRegisterDate;
    }
}
