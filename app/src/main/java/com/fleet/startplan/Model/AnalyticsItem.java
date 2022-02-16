package com.fleet.startplan.Model;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;

public class AnalyticsItem {

    String _registerDate;

    public AnalyticsItem(String _registerDate) {
        this._registerDate = _registerDate;
    }

    public String get_registerDate() {
        return _registerDate;
    }
    public void set_registerDate(String _registerDate) {
        this._registerDate = _registerDate;
    }

}
