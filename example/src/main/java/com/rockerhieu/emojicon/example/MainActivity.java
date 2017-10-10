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
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import io.github.rockerhieu.emojicon.EmojiconsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onMethodButtonClicked(View view) {
        int method = EmojiconsFragment.DEFAULT_DISPLAY_METHOD;
        switch (view.getId()) {
            case R.id.btn_original:
                method = EmojiconsFragment.ORIGINAL_GRID;
                break;
            case R.id.btn_normal:
                method = EmojiconsFragment.NORMAL_GRID_VIEW;
                break;
            case R.id.btn_threads:
                method = EmojiconsFragment.NORMAL_GRID_VIEW_THREAD;
                break;
            case R.id.btn_caches:
                method = EmojiconsFragment.NORMAL_GRID_VIEW_CACHED_EMOJIS;
                break;
            case R.id.btn_static_layout:
                method = EmojiconsFragment.NORMAL_GRID_VIEW_STATIC_LAYOUT;
                break;
            case R.id.btn_rv_grid:
                method = EmojiconsFragment.RECYCLER_VIEW_GRID_VIEW;
                break;
            case R.id.btn_vertical_rv:
                method = EmojiconsFragment.VERTICAL_RECYCLER_VIEW;
                break;
            case R.id.btn_table_layout:
                method = EmojiconsFragment.TABLE_LAYOUT_VIEW;
                break;
            case R.id.btn_litho:
                method = EmojiconsFragment.LITHO_LIBRARY;
                break;
        }

        Intent intent = new Intent(this, MainEmojiconsActivity.class);
        intent.putExtra(EmojiconsFragment.ARG_DISPLAY_METHOD, method);
        startActivity(intent);
    }
}
