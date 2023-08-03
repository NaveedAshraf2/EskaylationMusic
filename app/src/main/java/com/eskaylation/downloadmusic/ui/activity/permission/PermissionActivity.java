package com.eskaylation.downloadmusic.ui.activity.permission;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.base.BaseActivity;
import com.eskaylation.downloadmusic.ui.activity.main.MainActivity;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.gun0912.tedpermission.TedPermission.Builder;
import java.util.List;

public class PermissionActivity extends BaseActivity {
    public Window mWindow;
    @BindView(R.id.btnPerrmission)
    public Button btnPerrmission;
    public PermissionListener permissionlistener;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_permission);
        this.mWindow = getWindow();
        this.mWindow.getDecorView().setSystemUiVisibility(1280);
        init();
        btnPerrmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    onClick();
                accessPermission();
            }
        });
    }

    public void init() {
        ButterKnife.bind(this);
    }



    public void accessPermission() {
        this.permissionlistener = new PermissionListener() {
            public void onPermissionGranted() {
                PermissionActivity permissionActivity = PermissionActivity.this;
                permissionActivity.startActivity(new Intent(permissionActivity, MainActivity.class));
                PermissionActivity.this.finish();
            }

            public void onPermissionDenied(List<String> list) {
                PermissionActivity permissionActivity = PermissionActivity.this;
                Toast.makeText(permissionActivity, permissionActivity.getString(R.string.need_permission), Toast.LENGTH_SHORT).show();
            }
        };
        Builder with = TedPermission.with(this);
        with.setPermissionListener(this.permissionlistener);
        Builder builder = with;
        builder.setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]");
        Builder builder2 = builder;
        builder2.setPermissions("android.permission.WRITE_EXTERNAL_STORAGE");
        builder2.check();
    }
}
