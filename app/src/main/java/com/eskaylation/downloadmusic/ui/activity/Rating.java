package com.eskaylation.downloadmusic.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eskaylation.downloadmusic.R;

public class Rating extends AppCompatActivity {
    private RatingBar ratingBar;
    private Button submitButton;
    private TextView rate_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        ratingBar = findViewById(R.id.rating);
        submitButton = findViewById(R.id.submit_button);
        rate_tv = findViewById(R.id.rate_tv);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitClicked();
            }
        });


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    if (rating==.5||rating==1){
                        rate_tv.setText("Poor");
                    }else if (rating==1.5||rating==2){
                        rate_tv.setText("Not Good");
                    }else if (rating==2.5 || rating==3){
                        rate_tv.setText("Little Good");
                    }else if (rating==3.5 || rating==4){
                        rate_tv.setText("Very Good");
                    }else if (rating==4.5 || rating==5){
                        rate_tv.setText("Excellent");
                    }
                }
            }
        });


    }
    private void onSubmitClicked() {
        float rating = ratingBar.getRating();



        if (rating >= 4.0f) {
            openPlayStoreForRating();

        } else {
            showFeedbackDialog();
        }
    }

    private void openPlayStoreForRating() {
        String packageName = getPackageName();
        String url = "market://details?id=" + packageName;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Play Store app is not installed
            String webUrl = "https://play.google.com/store/apps/details?id=" + packageName;
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
            startActivity(webIntent);
        }
    }

    public void sendEmail(String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact@fahadhanif.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Flow Download Help Center!");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setPackage("com.google.android.gm");
        startActivity(intent);
    }

    private void showFeedbackDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Feedback");
        dialogBuilder.setMessage("Please provide your feedback:");
        final EditText editText = new EditText(this);
        dialogBuilder.setView(editText);
        dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String feedback = editText.getText().toString();
                sendEmail(feedback);
//                showFeedbackToast(feedback);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void showFeedbackToast(String feedback) {
        Toast.makeText(this, "Feedback: " + feedback, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("WrongConstant")
    public void rateInStore() {
        StringBuilder sb = new StringBuilder();
        sb.append("market://details?id=");
        sb.append(getPackageName());
        String str = "android.intent.action.VIEW";
        Intent intent = new Intent(str, Uri.parse(sb.toString()));
        intent.addFlags(1208483840);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("http://play.google.com/store/apps/details?id=");
            sb2.append(getPackageName());
            startActivity(new Intent(str, Uri.parse(sb2.toString())));
        }
    }
}