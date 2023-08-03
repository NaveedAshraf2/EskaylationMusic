package com.eskaylation.downloadmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.listener.OnClickedItemSearchListener;
import java.util.ArrayList;

public class SugestionsAdapter extends BaseAdapter {
    public Context context;
    public ArrayList<String> lst;
    public OnClickedItemSearchListener onClickedItemSearchListener;

    public long getItemId(int i) {
        return 0;
    }

    public SugestionsAdapter(Context context2, ArrayList<String> arrayList, OnClickedItemSearchListener onClickedItemSearchListener2) {
        this.context = context2;
        this.lst = arrayList;
        this.onClickedItemSearchListener = onClickedItemSearchListener2;
    }

    public int getCount() {
        ArrayList<String> arrayList = this.lst;
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    public Object getItem(int i) {
        return this.lst.get(i);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View inflate = ((LayoutInflater) this.context.getSystemService("layout_inflater")).
                inflate(R.layout.item_search, null);
        TextView textView = (TextView) inflate.findViewById(R.id.txt_suggestion);
        textView.setText((CharSequence) this.lst.get(i));
        textView.setOnClickListener(new OnClickListener() {
      

            public final void onClick(View view) {
                SugestionsAdapter.this.SugestionsAdapter0(i, view);
            }
        });
        return inflate;
    }

    public  void SugestionsAdapter0(int i, View view) {
        this.onClickedItemSearchListener.onItemClicked.onClick((String) this.lst.get(i));
    }
}
