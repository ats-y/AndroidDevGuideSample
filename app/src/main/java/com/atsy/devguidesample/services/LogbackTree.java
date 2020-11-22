package com.atsy.devguidesample.services;

import android.util.Log;

import com.atsy.devguidesample.views.MainActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import timber.log.Timber;

/**
 * TimberのLogback-Android出力実体。
 * https://github.com/JakeWharton/timber
 */
public class LogbackTree extends Timber.Tree {

    @Override
    protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {

        Logger logger = LoggerFactory.getLogger("DevGuideSampleLog");
        switch(priority){
            case Log.ASSERT:
            case Log.ERROR:
                logger.error(message);
                break;
            case Log.WARN:
                logger.warn(message);
                break;
            case Log.INFO:
                logger.info(message);
                break;
            case Log.DEBUG:
                logger.debug(message);
                break;
            case Log.VERBOSE:
                logger.trace(message);
                break;
            default:
                // 出力しない。
                return;
        }
    }
}
