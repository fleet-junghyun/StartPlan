package com.fleet.startplan.Model;

import java.util.Comparator;

public class StorageItem {

    int stId;
    String stCategory;
    String stContents;
    String stEmoji;
    String stRegisteredDate;
    String stDeletedDate;
    int stListPosition;
    int stAddCount;

    public StorageItem(int stId, String stCategory, String stContents, String stEmoji, String stRegisteredDate, String stDeletedDate, int stListPosition, int stAddCount) {
        this.stId = stId;
        this.stCategory = stCategory;
        this.stContents = stContents;
        this.stEmoji = stEmoji;
        this.stRegisteredDate = stRegisteredDate;
        this.stDeletedDate = stDeletedDate;
        this.stListPosition = stListPosition;
        this.stAddCount = stAddCount;
    }

    public static Comparator<StorageItem> setStorageItemList = new Comparator<StorageItem>() {
        @Override
        public int compare(StorageItem p1, StorageItem p2) {
            return p2.getStListPosition() - p1.getStListPosition();
        }
    };


    public int getStId() {
        return stId;
    }

    public void setStId(int stId) {
        this.stId = stId;
    }

    public String getStCategory() {
        return stCategory;
    }

    public void setStCategory(String stCategory) {
        this.stCategory = stCategory;
    }

    public String getStContents() {
        return stContents;
    }

    public void setStContents(String stContents) {
        this.stContents = stContents;
    }

    public String getStEmoji() {
        return stEmoji;
    }

    public void setStEmoji(String stEmoji) {
        this.stEmoji = stEmoji;
    }

    public String getStRegisteredDate() {
        return stRegisteredDate;
    }

    public void setStRegisteredDate(String stRegisteredDate) {
        this.stRegisteredDate = stRegisteredDate;
    }

    public String getStDeletedDate() {
        return stDeletedDate;
    }

    public void setStDeletedDate(String stDeletedDate) {
        this.stDeletedDate = stDeletedDate;
    }

    public int getStListPosition() {
        return stListPosition;
    }

    public void setStListPosition(int stListPosition) {
        this.stListPosition = stListPosition;
    }

    public int getStAddCount() {
        return stAddCount;
    }

    public void setStAddCount(int stAddCount) {
        this.stAddCount = stAddCount;
    }
}
