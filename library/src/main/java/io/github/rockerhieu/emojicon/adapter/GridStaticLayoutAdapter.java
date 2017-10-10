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

import io.github.rockerhieu.emojicon.R;
import io.github.rockerhieu.emojicon.emoji.Emojicon;
import io.github.rockerhieu.emojicon.util.TextLayoutView;

/**
 * @author Hieu Rocker (rockerhieu@gmail.com)
 */
public class GridStaticLayoutAdapter extends ArrayAdapter<Emojicon> {

    public GridStaticLayoutAdapter(Context context, List<Emojicon> data) {
        super(context, R.layout.emojicon_item, data);
    }

    public GridStaticLayoutAdapter(Context context, Emojicon[] data) {
        super(context, R.layout.emojicon_item, data);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.emojicon_static_layout_item, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.icon = v.findViewById(R.id.emojicon_icon);
            v.setTag(holder);
        }

        Emojicon emoji = getItem(position);
        final ViewHolder holder = (ViewHolder) v.getTag();
        holder.icon.setEmoji(emoji.getEmoji());

        return v;
    }

    private static class ViewHolder {
        TextLayoutView icon;
    }
}