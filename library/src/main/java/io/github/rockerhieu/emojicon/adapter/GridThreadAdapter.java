/*
 * Copyright 2014 Hieu Rocker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.rockerhieu.emojicon.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.github.rockerhieu.emojicon.EmojiconHandler;
import io.github.rockerhieu.emojicon.EmojiconTextView;
import io.github.rockerhieu.emojicon.R;
import io.github.rockerhieu.emojicon.emoji.Emojicon;

/**
 * @author Hieu Rocker (rockerhieu@gmail.com)
 */
public class GridThreadAdapter extends ArrayAdapter<Emojicon> {

    private boolean mUseSystemDefault = false;

    public GridThreadAdapter(Context context, Emojicon[] data, boolean useSystemDefault) {
        super(context, R.layout.emojicon_item, data);
        mUseSystemDefault = useSystemDefault;

        initializeComponents();
    }

    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private ThreadPoolExecutor mExecutor;
    private int mEmojiconSize;
    private int mEmojiconAlignment;
    private int mEmojiconTextSize;
    private int mTextStart;
    private int mTextLength;
    private void initializeComponents() {

        mExecutor = new ThreadPoolExecutor(
                NUMBER_OF_CORES * 2,
                NUMBER_OF_CORES * 2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>()
        );

        View v = View.inflate(getContext(), R.layout.emojicon_item, null);
        EmojiconTextView icon = (EmojiconTextView) v.findViewById(R.id.emojicon_icon);
        mEmojiconSize = icon.getEmojiconSize();
        mEmojiconAlignment = icon.getEmojiconAlignment();
        mEmojiconTextSize = icon.getEmojiconSize();
        mTextStart = icon.getTextStart();
        mTextLength = icon.getTextLength();
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        final ViewHolder holder;

        if (v == null) {
            v = View.inflate(getContext(), R.layout.emojicon_item, null);
            holder = new ViewHolder();
            holder.icon = (EmojiconTextView) v.findViewById(R.id.emojicon_icon);
            holder.icon.setUseSystemDefault(mUseSystemDefault);
            holder.position = position;
            v.setTag(holder);

            // TODO: 9/28/17 only use multithread in first time
            mExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    Emojicon emoji = getItem(position);
                    if (!TextUtils.isEmpty(emoji.getEmoji())) {
                        final SpannableStringBuilder builder = new SpannableStringBuilder(emoji.getEmoji());
                        EmojiconHandler.addEmojis(getContext(), builder,
                                mEmojiconSize, mEmojiconAlignment, mEmojiconTextSize, mTextStart, mTextLength, mUseSystemDefault);

                        ((Activity)getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                holder.icon.setText(builder);
                            }
                        });
                    }
                }
            });
        } else {
            holder = (ViewHolder) v.getTag();
            holder.icon.setText(getItem(position).getEmoji());
        }

        return v;
    }

    private static class ViewHolder {
        EmojiconTextView icon;
        int position;
    }
}