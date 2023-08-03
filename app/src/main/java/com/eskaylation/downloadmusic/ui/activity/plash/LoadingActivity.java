package com.eskaylation.downloadmusic.ui.activity.plash;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import butterknife.ButterKnife;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest.GetRequestBuilder;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.eskaylation.downloadmusic.adapter.Connection;
import com.eskaylation.downloadmusic.base.BaseActivity;
import com.eskaylation.downloadmusic.model.AdsManager;
import com.eskaylation.downloadmusic.ui.activity.main.MainActivity;
import com.eskaylation.downloadmusic.ui.activity.permission.PermissionActivity;

import com.eskaylation.downloadmusic.utils.AppUtils;

import com.eskaylation.downloadmusic.utils.ConfigApp;
import com.google.android.exoplayer2.extractor.ts.AdtsReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.eskaylation.downloadmusic.R;
public class LoadingActivity extends BaseActivity {
    public CountDownTimer mCountDownTimer;

    RelativeLayout btnupdate,splash;

    public static String AppEnable = "AppEnable";
    public static String PlayStoreLink = "PlayStoreLink";
    public static String Networkxx = "123";

    public static String OPENAPP_ID_ADMOB = "123";
    public static String NATIVE_ID_ADMOB = "123";
    public static String BANNER_ID_ADMOB = "123";
    public static String INTERS_ID_ADMOB = "123";

    public static String NATIVE_ID_APPLOVIN = "123";
    public static String BANNER_ID_APPLOVIN = "123";
    public static String INTERS_ID_APPLOVIN= "123";

    public static String NATIVE_ID_FACEBOOK = "123";
    public static String BANNER_ID_FACEBOOK = "123";
    public static String INTERS_ID_FACEBOOK= "123";


    String zaib="{\n" +
            "\t\"version_code\":13,\n" +
            "\t\"require_update\": false,\n" +
            "\t\"hideRate\":false,\n" +
            "\t\"showAds\":true,\n" +
            "\t\"showAds2\":true,\n" +
            "\t\"data_type\":0\n" +
            "}";
    public void onCreate(Bundle bundle) {
          final int MATCH_STATE_VALUE_SHIFT = 8;
        getWindow().setFlags(2 << MATCH_STATE_VALUE_SHIFT, 2 << MATCH_STATE_VALUE_SHIFT);
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_start);
        init();
        ConfigApp.getInstance(LoadingActivity.this).putConfigApp(zaib);
        btnupdate = findViewById(R.id.btnUpdate);
        splash = findViewById(R.id.splash);
        if(Connection.isConnected(this)) {
            AndroidNetworking.post("https://thefahadhanif.com/develop.php")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            JSONObject jsonObject;
                            try {
                                jsonObject = response.getJSONObject(0);
                                AppEnable = jsonObject.getString("AppEnable");
                                PlayStoreLink = jsonObject.getString("PlayStoreLink");

                                Networkxx = jsonObject.getString("Network");

                                OPENAPP_ID_ADMOB = jsonObject.getString("OPENAPP_ID_ADMOB");
                                NATIVE_ID_ADMOB = jsonObject.getString("NATIVE_ID_ADMOB");
                                BANNER_ID_ADMOB = jsonObject.getString("BANNER_ID_ADMOB");
                                INTERS_ID_ADMOB = jsonObject.getString("INTERS_ID_ADMOB");

                                NATIVE_ID_APPLOVIN = jsonObject.getString("NATIVE_ID_APPLOVIN");
                                BANNER_ID_APPLOVIN = jsonObject.getString("BANNER_ID_APPLOVIN");
                                INTERS_ID_APPLOVIN = jsonObject.getString("INTERS_ID_APPLOVIN");

                                NATIVE_ID_FACEBOOK = jsonObject.getString("NATIVE_ID_FACEBOOK");
                                BANNER_ID_FACEBOOK = jsonObject.getString("BANNER_ID_FACEBOOK");
                                INTERS_ID_FACEBOOK = jsonObject.getString("INTERS_ID_FACEBOOK");

                                if(AppEnable.equalsIgnoreCase("true")){

                                    LoadingActivity.this.checkFinishPermission();

                                }
                                else
                                {
                                    splash.setVisibility(View.GONE);
                                    btnupdate.setVisibility(View.VISIBLE);
                                    findViewById(R.id.download).setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            try {
                                                Uri marketUri = Uri.parse(PlayStoreLink);
                                                Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                                                startActivity(marketIntent);
                                            }catch(ActivityNotFoundException e) {
                                                Toast.makeText(LoadingActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                LoadingActivity.this.checkFinishPermission();

                            }

                        }

                        @Override
                        public void onError(ANError anError) {
                            LoadingActivity.this.checkFinishPermission();
                        }
                    });

        }
        else
        {
            LoadingActivity.this.checkFinishPermission();
            Toast.makeText(LoadingActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
        }

       // requestCheckUpdate();

    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onPause() {
        super.onPause();
        CountDownTimer countDownTimer = this.mCountDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            finish();
        }
    }

    public void showUpdatePopUp() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_update);
        Window window = dialog.getWindow();
        LayoutParams attributes = window.getAttributes();
        attributes.gravity = 17;
        attributes.flags &= -5;
        window.setAttributes(attributes);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        dialog.getWindow().setLayout(-1, -2);
        ((Button) dialog.findViewById(R.id.btnUpdate)).setOnClickListener(new OnClickListener() {
          

            public final void onClick(View view) {
                LoadingActivity.this.lambda$showUpdatePopUp$0$LoadingActivity(dialog, view);
            }
        });
        dialog.show();
    }

    public /* synthetic */ void lambda$showUpdatePopUp$0$LoadingActivity(Dialog dialog, View view) {
        openAppInGooglePlay(getPackageName());
        dialog.dismiss();
        finish();
    }

    public void checkFinishPermission() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == -1) {
            startActivity(new Intent(this, PermissionActivity.class));
            finish();
        } else if (AppUtils.isOnline(this)) {
            CountDownTimer r1 = new CountDownTimer(2500, 1) {
                public void onTick(long j) {
                }

                public void onFinish() {

                    Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                    intent.putExtra("show_ads", true);
                    AdsManager.init(LoadingActivity.this,intent);
                }
            };
            this.mCountDownTimer = r1;
            this.mCountDownTimer.start();
        } else {
            AdsManager.init(LoadingActivity.this,new Intent(this, MainActivity.class));
            //finish();
        }
    }

    public void init() {
        ButterKnife.bind(this);
    }
}
