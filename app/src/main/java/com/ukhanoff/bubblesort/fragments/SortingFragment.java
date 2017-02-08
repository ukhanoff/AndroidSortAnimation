package com.ukhanoff.bubblesort.fragments;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ukhanoff.bubblesort.R;
import com.ukhanoff.bubblesort.common.AlgorithmAnimationListener;
import com.ukhanoff.bubblesort.common.AnimationScenarioItem;
import com.ukhanoff.bubblesort.common.AnimationsCoordinator;
import com.ukhanoff.bubblesort.utils.Utils;
import com.ukhanoff.bubblesort.views.BubbleImageView;

import java.util.ArrayList;

/**
 * Main fragment where sorting visualisation appears
 */

public class SortingFragment extends Fragment {
    public static final int PADDING = 50;
    public static final int BUBBLE_MARGIN = 4;
    int i = 0;
    private EditText editText;
    private Button goButton;
    private Button animButton;
    private LinearLayout bubblesContainer;
    private AnimationsCoordinator animationsCoordinator;
    private ArrayList<AnimationScenarioItem> scenario;
    View.OnClickListener buttonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String inputUserArray = editText.getText().toString();
            if (!TextUtils.isEmpty(inputUserArray)) {
                scenario = new ArrayList<>();
                ArrayList<Integer> integerArrayList = new ArrayList<>(convertToIntArray(inputUserArray));
                drawBubbles(integerArrayList);
                sort(integerArrayList);
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
                runAnimationIteration();
            }
        });
        bubblesContainer = (LinearLayout) fragmentView.findViewById(R.id.bubbles_container);
        animationsCoordinator = new AnimationsCoordinator(bubblesContainer);
        animationsCoordinator.addListener(new AlgorithmAnimationListener() {
            @Override
            public void onSwapStepAnimationEnd(int endedPosition) {
                runAnimationIteration();
            }
        });
        return fragmentView;
    }

    private void runAnimationIteration() {
        if (scenario != null && !scenario.isEmpty() && scenario.size() == i) {
            animationsCoordinator.showFinish();
            return;
        }
        if (scenario != null && !scenario.isEmpty() && scenario.size() > i) {
            AnimationScenarioItem animationStep = scenario.get(i);
            i++;
            if (animationStep.isShouldBeSwapped()) {
                animationsCoordinator.showSwapStep(animationStep.getAnimationViewItemPosition(), animationStep.isFinalPlace());
            } else {
                animationsCoordinator.showNonSwapStep(animationStep.getAnimationViewItemPosition(), animationStep.isFinalPlace());
            }
        }

    }

    private void swap(final ArrayList<Integer> list, final int inner) {
        int temp = list.get(inner);
        list.set(inner, list.get(inner + 1));
        list.set(inner + 1, temp);
    }

    private void drawBubbles(ArrayList<Integer> listToDraw) {
        if (bubblesContainer != null) {
            bubblesContainer.removeAllViews();
        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int marginInPx = Utils.dpToPx(getContext(), BUBBLE_MARGIN);
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
        paint.setTextSize(BubbleImageView.TEXT_SIZE);
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

    private ArrayList<Integer> sort(ArrayList<Integer> unsortedValues) {
        ArrayList<Integer> values = new ArrayList<>(unsortedValues);
        boolean isLastInLoop;
        for (int i = 0; i < values.size() - 1; i++) {
            for (int j = 0; j < values.size() - i - 1; j++) {
                if (j == values.size() - i - 2) {
                    isLastInLoop = true;
                } else {
                    isLastInLoop = false;
                }
                if (values.get(j) > values.get(j + 1)) {
                    swap(values, j);
                    scenario.add(new AnimationScenarioItem(true, j, isLastInLoop));
                } else {
                    scenario.add(new AnimationScenarioItem(false, j, isLastInLoop));
                }
            }
        }
        return values;
    }

}
