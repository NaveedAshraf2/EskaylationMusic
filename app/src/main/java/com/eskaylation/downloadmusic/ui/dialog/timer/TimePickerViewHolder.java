package com.eskaylation.downloadmusic.ui.dialog.timer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.eskaylation.downloadmusic.R;

import kotlin.jvm.internal.Intrinsics;


public final class TimePickerViewHolder extends ViewHolder {
    public TextView tvTime;

    public TimePickerViewHolder(Context context, LayoutInflater layoutInflater, ViewGroup viewGroup) {
        super(layoutInflater.inflate(R.layout.item_time_picker, viewGroup, false));
        Intrinsics.checkParameterIsNotNull(layoutInflater, "inflater");
        Intrinsics.checkParameterIsNotNull(viewGroup, "parent");

        View findViewById = this.itemView.findViewById(R.id.tv_Time);
        Intrinsics.checkExpressionValueIsNotNull(findViewById, "itemView.findViewById(R.id.tv_Time)");
        this.tvTime = (TextView) findViewById;
    }

    public final TextView getTvTime() {
        return this.tvTime;
    }

    public final void bind(String str) {
        Intrinsics.checkParameterIsNotNull(str, "time");
        if (str.length() == 1) {
            TextView textView = this.tvTime;
            StringBuilder sb = new StringBuilder();
            sb.append("0");
            sb.append(str);
            textView.setText(sb.toString());
            return;
        }
        this.tvTime.setText(str);
    }
}
