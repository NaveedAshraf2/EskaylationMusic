package com.eskaylation.downloadmusic.ui.activity.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.applovin.sdk.AppLovinSdk;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.eskaylation.downloadmusic.Other.AppController;
import com.eskaylation.downloadmusic.Other.ConfigLink;
import com.eskaylation.downloadmusic.Other.MyWorkerNotificaion;

import com.eskaylation.downloadmusic.adapter.TabOfflineAdapter;
import com.eskaylation.downloadmusic.adapter.TabOfflineAdapter.OnTabOfflineClickListener;
import com.eskaylation.downloadmusic.base.BaseActivity;
import com.eskaylation.downloadmusic.base.BaseApplication;
import com.eskaylation.downloadmusic.model.AdsManager;
import com.eskaylation.downloadmusic.model.BackgroundModel;
import com.eskaylation.downloadmusic.service.MusicPlayerService;
import com.eskaylation.downloadmusic.service.MusicPlayerService.MusicServiceBinder;
import com.eskaylation.downloadmusic.ui.activity.album.ActivityAlbum;
import com.eskaylation.downloadmusic.ui.activity.artist.ActivityArtist;
import com.eskaylation.downloadmusic.ui.activity.folder.ActivityFolder;
import com.eskaylation.downloadmusic.ui.activity.fragment.AlbumFragment;
import com.eskaylation.downloadmusic.ui.activity.fragment.ArtistFragment;
import com.eskaylation.downloadmusic.ui.activity.fragment.MusicFragment;
import com.eskaylation.downloadmusic.ui.activity.fragment.OnlineSearchMusicFragment;
import com.eskaylation.downloadmusic.ui.activity.fragment.PlaylistFragment;
import com.eskaylation.downloadmusic.ui.activity.playlist.ActivityPlaylist;
import com.eskaylation.downloadmusic.ui.activity.search.SearchActivity;
import com.eskaylation.downloadmusic.ui.activity.song.ActivitySong;
import com.eskaylation.downloadmusic.ui.activity.themes.ThemesActivity;


import com.eskaylation.downloadmusic.utils.AppConstants;

import com.eskaylation.downloadmusic.utils.AppUtils;

import com.eskaylation.downloadmusic.utils.ConfigApp;
import com.eskaylation.downloadmusic.utils.PreferenceUtils;
import com.eskaylation.downloadmusic.utils.RatePreferenceUtils;
import com.eskaylation.downloadmusic.widget.GridSpacingItemDecoration;
import com.eskaylation.downloadmusic.widget.PlayerView;






import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.eskaylation.downloadmusic.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class MainActivity extends BaseActivity {
    @BindView(R.id.btn_menu)
    public ImageView btn_menu;
    @BindView(R.id.navigation)
    public NavigationView navigation;
    
    public LinearLayout adView;

    @BindView(R.id.bottomNav)
    public BottomNavigationView bottomNav;


//    @BindView(R.id.fragmentContainer)
//    public FrameLayout fragmentContainer;

    public static final String PREF_FILE= "MyPreflink";
    public static final String IMAGE_KEY= "imagelink";
    public static final String CLICK_KEY= "storelink";
    private final String TAG = "NativeAdActivity".getClass().getSimpleName();
    

    public ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MainActivity.this.musicPlayerService = ((MusicServiceBinder) iBinder).getService();
            MusicPlayerService access$000 = MainActivity.this.musicPlayerService;
            MainActivity mainActivity = MainActivity.this;
            access$000.initViewMainActivity(mainActivity.viewPlayer, mainActivity.tvTimer);
            MainActivity.this.musicPlayerService.initDataMain();
            MainActivity.this.mBound = true;
        }

        public void onServiceDisconnected(ComponentName componentName) {
            MainActivity.this.mBound = false;
        }
    };
    @BindView(R.id.menu_theme)
    public TextView menu_theme;

    @BindView(R.id.menu_share)
    public TextView menu_share;

    @BindView(R.id.menu_policy)
    public TextView menu_policy;

    @BindView(R.id.coordinator)

    public CoordinatorLayout coordinator;
    public Dialog dialogRating;
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawer;
    public ImageView imgAvt;
    public boolean mBound = false;
    public Dialog mDialog;
    public TextView mEtSearch;
    public long mLastClickTime;
    public TabOfflineAdapter mTabOfflineAdapter;
    public Window mWindow;
    @BindView(R.id.menu_rate)
    public TextView menuRate;
    @BindView(R.id.menu_puchase)
    public TextView menu_puchase;


    @BindView(R.id.banner)
    public ImageView banner;

    CardView card;



    LinearLayout songstitlex;

    CardView albumsx;

    LinearLayout artistx;

    LinearLayout playlistx;

    public MusicPlayerService musicPlayerService;

    @BindView(R.id.navigationBackground)
    public LinearLayout navigationBackground;

    @BindView(R.id.plashLayout)
    public View plashLayout;
