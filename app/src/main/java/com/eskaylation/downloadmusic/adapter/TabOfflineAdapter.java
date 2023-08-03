package com.eskaylation.downloadmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.balysv.materialripple.MaterialRippleLayout;
import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.model.TabModel;
import com.eskaylation.downloadmusic.utils.AppConstants;
import java.util.ArrayList;

public class TabOfflineAdapter extends Adapter<TabOfflineAdapter.TabHolder> {
    public Context mContext;
    public OnTabOfflineClickListener mListener;
    public ArrayList<TabModel> mTabModels;

    public interface OnTabOfflineClickListener {
        void onTabClick(int i);
    }

    public class TabHolder extends ViewHolder {
        @BindView(R.id.imgBg)
        public ImageView imgBg;
        @BindView(R.id.imgThumbTab)
        public ImageView imgThumbTab;
        @BindView(R.id.ripple)
        public MaterialRippleLayout ripple;
        @BindView(R.id.tab_title)
        public TextView tabTitle;

        public TabHolder(TabOfflineAdapter tabOfflineAdapter, View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindData(TabModel tabModel) {
            this.tabTitle.setText(tabModel.name);
            this.imgThumbTab.setImageResource(tabModel.resource);
            this.imgBg.setImageResource(tabModel.background);
        }
    }



    public TabOfflineAdapter(Context context, OnTabOfflineClickListener onTabOfflineClickListener) {
        this.mContext = context;
        this.mTabModels = AppConstants.getListTab(context);
        this.mListener = onTabOfflineClickListener;
    }

    public TabHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new TabHolder(this, LayoutInflater.from(this.mContext).
                inflate(R.layout.item_offline, viewGroup, false));
    }

    public void onBindViewHolder(TabHolder tabHolder, int i) {
        tabHolder.bindData((TabModel) this.mTabModels.get(i));
        tabHolder.ripple.setOnClickListener(new OnClickListener() {
      
            public final void onClick(View view) {
                TabOfflineAdapter.this.TabOfflineAdapter0(i, view);
            }
        });
    }

    public  void TabOfflineAdapter0(int i, View view) {
        this.mListener.onTabClick(i);
    }

    public int getItemCount() {
        return this.mTabModels.size();
    }
}
