package com.eskaylation.downloadmusic.net;

import android.content.Context;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.eskaylation.downloadmusic.listener.SugestionsInterface;
import com.eskaylation.downloadmusic.listener.SusguestionListerner;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class RequestSugestion {
    public Context context;
    public SusguestionListerner susguestionListerner;

    public static  void lambda$querySearch$0(VolleyError volleyError) {
    }

    public RequestSugestion(Context context2, SugestionsInterface sugestionsInterface) {
        this.context = context2;
        this.susguestionListerner = new SusguestionListerner(sugestionsInterface);
    }

    public void querySearch(String str) {
        String replaceAll = str.replaceAll(" ", "%20");
        StringBuilder sb = new StringBuilder();
        sb.append("http://suggestqueries.google.com/complete/search?client=toolbar&q=");
        sb.append(replaceAll);
        VolleySingleton.getInstance(this.context).addToRequestQueue(
                new StringRequest(0, sb.toString(), new Listener() {
            public final void onResponse(Object obj) {
                RequestSugestion.this.parseXML((String) obj);
            }
        }, RequestSugestion0.INSTANCE));
    }


    public final void parseXML(String str) {
        DocumentBuilder documentBuilder;
        ArrayList arrayList = new ArrayList();
        arrayList.clear();
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            documentBuilder = null;
        }
        if (documentBuilder != null) {
            try {
                Document parse = documentBuilder.parse(new InputSource(new StringReader(str)));
                if (parse != null) {
                    try {
                        parse.getDocumentElement().normalize();
                        NodeList elementsByTagName = parse.getElementsByTagName("suggestion");
                        for (int i = 0; i < elementsByTagName.getLength(); i++) {
                            arrayList.add(elementsByTagName.item(i).getAttributes().getNamedItem("data").getNodeValue());
                        }
                        this.susguestionListerner.sugestionsInterface.onSuccesSearch(arrayList);
                    } catch (Exception unused) {
                    }
                }
            } catch (IOException | SAXException e2) {
                e2.printStackTrace();
            }
        }
    }




}
