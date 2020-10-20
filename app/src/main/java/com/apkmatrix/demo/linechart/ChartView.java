package com.apkmatrix.demo.linechart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义折线图
 * Created by xiaoyunfei on 16/11/29.
 */
public class ChartView extends View {
    //xy坐标轴颜色
    private int xylinecolor = Color.BLACK;
    //xy坐标轴宽度
    private int xylinewidth = dpToPx(1);
    //xy坐标轴文字颜色
    private int xytextcolor = Color.BLACK;
    //xy坐标轴文字大小
    private int xytextsize = spToPx(12);
    //折线图中折线的颜色
    private int linecolor = Color.BLACK;
    //x轴各个坐标点水平间距
    private int interval = dpToPx(50);
    //背景颜色
    private int bgcolor = Color.BLACK;
    //绘制XY轴坐标对应的画笔
    private Paint xyPaint;
    //绘制XY轴的文本对应的画笔
    private Paint xyTextPaint;
    //画折线对应的画笔
    private Paint linePaint;
    private int width;
    private int height;
    //x轴的原点坐标
    private int xOri;
    //y轴的原点坐标
    private int yOri;
    //第一个点X的坐标
    private float xInit;
    //x轴坐标对应的数据
    private List<String> xValue = new ArrayList<>();
    //y轴坐标对应的数据
    private List<Integer> yValue = new ArrayList<>();
    //折线对应的数据
    private Map<String, Integer> value = new HashMap<>();

    public ChartView(Context context) {
        this(context, null);
    }

    public ChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
        initPaint();
    }

    /**
     * 初始化畫筆
     */
    private void initPaint() {
        xyPaint = new Paint();
        xyPaint.setAntiAlias(true);
        xyPaint.setStrokeWidth(xylinewidth);
        xyPaint.setStrokeCap(Paint.Cap.ROUND);
        xyPaint.setColor(xylinecolor);

        xyTextPaint = new Paint();
        xyTextPaint.setAntiAlias(true);
        xyTextPaint.setTextSize(xytextsize);
        xyTextPaint.setColor(xytextcolor);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(xylinewidth);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setColor(linecolor);
        linePaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.chartView, defStyleAttr, 0);
        int count = array.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.chartView_xylinecolor://xy坐标轴颜色
                    xylinecolor = array.getColor(attr, xylinecolor);
                    break;
                case R.styleable.chartView_xylinewidth://xy坐标轴宽度
                    xylinewidth = (int) array.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, xylinewidth, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.chartView_xytextcolor://xy坐标轴文字颜色
                    xytextcolor = array.getColor(attr, xytextcolor);
                    break;
                case R.styleable.chartView_xytextsize://xy坐标轴文字大小
                    xytextsize = (int) array.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, xytextsize, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.chartView_linecolor://折线图中折线的颜色
                    linecolor = array.getColor(attr, linecolor);
                    break;
                case R.styleable.chartView_bgcolor: //背景颜色
                    bgcolor = array.getColor(attr, bgcolor);
                    break;
            }
        }
        array.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            //这里需要确定几个基本点，只有确定了xy轴原点坐标，第一个点的X坐标值及其最大最小值
            width = getWidth();
            height = getHeight();
            //Y轴文本最大宽度
            float textYWdith = getTextBounds("000", xyTextPaint).width();
            for (int i = 0; i < yValue.size(); i++) {//求取y轴文本最大的宽度
                float temp = getTextBounds(yValue.get(i) + "", xyTextPaint).width();
                if (temp > textYWdith)
                    textYWdith = temp;
            }
