package com.zkrt.zkrtdrone.view.widget.joystick;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by root on 17-3-2.
 */

public class joystick extends View {
    public static final int INVALID_POINTER_ID = -1;
    public String TAG = "JoystickView";

    private static final double HAPTIC_FEEDBACK_ZONE = 0.05;
    private int handleRadius = 20;
    private int movementRadius = handleRadius * 4;

    private boolean yAxisInverted = false;
    private boolean xAxisInverted = false;
    private boolean yAxisAutoReturnToCenter = true;
    private boolean xAxisAutoReturnToCenter = true;
    private boolean autoReturnToCenter = true;
    private JoystickMovedListener moveListener;
    private int pointerId = INVALID_POINTER_ID;
    private float touchX, touchY;
    private double cartX, cartY;
    private double userX, userY;
    private double userXold, userYold;
    private float firstTouchX, firstTouchY;
    private double releaseX = 1500;
    private double releaseY = 1500;
    private Paint mainCircle;
    private Paint secondaryCircle;
    private Paint button;

    private int joystickRadius;
    private int buttonRadius;
    private int xPosition = 0;
    private int yPosition = 0;
    private double centerX = 0;
    private double centerY = 0;

    public joystick(Context context) {
        super(context);
        initJoystickView();
    }

    public joystick(Context context, AttributeSet attrs) {
        super(context, attrs);
        initJoystickView();
    }

