package com.allever.lose.weight.ui.view.widget;

/**
 * Created by Mac on 18/3/5.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.allever.lose.weight.R;
import com.allever.lose.weight.util.DensityUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BMIView extends View {

    private static final String TAG = "BMIView";

    private final static int VERY_SEVERELY_UNDERWEIGHT = 1;
    private final static int SEVERELY_UNDERWEIGHT = 2;
    private final static int UNDERWEIGHT = 3;
    private final static int NORMAL = 4;
    private final static int OVERWEIGHT = 5;
    private final static int OBESE_CLASS_1 = 6;
    private final static int OBESE_CLASS_2 = 7;
    private final static int OBESE_CLASS_3 = 8;

    private int colorNeutral = Color.parseColor("#727272");
    private int verySeverelyUnderweightColor;
    private int severelyUnderweightColor;
    private int underweightColor;
    private int normalColor;
    private int overweightColor;
    private int obeseClass1Color;
    private int obeseClass2Color;
    private int obeseClass3Color;

    private int mMin = 15, mMax = 40;
    private int mWidth, mHeight;
    private Paint mPaint;//矩形
    private Paint mLinePaint;//指示器
    private Paint mBmiPaint;//具体mbi指示器
    private Paint mDescPaint;//等级
    private int currentBodyCategory;
    private ArrayList<BodyCategory> bodyCategoryList;
    private float bmiValue = 0f;
    private float weight = 0;
    private float height = 0;

    // 0 = male, 1 = female
    private int gender = 0;

    private Context mContext;

    public BMIView(Context context) {
        super(context);
        mContext = context;
        initPainting();
        initBodyCategories();
        initDefaultValue();
    }

    public BMIView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        // Apply all attributes
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.BMIView,
                0, 0);
        try {
            verySeverelyUnderweightColor = a.getColor(R.styleable.BMIView_verySeverelyUnderweightColor, Color.parseColor("#ff6f69"));
            severelyUnderweightColor = a.getColor(R.styleable.BMIView_severelyUnderweightColor, Color.parseColor("#ffcc5c"));
            underweightColor = a.getColor(R.styleable.BMIView_underweightColor, Color.parseColor("#ffeead"));
            normalColor = a.getColor(R.styleable.BMIView_normalColor, Color.parseColor("#88d8b0"));
            overweightColor = a.getColor(R.styleable.BMIView_overweightColor, Color.parseColor("#ffeead"));
            obeseClass1Color = a.getColor(R.styleable.BMIView_obeseClass1Color, Color.parseColor("#ffcc5c"));
            obeseClass2Color = a.getColor(R.styleable.BMIView_obeseClass2Color, Color.parseColor("#ff6f69"));
            obeseClass3Color = a.getColor(R.styleable.BMIView_obeseClass3Color, Color.parseColor("#ff6f69"));
        } finally {
            a.recycle();
        }
        initPainting();
        initBodyCategories();
        initDefaultValue();
    }

    private void initDefaultValue() {
        mWidth = DensityUtil.getScreenWidth(mContext);
        //mHeight = dp2px(110f);
        mHeight = 350;
    }

    private void initPainting() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(colorNeutral);

        // Scale the desired text size to match screen density
        // mPaint.setTextSize(mFontSize * getResources().getDisplayMetrics().density);
        mPaint.setStrokeWidth(2f);
        setPadding(5, 5, 5, 5);


        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);//去除锯齿
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setColor(getResources().getColor(R.color.black));
        mLinePaint.setStrokeWidth(10);

        mBmiPaint = new Paint();
        mBmiPaint.setAntiAlias(true);//去除锯齿
        mBmiPaint.setTextSize(sp2px(20));

        mDescPaint = new Paint();
        mDescPaint.setAntiAlias(true);//去除锯齿
        mDescPaint.setTextSize(sp2px(13));
    }

    private void initBodyCategories() {
        bodyCategoryList = new ArrayList<>();

        bodyCategoryList.add(new BodyCategory(SEVERELY_UNDERWEIGHT, severelyUnderweightColor, getResources().getString(R.string.SEVERELY_UNDERWEIGHT), 15, 15));
        bodyCategoryList.add(new BodyCategory(UNDERWEIGHT, underweightColor, getResources().getString(R.string.UNDERWEIGHT), 16, 16));
        bodyCategoryList.add(new BodyCategory(NORMAL, normalColor, getResources().getString(R.string.NORMAL), 18.5f, 18.5f));
        bodyCategoryList.add(new BodyCategory(OVERWEIGHT, overweightColor, getResources().getString(R.string.OVERWEIGHT), 25, 25));
        bodyCategoryList.add(new BodyCategory(OBESE_CLASS_1, obeseClass1Color, getResources().getString(R.string.OBESE_CLASS_1), 30, 30));
        bodyCategoryList.add(new BodyCategory(OBESE_CLASS_2, obeseClass2Color, getResources().getString(R.string.OBESE_CLASS_2), 35, 35));
        bodyCategoryList.add(new BodyCategory(OBESE_CLASS_3, obeseClass3Color, getResources().getString(R.string.OBESE_CLASS_3), 40, 40));

        currentBodyCategory = NORMAL;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
            mWidth = width;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = MeasureSpec.getSize(widthMeasureSpec);
            mWidth = width;
        } else {
            width = mWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //精确值
            Log.d(TAG, "onMeasure: EXACTLY");
            if (heightSize <= mHeight) {
                height = mHeight;
            } else {
                height = heightSize;
            }
        } else if (heightMode == MeasureSpec.AT_MOST) {
            Log.d(TAG, "onMeasure: AT_MOST");
            Log.d(TAG, "onMeasure: AT_MOST mHeight = " + mHeight);
            Log.d(TAG, "onMeasure: AT_MOST heightSize = " + heightSize);
            height = Math.min(mHeight, heightSize);
        } else {
            //wrap_parent
            Log.d(TAG, "onMeasure: ELSE");
            height = mHeight;
        }

        mWidth = width;
        mHeight = height;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int topOfBar = getPaddingTop() + dp2px(30);
        int botOfBar = getPaddingTop() + dp2px(30) + dp2px(40);
        int rightSideOfBar = mWidth;

        float valueOfBar = rightSideOfBar;
        if (bmiValue <= mMax) {
            // - mMin moves the value to the left. The min value should be at the left border
            valueOfBar = rightSideOfBar * ((bmiValue - mMin) / (float) (mMax - mMin));
        }

        Paint.Style oldStyle = mPaint.getStyle();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        // Draws the categories
        for (int i = 0; i < bodyCategoryList.size() - 1; i++) {
            mPaint.setColor(bodyCategoryList.get(i).color);
            float bodyValue1 = rightSideOfBar * ((bodyCategoryList.get(i).getLimit(gender) - mMin) / (float) (mMax - mMin));
            float bodyValue2 = rightSideOfBar * ((bodyCategoryList.get(i + 1).getLimit(gender) - mMin) / (float) (mMax - mMin));
            canvas.drawRect(bodyValue1, topOfBar, bodyValue2, botOfBar, mPaint);
        }
        //Draws the last category
        mPaint.setColor(bodyCategoryList.get(bodyCategoryList.size() - 1).color);
        float bodyValueOfBar = rightSideOfBar * ((bodyCategoryList.get(bodyCategoryList.size() - 1).getLimit(gender) - mMin) / (float) (mMax - mMin));
        canvas.drawRect(bodyValueOfBar, topOfBar, rightSideOfBar, botOfBar, mPaint);

        //绘制指示器
        canvas.drawLine(valueOfBar, topOfBar - dp2px(5), valueOfBar, botOfBar + dp2px(5), mLinePaint);

        //绘制bmi数值
        String bmi = getRealBmiStr(height, weight);
        float x = valueOfBar - ((mBmiPaint.getTextSize() * bmi.length()) / 4);
        Log.d(TAG, "onDraw: x = " + x);
        //处理左边界
        if (x <= 0) {
            x = 0;
        }
        //处理右边界
        if (valueOfBar + (mBmiPaint.getTextSize() * bmi.length()) / 4 > DensityUtil.getScreenWidth(mContext)) {
            x = valueOfBar - ((mBmiPaint.getTextSize() * bmi.length()) / 4) - dp2px(25f);
        }
        canvas.drawText(bmi, 0, bmi.length(), x, topOfBar - dp2px(5) - dp2px(10), mBmiPaint);

        //绘制等级内容
        String level = getBodyDescription();
        Log.d(TAG, "onDraw: currentBodyCategory = " + currentBodyCategory);
        int currentLevel = currentBodyCategory - 2;
        if (currentLevel < 0) {
            currentLevel = 0;
        }
        Log.d(TAG, "onDraw: currentLevel = " + currentLevel);
        mDescPaint.setColor(bodyCategoryList.get(currentLevel).color);
        canvas.drawText(level, 0, level.length(), mWidth / 2 - (level.length() / 4) * mDescPaint.getTextSize(), botOfBar + dp2px(5) + dp2px(30), mDescPaint);

        mPaint.setColor(colorNeutral);
        mPaint.setStyle(oldStyle);

    }

    /**
     * Calculates the bodycategory based on the current bmi value
     *
     * @return bodycategory index
     */
    private int calculateBodyCategory() {
        int category = 1;
        for (BodyCategory b : bodyCategoryList) {
            if (b.getLimit(gender) <= getBmiValue()) {
                category = b.bodyCategory;
            }
        }
        return category;
    }

    /**
     * Calculates the bmi value and triggers onDraw().
     * This will refresh the view
     */
    @Override
    public void invalidate() {
        if (height == 0f) {
            bmiValue = 0;
        } else {
            bmiValue = weight / (height * height);
        }

        if (bmiValue < mMin) {
            bmiValue = mMin;
        }
        currentBodyCategory = calculateBodyCategory();

        super.invalidate();
    }

    /**
     * Returns more information about the given bodyCategory
     *
     * @param bodyCategory a bodycategory
     * @return bmi category limit
     */
    private BodyCategory getBodyCategory(int bodyCategory) {
        for (BodyCategory b : bodyCategoryList) {
            if (b.bodyCategory == bodyCategory) {
                return b;
            }
        }
        return bodyCategoryList.get(0);
    }

    public String getBodyDescription() {
        return getBodyCategory(currentBodyCategory).text;
    }

    public BMIView setHeight(float height) {
        this.height = height;
        invalidate();
        return this;
    }

    public BMIView setWeight(float weight) {
        this.weight = weight;
        invalidate();
        return this;
    }

    public float getWeight() {
        return weight;
    }

    /**
     * Returns the bmi value rounded to 1 digit
     *
     * @return bmiValue
     */
    public float getBmiValue() {
        return (float) (((int) (bmiValue * 10)) / 10.0);
    }

    /**
     * Returns the current gender
     * 0 = male, 1 = female
     *
     * @return bmi category limit
     */
    public int getGender() {
        return gender;
    }

    /**
     * Sets the gender
     *
     * @param gender 0 = male, 1 = female
     * @return BMIView
     */
    public BMIView setGender(int gender) {
        this.gender = gender;
        invalidate();
        return this;
    }

    /**
     * kg
     * m
     */
    private String getRealBmiStr(float height, float weight) {
        float value = weight / (height * height);
        Log.d(TAG, "getRealBmi: realBmi = " + value);
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(value);
        //return Math.round(weight/(height*height)*100);
    }

    private int dp2px(float value) {
        return DensityUtil.dip2px(mContext, value);
    }

    private int sp2px(float value) {
        return DensityUtil.sp2px(mContext, value);
    }


}