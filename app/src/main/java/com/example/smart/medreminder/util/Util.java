package com.example.smart.medreminder.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by smart on 10/04/2018, 1:49 PM.
 */

public class Util {

    Context context;

    private void hideKeyboardFrom(View view) {
        InputMethodManager methodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        methodManager.hideSoftInputFromWindow(view.getWindowToken(),
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
