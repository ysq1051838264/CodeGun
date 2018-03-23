package com.jiafeng.codegun.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by yangshuquan on 2018/3/22.
 */

public class ToastMaker {

    public static void show(Context context, String text) {

        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void show(Context context, int textResId) {
        show(context, context.getString(textResId));
    }


}
