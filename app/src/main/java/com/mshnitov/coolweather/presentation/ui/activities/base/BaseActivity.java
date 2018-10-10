/*
 * Copyright (C) 2018 rush Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mshnitov.coolweather.presentation.ui.activities.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import com.mshnitov.coolweather.R;
import com.mshnitov.coolweather.presentation.ui.fragments.base.BaseFragment;

public abstract class BaseActivity extends AppCompatActivity {

  public ConstraintLayout progress;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolBar);
    setSupportActionBar(toolbar);
    addFragment(savedInstanceState);

    progress = toolbar.findViewById(R.id.progress);
  }

  @Override public void onBackPressed() {
    FragmentManager fragmentManager = getSupportFragmentManager();
    BaseFragment bf = (BaseFragment) fragmentManager.findFragmentById(R.id.fragmentContainer);
    if (bf != null) bf.onBackPressed();
    super.onBackPressed();
  }

  private void addFragment(Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      FragmentManager fragmentManager = getSupportFragmentManager();
      fragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment()).commit();
    }
  }

  abstract protected BaseFragment fragment();
}
