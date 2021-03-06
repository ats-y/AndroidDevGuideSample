package com.atsy.devguidesample.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.atsy.devguidesample.R;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class ListTrialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_trial);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ListTrialFragment.newInstance())
                    .commitNow();
        }

        // 戻るボタンを表示する。
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {

        // アクションバーの戻るボタンタップでActivityを終了させる。
        Timber.d("アクションバーの戻るボタンタップ");
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Androidの戻るキーがタップされたイベント。
        Timber.d("戻るキータップ");
    }
}