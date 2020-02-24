package com.allever.lose.weight.bean;

import java.util.List;

/**
 * Created by Mac on 18/3/6.
 */

public class TrainData {
    private String name;
    private List<Exercise> exercise;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Exercise> getExercise() {
        return exercise;
    }

    public void setExercise(List<Exercise> exercise) {
        this.exercise = exercise;
    }
}
