package com.allever.lose.weight.util;

/**
 * Created by Jerry on 2017/9/5.
 */

public class CalculationUtil {
//    /**
//     * @param lb 磅转kg
//     * @return 四舍五入的kg
//     */
//    public static float lb2Kg(int lb) {
//        long kg = Math.round(lb * 0.453);
//        return (float) kg;
//    }

    /**
     * @param lb 磅转kg
     * @return 四舍五入的kg
     */
    public static float lb2Kg(float lb) {
        long kg = Math.round(lb * 0.453);
        return (float) kg;
    }

    /**
     * @param kg kg转磅
     * @return 四舍五入的磅
     */
    public static float kg2Lb(float kg) {
        long lb = Math.round(kg * 2.20);
        return (float) lb;
    }


    public static float cm2in(float cm) {
        return cm * 0.397f;
    }

    public static float in2cm(float in) {
        return in * 2.54f;
    }

    public static float in2ft(float in) {
        return in * 0.083f;
    }

    public static float ft2in(float ft) {
        return ft * 12f;
    }

    /**
     * 0: 英尺 ft
     * 1: 英寸 in
     */
    public static float[] cm2ft_in(float cm) {
        float[] value = new float[2];
        float in = cm2in(cm);
        float ft = in2ft(in);
        value[0] = (float) Math.floor(ft);
        float restFt = ft - value[0];
        value[1] = (float) Math.floor(ft2in(restFt));
        return value;
    }

    public static float ft_in2cm(float ft, float in) {
        float totalIn = ft2in(ft) + in;
        return (float) Math.floor(in2cm(totalIn));
    }
}
