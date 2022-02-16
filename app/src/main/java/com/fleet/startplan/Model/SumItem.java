package com.fleet.startplan.Model;

public class SumItem {

    String _registerDate;
    int _complete;
    int _todoSize;

    public SumItem(String _registerDate, int _complete, int _todoSize) {
        this._registerDate = _registerDate;
        this._complete = _complete;
        this._todoSize = _todoSize;
    }

    public String get_registerDate() {
        return _registerDate;
    }

    public void set_registerDate(String _registerDate) {
        this._registerDate = _registerDate;
    }

    public int get_complete() {
        return _complete;
    }

    public void set_complete(int _complete) {
        this._complete = _complete;
    }

    public int get_todoSize() {
        return _todoSize;
    }

    public void set_todoSize(int _todoSize) {
        this._todoSize = _todoSize;
    }
}
