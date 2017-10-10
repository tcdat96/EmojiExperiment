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

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.github.rockerhieu.emojicon.EmojiconTextView;
import io.github.rockerhieu.emojicon.R;

/**
 * @author Hieu Rocker (rockerhieu@gmail.com)
 */
public class GridStringAdapter extends ArrayAdapter<CharSequence> {

    private static final String TAG = GridStringAdapter.class.getSimpleName();

    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private ThreadPoolExecutor mExecutor;

    private boolean mUseSystemDefault = false;

    public GridStringAdapter(Context context, List<CharSequence> data, boolean useSystemDefault) {
        super(context, R.layout.emojicon_item, data);
        mUseSystemDefault = useSystemDefault;

        mExecutor = new ThreadPoolExecutor(
                NUMBER_OF_CORES * 2,
                NUMBER_OF_CORES * 2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>()
        );
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;

        if (v == null) {
//            v = View.inflate(getContext(), R.layout.emojicon_item, parent);
            v = LayoutInflater.from(getContext()).inflate(R.layout.emojicon_item, parent, false);
            holder = new ViewHolder();
            holder.icon = (EmojiconTextView) v.findViewById(R.id.emojicon_icon);
            holder.icon.setUseSystemDefault(mUseSystemDefault);
            holder.position = position;
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        final CharSequence emoji = getItem(position);
        holder.icon.setEmoji(emoji);
//        mExecutor.submit(new Runnable() {
//            @Override
//            public void run() {
//                holder.icon.setText(emoji);
//            }
//        });

        return v;
    }

    private static class ViewHolder {
        EmojiconTextView icon;
        int position;
    }
}