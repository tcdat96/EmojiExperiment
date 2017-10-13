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

package com.rockerhieu.emojicon.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.LinkedHashMap;

import io.github.rockerhieu.emojicon.EmojiconsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LinkedHashMap<Integer, Integer> displayMethods = new LinkedHashMap<>();
        displayMethods.put(R.string.original_grid_view, EmojiconsFragment.ORIGINAL_GRID);
        displayMethods.put(R.string.normal_grid_view, EmojiconsFragment.NORMAL_GRID_VIEW);
        displayMethods.put(R.string.grid_view_with_threads, EmojiconsFragment.NORMAL_GRID_VIEW_THREAD);
        displayMethods.put(R.string.grid_view_with_caches, EmojiconsFragment.NORMAL_GRID_VIEW_CACHED_EMOJIS);
        displayMethods.put(R.string.grid_view_with_static_layout, EmojiconsFragment.NORMAL_GRID_VIEW_STATIC_LAYOUT);
        displayMethods.put(R.string.recycler_view_grid, EmojiconsFragment.RECYCLER_VIEW_GRID_VIEW);
        displayMethods.put(R.string.vertical_recycler_view, EmojiconsFragment.VERTICAL_RECYCLER_VIEW);
        displayMethods.put(R.string.table_layout, EmojiconsFragment.TABLE_LAYOUT_VIEW);
        displayMethods.put(R.string.litho_library, EmojiconsFragment.LITHO_LIBRARY);
        displayMethods.put(R.string.text_not_emoji, EmojiconsFragment.TEXT_NOT_EMOJI);

        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.ll_buttons);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 8, 0, 0);
        for (final int id : displayMethods.keySet()) {
            Button button = new Button(this);
            button.setLayoutParams(params);
            button.setText(id);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, MainEmojiconsActivity.class);
                    intent.putExtra(EmojiconsFragment.ARG_DISPLAY_METHOD, displayMethods.get(id));
                    startActivity(intent);
                }
            });
            rootLayout.addView(button);
        }
    }
}
