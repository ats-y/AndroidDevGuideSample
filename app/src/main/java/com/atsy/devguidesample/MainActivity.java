package com.atsy.devguidesample;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.atsy.devguidesample.databinding.ActivityMainBinding;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /** ビューバインディング */
    private ActivityMainBinding mViewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ビューバインディングの設定。
        mViewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mViewBinding.getRoot();
        setContentView(view);

        // 必要な権限が許可されているかチェックする。
        String[] needPermissions = new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
        };
        List<String> noPermissions = new ArrayList<>();
        for( String p : needPermissions) {
            if (ContextCompat.checkSelfPermission(
                    getApplicationContext(), p ) != PackageManager.PERMISSION_GRANTED) {
                Log.d("abc", MessageFormat.format("no {0}", p));
                noPermissions.add(p);
            }
        }

        // 許可されていない権限があれば、権限リクエストする。
        if( noPermissions.size() > 0){
            ActivityCompat.requestPermissions(this, noPermissions.toArray(
                    new String[noPermissions.size()]),0);
            return;
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

        if(isPermitted) {
            mViewBinding.Label.setText("OK");
        } else {

            mViewBinding.Label.setText("NG");

            // アプリの設定画面を開く。
            Context context = getApplicationContext();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }
    }
}