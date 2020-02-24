package com.allever.lose.weight.bean;

import java.util.List;

/**
 * Created by Mac on 18/3/21.
 */

public class ActionData {
    private int id;
    private String name;
    private float factor;
    private List<String> desc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getFactor() {
        return factor;
    }

    public void setFactor(float factor) {
        this.factor = factor;
    }

    public List<String> getDesc() {
        return desc;
    }

    public void setDesc(List<String> desc) {
        this.desc = desc;
    }
}
