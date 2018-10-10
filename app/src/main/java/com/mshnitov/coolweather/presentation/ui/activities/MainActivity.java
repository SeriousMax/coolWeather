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

package com.mshnitov.coolweather.presentation.ui.activities;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import com.mshnitov.coolweather.R;
import com.mshnitov.coolweather.presentation.ui.activities.base.BaseActivity;
import com.mshnitov.coolweather.presentation.ui.fragments.CityWeatherFragment;
import com.mshnitov.coolweather.presentation.ui.fragments.base.BaseFragment;

public class MainActivity extends BaseActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (savedInstanceState == null) {
      FragmentManager supportFragmentManager = getSupportFragmentManager();

      supportFragmentManager.beginTransaction()
          .add(R.id.fragmentContainer, CityWeatherFragment.newInstance(), "cityWeather")
          .commit();
    }
  }

  @Override protected BaseFragment fragment() {
    return CityWeatherFragment.newInstance();
  }
}
