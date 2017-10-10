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

package io.github.rockerhieu.emojicon.adapter.Litho;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import io.github.rockerhieu.emojicon.EmojiconGridFragment;
import io.github.rockerhieu.emojicon.EmojiconRecents;
import io.github.rockerhieu.emojicon.EmojiconRecentsManager;
import io.github.rockerhieu.emojicon.EmojisTable;
import io.github.rockerhieu.emojicon.adapter.GridAdapter;
import io.github.rockerhieu.emojicon.emoji.Emojicon;

/**
 * @author Daniele Ricci
 */
public class EmojiconRecentsGridFragmentUsingLitho extends EmojiconGridFragmentUsingLitho implements EmojiconRecents {

    private GridAdapter mAdapter;
    private boolean mUseSystemDefault = false;
    private EmojisTable mEmojisTable;

    private static final String USE_SYSTEM_DEFAULT_KEY = "useSystemDefaults";
    private static final String ARG_EMOJICONS = "emojicons";

    public static EmojiconRecentsGridFragmentUsingLitho newInstance() {
        return newInstance(false);
    }

    public static EmojiconRecentsGridFragmentUsingLitho newInstance(boolean useSystemDefault) {
        EmojiconRecentsGridFragmentUsingLitho fragment = new EmojiconRecentsGridFragmentUsingLitho();
        Bundle bundle = new Bundle();
        bundle.putBoolean(USE_SYSTEM_DEFAULT_KEY, useSystemDefault);
        bundle.putParcelableArray(ARG_EMOJICONS, new Emojicon[0]);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUseSystemDefault = getArguments().getBoolean(USE_SYSTEM_DEFAULT_KEY);
        } else {
            mUseSystemDefault = false;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        EmojiconRecentsManager recents = EmojiconRecentsManager
                .getInstance(view.getContext());

//        mAdapter = new GridAdapter(view.getContext(), recents, mUseSystemDefault);
//        GridView gridView = (GridView) view.findViewById(R.id.Emoji_GridView);
//        gridView.setAdapter(mAdapter);
//        gridView.setOnItemClickListener(this);

//        mEmojisTable = (EmojisTable) view.findViewById(R.id.Emoji_Table);
//        mEmojisTable.setEmojis(recents, mUseSystemDefault);
//        mEmojisTable.setOnItemClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAdapter = null;
    }

    @Override
    public void addRecentEmoji(Context context, Emojicon emojicon) {
        EmojiconRecentsManager recents = EmojiconRecentsManager
                .getInstance(context);
        recents.push(emojicon);

//        // notify dataset changed
//        if (mAdapter != null)
//            mAdapter.notifyDataSetChanged();

//        if (mEmojisTable != null) {
//            mEmojisTable.notifyDataSetChanged();
//        }
    }

}