    public joystick(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initJoystickView();
    }



    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        // before measure, get the center of view
        xPosition = (int) getWidth() / 2;
        yPosition = (int) getWidth() / 2;
        int d = Math.min(xNew, yNew);
        buttonRadius = (int) (d / 2 * 0.25);
        joystickRadius = (int) (d / 2 * 0.75);
    }

    private void initJoystickView() {
        setFocusable(true);
        setHapticFeedbackEnabled(true);

        mainCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mainCircle.setColor(Color.WHITE);
        mainCircle.setStyle(Paint.Style.FILL_AND_STROKE);

        secondaryCircle = new Paint();
        secondaryCircle.setColor(Color.GREEN);
        secondaryCircle.setStyle(Paint.Style.STROKE);

        button = new Paint(Paint.ANTI_ALIAS_FLAG);
        button.setColor(Color.RED);
        button.setStyle(Paint.Style.FILL);
    }

    public void setAutoReturnToCenter(boolean autoReturnToCenter) {
        this.autoReturnToCenter = autoReturnToCenter;
    }

    public boolean isAutoReturnToCenter() {
        return autoReturnToCenter;
    }

    public boolean isXAxisInverted() {
        return xAxisInverted;
    }

    public boolean isYAxisInverted() {
        return yAxisInverted;
    }

    public void setXAxisInverted(boolean xAxisInverted) {
        this.xAxisInverted = xAxisInverted;
    }

    public void setYAxisInverted(boolean yAxisInverted) {
        this.yAxisInverted = yAxisInverted;
    }

    public void setOnJostickMovedListener(JoystickMovedListener listener) {
        this.moveListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        // Draw the handle

        canvas.restore();

        centerX = (getWidth()) / 2;
        centerY = (getHeight()) / 2;

        canvas.drawCircle((int) centerX, (int) centerY, joystickRadius,
                mainCircle);
        // painting the secondary circle
        canvas.drawCircle((int) centerX, (int) centerY, joystickRadius / 2,
                secondaryCircle);

        // painting the move button
        canvas.drawCircle(xPosition, yPosition, buttonRadius, button);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerIndex;
        int pointerId;
        final int action = event.getAction();
        xPosition = (int) event.getX();
        yPosition = (int) event.getY();
        double abs = Math.sqrt((xPosition - centerX) * (xPosition - centerX)
                + (yPosition - centerY) * (yPosition - centerY));
        if (abs > joystickRadius) {
            xPosition = (int) ((xPosition - centerX) * joystickRadius / abs + centerX);
            yPosition = (int) ((yPosition - centerY) * joystickRadius / abs + centerY);
        }
        invalidate();

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE://表示用户在移动(手指或者其他)
                return processMove(event);
            case MotionEvent.ACTION_CANCEL:  //表示手势被取消了,一些关于这个事件类型的讨论
            case MotionEvent.ACTION_UP:  //表示用户抬起了手指
                xPosition = (int) centerX;
                yPosition = (int) centerY;
                if (isPointerValid()) {
                    processFirstTouch(event);
                    return processRelease();//processRelease();
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:  //一个非主要的手指抬起来了
                pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                pointerId = event.getPointerId(pointerIndex);
                if (pointerId == this.pointerId) {
                    processFirstTouch(event);
                    return processRelease();//;
                }
                break;
            case MotionEvent.ACTION_DOWN://表示用户开始触摸.
                if (!isPointerValid()) {
                    this.pointerId = event.getPointerId(0);
                    processFirstTouch(event);
                    return true;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:  //有一个非主要的手指按下了.
                pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                pointerId = event.getPointerId(pointerIndex);
                if (pointerId == INVALID_POINTER_ID) {
                    this.pointerId = pointerId;
                    //processFirstTouch(event);
                    return true;
                }
                break;
        }
        return false;
    }

    private boolean processRelease() {
        this.pointerId = INVALID_POINTER_ID;
        invalidate();
        if (moveListener != null) {
            releaseX = xAxisAutoReturnToCenter ? 1500 : userX;
            releaseY = yAxisAutoReturnToCenter ? 1500 : userY;
            moveListener.OnMoved(releaseX, releaseY);
        }
        return true;
    }

    private void processFirstTouch(MotionEvent ev) {
        firstTouchX = ev.getX();
        firstTouchY = ev.getY();
        touchX = 1500;
        touchY = 1500;
        invalidate();
    }

    private boolean isPointerValid() {
        return pointerId != INVALID_POINTER_ID;
    }

    private boolean processMove(MotionEvent ev) {
        if (isPointerValid()) {
            final int pointerIndex = ev.findPointerIndex(pointerId);

            // Translate touch position to center of view
            float x = ev.getX(pointerIndex);
            float y = ev.getY(pointerIndex);

            touchX = x - firstTouchX;
            touchY = y - firstTouchY;

            reportOnMoved();
            return true;
        }
        return false;
    }

    private void reportOnMoved() {
        calcUserCoordinates();
        constrainBox();
        hapticFeedback();

        if (moveListener != null) {
            moveListener.OnMoved(userX, userY);
        }
    }

    private void hapticFeedback() {
        if (hasEnteredHapticFeedbackZone(userX, userXold)) {
            performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        }
        if (hasEnteredHapticFeedbackZone(userY, userYold)) {
            performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        }
        userXold = userX;
        userYold = userY;
    }

    private boolean hasEnteredHapticFeedbackZone(double value, double oldValue) {
        return isInHapticFeedbackZone(value)
                & (!isInHapticFeedbackZone(oldValue));
    }

    private boolean isInHapticFeedbackZone(double value) {
        return Math.abs(value) < HAPTIC_FEEDBACK_ZONE;
    }

    private void calcUserCoordinates() {
        // First convert to cartesian coordinates
        cartX = (touchX / movementRadius);
        cartY = (touchY / movementRadius);

        // Invert axis if requested
        if (!xAxisInverted)
            cartX *= 1000;
        if (!yAxisInverted)
            cartY *= 1000;

        userX = cartX + (xAxisAutoReturnToCenter ? 1500 : releaseX);
        userY = cartY + (yAxisAutoReturnToCenter ? 1500 : releaseY);

    }

    // Constrain touch within a box
    private void constrainBox() {
        userX = Math.max(Math.min(userX, 1650), 1350);
        userY = Math.max(Math.min(userY, 1650), 1350);
    }

    public void setAxisAutoReturnToCenter(boolean yAxisAutoReturnToCenter,
                                          boolean xAxisAutoReturnToCenter) {
        this.yAxisAutoReturnToCenter = yAxisAutoReturnToCenter;
        this.xAxisAutoReturnToCenter = xAxisAutoReturnToCenter;
    }
}
