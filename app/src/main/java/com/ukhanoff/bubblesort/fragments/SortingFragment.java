package com.ukhanoff.bubblesort.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ukhanoff.bubblesort.R;
import com.ukhanoff.bubblesort.utils.Utils;
import com.ukhanoff.bubblesort.views.BubbleImageView;

import java.util.ArrayList;

/**
 * Created by ukhanoff on 2/2/17.
 */

public class SortingFragment extends Fragment {
    public static final int PADDING = 50;
    private EditText editText;
    private Button goButton;
    private Button animButton;
    private LinearLayout bubblesContainer;
    View.OnClickListener buttonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String inputUserArray = editText.getText().toString();
            if (!TextUtils.isEmpty(inputUserArray)) {
                ArrayList<Integer> integerArrayList = new ArrayList<>(convertToIntArray(inputUserArray));
                drawBubbles(integerArrayList);
//                sort(integerArrayList);
            } else {
                Toast.makeText(getContext(), R.string.empty_field_warning, Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.sorting_main_fragment_view, container, false);
        editText = (EditText) fragmentView.findViewById(R.id.array_input);
        goButton = (Button) fragmentView.findViewById(R.id.go_sort_btn);
        goButton.setOnClickListener(buttonClickListener);
        animButton = (Button) fragmentView.findViewById(R.id.start_animation_btn);
        animButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateBubble();
            }
        });
        bubblesContainer = (LinearLayout) fragmentView.findViewById(R.id.bubbles_container);
        return fragmentView;
    }

    private void animateBubble() {
        //TODO refactor this stuff
        if (bubblesContainer != null && bubblesContainer.getChildCount() > 0) {
            final BubbleImageView tempView = (BubbleImageView) bubblesContainer.getChildAt(0);
            final BubbleImageView nextTempView = (BubbleImageView) bubblesContainer.getChildAt(1);
            final int currItemWidth = tempView.getWidth();
            final int nextItemWidth = nextTempView.getWidth();

            //BLINKING
            ValueAnimator blinkAnimation = ValueAnimator.ofInt(0, 5);
            blinkAnimation.setDuration(1000);
            blinkAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = ((Integer) animation.getAnimatedValue()).intValue();
                    if (value % 2 == 0) {
                        tempView.setBubbleSelected(false);
                        nextTempView.setBubbleSelected(false);
                    } else {
                        tempView.setBubbleSelected(true);
                        nextTempView.setBubbleSelected(true);
                    }
                }
            });

            // CLOCKWISE SWAP
            final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
            animator.setDuration(2000);
            tempView.setBubbleSelected(true);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = ((Float) (animation.getAnimatedValue()))
                            .floatValue();
                    tempView.setTranslationX((float) (-nextItemWidth * Math.sin(value * Math.PI + Math.PI / 2) + 8));
                    tempView.setTranslationY((float) (50.0 * Math.cos(value * Math.PI + Math.PI / 2)));
                }
            });

            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    tempView.setBubbleSelected(false);
                }
            });
            animator.setInterpolator(new BounceInterpolator());

            // COUNTER CLOCKWISE SWAP
            final ValueAnimator animatorBack = ValueAnimator.ofFloat(0, 1);
            animatorBack.setDuration(2000);
            nextTempView.setBubbleSelected(true);
            animatorBack.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = ((Float) (animation.getAnimatedValue()))
                            .floatValue();

                    nextTempView.setTranslationX((float) (currItemWidth * Math.sin(value * Math.PI + Math.PI / 2) - 8));
                    nextTempView.setTranslationY((float) (-50.0 * Math.cos(value * Math.PI + Math.PI / 2)));
                }
            });

            animatorBack.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    nextTempView.setBubbleSelected(false);
                }
            });
            animatorBack.setInterpolator(new BounceInterpolator());

            blinkAnimation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animator.start();
                    animatorBack.start();
                }
            });

            blinkAnimation.start();
        }
    }


    private void swap(ArrayList<Integer> list, int inner) {
        swapViews(inner);
        int temp = list.get(inner);
        list.set(inner, list.get(inner + 1));
        list.set(inner + 1, temp);
    }

    private void swapViews(final int innerPosition) {

    }

    private void drawBubbles(ArrayList<Integer> listToDraw) {
        if (bubblesContainer != null) {
            bubblesContainer.removeAllViews();
        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int marginInPx = Utils.dpToPx(getContext(), 4);
        lp.setMargins(0, 0, marginInPx, 0);

        int pos = 0;
        for (Integer currentIntValue : listToDraw) {
            BubbleImageView bubbleImageView = new BubbleImageView(getContext());
            bubbleImageView.setImageBitmap(createCalculatedBitmap(currentIntValue));
            bubbleImageView.setMinimumHeight(200);
            bubbleImageView.setNumber(currentIntValue);
            bubbleImageView.setId(pos);
            if (bubblesContainer != null) {
                bubblesContainer.addView(bubbleImageView, lp);
            }
            pos++;
        }
    }

    /**
     * Calculates size of ImageView which would be generated with current text value.
     *
     * @param currentIntValue
     * @return empty bitmap with calculated size
     */
    private Bitmap createCalculatedBitmap(Integer currentIntValue) {
        final Rect bounds = new Rect();
        Paint paint = new Paint(Paint.LINEAR_TEXT_FLAG);
        paint.setTextSize(40f);
        paint.getTextBounds(currentIntValue.toString(), 0, currentIntValue.toString().length(), bounds);
        return Bitmap.createBitmap(bounds.width() + PADDING, bounds.height() + PADDING, Bitmap.Config.ALPHA_8);
    }

    private ArrayList convertToIntArray(String inputUserArray) {
        ArrayList<Integer> parsedUserArray = new ArrayList<>();
        String[] stringArray = inputUserArray.split(",");
        int numberOfElements = stringArray.length;
        for (int i = 0; i < numberOfElements; i++) {
            if (!TextUtils.isEmpty(stringArray[i])) {
                parsedUserArray.add(Integer.parseInt(stringArray[i]));
            }
        }
        return parsedUserArray;

    }

    public ArrayList<Integer> sort(ArrayList<Integer> unsortedValues) {
        ArrayList<Integer> values = new ArrayList<>(unsortedValues);
        for (int i = 0; i < values.size() - 1; i++) {
            for (int j = 0; j < values.size() - i - 1; j++) {
                if (values.get(j) > values.get(j + 1)) {
                    swap(values, j);
                }
            }
        }
        return values;
    }

}
