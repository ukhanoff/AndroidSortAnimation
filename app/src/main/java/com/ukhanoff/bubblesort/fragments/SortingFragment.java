package com.ukhanoff.bubblesort.fragments;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ukhanoff.bubblesort.R;
import com.ukhanoff.bubblesort.utils.Utils;
import com.ukhanoff.bubblesort.views.BubbleImageView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by ukhanoff on 2/2/17.
 */

public class SortingFragment extends Fragment {
    public static final int PADDING = 50;
    public static final int INTERNAL_ID_PREFIX = 100000;
    private EditText editText;
    private Button goButton;
    private LinearLayout bubblesContainer;
    View.OnClickListener buttonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String inputUserArray = editText.getText().toString();
            if (!TextUtils.isEmpty(inputUserArray)) {
                parseData(inputUserArray);
            } else {
                Toast.makeText(getContext(), R.string.empty_field_warning, Toast.LENGTH_LONG).show();
            }
        }
    };

    static void swap(ArrayList<Integer> list, int inner) {
        //TODO Add animator for this step
        int temp = list.get(inner);
        list.set(inner, list.get(inner + 1));
        list.set(inner + 1, temp);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.sorting_main_fragment_view, container, false);
        editText = (EditText) fragmentView.findViewById(R.id.array_input);
        goButton = (Button) fragmentView.findViewById(R.id.go_sort_btn);
        goButton.setOnClickListener(buttonClickListener);
        bubblesContainer = (LinearLayout) fragmentView.findViewById(R.id.bubbles_container);
        return fragmentView;
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
            bubbleImageView.setNumber(currentIntValue);
            bubbleImageView.setId(INTERNAL_ID_PREFIX + pos);
            if (bubblesContainer != null) {
                bubblesContainer.addView(bubbleImageView, lp);
            }
            pos++;
        }
    }

    private Bitmap createCalculatedBitmap(Integer currentIntValue) {
        final Rect bounds = new Rect();
        Paint paint = new Paint(Paint.LINEAR_TEXT_FLAG);
        paint.setTextSize(40f);
        paint.getTextBounds(currentIntValue.toString(), 0, currentIntValue.toString().length(), bounds);
        return Bitmap.createBitmap(bounds.width() + PADDING, bounds.height() + PADDING, Bitmap.Config.RGB_565);
    }

    private void parseData(String inputUserArray) {
        ArrayList<Integer> parsedUserArray = new ArrayList<>();
        String[] stringArray = inputUserArray.split(",");
        int numberOfElements = stringArray.length;
        for (int i = 0; i < numberOfElements; i++) {
            parsedUserArray.add(Integer.parseInt(stringArray[i]));
        }
        Log.d(TAG, "parseData: " + sort(parsedUserArray));
        drawBubbles(sort(parsedUserArray));

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
