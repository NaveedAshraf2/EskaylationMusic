package com.eskaylation.downloadmusic.Other;



import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController {

    public  static  AppController mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;


    private AppController(Context context)
    {
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    public  static  synchronized AppController getInstance(Context context)
    {
        if(mInstance==null)
        {
            mInstance = new AppController(context);
        }
        return  mInstance;
    }
    public RequestQueue getRequestQueue()
    {
        if(requestQueue==null)
        {
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());

        }
        return  requestQueue;
    }

    public  <T> void addToRequest(Request<T> request)
    {
        requestQueue.add(request);
    }


}