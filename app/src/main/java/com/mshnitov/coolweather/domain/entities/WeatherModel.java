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

package com.mshnitov.coolweather.domain.entities;

public class WeatherModel {

  private final String mCityName;
  private final float mTemperature;
  private final String mIconUrl;

  public WeatherModel(String cityName, int temperature, String iconUrl) {
    mCityName = cityName;
    mTemperature = temperature;
    mIconUrl = iconUrl;
  }

  public WeatherModel(WeatherResponse response) {
    mCityName = response.getCityName();
    mTemperature = response.getTemperature();
    mIconUrl = response.getIconUrl();
  }

  public String getCityName() {
    return mCityName;
  }

  public float getTemperature() {
    return mTemperature;
  }

  public String getIconUrl() {
    return mIconUrl;
  }
}
