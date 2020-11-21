package com.atsy.devguidesample.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.atsy.devguidesample.DevGuideSampleApplication;
import com.atsy.devguidesample.R;
import com.atsy.devguidesample.databinding.ActivityMainBinding;
import com.atsy.devguidesample.models.Const;
import com.atsy.devguidesample.repositories.WeatherRepository;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    /** ビューバインディング */
    private ActivityMainBinding mViewBinding;

    @Inject
    public WeatherRepository mWeatherRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // フィールドインジェクションする。
        // このタイミングで@Injectフィールドにインジェクションされる。
        ((DevGuideSampleApplication)getApplicationContext()).appComponent.inject(this);

        // ビューバインディングの設定。
        mViewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mViewBinding.getRoot();
        setContentView(view);

        // 許可されていない権限があれば、権限リクエストする。
        List<String> noPermissions = getNoPermitted();
        if( noPermissions.size() > 0){
            ActivityCompat.requestPermissions(this, noPermissions.toArray(
                    new String[noPermissions.size()]),0);
            return;
        }

        mViewBinding.btnLogging.setOnClickListener( view1 -> {
            Timber.d("ロギング！！");
        });

        // ListTrialActivityへの遷移。
        mViewBinding.btnNavigateListTrial.setOnClickListener(view1 -> {

            Timber.i("ListTrialActivityへの遷移");
           Intent intent = new Intent(this, ListTrialActivity.class);
           startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private List<String> getNoPermitted() {
        // 必要な権限が許可されているかチェックする。
        String[] needPermissions = new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
        };
        List<String> noPermissions = new ArrayList<>();
        for( String p : needPermissions) {
            if (ContextCompat.checkSelfPermission(
                    getApplicationContext(), p ) != PackageManager.PERMISSION_GRANTED) {
                Timber.d(MessageFormat.format("no {0}", p));
                noPermissions.add(p);
            }
        }
        return noPermissions;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Timber.d("onResume()");

        // 許可されていない権限があれば、権限リクエストする。
        List<String> noPermissions = getNoPermitted();

        FragmentManager fm = getSupportFragmentManager();
        Fragment noPermitFragment = fm.findFragmentByTag("NO");
        if( noPermissions.size() > 0 ) {
            if (noPermitFragment == null) {

                fm.beginTransaction()
                        .add(R.id.container, new NoPermitFragment(), "NO")
                        .commit();
            }
        } else {
            if (noPermitFragment != null) {
                fm.beginTransaction()
                        .remove(noPermitFragment)
                        .commit();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 権限リクエストの結果を判定する。
        boolean isPermitted = true;
        for(int result : grantResults){
            if( result != PackageManager.PERMISSION_GRANTED){
                isPermitted = false;
                break;
            }
        }

        if(isPermitted ){
            mViewBinding.Label.setText("OK");
        } else {


            mViewBinding.Label.setText("NG");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}