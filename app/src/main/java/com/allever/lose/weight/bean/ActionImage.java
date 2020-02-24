package com.allever.lose.weight.bean;

import java.util.List;

/**
 * Created by Mac on 18/3/6.
 */

public class ActionImage {
    private List<ActionInfo> uris;
    //actionId
    private int id;

    public List<ActionInfo> getUris() {
        return uris;
    }

    public void setUris(List<ActionInfo> uris) {
        this.uris = uris;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
