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
import android.os.Debug;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.OrientationHelper;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.ComponentTree;
import com.facebook.litho.ComponentsSystrace;
import com.facebook.litho.LithoView;
import com.facebook.litho.utils.DisplayListUtils;
import com.facebook.litho.widget.GridLayoutInfo;
import com.facebook.litho.widget.Recycler;
import com.facebook.litho.widget.RecyclerBinder;

import io.github.rockerhieu.emojicon.EmojiconRecents;
import io.github.rockerhieu.emojicon.emoji.Emojicon;
import io.github.rockerhieu.emojicon.emoji.People;

/**
 * @author Hieu Rocker (rockerhieu@gmail.com)
 */
public class EmojiconGridFragmentUsingLitho extends Fragment implements EmojiComponentSpec.OnClickListener {

    private static final String TAG = EmojiconGridFragmentUsingLitho.class.getSimpleName();

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

    public static EmojiconGridFragmentUsingLitho newInstance(Emojicon[] emojicons, EmojiconRecents recents) {
        return newInstance(Emojicon.TYPE_UNDEFINED, emojicons, recents, false);
    }

    public static EmojiconGridFragmentUsingLitho newInstance(
            @Emojicon.Type int type, EmojiconRecents recents, boolean useSystemDefault) {
        return newInstance(type, null, recents, useSystemDefault);
    }

    public static EmojiconGridFragmentUsingLitho newInstance(
            @Emojicon.Type int type, Emojicon[] emojicons, EmojiconRecents recents, boolean useSystemDefault) {
        EmojiconGridFragmentUsingLitho emojiGridFragment = new EmojiconGridFragmentUsingLitho();
        Bundle args = new Bundle();
        args.putInt(ARG_EMOJICON_TYPE, type);
        args.putParcelableArray(ARG_EMOJICONS, emojicons);
        args.putBoolean(ARG_USE_SYSTEM_DEFAULTS, useSystemDefault);
        emojiGridFragment.setArguments(args);
        emojiGridFragment.setRecents(recents);
        return emojiGridFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("EmojiconGridFragment", "Litho");

        final ComponentContext context = new ComponentContext(getContext());

        final RecyclerBinder binder = new RecyclerBinder.Builder()
                .layoutInfo(new GridLayoutInfo(getContext(), 10, OrientationHelper.VERTICAL, false))
                .canPrefetchDisplayLists(true)
                .canCacheDrawingDisplayLists(true)
                .build(context);

        final Component component = Recycler.create(context)
                .binder(binder)
                .hasFixedSize(true)
                .build();

        for (int i = 0; i < mEmojicons.length; i++) {
            Component emojiComponent = EmojiComponent.create(context)
                    .text(mEmojicons[i].getEmoji())
                    .textSizeDip(15)
                    .textAlignment(Layout.Alignment.ALIGN_CENTER)
                    .listener(this)
                    .build();
            binder.insertItemAt(i, emojiComponent);
        }

        return LithoView.create(context, component);

//        final ComponentTree componentTree = ComponentTree.create(context, component).canPrefetchDisplayLists(true).build();
//        LithoView lithoView = new LithoView(getContext());
//        lithoView.setComponentTree(componentTree);
//        DisplayListUtils.prefetchDisplayLists(lithoView);
//        return lithoView;
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

    private void setRecents(EmojiconRecents recents) {
        mRecents = recents;
    }

    @Override
    public void onClickListener(Emojicon emojicon) {
        if (mOnEmojiconClickedListener != null) {
            mOnEmojiconClickedListener.onEmojiconClicked(emojicon);
        }
        if (mRecents != null) {
            mRecents.addRecentEmoji(getContext(), emojicon);
        }
    }

    public interface OnEmojiconClickedListener {
        void onEmojiconClicked(Emojicon emojicon);
    }
}
