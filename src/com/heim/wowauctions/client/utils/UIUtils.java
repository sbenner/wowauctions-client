package com.heim.wowauctions.client.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: sbenner
 * Date: 8/17/14
 * Time: 11:26 PM
 */
public class UIUtils {
    public static void showToast(Context context,String msg){

        Toast toast = Toast.makeText(context, msg, 10);
        toast.setGravity(Gravity.CENTER| Gravity.CENTER_HORIZONTAL| Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

}
