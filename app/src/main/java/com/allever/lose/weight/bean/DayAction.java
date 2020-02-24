package com.allever.lose.weight.bean;

import java.util.List;

/**
 * Created by Mac on 18/3/7.
 */

public class DayAction {
    private int dayId;
    private int totalAction;
    private int currentActionPosition;
    private List<ActionItem> actionItemList;

    public int getDayId() {
        return dayId;
    }

    public void setDayId(int dayId) {
        this.dayId = dayId;
    }

    public int getTotalAction() {
        return totalAction;
    }

    public void setTotalAction(int totalAction) {
        this.totalAction = totalAction;
    }


    public int getCurrentActionPosition() {
        return currentActionPosition;
    }

    public void setCurrentActionPosition(int currentActionPosition) {
        this.currentActionPosition = currentActionPosition;
    }

    public List<ActionItem> getActionItemList() {
        return actionItemList;
    }

    public void setActionItemList(List<ActionItem> actionItemList) {
        this.actionItemList = actionItemList;
    }
}
