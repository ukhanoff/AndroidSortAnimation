package com.ukhanoff.bubblesort.utils;

import android.content.Context;

/**
 * Typical android helpful staff lives here.
 */

public class Utils {

    public static int dpToPx(Context context, int sizeInDp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (sizeInDp * scale + 0.5f);
    }
}
