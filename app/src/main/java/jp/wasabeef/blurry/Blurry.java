package jp.wasabeef.blurry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import jp.wasabeef.blurry.internal.Blur;
import jp.wasabeef.blurry.internal.BlurFactor;
import jp.wasabeef.blurry.internal.BlurTask;
import jp.wasabeef.blurry.internal.BlurTask.Callback;

public class Blurry {
    public static final String TAG = "Blurry";

    public static class BitmapComposer {
        public boolean async;
        public Bitmap bitmap;
        public Context context;
        public BlurFactor factor;
        public Blurry$ImageComposer$ImageComposerListener listener;

        public BitmapComposer(Context context2, Bitmap bitmap2, BlurFactor blurFactor, boolean z, Blurry$ImageComposer$ImageComposerListener blurry$ImageComposer$ImageComposerListener) {
            this.context = context2;
            this.bitmap = bitmap2;
            this.factor = blurFactor;
            this.async = z;
            this.listener = blurry$ImageComposer$ImageComposerListener;
        }

        public void into(final View view) {
            this.factor.width = this.bitmap.getWidth();
            this.factor.height = this.bitmap.getHeight();
            if (this.async) {
                new BlurTask(view.getContext(), this.bitmap, this.factor, new Callback() {
                    public void done(BitmapDrawable bitmapDrawable) {
                        if (BitmapComposer.this.listener == null) {
                            view.setBackground(bitmapDrawable);
                        } else {
                            BitmapComposer.this.listener.onImageReady(bitmapDrawable);
                        }
                    }
                }).execute();
            } else {
                view.setBackground(new BitmapDrawable(this.context.getResources(), Blur.of(view.getContext(), this.bitmap, this.factor)));
            }
        }
    }

    public static class Composer {
        public boolean async;
        public View blurredView;
        public Context context;
        public BlurFactor factor = new BlurFactor();
        public Blurry$ImageComposer$ImageComposerListener listener;

        public Composer animate() {
            return this;
        }

        public Composer(Context context2) {
            this.context = context2;
            this.blurredView = new View(context2);
            this.blurredView.setTag(Blurry.TAG);
        }

        public Composer radius(int i) {
            this.factor.radius = i;
            return this;
        }

        public Composer async() {
            this.async = true;
            return this;
        }

        public BitmapComposer from(Bitmap bitmap) {
            BitmapComposer bitmapComposer = new BitmapComposer(this.context, bitmap, this.factor, this.async, this.listener);
            return bitmapComposer;
        }
    }

    public static Composer with(Context context) {
        return new Composer(context);
    }
}
