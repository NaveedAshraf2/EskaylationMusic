package com.eskaylation.downloadmusic.ui.activity.search;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.eskaylation.downloadmusic.adapter.OnlineAudioAdapter;
import com.eskaylation.downloadmusic.adapter.OnlineAudioAdapter.OnItemSelected;
import com.eskaylation.downloadmusic.adapter.OnlineAudioAdapter.OnLoadMoreListener;
import com.eskaylation.downloadmusic.adapter.SugestionsAdapter;
import com.eskaylation.downloadmusic.base.BaseActivity;
import com.eskaylation.downloadmusic.listener.LoadMoreListener;
import com.eskaylation.downloadmusic.listener.OnClickedItemSearchListener;
import com.eskaylation.downloadmusic.listener.OnItemClickedSearch;
import com.eskaylation.downloadmusic.listener.SugestionsInterface;
import com.eskaylation.downloadmusic.model.AudioExtract;
import com.eskaylation.downloadmusic.net.RequestSugestion;
import com.eskaylation.downloadmusic.net.SearchUtils;
import com.eskaylation.downloadmusic.net.listener.OnSearchResult;
import com.eskaylation.downloadmusic.service.MusicPlayerService;
import com.eskaylation.downloadmusic.service.MusicPlayerService.MusicServiceBinder;
import com.eskaylation.downloadmusic.ui.activity.PlayerActivity;
import com.eskaylation.downloadmusic.utils.AppUtils;

import com.eskaylation.downloadmusic.utils.PreferenceUtils;
import com.eskaylation.downloadmusic.utils.ToastUtils;
import com.eskaylation.downloadmusic.widget.PlayerView;
import com.wang.avi.AVLoadingIndicatorView;
import java.util.ArrayList;
import org.schabi.newpipe.extractor.Page;
import com.eskaylation.downloadmusic.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SearchActivity extends BaseActivity implements SugestionsInterface, OnItemClickedSearch, OnSearchResult, OnItemSelected, OnLoadMoreListener, LoadMoreListener {
   @BindView(R.id.ad_view_container)
    public FrameLayout adView;
   @BindView(R.id.btnBack)
    ImageButton btnBack;
    public ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ((MusicServiceBinder) iBinder).getService().initViewSearch(SearchActivity.this.searchPlayerView);
            SearchActivity.this.mBound = true;
        }

        public void onServiceDisconnected(ComponentName componentName) {
            SearchActivity.this.mBound = false;
        }
    };
    @BindView(R.id.coordinator)
    public CoordinatorLayout coordinator;
    public boolean mBound = false;
    @BindView(R.id.edt_search)
    public EditText mEtSearch;

    public long mLastClickTime;
    public String mLastKeyword;
    @BindView(R.id.progress_bar)
    public AVLoadingIndicatorView mProgressBar;
    @BindView(R.id.tv_empty)
    public TextView mTvEmpty;
    public OnlineAudioAdapter mYoutubeVideoAdapter;
    public Page nextPage;
    public OnClickedItemSearchListener onItemClickedSearch;
    public RequestSugestion requestSugestion;
@BindView(R.id.rv_search_complete)

    public ListView rvSearchSuggest;
