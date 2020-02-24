package com.allever.lose.weight.bean;

/**
 * Created by Mac on 18/3/20.
 */

public class MenuEvent {
    public String event;
    public int pageIndex;

    public MenuEvent(String eventStr, int index) {
        this.event = eventStr;
        this.pageIndex = index;
    }
}
