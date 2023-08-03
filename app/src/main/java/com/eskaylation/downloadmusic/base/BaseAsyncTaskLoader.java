package com.eskaylation.downloadmusic.base;

import android.content.Context;
import androidx.loader.content.AsyncTaskLoader;

public abstract class BaseAsyncTaskLoader<T> extends AsyncTaskLoader<T> {
    public T mData;

    public void onReleaseResources(T t) {
    }

    public BaseAsyncTaskLoader(Context context) {
        super(context);
    }

    public void deliverResult(T t) {
        if (isReset() && t != null) {
            onReleaseResources(t);
        }
        T t2 = this.mData;
        this.mData = t;
        if (isStarted()) {
            super.deliverResult(t);
        }
        if (t2 != null) {
            onReleaseResources(t2);
        }
    }

    public void onStartLoading() {
        T t = this.mData;
        if (t != null) {
            deliverResult(t);
        }
        if (takeContentChanged() || this.mData == null) {
            forceLoad();
        }
    }

    public void onStopLoading() {
        cancelLoad();
    }

    public void onCanceled(T t) {
        super.onCanceled(t);
        onReleaseResources(t);
    }

    public void onReset() {
        super.onReset();
        onStopLoading();
        T t = this.mData;
        if (t != null) {
            onReleaseResources(t);
            this.mData = null;
        }
    }
}
