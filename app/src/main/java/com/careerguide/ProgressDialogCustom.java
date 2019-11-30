package com.careerguide;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Gaurav Gupta(9910781299) on 7/Feb/18-Wednesday.
 */

public class ProgressDialogCustom extends ProgressDialog {

    public ProgressDialogCustom(Context context) {
        super(context);
        setMessage("Loading...");
        init();
    }

    public ProgressDialogCustom(Context context, int theme) {
        super(context, theme);
        init();
    }


    public ProgressDialogCustom(Context context, String message) {
        super(context);
        setMessage(message);
        init();
    }



    private void init()
    {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public ProgressDialogCustom showIt() {
        super.show();
        return this;
    }
}
