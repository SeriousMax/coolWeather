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

package com.mshnitov.coolweather.data.repository;

import com.mshnitov.coolweather.data.network.WeatherService;
import com.mshnitov.coolweather.domain.entities.WeatherModel;
import com.mshnitov.coolweather.domain.entities.WeatherResponse;
import com.mshnitov.coolweather.tools.Env;
import com.mshnitov.coolweather.tools.Failure;
import com.mshnitov.coolweather.tools.Result;
import com.mshnitov.coolweather.tools.platform.NetworkHandler;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Response;

import static com.mshnitov.coolweather.tools.Failure.DATA_ERROR;
import static com.mshnitov.coolweather.tools.Failure.NETWORK_ERROR;
import static com.mshnitov.coolweather.tools.Failure.SERVER_ERROR;
import static com.mshnitov.coolweather.tools.Failure.UNDEFINED_ERROR;

/**
 * A WeatherRepository now has only network implementation, but it can be enhanced
 * with sql database and some kind of cache implementation.
 */
public interface WeatherRepository {

  /**
   * This method returns result of getting current weather for specified city.
   *
   * @param cityName - name of the city
   * @return result of trying to get the weather, that can contains WeatherModel on success or
   * some issue information on fail.
   */
  Result<WeatherModel> get(String cityName);

  /**
   * Network implementation of WeatherRepository interface.
   */
  class Network implements WeatherRepository {

    private final NetworkHandler mNetworkHandler;
    private final WeatherService mService;

    @Inject public Network(NetworkHandler networkHandler, WeatherService service) {
      mNetworkHandler = networkHandler;
      mService = service;
    }

    @Override public Result<WeatherModel> get(String cityName) {
      if (!mNetworkHandler.isConnected()) return new Result<>(new Failure(NETWORK_ERROR));

      try {
        Call<WeatherResponse> call = mService.getWeatherForCity(cityName, Env.API_ID);

        // Here we call 'execute' method to synchronously send the request and return its response.
        Response<WeatherResponse> response = call.execute();

        if (!response.isSuccessful()) {
          return new Result<>(new Failure(SERVER_ERROR, cityName + " - " + response.message()));
        }
        if (response.body() == null) return new Result<>(new Failure(DATA_ERROR));

        return new Result<>(new WeatherModel(response.body()));
      } catch (Exception e) {
        e.printStackTrace();
        return new Result<>(new Failure(UNDEFINED_ERROR));
      }
    }
  }
}
