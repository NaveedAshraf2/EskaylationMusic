package com.eskaylation.downloadmusic.Other;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.model.AdsManager;
import com.eskaylation.downloadmusic.ui.activity.plash.LoadingActivity;
import com.eskaylation.downloadmusic.ui.activity.plash.StartActivity;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WelcomeClass extends AppCompatActivity {
    TextView name_textview,link,desc_textview;
    RelativeLayout constraintLayout;
    String str_appimage,str_appid,str_appname,str_appdesc,str_applink="",str_appstatus;
    String str_bannerimage,
    str_bannerlink;
    ImageView playclick;
    ProgressDialog alertDialog;
    public static final String PREF_FILE= "MyPreflink";
    public static final String IMAGE_KEY= "imagelink";
    public static final String CLICK_KEY= "storelink";
    ImageView icon;
    AVLoadingIndicatorView avLoadingIndicatorView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        playclick=(ImageView)findViewById(R.id.linkbutton);
        icon=(ImageView)findViewById(R.id.icon);
        name_textview=(TextView)findViewById(R.id.name);
        avLoadingIndicatorView=(AVLoadingIndicatorView)findViewById(R.id.avi) ;
        desc_textview=(TextView)findViewById(R.id.desc);
        alertDialog=new ProgressDialog(WelcomeClass.this);
        constraintLayout=(RelativeLayout)findViewById(R.id.layout);

        /*
        playclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(str_applink.equals(""))
                {
                    savePurchaseValueToPref("","");
                    startActivity(new Intent(WelcomeClass.this, StartActivity.class));
                    finish();
                }
                else{
                    final String appPackageName = str_applink; // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            }
        });

         */
        savePurchaseValueToPref("","");
        startActivity(new Intent(WelcomeClass.this, StartActivity.class));
        finish();

    }

    public  void fetchdata()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,ConfigLink.CONIG_LINK,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("response",s.toString());
                        //   progressBar.setVisibility(View.INVISIBLE);
                        //alertDialog.hide();
                        avLoadingIndicatorView.show();
                        ///Toast.makeText(getActivity(), s.toString(), Toast.LENGTH_SHORT).show();
                        String type="";
                        if(s.equals("0"))
                        {

                        }

                        else {
                            try {
                                JSONObject object = new JSONObject(s);

                                JSONArray jArray = object.getJSONArray("result");
                                for (int i = 0; i < jArray.length(); i++) {
                                    JSONObject json = jArray.getJSONObject(i);

                                    /*
                                    {"result":[{"appid":"1","appname":"Mp3Juice Music Downloader","appimage":
                                    "icon.jpg","applink":"https:\/\/play.google.com\/store\/apps\/details?id=free.mp3juice.musicdownloader",
                                    "appdesc":"Mp3juice - Mp3 Juice Free Music Downloader is a best app on Android Phone
                                    which allows you to play and download music into your phone for free.",
                                    "appstatus":"1",
                                    "bannerimage":"promotion.jpg",
                                    "bannerlink":"https:\/\/play.google.com\/store\/apps\/details?id=com.Okappz.girlywallpapers"}]}

                                     */

                                     str_appid=json.getString("appid");
                                     str_appname=json.getString("appname");
                                    str_appimage=json.getString("appimage");
                                     str_appdesc=json.getString("appdesc");
                                     str_applink=json.getString("applink");
                                     str_appstatus=json.getString("appstatus");
                                    str_bannerimage=json.getString("bannerimage");
                                    str_bannerlink=json.getString("bannerlink");
                                    fill_details(str_appid,str_appname,str_appdesc,str_applink,str_appstatus
                                    ,str_bannerimage,str_bannerlink);
                                    savePurchaseValueToPref(str_bannerimage,str_bannerlink);



                                    Picasso.get().load(ConfigLink.CONIG_LINK_IMAGES+str_appimage).into(icon);

                                    if(str_appstatus.equals("1"))
                                    {
                                        constraintLayout.setVisibility(View.VISIBLE);
                                    }
                                    else{
                                        constraintLayout.setVisibility(View.GONE);
                                        startActivity(new Intent(WelcomeClass.this, StartActivity.class));
                                        finish();


                                    }

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
               // Toast.makeText(WelcomeClass.this,"No data from server", Toast.LENGTH_SHORT).show();
                ///intent kholna ha
                savePurchaseValueToPref("","");
                startActivity(new Intent(WelcomeClass.this, StartActivity.class));
                finish();
            }
        })
        {

        };
        int socketTimeout = 15000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        avLoadingIndicatorView.hide();
        //progressBar.setVisibility(View.VISIBLE);
      //  alertDialog.setTitle("Loading data...");
      //  alertDialog.setMessage("Please Wait... ");
      //  alertDialog.show();
        //AppController.getInstance(WelcomeClass.this).addToRequest(stringRequest);
    }
    private SharedPreferences.Editor getPreferenceEditObject() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_FILE, 0);
        return pref.edit();
    }
    private void savePurchaseValueToPref(String image1,String storelink){
        getPreferenceEditObject().putString(IMAGE_KEY,image1).putString(CLICK_KEY,storelink).commit();
    }
    private SharedPreferences getPreferenceObject() {
        return getApplicationContext().getSharedPreferences(PREF_FILE, 0);
    }

public  void fill_details(String param_str_appid,String param_str_appname,
                          String param_str_appdesc,String param_str_applink,String param_str_appstatus
                                    ,String param_str_bannerimage,String param_str_bannerlink)
            {

            desc_textview.setText(param_str_appdesc);
            name_textview.setText(param_str_appname);
            str_applink=param_str_applink;
            }
}
