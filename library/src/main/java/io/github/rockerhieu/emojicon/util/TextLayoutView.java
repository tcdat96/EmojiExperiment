package io.github.rockerhieu.emojicon.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by cpu10661 on 10/6/17.
 */

public class TextLayoutView extends View {

    private static final String TAG = TextLayoutView.class.getSimpleName();
    private static int EMOJI_SIZE;

    public TextLayoutView(Context context) {
        super(context);
        init();
    }

    public TextLayoutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private static TextPaint mTextPaint;
    private StaticLayout mLayout;

    private void init() {
        EMOJI_SIZE = (int)sp(15);
        if (mTextPaint == null){
            mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            mTextPaint.setColor(Color.WHITE);
            mTextPaint.setTextSize(EMOJI_SIZE);
        }
        LayoutCache.INSTANCE.changeWidth(100);
    }

    private float sp(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    private float dp(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        if (mLayout != null) {
            canvas.translate((canvas.getWidth() / 2) - (EMOJI_SIZE / 2), (canvas.getHeight() / 2) - ((mLayout.getHeight() / 2)));
            mLayout.draw(canvas);
        }
        canvas.restore();
    }

    public void setEmoji(CharSequence emoji) {
        mLayout = LayoutCache.INSTANCE.layoutFor(emoji);
        requestLayout();
        invalidate();
    }

    private enum LayoutCache {
        INSTANCE;

        private int width;
        private final LruCache<CharSequence, StaticLayout> layoutCache = new LruCache<CharSequence, StaticLayout>(1500) {
            @Override
            protected StaticLayout create(CharSequence key) {
                CharSequence truncatedTitle = TextUtils.ellipsize(key, mTextPaint, width, TextUtils.TruncateAt.END);
                return new StaticLayout(truncatedTitle, mTextPaint, width, Layout.Alignment.ALIGN_NORMAL, 1, 1, true);
            }
        };

        public void changeWidth(int newWidth) {
            if (width != newWidth) {
                width = newWidth;
                layoutCache.evictAll();
            }
        }

        public StaticLayout layoutFor(CharSequence text) {
            return layoutCache.get(text);
        }
    }
}
