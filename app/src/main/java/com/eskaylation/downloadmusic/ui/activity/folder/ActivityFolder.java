package com.eskaylation.downloadmusic.ui.activity.folder;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import com.eskaylation.downloadmusic.R;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.eskaylation.downloadmusic.adapter.FolderAdapter;
import com.eskaylation.downloadmusic.adapter.FolderAdapter.OnItemFolderClickListener;
import com.eskaylation.downloadmusic.base.BaseActivityLoader;
import com.eskaylation.downloadmusic.model.AdsManager;
import com.eskaylation.downloadmusic.model.Folder;
import com.eskaylation.downloadmusic.model.Song;
import com.eskaylation.downloadmusic.ui.activity.ListSongActivity;
import com.eskaylation.downloadmusic.ui.activity.folder.loader.ScanningFolderAsync;



import com.eskaylation.downloadmusic.utils.PreferenceUtils;
import com.kunkun.mp3player.ui.fragment.main.folder_selected.loader.ScaningFolderListener;
import java.util.ArrayList;

public class ActivityFolder extends BaseActivityLoader implements OnItemFolderClickListener, ScaningFolderListener {
    @BindView(R.id.ad_view_container)
    public FrameLayout adView;
    public FolderAdapter adapter;
    @BindView(R.id.coordinator)
    public CoordinatorLayout coordinator;
    public LinearLayoutManager llManager;
    public ArrayList<Folder> lstFolder = new ArrayList<>();
    public ScanningFolderAsync mFolderAsync;
    public long mLastClickTime;
    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    public Window mWindow;
    @BindView(R.id.rv_folder)
    public RecyclerView rv_folder;

    public void setRingtone(Song song) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.fragment_folder);
        this.mWindow = getWindow();
        this.mWindow.getDecorView().setSystemUiVisibility(1280);
        init();
        loadBanner(this,adView);
        this.mToolbar.setNavigationOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                ActivityFolder.this.lambda$onCreate$0$ActivityFolder(view);
            }
        });
    }

    public /* synthetic */ void lambda$onCreate$0$ActivityFolder(View view) {
        super.onBackPressed();
    }

    public void init() {
        ButterKnife.bind(this);
        setBackgroundThemes(this.coordinator, PreferenceUtils.getInstance(this).getThemesPosition());
        this.adapter = new FolderAdapter(this, this);
        this.llManager = new LinearLayoutManager(this);
        this.rv_folder.setLayoutManager(this.llManager);
        this.rv_folder.setHasFixedSize(true);
        this.rv_folder.setAdapter(this.adapter);
    }

    public void onResume() {
        super.onResume();
        loader();
    }

    public void onStop() {
        super.onStop();
    }

    public void onFolderClick(int i, final Folder folder, ImageView imageView, TextView textView, TextView textView2) {
        if (SystemClock.elapsedRealtime() - this.mLastClickTime >= 1000) {


            AdsManager.showNext(this, new AdsManager.AdCloseListener() {
                @Override
                public void onAdClosed() {
                    Intent intent = new Intent(ActivityFolder.this, ListSongActivity.class);
                    intent.putExtra("folder_extra", folder);
                    ActivityFolder.this.startActivity(intent);
                }
            });

            this.mLastClickTime = SystemClock.elapsedRealtime();
        }
    }

    public void loader() {
        this.mFolderAsync = new ScanningFolderAsync(this, this);
        this.mFolderAsync.execute(new Void[0]);
    }

    public void onScanningSuccessFull(ArrayList<Folder> arrayList) {
        this.lstFolder.clear();
        this.lstFolder.addAll(arrayList);
        this.adapter.setData(this.lstFolder);
    }
}