//          //X轴文本最大高度
            float textXHeight = getTextBounds("000", xyTextPaint).height();
            for (int i = 0; i < xValue.size(); i++) {//求取x轴文本最大的高度
                float temp = getTextBounds(xValue.get(i) + "", xyTextPaint).height();
                if (temp > textXHeight)
                    textXHeight = temp;
            }
            interval = (width - xOri) / 8;
            int dp2 = dpToPx(2);
            int dp3 = dpToPx(3);
            xOri = (int) (dp2 + textYWdith + dp2 + xylinewidth);//dp2是y轴文本距离左边，以及距离y轴的距离
            yOri = (int) (height - dp2 - textXHeight - dp3 - xylinewidth);//dp3是x轴文本距离底边，dp2是x轴文本距离x轴的距离
            xInit = interval + xOri;
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (xValue.size() <= 0) return;
        canvas.drawColor(bgcolor);
        drawXY(canvas);
        drawBrokenLineAndPoint(canvas);
    }

    /**
     * 绘制折线和折线交点处对应的点
     *
     * @param canvas
     */
    private void drawBrokenLineAndPoint(Canvas canvas) {
        if (xValue.size() <= 0) return;
        drawBrokenLine(canvas);
        drawBrokenPoint(canvas);
    }

    /**
     * 绘制折线对应的点
     *
     * @param canvas
     */
    private void drawBrokenPoint(Canvas canvas) {
        float dp2 = dpToPx(2);
        //绘制节点对应的原点
        for (int i = 0; i < xValue.size(); i++) {
            float x = xInit + interval * i;
            float y = yOri - yOri * (1 - 0.2f) * value.get(xValue.get(i)) / yValue.get(yValue.size() - 1);
            linePaint.setStyle(Paint.Style.FILL);
            linePaint.setColor(xytextcolor);
            linePaint.setTextSize(xytextsize);
            canvas.drawCircle(x, y, dp2, linePaint);
            Rect rect = getTextBounds(value.get(xValue.get(i)) + "", linePaint);
            canvas.drawText(value.get(xValue.get(i)) + "", x - rect.width() / 2, y - rect.height() / 2, linePaint);
        }
    }

    /**
     * 绘制折线
     *
     * @param canvas
     */
    private void drawBrokenLine(Canvas canvas) {
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(linecolor);
        //绘制折线
        Path path = new Path();
        float x = xInit + interval * 0;
        float y = yOri - yOri * (1 - 0.2f) * value.get(xValue.get(0)) / yValue.get(yValue.size() - 1);
        path.moveTo(x, y);
        for (int i = 1; i < xValue.size(); i++) {
            x = xInit + interval * i;
            y = yOri - yOri * (1 - 0.2f) * value.get(xValue.get(i)) / yValue.get(yValue.size() - 1);
            path.lineTo(x, y);
        }
        canvas.drawPath(path, linePaint);
    }

    /**
     * 绘制XY坐标
     *
     * @param canvas
     */
    private void drawXY(Canvas canvas) {
        int length = dpToPx(4);//刻度的长度
        int dp5 = dpToPx(5);
        float jbHeight = getTextBounds("加班时间", xyTextPaint).height();
        canvas.drawText("加班时间", 0, jbHeight, xyTextPaint);
        //绘制Y坐标
        canvas.drawLine(xOri - xylinewidth / 2, jbHeight + dp5, xOri - xylinewidth / 2, yOri, xyPaint);
        //绘制y轴箭头
        xyPaint.setStyle(Paint.Style.STROKE);
        Path path = new Path();
        path.moveTo(xOri - xylinewidth / 2 - dpToPx(5), dpToPx(12) + jbHeight + dp5);
        path.lineTo(xOri - xylinewidth / 2, xylinewidth / 2 + jbHeight + dp5);
        path.lineTo(xOri - xylinewidth / 2 + dpToPx(5), dpToPx(12) + jbHeight + dp5);
        canvas.drawPath(path, xyPaint);
        //绘制y轴刻度
        int yLength = (int) (yOri * (1 - 0.2f) / (yValue.size() - 1));//y轴上面空出20%,计算出y轴刻度间距
        for (int i = 0; i < yValue.size(); i++) {
            //绘制Y轴刻度
            canvas.drawLine(xOri, yOri - yLength * i + xylinewidth / 2, xOri + length, yOri - yLength * i + xylinewidth / 2, xyPaint);
            xyTextPaint.setColor(xytextcolor);
            //绘制Y轴文本
            String text = yValue.get(i) + "";
            Rect rect = getTextBounds(text, xyTextPaint);
            canvas.drawText(text, 0, text.length(), xOri - xylinewidth - dpToPx(2) - rect.width(), yOri - yLength * i + rect.height() / 2, xyTextPaint);
        }
        //绘制X轴坐标(空出xInit)
        canvas.drawLine(xOri, yOri + xylinewidth / 2, xInit * 2 + interval * (xValue.size() - 1), yOri + xylinewidth / 2, xyPaint);
        //绘制x轴箭头
        xyPaint.setStyle(Paint.Style.STROKE);
        path = new Path();
        //整个X轴的长度
        float xLength = xInit * 2 + interval * (xValue.size() - 1) - xOri;
        path.moveTo(xLength - dpToPx(12), yOri + xylinewidth / 2 - dpToPx(5));
        path.lineTo(xLength - xylinewidth / 2, yOri + xylinewidth / 2);
        path.lineTo(xLength - dpToPx(12), yOri + xylinewidth / 2 + dpToPx(5));
        canvas.drawPath(path, xyPaint);
        //绘制x轴刻度
        for (int i = 0; i < xValue.size(); i++) {
            float x = xInit + interval * i;
            xyTextPaint.setColor(xytextcolor);
            canvas.drawLine(x, yOri, x, yOri - length, xyPaint);
            //绘制X轴文本
            String text = xValue.get(i);
            Rect rect = getTextBounds(text, xyTextPaint);
            canvas.drawText(text, 0, text.length(), x - rect.width() / 2, yOri + xylinewidth + dpToPx(2) + rect.height(), xyTextPaint);
        }
    }

    public void setValue(Map<String, Integer> value, List<String> xValue, List<Integer> yValue) {
        this.value = value;
        this.xValue = xValue;
        this.yValue = yValue;
        invalidate();
    }

    /**
     * 获取丈量文本的矩形
     *
     * @param text
     * @param paint
     * @return
     */
    private Rect getTextBounds(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect;
    }

    /**
     * dp转化成为px
     *
     * @param dp
     * @return
     */
    private int dpToPx(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f * (dp >= 0 ? 1 : -1));
    }

    /**
     * sp转化为px
     *
     * @param sp
     * @return
     */
    private int spToPx(int sp) {
        float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (scaledDensity * sp + 0.5f * (sp >= 0 ? 1 : -1));
    }
}

