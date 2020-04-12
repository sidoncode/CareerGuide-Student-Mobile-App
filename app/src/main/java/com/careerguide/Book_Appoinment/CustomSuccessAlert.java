package com.careerguide.Book_Appoinment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.careerguide.HomeActivity;
import com.careerguide.ProfileDetailActivity;
import com.careerguide.R;

public class CustomSuccessAlert {
    Context context ;
    AlertDialog alertDialog;
    Activity activity  ;

    public CustomSuccessAlert(Context context , Activity activity) {
        this.context = context;
        this.activity = activity;
    }
    public  void showAlert(String msg){

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.success_alert , null);
        TextView text  = view.findViewById(R.id.text);
        Button ok = view.findViewById(R.id.ok) ;

        text.setText(msg);
        alert.setView(view);
        alert.setCancelable(false) ;
        alertDialog  = alert.create() ;
        alertDialog.show();
        ok.setOnClickListener(view1 -> {
            if (alertDialog.isShowing()){
                dismiss();
                Intent intent = new Intent(context , HomeActivity.class);
                context.startActivity(intent);
                activity.finish();
            }
        });
    }
    public  void dismiss(){
        alertDialog.dismiss();
    }
}
