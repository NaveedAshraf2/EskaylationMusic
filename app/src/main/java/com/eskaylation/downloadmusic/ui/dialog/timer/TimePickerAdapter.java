package com.eskaylation.downloadmusic.ui.dialog.timer;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import java.util.ArrayList;
import kotlin.jvm.internal.Intrinsics;


public final class TimePickerAdapter extends Adapter<TimePickerViewHolder> {
    public final Context context;
    public int lastPos;
    public ArrayList<String> listTime = new ArrayList<>();

    public TimePickerAdapter(Context context2) {
        this.context = context2;
    }

    public TimePickerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Intrinsics.checkParameterIsNotNull(viewGroup, "parent");
        LayoutInflater from = LayoutInflater.from(this.context);
        Context context2 = this.context;
        Intrinsics.checkExpressionValueIsNotNull(from, "layoutInflater");
        return new TimePickerViewHolder(context2, from, viewGroup);
    }

    public int getItemCount() {
        return this.listTime.size();
    }

    public void onBindViewHolder(TimePickerViewHolder timePickerViewHolder, int i) {
        Intrinsics.checkParameterIsNotNull(timePickerViewHolder, "holder");
        Object obj = this.listTime.get(i);
        Intrinsics.checkExpressionValueIsNotNull(obj, "listTime.get(position)");
        timePickerViewHolder.bind((String) obj);
        if (i == this.lastPos) {
            timePickerViewHolder.getTvTime().setTextColor(-16777216);
        } else {
            timePickerViewHolder.getTvTime().setTextColor(Color.parseColor("#bfbdbd"));
        }
        Integer.valueOf(i);
    }

    public final void setPos(int i) {
        this.lastPos = i;
        notifyDataSetChanged();
    }

    public final void initlistHour() {
        for (int i = 0; i <= 60; i++) {
            this.listTime.add(String.valueOf(i));
        }
        notifyDataSetChanged();
    }

    public final void initListMinute() {
        for (int i = 0; i <= 60; i++) {
            this.listTime.add(String.valueOf(i));
        }
        notifyDataSetChanged();
    }
}