@BindView(R.id.recycler_view)
    public RecyclerView rv_video_search;
    @BindView(R.id.viewPlayer)
    public PlayerView searchPlayerView;
    public SearchUtils searchUtils;
    public TextWatcher textWatcher;

    public void onStart() {
        super.onStart();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_search);
        init();
        loadBanner(this,adView);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBack();
            }
        });
    }

    public void onResume() {
        bindService();
        super.onResume();
    }

    public void onClickBack() {
        onBackPressed();
    }

    public void onClickSearch() {
        this.rvSearchSuggest.setVisibility(GONE);
        this.rv_video_search.setVisibility(VISIBLE);
        if (TextUtils.isEmpty(getQueryKeyword())) {
            ToastUtils.error((Context) this, getString(R.string.enter_search_text));
        } else {
            onBtnSearchClick(getQueryKeyword());
        }
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public void init() {
        ButterKnife.bind(this);

        setBackgroundThemes(this.coordinator, PreferenceUtils.getInstance(this).getThemesPosition());

        this.searchPlayerView.setPlayerListener(this);
        this.requestSugestion = new RequestSugestion(this, this);
        this.onItemClickedSearch = new OnClickedItemSearchListener(this);
        this.searchUtils = new SearchUtils(this, this);
        this.textWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                SearchActivity.this.mTvEmpty.setVisibility(GONE);
            }

            public void afterTextChanged(Editable editable) {
                if (editable.length() < 20) {
                    SearchActivity.this.requestSugestion.querySearch(String.valueOf(editable));
                }
                if (TextUtils.isEmpty(SearchActivity.this.mEtSearch.getText())) {
                    SearchActivity.this.mLastKeyword = null;
                    SearchActivity.this.rvSearchSuggest.setVisibility(GONE);
                    SearchActivity.this.rv_video_search.setVisibility(VISIBLE);
                    return;
                }
                SearchActivity.this.rvSearchSuggest.setVisibility(VISIBLE);
                SearchActivity.this.rv_video_search.setVisibility(GONE);
            }
        };
        this.mEtSearch.addTextChangedListener(this.textWatcher);
        this.mEtSearch.setOnTouchListener(new OnTouchListener() {
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                return SearchActivity.this.lambda$init$0$SearchActivity(view, motionEvent);
            }
        });
        this.mEtSearch.setOnKeyListener(new OnKeyListener() {
            public final boolean onKey(View view, int i, KeyEvent keyEvent) {
                return SearchActivity.this.lambda$init$1$SearchActivity(view, i, keyEvent);
            }
        });
        this.mYoutubeVideoAdapter = new OnlineAudioAdapter(this, this);
        this.mYoutubeVideoAdapter.setLoadMore(this);
        this.rv_video_search.setAdapter(this.mYoutubeVideoAdapter);
        this.rv_video_search.setHasFixedSize(true);
        this.rv_video_search.setAdapter(this.mYoutubeVideoAdapter);
        this.mEtSearch.requestFocus();
        this.rv_video_search.addOnScrollListener(new OnScrollListener() {


            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (i2 > 0 && linearLayoutManager.findLastCompletelyVisibleItemPosition() == SearchActivity.this.mYoutubeVideoAdapter.getItemCount() - 2) {
                    SearchActivity.this.mYoutubeVideoAdapter.showLoading();
                }
            }
        });
    }

    public /* synthetic */ boolean lambda$init$0$SearchActivity(View view, MotionEvent motionEvent) {
        this.mEtSearch.addTextChangedListener(this.textWatcher);
        return false;
    }

    public /* synthetic */ boolean lambda$init$1$SearchActivity(View view, int i, KeyEvent keyEvent) {
        if ((i != 3 && i != 6 && (keyEvent == null || keyEvent.getAction() != 0 || keyEvent.getKeyCode() != 66)) || (keyEvent != null && keyEvent.isShiftPressed())) {
            return false;
        }
        this.rvSearchSuggest.setVisibility(GONE);
        this.rv_video_search.setVisibility(VISIBLE);
        if (TextUtils.isEmpty(getQueryKeyword())) {
            ToastUtils.error((Context) this, getString(R.string.enter_search_text));
        } else {
            onBtnSearchClick(getQueryKeyword());
        }
        return true;
    }

    public final String getQueryKeyword() {
        return this.mEtSearch.getText().toString();
    }

    public void onBtnSearchClick(String str) {
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.mEtSearch.getWindowToken(), 0);
        this.rvSearchSuggest.setVisibility(GONE);
        this.rv_video_search.setVisibility(GONE);
        this.mTvEmpty.setVisibility(GONE);
        this.mProgressBar.setVisibility(VISIBLE);
        this.searchUtils.search(this, str);
    }

    public final void updateSearchViewResult() {
        if (this.mYoutubeVideoAdapter.isEmpty()) {
            this.mTvEmpty.setText(getString(R.string.txt_no_search_result));
            this.mTvEmpty.setVisibility(VISIBLE);
            this.rv_video_search.setVisibility(GONE);
        } else {
            this.mTvEmpty.setVisibility(GONE);
            this.rv_video_search.setVisibility(VISIBLE);
        }
        this.mProgressBar.setVisibility(GONE);
    }

    public void onSuccesSearch(ArrayList<String> arrayList) {
        SugestionsAdapter sugestionsAdapter = new SugestionsAdapter(this, arrayList, this.onItemClickedSearch);
        this.rvSearchSuggest.setAdapter(sugestionsAdapter);
        sugestionsAdapter.notifyDataSetChanged();
        this.mProgressBar.setVisibility(GONE);
        this.mTvEmpty.setVisibility(GONE);
    }

    public void onClick(String str) {
        this.mEtSearch.setText(str);
        this.mEtSearch.setSelection(str.length());
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.mEtSearch.getWindowToken(), 0);
        this.rvSearchSuggest.setVisibility(GONE);
        this.rv_video_search.setVisibility(GONE);
        this.mTvEmpty.setVisibility(GONE);
        this.mProgressBar.setVisibility(VISIBLE);
        this.searchUtils.search(this, str);
    }

    public void onSearchSuccessful(ArrayList<AudioExtract> arrayList, Page page) {
        this.mYoutubeVideoAdapter.addAll(arrayList);
        if (arrayList.size() > 0) {
            this.nextPage = page;
            this.rv_video_search.scrollToPosition(0);
            this.rvSearchSuggest.setVisibility(GONE);
            updateSearchViewResult();
            this.mTvEmpty.setVisibility(GONE);
            return;
        }
        this.mTvEmpty.setText(getString(R.string.txt_no_search_result));
        this.rv_video_search.setVisibility(GONE);
        this.rvSearchSuggest.setVisibility(GONE);
        this.mTvEmpty.setVisibility(VISIBLE);
        this.mProgressBar.setVisibility(GONE);
    }

    public void onSearchFailed(String str) {
        hideLoading();
        if (AppUtils.isOnline(this)) {
            this.mTvEmpty.setText(getString(R.string.txt_no_search_result));
        } else {
            this.mTvEmpty.setText(getString(R.string.txt_check_connection));
        }
        this.mProgressBar.setVisibility(GONE);
        this.mTvEmpty.setVisibility(VISIBLE);
    }

    public void onClickItem(AudioExtract audioExtract, int i) {
        if (SystemClock.elapsedRealtime() - this.mLastClickTime >= 1000) {
            if (AppUtils.isOnline(this)) {
                this.mLastClickTime = SystemClock.elapsedRealtime();
                Intent intent = new Intent(this, MusicPlayerService.class);
                String str = "com.eskaylation.downloadmusic.ACTION.SETDATAONLINEPLAYER";
                intent.setAction(str);
                intent.putExtra(str, audioExtract);
                startService(intent);
                startActivity(new Intent(this, PlayerActivity.class));
            } else {
                ToastUtils.error((Context) this, getString(R.string.txt_check_connection));
            }
        }
    }

    public final void bindService() {
        bindService(new Intent(this, MusicPlayerService.class), this.connection, 1);
    }

    public final void unbindServicePlayMusic() {
        if (this.mBound) {
            try {
                unbindService(this.connection);
            } catch (Exception unused) {
            }
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onDestroy() {
        super.onDestroy();
        unbindServicePlayMusic();
    }

    public void onLoadMore() {
        this.searchUtils.searchMore(this, getQueryKeyword(), this.nextPage);
    }

    public void onLoadMoreSuccess(ArrayList<AudioExtract> arrayList, Page page) {
        this.nextPage = page;
        this.mYoutubeVideoAdapter.dismissLoading();
        this.mYoutubeVideoAdapter.addItemMore(arrayList);
        this.mYoutubeVideoAdapter.setMore(true);
    }

    public void onLoadMoreFailed() {
        this.mYoutubeVideoAdapter.dismissLoading();
        this.mYoutubeVideoAdapter.setMore(true);
    }
}
