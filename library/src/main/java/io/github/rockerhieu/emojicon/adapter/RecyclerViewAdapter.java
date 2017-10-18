package io.github.rockerhieu.emojicon.adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import io.github.rockerhieu.emojicon.BuildConfig;
import io.github.rockerhieu.emojicon.EmojiconTextView;
import io.github.rockerhieu.emojicon.R;
import io.github.rockerhieu.emojicon.emoji.Emojicon;

/**
 * Created by cpu10661 on 10/2/17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.EmojiconViewHolder> {

    private static final String TAG  = RecyclerViewAdapter.class.getSimpleName();

    private static final float DENSITY = Resources.getSystem().getDisplayMetrics().density;
    private static final int EMOJIS_PER_ROW = 10;
    private ArrayList<Emojicon> mEmojicons;
    private boolean mUseSystemDefault;
    private OnScrollToBottomListener mOnScrollToBottomListener;

    public RecyclerViewAdapter(ArrayList<Emojicon> emojicons, boolean useSystemDefault) {
        this.mEmojicons = emojicons;
        this.mUseSystemDefault = useSystemDefault;
    }

    public RecyclerViewAdapter(Emojicon[] emojicons, boolean useSystemDefault) {
        this.mEmojicons = new ArrayList<>(Arrays.asList(emojicons));
        this.mUseSystemDefault = useSystemDefault;
    }

    @Override
    public EmojiconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emojicon_list_item, parent, false);
        return new EmojiconViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(EmojiconViewHolder holder, int position) {

        if (BuildConfig.DEBUG && EMOJIS_PER_ROW == holder.mRootLayout.getChildCount()) {
            throw new IllegalArgumentException("Number of emojis per row have to be consistent");
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPixel(36), dpToPixel(36), 1f);

        int index = EMOJIS_PER_ROW * position;
        for (int i = 0; i < holder.mRootLayout.getChildCount(); i++) {
            EmojiconTextView emojiTextView = (EmojiconTextView) holder.mRootLayout.getChildAt(i);
            emojiTextView.setUseSystemDefault(mUseSystemDefault);
            emojiTextView.setText(mEmojicons.get(index + i).getEmoji());
            emojiTextView.setLayoutParams(params);
        }
    }

    private static int dpToPixel(int dp) {
        return (int) (dp * DENSITY + 0.5f);
    }

    interface OnScrollToBottomListener {
        void onScrollToBottomListener();
    }

    public void setOnScrollToBottomListener(OnScrollToBottomListener listener) {
        mOnScrollToBottomListener = listener;
    }

    @Override
    public int getItemCount() {
        return (mEmojicons.size() - 1) / EMOJIS_PER_ROW;
    }

    class EmojiconViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mRootLayout;

        EmojiconViewHolder(View itemView) {
            super(itemView);
            mRootLayout = (LinearLayout) itemView.findViewById(R.id.ll_emojis_row);
        }
    }
}
