package com.bwi.onboard.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bwi.onboard.R;

public class DialogUtils {

    public static void showNoInternetDialog(Context context, View customView, String title, String message, Runnable onRetry) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        if (customView != null) {
            // Set up the custom view content
            TextView titleView = customView.findViewById(R.id.dialogTitle);
            TextView messageView = customView.findViewById(R.id.dialogMessage);

            if (titleView != null && title != null) titleView.setText(title);
            if (messageView != null && message != null) messageView.setText(message);

            // Retry button
            View retryButton = customView.findViewById(R.id.buttonRetry);
            if (retryButton != null) {
                retryButton.setOnClickListener(v -> {
                    dialog.dismiss();
                    if (onRetry != null) onRetry.run();
                });
            }

            // Exit button
            View exitButton = customView.findViewById(R.id.buttonExit);
            if (exitButton != null) {
                exitButton.setOnClickListener(v -> {
                    dialog.dismiss();
                    ((Activity) context).finish();
                });
            }

            builder.setView(customView);
        } else {
            // Fallback default dialog
            builder.setTitle(title != null ? title : "No Internet Connection")
                    .setMessage(message != null ? message : "Please check your connection and try again.")
                    .setPositiveButton("Retry", (dialog, which) -> {
                        dialog.dismiss();
                        if (onRetry != null) onRetry.run();
                    })
                    .setNegativeButton("Exit", (dialog, which) -> {
                        dialog.dismiss();
                        ((Activity) context).finish();
                    });
        }

        dialog = builder.create();
        dialog.show();
    }

    private static AlertDialog dialog;
}