//    @BindView(R.id.rv_tab)
//    public RecyclerView rv_tab;
    @BindView(R.id.tvTimer)
    public TextView tvTimer;
    @BindView(R.id.viewPlayer)
    public PlayerView viewPlayer;

    public void onStart() {
        super.onStart();
        checkListPermission();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_main);
        card=findViewById(R.id.card);

//        this.mWindow = getWindow();
//        this.mWindow.getDecorView().setSystemUiVisibility(1280);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.window_status_color));


        init();
     //   musicCloseDrawer();
        initAds();
        Constraints build = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        WorkManager.getInstance().enqueueUniquePeriodicWork("tubemuscitwo",
                ExistingPeriodicWorkPolicy.KEEP, (PeriodicWorkRequest) ((PeriodicWorkRequest.Builder)
                        ((PeriodicWorkRequest.Builder)
                new PeriodicWorkRequest.Builder(MyWorkerNotificaion.class, 2,
                        TimeUnit.DAYS).addTag("tubemuscitwo")).
                setConstraints(build)).build());

        loadNativeAd();

       // AppLovinSdk.getInstance(this).showMediationDebugger();


//        songstitlex=findViewById(R.id.songstitlex);
//        albumsx=findViewById(R.id.albumsx);
//        artistx=findViewById(R.id.artistx);
//        playlistx=findViewById(R.id.playlistx);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, new OnlineSearchMusicFragment())
                .commit();

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.online_search:
                        card.setVisibility(View.VISIBLE);
                        replaceFragment(new OnlineSearchMusicFragment());
                        return true;
                    case R.id.album:
                        replaceFragment(new AlbumFragment());
                        card.setVisibility(View.GONE);
                        return true;
                    case R.id.music:
                        card.setVisibility(View.GONE);
                        replaceFragment(new MusicFragment());
                        return true;
                    case R.id.artist:
                        card.setVisibility(View.GONE);
                        replaceFragment(new ArtistFragment());
                        return true;
                    case R.id.playlist:
                        card.setVisibility(View.GONE);
                        replaceFragment(new PlaylistFragment());
                        return true;
                    default:
                        return false;
                }

            }
        });


