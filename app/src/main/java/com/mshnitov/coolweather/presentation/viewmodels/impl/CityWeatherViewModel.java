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

package com.mshnitov.coolweather.presentation.viewmodels.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.mshnitov.coolweather.data.repository.WeatherRepository;
import com.mshnitov.coolweather.domain.entities.WeatherModel;
import com.mshnitov.coolweather.domain.executor.impl.ThreadExecutor;
import com.mshnitov.coolweather.domain.interactors.GetWeatherInteractor;
import com.mshnitov.coolweather.domain.interactors.impl.GetWeatherInteractorImpl;
import com.mshnitov.coolweather.presentation.viewmodels.base.AbstractViewModel;
import com.mshnitov.coolweather.tools.Failure;
import com.mshnitov.coolweather.tools.threading.impl.MainThreadImpl;

public class CityWeatherViewModel extends AbstractViewModel {

  private final MutableLiveData<WeatherModel> mWeather = new MutableLiveData<>();
  private GetWeatherInteractorImpl mInteractor;

  public LiveData<WeatherModel> getWeather() {
    return mWeather;
  }

  public void getWeatherForCityName(String cityName, WeatherRepository weatherRepository) {

    // If interactor is already running we will cancel it
    cancelInteractor();

    mInteractor =
        new GetWeatherInteractorImpl(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(),
            weatherRepository, new GetWeatherInteractor.Callback() {

          @Override public void onWeatherRetrieved(WeatherModel weatherModel) {
            mWeather.setValue(weatherModel);
          }

          @Override public void onRetrievalFailed(Failure failure) {
            handleFailure(failure);
          }
        }, cityName);

    mInteractor.execute();
  }

  @Override public void destroy() {
    cancelInteractor();
  }

  private void cancelInteractor() {
    if (mInteractor != null && mInteractor.isRunning()) {
      mInteractor.cancel();
    }
  }
}
