package jp.wasabeef.blurry.internal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlurTask {
    public static ExecutorService THREAD_POOL = Executors.newCachedThreadPool();
    public Bitmap bitmap;
    public Callback callback;
    public WeakReference<Context> contextWeakRef;
    public BlurFactor factor;
    public Resources res;

    public interface Callback {
        void done(BitmapDrawable bitmapDrawable);
    }

    public BlurTask(Context context, Bitmap bitmap2, BlurFactor blurFactor, Callback callback2) {
        this.res = context.getResources();
        this.factor = blurFactor;
        this.callback = callback2;
        this.contextWeakRef = new WeakReference<>(context);
        this.bitmap = bitmap2;
    }

    public void execute() {
        THREAD_POOL.execute(new Runnable() {
            public void run() {
                final BitmapDrawable bitmapDrawable = new BitmapDrawable(BlurTask.this.res, Blur.of((Context) BlurTask.this.contextWeakRef.get(), BlurTask.this.bitmap, BlurTask.this.factor));
                if (BlurTask.this.callback != null) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            BlurTask.this.callback.done(bitmapDrawable);
                        }
                    });
                }
            }
        });
    }
}
