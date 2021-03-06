package com.ukhanoff.bubblesort.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ukhanoff.bubblesort.R;

import static com.ukhanoff.bubblesort.fragments.SortingFragment.PADDING;

/**
 * This is custom ImageView which could draw a "Bubble with a number inside".
 */

public class BubbleImageView extends ImageView {
    public static final int START_X_POS = 25;
    public static final int TEXT_BASELINE_Y = 105;
    public static final int BOTTOM_POS = 120;
    public static final int TOP_POS = 60;
    public static final float TEXT_SIZE = 40f;
    private Integer valueToDraw;
    private boolean isSelected;
    private boolean isOnFinalPlace;

    public BubbleImageView(Context context) {
        this(context, null);
    }

    public BubbleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (valueToDraw != null) {
            String text = valueToDraw.toString();
            Paint paint = new Paint(Paint.LINEAR_TEXT_FLAG);
            Rect bounds = new Rect();
            paint.setAntiAlias(true);
            paint.setTextSize(TEXT_SIZE);
            paint.getTextBounds(text, 0, text.length(), bounds);
            if (isOnFinalPlace) {
                paint.setColor(getResources().getColor(R.color.colorPrimaryDark));
            } else {
                if (isSelected) {
                    paint.setColor(getResources().getColor(R.color.colorIndigo));
                } else {
                    paint.setColor(getResources().getColor(R.color.colorAccent));
                }
            }
            canvas.drawOval(0, TOP_POS, bounds.width() + PADDING, BOTTOM_POS, paint);
            paint.setColor(Color.WHITE);
            canvas.drawText(text.toString(), START_X_POS, TEXT_BASELINE_Y, paint);
        }
    }

    /**
     * Draws a number as a bitmap inside of the bubble circle.
     *
     * @param numberValueToDraw value which should appears in the center of {@link BubbleImageView}
     */
    public void setNumber(Integer numberValueToDraw) {
        valueToDraw = numberValueToDraw;
        invalidate();
    }

    /**
     * Background color of bubble will be changed to dark blue.
     *
     * @param isOnFinalPlace
     */
    public void setBubbleIsOnFinalPlace(boolean isOnFinalPlace) {
        this.isOnFinalPlace = isOnFinalPlace;
        invalidate();
    }

    public boolean isBubbleSelected() {
        return isSelected;
    }

    /**
     * Background color will be changed to blue if true
     *
     * @param isSelected
     */
    public void setBubbleSelected(boolean isSelected) {
        this.isSelected = isSelected;
        invalidate();
    }
}
