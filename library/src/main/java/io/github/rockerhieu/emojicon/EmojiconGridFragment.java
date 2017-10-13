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

package io.github.rockerhieu.emojicon;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import io.github.rockerhieu.emojicon.adapter.GridAdapter;
import io.github.rockerhieu.emojicon.adapter.GridStaticLayoutAdapter;
import io.github.rockerhieu.emojicon.adapter.GridStringAdapter;
import io.github.rockerhieu.emojicon.adapter.GridTextViewAdapter;
import io.github.rockerhieu.emojicon.adapter.GridThreadAdapter;
import io.github.rockerhieu.emojicon.adapter.RecyclerViewAdapter;
import io.github.rockerhieu.emojicon.adapter.RecyclerViewGridAdapter;
import io.github.rockerhieu.emojicon.emoji.Emojicon;
import io.github.rockerhieu.emojicon.emoji.People;
import io.github.rockerhieu.emojicon.util.TextLayoutView;

/**
 * @author Hieu Rocker (rockerhieu@gmail.com)
 */
public class EmojiconGridFragment extends Fragment
        implements AdapterView.OnItemClickListener, EmojisTable.OnItemClickListener {

    private static final String TAG = EmojiconGridFragment.class.getSimpleName();

    private OnEmojiconClickedListener mOnEmojiconClickedListener;
    private EmojiconRecents mRecents;
    private Emojicon[] mEmojicons;
    private
    @Emojicon.Type
    int mEmojiconType;
    private boolean mUseSystemDefault = false;

    private static final String ARG_USE_SYSTEM_DEFAULTS = "useSystemDefaults";
    private static final String ARG_EMOJICONS = "emojicons";
    private static final String ARG_EMOJICON_TYPE = "emojiconType";

    protected static EmojiconGridFragment newInstance(Emojicon[] emojicons, EmojiconRecents recents, int method) {
        return newInstance(Emojicon.TYPE_UNDEFINED, emojicons, recents, false, method);
    }

    protected static EmojiconGridFragment newInstance(
            @Emojicon.Type int type, EmojiconRecents recents, boolean useSystemDefault, int method) {
        return newInstance(type, null, recents, useSystemDefault, method);
    }

    protected static EmojiconGridFragment newInstance(
            @Emojicon.Type int type, Emojicon[] emojicons, EmojiconRecents recents, boolean useSystemDefault, int method) {
        EmojiconGridFragment emojiGridFragment = new EmojiconGridFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_EMOJICON_TYPE, type);
        args.putParcelableArray(ARG_EMOJICONS, emojicons);
        args.putBoolean(ARG_USE_SYSTEM_DEFAULTS, useSystemDefault);
        args.putInt(EmojiconsFragment.ARG_DISPLAY_METHOD, method);
        emojiGridFragment.setArguments(args);
        emojiGridFragment.setRecents(recents);
        return emojiGridFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.emojicon_grid, container, false);
    }


    private int mDisplayMethod = EmojiconsFragment.DEFAULT_DISPLAY_METHOD;

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle == null) {
            mEmojiconType = Emojicon.TYPE_UNDEFINED;
            mEmojicons = People.DATA;
            mUseSystemDefault = false;
        } else {
            //noinspection WrongConstant
            mEmojiconType = bundle.getInt(ARG_EMOJICON_TYPE);
            if (mEmojiconType == Emojicon.TYPE_UNDEFINED) {
                Parcelable[] parcels = bundle.getParcelableArray(ARG_EMOJICONS);
                mEmojicons = new Emojicon[parcels.length];
                for (int i = 0; i < parcels.length; i++) {
                    mEmojicons[i] = (Emojicon) parcels[i];
                }
            } else {
                mEmojicons = Emojicon.getEmojicons(mEmojiconType);
            }
            mUseSystemDefault = bundle.getBoolean(ARG_USE_SYSTEM_DEFAULTS);

            mDisplayMethod = bundle.getInt(EmojiconsFragment.ARG_DISPLAY_METHOD);
        }

        switch (mDisplayMethod) {
            case EmojiconsFragment.ORIGINAL_GRID:
                usingOriginalGrid(view);
                break;
            case EmojiconsFragment.NORMAL_GRID_VIEW:
                usingNormalGridView(view);
                break;
            case EmojiconsFragment.NORMAL_GRID_VIEW_THREAD:
                usingGridViewWithThreads(view);
                break;
            case EmojiconsFragment.NORMAL_GRID_VIEW_CACHED_EMOJIS:
                usingGridViewWithCachedEmojis(view);
                break;
            case EmojiconsFragment.NORMAL_GRID_VIEW_STATIC_LAYOUT:
                usingGridViewWithStaticLayout(view);
                break;
            case EmojiconsFragment.RECYCLER_VIEW_GRID_VIEW:
                usingRecyclerViewGrid(view);
                break;
            case EmojiconsFragment.VERTICAL_RECYCLER_VIEW:
                usingVerticalRecyclerView(view);
                break;
            case EmojiconsFragment.TABLE_LAYOUT_VIEW:
                usingTableLayout(view);
                break;
            case EmojiconsFragment.TEXT_NOT_EMOJI:
                usingTextInsteadOfEmoji(view);
                break;
        }
    }

    private void usingOriginalGrid(View view) {
        Log.d(TAG, "Original");

        final GridView gridView = view.findViewById(R.id.grid_view);
        gridView.setAdapter(new GridAdapter(view.getContext(), mEmojicons));
        gridView.setOnItemClickListener(this);
        gridView.setVisibility(View.VISIBLE);
    }

    private void usingNormalGridView(View view) {
        Log.d(TAG, "Normal");

        final GridView gridView = view.findViewById(R.id.grid_view);
        gridView.setAdapter(new GridTextViewAdapter(view.getContext(), mEmojicons));
        gridView.setOnItemClickListener(this);
        gridView.setVisibility(View.VISIBLE);

        TextView textView = new TextView(getContext());
        for (Emojicon emojicon : mEmojicons) {
            textView.setText(emojicon.getEmoji());
        }
    }

    private void usingGridViewWithThreads(View view) {
        Log.d(TAG, "Threads");

        GridView gridView = (GridView) view.findViewById(R.id.grid_view);
        gridView.setAdapter(new GridThreadAdapter(getContext(), mEmojicons, mUseSystemDefault));
        gridView.setOnItemClickListener(this);
        gridView.setVisibility(View.VISIBLE);
    }

    private void usingGridViewWithCachedEmojis(View view) {
        Log.d(TAG, "Caches");

        final GridView gridView = (GridView) view.findViewById(R.id.grid_view);
        gridView.setVisibility(View.VISIBLE);
        new AsyncTask<Void, Void, ArrayList<CharSequence>>() {

            int mEmojiconSize;
            int mEmojiconAlignment;
            int mEmojiconTextSize;
            int mTextStart;
            int mTextLength;

            EmojiconTextView mIcon;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                View v = View.inflate(getContext(), R.layout.emojicon_item, null);
                mIcon = (EmojiconTextView) v.findViewById(R.id.emojicon_icon);
                mEmojiconSize = mIcon.getEmojiconSize();
                mEmojiconAlignment = mIcon.getEmojiconAlignment();
                mEmojiconTextSize = mIcon.getEmojiconTextSize();
                mTextStart = mIcon.getTextStart();
                mTextLength = mIcon.getTextLength();
            }

            @Override
            protected ArrayList<CharSequence> doInBackground(Void... voids) {

                ArrayList<CharSequence> emojis = new ArrayList<>();

                for (Emojicon emojicon : mEmojicons) {
                    if (!TextUtils.isEmpty(emojicon.getEmoji())) {
                        SpannableStringBuilder builder = new SpannableStringBuilder(emojicon.getEmoji());
                        EmojiconHandler.addEmojis(getContext(), builder, mEmojiconSize, mEmojiconAlignment,
                                mEmojiconTextSize, mTextStart, mTextLength, mUseSystemDefault);
                        emojis.add(builder);
                    }
                }

                return emojis;
            }

            @Override
            protected void onPostExecute(ArrayList<CharSequence> emojis) {
                super.onPostExecute(emojis);

                for (CharSequence emoji: emojis) {
                    mIcon.setEmoji(emoji);
                }

                gridView.setAdapter(new GridStringAdapter(getContext(), emojis, mUseSystemDefault));
                gridView.setOnItemClickListener(EmojiconGridFragment.this);
            }
        }.execute();
    }

    private void usingGridViewWithStaticLayout(View view) {
        Log.d(TAG, "Static Layout");

        final GridView gridView = view.findViewById(R.id.grid_view);
        gridView.setAdapter(new GridStaticLayoutAdapter(view.getContext(), mEmojicons));
        gridView.setOnItemClickListener(this);
        gridView.setVisibility(View.VISIBLE);

        TextLayoutView textView = new TextLayoutView(getContext());
        for (Emojicon emojicon : mEmojicons) {
            textView.setEmoji(emojicon.getEmoji());
        }
    }

    private void usingRecyclerViewGrid(View view) {
        Log.d(TAG, "RecyclerView with GridLayout");

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_grid_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 8));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setVisibility(View.VISIBLE);

        final ArrayList<CharSequence> emojis = new ArrayList<>();
        final RecyclerViewGridAdapter adapter = new RecyclerViewGridAdapter(emojis, mUseSystemDefault);
        recyclerView.setAdapter(adapter);

        new AsyncTask<Void, Void, Void>() {

            int mEmojiconSize;
            int mEmojiconTextSize;
            final int mEmojiconAlignment = DynamicDrawableSpan.ALIGN_BASELINE;
            final int mTextStart = 0;
            final int mTextLength = -1;

            EmojiconTextView mIcon;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                View v = View.inflate(getContext(), R.layout.emojicon_item, null);
                mIcon = (EmojiconTextView) v.findViewById(R.id.emojicon_icon);
                mEmojiconSize = mIcon.getEmojiconSize();
                mEmojiconTextSize = mIcon.getEmojiconTextSize();
            }

            @Override
            protected Void doInBackground(Void... voids) {

                for (Emojicon emojicon : mEmojicons) {
                    if (!TextUtils.isEmpty(emojicon.getEmoji())) {
                        SpannableStringBuilder builder = new SpannableStringBuilder(emojicon.getEmoji());
                        EmojiconHandler.addEmojis(getContext(), builder, mEmojiconSize, mEmojiconAlignment,
                                mEmojiconTextSize, mTextStart, mTextLength, mUseSystemDefault);
                        emojis.add(builder);
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                for (CharSequence emoji: emojis) {
                    mIcon.setEmoji(emoji);
                }

                adapter.notifyDataSetChanged();
            }
        }.execute();

    }

    private void usingVerticalRecyclerView(View view) {
        Log.d(TAG, "Vertical RecyclerView");

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_grid_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mEmojicons, mUseSystemDefault);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void usingTableLayout(View view) {
        Log.d(TAG, "TableLayout");

        EmojisTable emojisTable = (EmojisTable) view.findViewById(R.id.tl_grid_view);
        emojisTable.setEmojis(mEmojicons, mUseSystemDefault);
        emojisTable.setOnItemClickListener(this);

        view.findViewById(R.id.sv_grid_view).setVisibility(View.VISIBLE);
    }

    private void usingTextInsteadOfEmoji(View view) {
        Log.d(TAG, "Text instead of emoji");

        final GridView gridView = view.findViewById(R.id.grid_view);
        gridView.setVisibility(View.VISIBLE);

        ArrayList<CharSequence> texts = new ArrayList<>();
        for (int i = 0; i < mEmojicons.length; i++) {
            texts.add(" " + i);
        }
        gridView.setAdapter(new GridStringAdapter(view.getContext(), texts, false));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray(ARG_EMOJICONS, mEmojicons);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEmojiconClickedListener) {
            mOnEmojiconClickedListener = (OnEmojiconClickedListener) context;
        } else if (getParentFragment() instanceof OnEmojiconClickedListener) {
            mOnEmojiconClickedListener = (OnEmojiconClickedListener) getParentFragment();
        } else {
            throw new IllegalArgumentException(context + " must implement interface " + OnEmojiconClickedListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        mOnEmojiconClickedListener = null;
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mOnEmojiconClickedListener != null) {
            mOnEmojiconClickedListener.onEmojiconClicked((Emojicon) parent.getItemAtPosition(position));
        }
        if (mRecents != null) {
            mRecents.addRecentEmoji(view.getContext(), ((Emojicon) parent.getItemAtPosition(position)));
        }
    }

    @Override
    public void onItemClick(Emojicon emojicon) {
        if (mOnEmojiconClickedListener != null) {
            mOnEmojiconClickedListener.onEmojiconClicked(emojicon);
        }
        if (mRecents != null) {
            mRecents.addRecentEmoji(getContext(), emojicon);
        }
    }

    private void setRecents(EmojiconRecents recents) {
        mRecents = recents;
    }

    public interface OnEmojiconClickedListener {
        void onEmojiconClicked(Emojicon emojicon);
    }
}
