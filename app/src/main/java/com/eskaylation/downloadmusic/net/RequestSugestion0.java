package com.eskaylation.downloadmusic.net;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;


public final  class RequestSugestion0 implements ErrorListener {
    public static final  RequestSugestion0 INSTANCE = new RequestSugestion0();

    private  RequestSugestion0() {
    }

    public final void onErrorResponse(VolleyError volleyError) {
        RequestSugestion.lambda$querySearch$0(volleyError);
    }
}
