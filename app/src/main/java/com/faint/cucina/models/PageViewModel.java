package com.faint.cucina.models;

import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    private int mIndex;

    public void setIndex(int index) {
        mIndex = index;
    }

    public int getIndex() {
        return mIndex;
    }
}