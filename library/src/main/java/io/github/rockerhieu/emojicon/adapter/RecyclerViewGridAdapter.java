package io.github.rockerhieu.emojicon.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;

import io.github.rockerhieu.emojicon.EmojiconTextView;
import io.github.rockerhieu.emojicon.R;

/**
 * Created by cpu10661 on 10/2/17.
 */

public class RecyclerViewGridAdapter extends RecyclerView.Adapter<RecyclerViewGridAdapter.EmojiconViewHolder> {

    private static final String TAG  = RecyclerViewGridAdapter.class.getSimpleName();

    private ArrayList<CharSequence> mEmojicons;
    private boolean mUseSystemDefault;

    public RecyclerViewGridAdapter(ArrayList<CharSequence> emojicons, boolean useSystemDefault) {
        this.mEmojicons = emojicons;
        this.mUseSystemDefault = useSystemDefault;
    }

    public RecyclerViewGridAdapter(CharSequence[] emojicons, boolean useSystemDefault) {
        this.mEmojicons = new ArrayList<>(Arrays.asList(emojicons));
        this.mUseSystemDefault = useSystemDefault;
    }

    @Override
    public EmojiconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emojicon_item, parent, false);
        return new EmojiconViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmojiconViewHolder holder, int position) {
        holder.mEmojiconTextView.setUseSystemDefault(mUseSystemDefault);
        holder.mEmojiconTextView.setText(mEmojicons.get(position));
    }

    @Override
    public int getItemCount() {
        return mEmojicons.size();
    }

    class EmojiconViewHolder extends RecyclerView.ViewHolder {

        EmojiconTextView mEmojiconTextView;

        EmojiconViewHolder(View itemView) {
            super(itemView);
            mEmojiconTextView = (EmojiconTextView) itemView.findViewById(R.id.emojicon_icon);
        }
    }
}
