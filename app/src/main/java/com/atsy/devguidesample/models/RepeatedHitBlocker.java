package com.atsy.devguidesample.models;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import timber.log.Timber;

/**
 * 連打防止クラス
 * 一定時間の間、処理ブロックフラグをたてる。
 */
public final class RepeatedHitBlocker {

    /** 処理ブロック中かどうか */
    private static boolean mIsBlocked = false;

    /** 処理ブロックから解除するまでの時間 */
    private static final int RejectPeriodMillis = 500;

    /** 処理ブロックフラグ更新排他オブジェクト */
    private static final Object mLocker = new Object();

    /**
     * 処理ブロックフラグを立てる。
     * @return true:ブロックした／false:すでにブロックしている
     */
    public static boolean Block() {

        // すでにブロック中であればfalseを返す。
        if (mIsBlocked) {
            Timber.d("RepeatedTapBlocker - reject");
            return false;
        }

        // ブロックフラグを立てる。
        synchronized (mLocker) {
            Timber.d("RepeatedTapBlocker - locked");
            mIsBlocked = true;
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {

                // 一定時間待機。
                try {
                    Thread.sleep(RejectPeriodMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // ブロックフラグを解除する。
                mIsBlocked = false;
                Timber.d("RepeatedTapBlocker - released");

            });
        }
        return true;
    }
}
