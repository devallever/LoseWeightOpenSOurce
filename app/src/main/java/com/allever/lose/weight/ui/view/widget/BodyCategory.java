package com.allever.lose.weight.ui.view.widget;

/**
 * Created by Mac on 18/3/5.
 */

class BodyCategory {
    protected int bodyCategory;
    protected int color;
    protected String text;
    private float valueMale, valueFemale;

    protected BodyCategory(int bodyCategory, int color, String text, float valueMale, float valueFemale) {
        this.bodyCategory = bodyCategory;
        this.color = color;
        this.text = text;
        this.valueMale = valueMale;
        this.valueFemale = valueFemale;
    }

    /**
     * Returns the bmi category limit for a given gender
     *
     * @param gender 0 = men, 1 = women
     * @return bmi category limit
     */
    protected float getLimit(int gender) {
        if (gender == 0) {
            return valueMale;
        } else if (gender == 1) {
            return valueFemale;
        }
        return 0;
    }
}