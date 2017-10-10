package io.github.rockerhieu.emojicon;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.Arrays;

import io.github.rockerhieu.emojicon.emoji.Emojicon;

/**
 * Created by cpu10661 on 9/29/17.
 */

public class EmojisTable extends TableLayout {

    private static final float DENSITY = Resources.getSystem().getDisplayMetrics().density;
    private static final int NUMBER_OF_EMOJIS_PER_ROW = 10;

    private OnItemClickListener mOnItemClickListener = null;
    private ArrayList<Emojicon> mEmojicons;
    private boolean mUseSystemDefault = false;

    public EmojisTable(Context context) {
        super(context);
    }

    public EmojisTable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }


    public void setEmojis(Emojicon[] emojicons, boolean useSystemDefault) {
        mEmojicons = new ArrayList<>(Arrays.asList(emojicons));
        mUseSystemDefault = useSystemDefault;
        populateEmojisTable();
    }

    public void setEmojis(ArrayList<Emojicon> emojicons, boolean useSystemDefault) {
        mEmojicons = emojicons;
        mUseSystemDefault = useSystemDefault;
        populateEmojisTable();
    }

    public void notifyDataSetChanged() {
        removeAllViews();
        populateEmojisTable();
    }

    private void populateEmojisTable() {

        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams emojiParams = new TableRow.LayoutParams(0, dpToPixel(36), 1f);

        TableRow row;
        int rows = mEmojicons.size() / 10 + 1;
        for (int i = 0; i < rows; i++) {

            // create new row
            row = new TableRow(getContext());
            row.setLayoutParams(rowParams);
            row.setWeightSum(NUMBER_OF_EMOJIS_PER_ROW);

            // create emojis in each row
            int end = Math.min((i + 1) * NUMBER_OF_EMOJIS_PER_ROW, mEmojicons.size());
            for (int j = i * NUMBER_OF_EMOJIS_PER_ROW; j < end; j++) {

                EmojiconTextView emojiconTextView = new EmojiconTextView(getContext());
                emojiconTextView.setLayoutParams(emojiParams);
                emojiconTextView.setGravity(Gravity.CENTER);

                emojiconTextView.setEmojiconSize(dpToPixel(30));
                emojiconTextView.setUseSystemDefault(mUseSystemDefault);
                emojiconTextView.setText(mEmojicons.get(j).getEmoji());

                final Emojicon emojicon = mEmojicons.get(i);
                emojiconTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(emojicon);
                        }
                    }
                });

                row.addView(emojiconTextView);
            }

            addView(row);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Emojicon emojicon);
    }

    public static int dpToPixel(int dp) {
        return (int) (dp * DENSITY + 0.5f);
    }
}