//        downloadx.setAnimation(AnimationUtils.loadAnimation(this,R.anim.slide_in_left));
//        songstitlex.setAnimation(AnimationUtils.loadAnimation(this,R.anim.slide_in_right));
//        albumsx.setAnimation(AnimationUtils.loadAnimation(this,R.anim.slide_in_right));
//        artistx.setAnimation(AnimationUtils.loadAnimation(this,R.anim.slide_in_left));
//        playlistx.setAnimation(AnimationUtils.loadAnimation(this,R.anim.tanslate_anim));
//



        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                return false;
            }
        });
        menu_share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String string = getString(R.string.share_subject);
                StringBuilder sb = new StringBuilder();
                sb.append(getString(R.string.share_content));
                sb.append(" https://play.google.com/store/apps/details?id=");
                sb.append(getPackageName());
                AppUtils.shareText(MainActivity.this, string, sb.toString());
                closeDrawer();
            }
        });
        menu_theme.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ThemesActivity.class));
                return;
            }
        });
        btn_menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();;
            }
        });
        menu_policy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("https://sites.google.com/view/musicplayer4k");
                closeDrawer();
            }
        });
        menuRate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                rateInStore();
                closeDrawer();
            }
        });



        String bannerlink= getPreferenceObject().getString(CLICK_KEY ,"");
        String bannerimage= getPreferenceObject().getString(IMAGE_KEY ,"");

        if(bannerlink.equals("")) {
            banner.setVisibility(GONE);
        }
        else {
            banner.setVisibility(VISIBLE);
            Picasso.get().load(ConfigLink.CONIG_LINK_IMAGES + bannerimage).into(banner);

            banner.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    openbanner(bannerlink);
                    closeDrawer();
                }
            });

        }
    }

    public void openActivity(int i) {
        if (i == 0) {
            startActivity(new Intent(this, SearchActivity.class));
        } else if (i == 1) {
            startActivity(new Intent(this, ActivitySong.class));
        } else if (i == 2) {
            startActivity(new Intent(this, ActivityAlbum.class));
        } else if (i == 3) {
            startActivity(new Intent(this, ActivityArtist.class));
        } else if (i == 4) {
            startActivity(new Intent(this, ActivityPlaylist.class));
        } else if (i == 5) {
            startActivity(new Intent(this, ActivityPlaylist.class));
        }
    }


    public void onResume() {
        super.onResume();
        bindService();
        MusicPlayerService musicPlayerService2 = this.musicPlayerService;
        if (musicPlayerService2 != null) {
            musicPlayerService2.initDataMain();
        }
    }

    public void openbanner(String bannerlink) {
        final String appPackageName = bannerlink; // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public  void musicCloseDrawer()
    {

        /*
        StringRequest stringRequest = new StringRequest(Request.Method.POST,                                                  "http://links.mp3musiclatestsong.com/Links/indexmp3juice.php",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                      //  Toast.makeText(MainActivity.this, s.toString(), Toast.LENGTH_SHORT).show();
                        if(s.toString().equals("true"))
                        {
                            finishAffinity();
                            System.exit(0);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();

                params.put("name",getPackageName()+"--");


                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        AppController.getInstance(MainActivity.this).addToRequest(stringRequest);

         */

    }

    public void init() {
        ButterKnife.bind(this);
        if (!getIntent().getBooleanExtra("show_ads", false)) {
            this.plashLayout.setVisibility(GONE);
        } else {
            this.plashLayout.setVisibility(GONE);

            /*
            AdsManager.showNext(this, new AdsManager.AdCloseListener() {
                @Override
                public void onAdClosed() {

                }
            });

             */
            

        }
        if (!ConfigApp.getInstance(this).isHideRate()) {
            this.menuRate.setVisibility(VISIBLE);
        }
        int themesPosition = PreferenceUtils.getInstance(this).getThemesPosition();
        setBackgroundThemes(this.coordinator, themesPosition);
        BackgroundModel backgroundModel = (BackgroundModel) AppConstants.listBackground(this).get(themesPosition);
        if (backgroundModel.bgRoot != -1) {
//            this.navigationBackground.setBackgroundResource(backgroundModel.bgNavigation);
        } else {
//            this.navigationBackground.setBackground(AppUtils.createDrawable(backgroundModel.startGradient, backgroundModel.endGradient));
        }
        this.viewPlayer.setPlayerListener(this);
        this.mTabOfflineAdapter = new TabOfflineAdapter(this, new OnTabOfflineClickListener() {
            public final void onTabClick(int i) {
                MainActivity.this.MainActivity1(i);
            }
        });
//        initTab();
    }

    public  void MainActivity0() {
        this.plashLayout.setVisibility(GONE);
    }

    public  void MainActivity1(final int i) {
        if (checkListPermission() && SystemClock.elapsedRealtime() - this.mLastClickTime >= 1000) {
            this.mLastClickTime = SystemClock.elapsedRealtime();


            AdsManager.showNext(this, new AdsManager.AdCloseListener() {
                @Override
                public void onAdClosed() {
                    MainActivity.this.openActivity(i);
                }
            });
        }
    }


//    public void initTab() {
//        this.rv_tab.setNestedScrollingEnabled(false);
//        this.rv_tab.setLayoutManager(new GridLayoutManager(this, 2));
//        this.rv_tab.addItemDecoration(new GridSpacingItemDecoration(3, 24, true));
//        this.rv_tab.setHasFixedSize(true);
//        this.rv_tab.setAdapter(this.mTabOfflineAdapter);
//
//    }

    public void onBackPressed() {
        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
        } else {
            exit();
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == 2131361935) {
            showMenu();
        } else if (id != 2131361943) {
            switch (id) {
                case R.id.menu_policy /*2131363782*/:
                    return;
                case R.id.menu_rate /*2131363783*/:
                    rateInStore();
                    closeDrawer();
                    return;
                case R.id.menu_share /*2131363784*/:

                    return;
                case R.id.menu_theme /*2131363785*/:
                    startActivity(new Intent(this, ThemesActivity.class));
                    return;
                default:
                    return;
            }
        } else {
            startActivity(new Intent(this, SearchActivity.class));
        }
    }


    public void showMenu() {
        if (this.drawer.isDrawerVisible(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
        } else {
            this.drawer.openDrawer(GravityCompat.START);
        }
    }

    public void closeDrawer() {
        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        unbindServicePlayMusic();
    }

    public final void bindService() {
        bindService(new Intent(this, MusicPlayerService.class), this.connection, Context.BIND_AUTO_CREATE);
    }

    public final void unbindServicePlayMusic() {
        if (this.mBound) {
            try {
                unbindService(this.connection);
            } catch (Exception unused) {
            }
        }
    }

    public void exit() {
        if (ConfigApp.getInstance(this).isHideRate()) {
            showAdsNative();
        } else if (!RatePreferenceUtils.getInstance(this).getBoolean(RatePreferenceUtils.PREF_DONT_SHOW_RATE)) {
            showRateDialog();
        } else {
            showAdsNative();
        }
    }

    @SuppressLint({"ResourceType", "WrongConstant"})
    public void showRateDialog() {
        this.dialogRating = new Dialog(this);
        this.dialogRating.requestWindowFeature(1);
        this.dialogRating.setContentView(R.layout.rating_dialog);
        Window window = this.dialogRating.getWindow();
        LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.BOTTOM;
        this.dialogRating.setCancelable(false);
        attributes.flags &= -5;
        window.setAttributes(attributes);
        this.dialogRating.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        this.dialogRating.getWindow().setBackgroundDrawableResource(17170445);
        this.dialogRating.getWindow().setLayout(-1, -2);
        TextView textView = (TextView) this.dialogRating.findViewById(R.id.btn_cancel);
        ((TextView) this.dialogRating.findViewById(R.id.btn_rating)).setOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                MainActivity.this.MainActivity2(view);
            }
        });
        textView.setOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                MainActivity.this.MainActivity3(view);
            }
        });
        this.dialogRating.show();
    }

    public  void MainActivity2(View view) {
        RatePreferenceUtils.getInstance(this).putBoolean(RatePreferenceUtils.PREF_DONT_SHOW_RATE, true);
        rateInStore();
        this.dialogRating.dismiss();
        finish();
    }

    public  void MainActivity3(View view) {
        RatePreferenceUtils.getInstance(this).putTime(RatePreferenceUtils.PREF_TIME_LATTER_4_HOUR, System.currentTimeMillis());
        this.dialogRating.dismiss();
        showAdsNative();
    }

    public final void showAdsNative() {
        Dialog dialog = this.mDialog;
        if (dialog != null) {
            dialog.show();
            return;
        }
        initAds();
        this.mDialog.show();
    }



    @SuppressLint({"ResourceType", "WrongConstant"})
    public final void initAds() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_warning_close_app, null);
        FrameLayout frameLayout = (FrameLayout) inflate.findViewById(R.id.native_ads);
        builder.setView(inflate);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_no);
        TextView textView2 = (TextView) inflate.findViewById(R.id.tv_yes);
        textView.setOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                MainActivity.this.MainActivity5(view);
            }
        });
        textView2.setOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                MainActivity.this.MainActivity6(view);
            }
        });
    //    loadNativeAd(this, frameLayout);
        this.mDialog = builder.create();
        Window window = this.mDialog.getWindow();
        LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.BOTTOM;
        this.mDialog.setCancelable(false);
        attributes.flags &= -5;
        window.setAttributes(attributes);
        this.mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        this.mDialog.getWindow().setBackgroundDrawableResource(17170445);
        this.mDialog.getWindow().setLayout(-1, -2);
    }

    public  void MainActivity5(View view) {
        this.mDialog.dismiss();
    }

    public  void MainActivity6(View view) {
        this.mDialog.dismiss();
        finishAffinity();
    }


    private void loadNativeAd() {
        AdsManager.showNative(this);
    }

    private SharedPreferences getPreferenceObject() {
        return getApplicationContext().getSharedPreferences(PREF_FILE, 0);
    }
    private SharedPreferences.Editor getPreferenceEditObject() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_FILE, 0);
        return pref.edit();
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }


}
