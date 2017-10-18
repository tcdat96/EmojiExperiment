package com.rockerhieu.emojicon.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import io.github.rockerhieu.emojicon.EmojiconEditText;
import io.github.rockerhieu.emojicon.EmojiconGridFragment;
import io.github.rockerhieu.emojicon.EmojiconGridFragmentBackup;
import io.github.rockerhieu.emojicon.EmojiconTextView;
import io.github.rockerhieu.emojicon.EmojiconsFragment;
import io.github.rockerhieu.emojicon.adapter.Litho.EmojiconGridFragmentUsingLitho;
import io.github.rockerhieu.emojicon.emoji.Emojicon;

public class MainEmojiconsActivity extends AppCompatActivity implements
        EmojiconGridFragment.OnEmojiconClickedListener, EmojiconGridFragmentUsingLitho.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {

    private EmojiconEditText mEditEmojicon;
    private EmojiconTextView mTxtEmojicon;
    private CheckBox mCheckBox;
    private int mDisplayMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_emojicons);
        mEditEmojicon = (EmojiconEditText) findViewById(R.id.editEmojicon);
        mTxtEmojicon = (EmojiconTextView) findViewById(R.id.txtEmojicon);
        mEditEmojicon.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTxtEmojicon.setText(s);
            }
        });
        mCheckBox = (CheckBox) findViewById(R.id.use_system_default);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mEditEmojicon.setUseSystemDefault(b);
                mTxtEmojicon.setUseSystemDefault(b);
                setEmojiconFragment(b);
            }
        });

        mDisplayMethod = getIntent().getIntExtra(EmojiconsFragment.ARG_DISPLAY_METHOD, EmojiconsFragment.DEFAULT_DISPLAY_METHOD);
        int methodResId = getIntent().getIntExtra(MainActivity.ARG_DISPLAY_METHOD_RES_ID, EmojiconsFragment.DEFAULT_DISPLAY_METHOD);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(methodResId);
        }

        setEmojiconFragment(mCheckBox.isChecked());
    }

    private void setEmojiconFragment(boolean useSystemDefault) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault, mDisplayMethod))
                .commit();
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(mEditEmojicon, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(mEditEmojicon);
    }

    public void openEmojiconsActivity(View view) {
        startActivity(new Intent(this, EmojiconsActivity.class));
    }
}
