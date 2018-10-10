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

package com.mshnitov.coolweather.domain.interactors.impl;

import com.mshnitov.coolweather.data.repository.WeatherRepository;
import com.mshnitov.coolweather.domain.entities.WeatherModel;
import com.mshnitov.coolweather.domain.executor.Executor;
import com.mshnitov.coolweather.domain.interactors.GetWeatherInteractor;
import com.mshnitov.coolweather.domain.interactors.base.AbstractInteractor;
import com.mshnitov.coolweather.tools.Failure;
import com.mshnitov.coolweather.tools.Result;
import com.mshnitov.coolweather.tools.threading.MainThread;
import javax.inject.Inject;

public class GetWeatherInteractorImpl extends AbstractInteractor implements GetWeatherInteractor {

  // Interactor name for threads debugging
  private static final String name = "GetWeatherInteractor";

  private final WeatherRepository mWeatherRepository;
  private final Callback mCallback;
  private final String mCityName;

  @Inject public GetWeatherInteractorImpl(Executor threadExecutor, MainThread mainThread,
      WeatherRepository weatherRepository, Callback callback, String cityName) {
    super(threadExecutor, mainThread, name);
    mWeatherRepository = weatherRepository;
    mCallback = callback;
    mCityName = cityName;
  }

  private void notifyError(Failure failure) {
    mMainThread.post(() -> mCallback.onRetrievalFailed(failure));
  }

  private void postWeather(final WeatherModel weather) {
    mMainThread.post(() -> mCallback.onWeatherRetrieved(weather));
  }

  @Override public void run() {
    final Result<WeatherModel> result = mWeatherRepository.get(mCityName);

    // If interactor canceled nothing will return
    if (mIsCanceled) return;

    if (result.isSuccess()) {
      WeatherModel weather = result.getSuccess();
      postWeather(weather);
    } else {
      notifyError(result.getFailure());
    }
  }
}
