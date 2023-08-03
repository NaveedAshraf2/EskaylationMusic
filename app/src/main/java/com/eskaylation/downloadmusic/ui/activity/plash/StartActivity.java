package com.eskaylation.downloadmusic.ui.activity.plash;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.eskaylation.downloadmusic.base.BaseActivity;
import com.eskaylation.downloadmusic.base.BaseApplication;

//import com.google.firebase.analytics.FirebaseAnalytics;

public class StartActivity extends BaseActivity {
    //public FirebaseAnalytics mFirebaseAnalytics;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
      //  this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle2 = new Bundle();
        bundle2.putString("BRAND", Build.BRAND);
        bundle2.putString("MODEL", Build.MODEL);
    //    this.mFirebaseAnalytics.logEvent("select_content", bundle2);
        
     //   BaseApplication.appOpenManager.fetchAd();
        startActivity(new Intent(this, LoadingActivity.class));
        finish();
    }
}
