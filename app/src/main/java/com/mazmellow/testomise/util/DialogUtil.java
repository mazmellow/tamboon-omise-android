package com.mazmellow.testomise.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.mazmellow.testomise.R;

/**
 * Created by Maz on 9/12/2017 AD.
 */
public class DialogUtil {

    public void showInternetAlert(Activity activity) {
        showAlertOneButton(activity, activity.getResources().getString(R.string.please_connect_internet), null, null);
    }

    public void showToast(String wording, boolean isLongToast, Activity activity) {
        int showLong = isLongToast ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Toast.makeText(activity, wording, showLong).show();
    }

    public static void showAlertThreeButtons(Activity activity, String title, String message
            , DialogInterface.OnClickListener positiveListener, String positiveMessage
            , DialogInterface.OnClickListener neutralListener, String neutralMessage
            , DialogInterface.OnClickListener negativeListener, String negativeMessage) {

        AlertDialog.Builder builder = new AlertDialog.Builder(
                activity);
        builder.setTitle(title);
        builder.setCancelable(false);
        if (message != null) {
            builder.setMessage(message);
        }

        if (positiveListener != null) {
            builder.setPositiveButton(positiveMessage, positiveListener);
        } else {
            builder.setPositiveButton(positiveMessage, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }

        if (neutralListener != null) {
            builder.setNeutralButton(neutralMessage, neutralListener);
        } else {
            builder.setNeutralButton(neutralMessage, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }

        if (negativeListener != null) {
            builder.setNegativeButton(negativeMessage, negativeListener);
        } else {
            builder.setNegativeButton(negativeMessage, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }

        builder.show();
    }

    public static void showAlert(Activity activity, String title, String message, DialogInterface.OnClickListener positiveListener, String positiveMessage, DialogInterface.OnClickListener negativeListener, String negativeMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                activity);
        builder.setTitle(title);
        builder.setCancelable(false);
        if (message != null) {
            builder.setMessage(message);
        }

        if (positiveListener != null) {
            builder.setPositiveButton(positiveMessage, positiveListener);
        } else {
            builder.setPositiveButton(positiveMessage, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        if (negativeListener != null) {
            builder.setNegativeButton(negativeMessage, negativeListener);
        } else {
            builder.setNegativeButton(negativeMessage, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }

        builder.show();
    }

    public static void showAlert(Activity activity, String title, String message, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                activity);
        builder.setTitle(title);
        builder.setCancelable(false);
        if (message != null) {
            builder.setMessage(message);
        }

        String ok = activity.getString(R.string.ok);
        String cancel = activity.getString(R.string.cancel);

        if (positiveListener != null) {
            builder.setPositiveButton(ok, positiveListener);
        } else {
            builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        if (negativeListener != null) {
            builder.setNegativeButton(cancel, negativeListener);
        } else {
            builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }

        builder.show();
    }

    public static void showAlertOneButton(Activity activity, String title, String message, DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                activity);
        builder.setTitle(title);
        builder.setCancelable(false);
        if (message != null) {
            builder.setMessage(message);
        }
        if (positiveListener != null) {
            builder.setPositiveButton(activity.getResources().getString(R.string.ok), positiveListener);
        } else {
            builder.setPositiveButton(activity.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }

        builder.show();
    }
}
