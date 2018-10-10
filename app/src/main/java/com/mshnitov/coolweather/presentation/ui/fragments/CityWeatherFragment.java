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

package com.mshnitov.coolweather.presentation.ui.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import com.bumptech.glide.Glide;
import com.mshnitov.coolweather.R;
import com.mshnitov.coolweather.data.repository.WeatherRepository;
import com.mshnitov.coolweather.presentation.ui.fragments.base.BaseFragment;
import com.mshnitov.coolweather.presentation.viewmodels.impl.CityWeatherViewModel;
import java.util.Locale;
import javax.inject.Inject;

public class CityWeatherFragment extends BaseFragment {

  private static final int INPUT_DELAY = 750;
  private final Handler handler = new Handler(Looper.getMainLooper());
  //region Properties
  @Inject WeatherRepository mWeatherRepository;
  private CityWeatherViewModel viewModel;
  private Runnable mRunnable;
  //endregion

  //region Constructor
  public static CityWeatherFragment newInstance() {
    Bundle args = new Bundle();
    CityWeatherFragment fragment = new CityWeatherFragment();
    fragment.setArguments(args);
    return fragment;
  }
  //endregion

  //region LifeCycle
  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    app.inject(this);

    viewModel = ViewModelProviders.of(this).get(CityWeatherViewModel.class);

    // Add viewModel as LifeCycle observer for current Fragment
    getLifecycle().addObserver(viewModel);
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    return inflater.inflate(R.layout.fragment_city_weather, container, false);
  }

  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    initView(view);

    if (savedInstanceState == null) {
      showProgress();
      viewModel.getWeatherForCityName("Kiev", mWeatherRepository);
    }
  }

  @Override public void onDestroy() {

    // Remove viewModel as LifeCycle observer for current Fragment
    getLifecycle().removeObserver(viewModel);

    super.onDestroy();
  }
  //endregion

  //region Override BaseFragment methods
  @Override protected int layoutId() {
    return R.layout.fragment_city_weather;
  }
  //endregion

  //region Private methods
  private void initView(View view) {
    TextView cityNameLbl = view.findViewById(R.id.cityNameLbl);
    TextView temperatureLbl = view.findViewById(R.id.temperatureLbl);
    EditText searchField = view.findViewById(R.id.searchField);
    ImageView icon = view.findViewById(R.id.weatherIcon);

    viewModel.getFailure().observe(this, failure -> {
      if (failure != null) {
        hideProgress();
        showToast(failure.toString());
      }
    });

    viewModel.getWeather().observe(this, weather -> {
      if (weather != null) {
        hideProgress();
        cityNameLbl.setText(weather.getCityName());
        temperatureLbl.setText(
            String.format(Locale.getDefault(), "%.1f °C", weather.getTemperature()));
        Glide.with(this).load(Uri.parse(weather.getIconUrl())).into(icon);
      }
    });

    searchField.addTextChangedListener(new TextChangedListener());
  }
  //endregion

  private class TextChangedListener implements TextWatcher {

    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override public void afterTextChanged(Editable s) {
      String query = s.toString();
      if (query.length() < 2) return;
      // Use input delay to request new weather no more than once per INPUT_DELAY
      handler.removeCallbacks(mRunnable);
      mRunnable = () -> viewModel.getWeatherForCityName(query, mWeatherRepository);
      handler.postDelayed(mRunnable, INPUT_DELAY);
    }
  }
}