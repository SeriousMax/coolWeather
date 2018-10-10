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

package com.mshnitov.coolweather.data.network;

import com.mshnitov.coolweather.domain.entities.WeatherResponse;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * A service class that implements WeatherApi.
 */
@Singleton public class WeatherService implements WeatherApi {

  private final WeatherApi mWeatherApi;

  @Inject public WeatherService(Retrofit retrofit) {
    mWeatherApi = retrofit.create(WeatherApi.class);
  }

  @Override public Call<WeatherResponse> getWeatherForCity(String cityName, String apiKey) {
    return mWeatherApi.getWeatherForCity(cityName, apiKey);
  }
}
