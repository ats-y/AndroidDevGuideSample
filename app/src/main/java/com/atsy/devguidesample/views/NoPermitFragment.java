package com.atsy.devguidesample.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atsy.devguidesample.R;
import com.atsy.devguidesample.databinding.FragmentNoPermitBinding;

public class NoPermitFragment extends Fragment {

    public NoPermitFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // ビューバインディング。
        FragmentNoPermitBinding biding = FragmentNoPermitBinding.inflate(inflater, container, false);

        // アプリの設定画面を開くボタン。
        biding.OpenSettingsBtn.setOnClickListener(view -> {

            // アプリの設定画面を開く。
            Context context = this.getContext();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent,123);
            //context.startActivity(intent);
        });

        return biding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}