package com.infowindtech.eguru;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.infowindtech.eguru.R;
import com.infowindtech.eguru.TeacherPanel.Fragments.Personal_Registration_Fragment;

import java.util.Locale;

public class AlertClass {

    public static boolean checkConnection(Context context) {

        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }
        return false;
    }


    public static void alertDialogShow(Context context, String message)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage(message);
        alertDialog.setButton(context.getString(R.string.ok), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
    public static void hideKeybord(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
    public static void DispToast(Context context, String msg){

        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
    }
    public static void setLocale(Context context,String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        conf.setLayoutDirection(new Locale(lang));
        res.updateConfiguration(conf, dm);
    }
    public static void signupdialog(final Context context, String message)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage(message);
        alertDialog.setButton(Dialog.BUTTON_POSITIVE,context.getString(R.string.ok), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                alertDialog.dismiss();
                Intent in=new Intent(context,Personal_Registration_Fragment.class);
                in.putExtra("go","student");
                context.startActivity(in);
            }
        });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE,context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
