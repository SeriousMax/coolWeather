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

package com.mshnitov.coolweather;

import com.mshnitov.coolweather.data.repository.WeatherRepository;
import com.mshnitov.coolweather.domain.entities.WeatherModel;
import com.mshnitov.coolweather.domain.executor.Executor;
import com.mshnitov.coolweather.domain.interactors.GetWeatherInteractor;
import com.mshnitov.coolweather.domain.interactors.impl.GetWeatherInteractorImpl;
import com.mshnitov.coolweather.tools.Result;
import com.mshnitov.coolweather.tools.threading.MainThread;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class) public class InteractorsUnitTest {

  @Mock private WeatherRepository.Network mWeatherRepository;

  @Mock private Executor mExecutor;

  @Mock private MainThread mMainThread;

  @Mock private GetWeatherInteractor.Callback mCallback;

  @Test public void testGettingWeatherSuccess() {

    String testCityName = "Kiev";
    WeatherModel weather = new WeatherModel(testCityName, 12, "10d");

    Mockito.when(mWeatherRepository.get(testCityName)).thenReturn(new Result<>(weather));

    // Since mCallback methods must be called in the main thread, but we do not have access to it
    // therefore, we will forcibly call the methods passed in Runnable
    Mockito.when(mMainThread.post(any(Runnable.class))).thenAnswer((Answer) invocation -> {
      ((Runnable) invocation.getArgument(0)).run();
      return null;
    });

    GetWeatherInteractorImpl interactor =
        new GetWeatherInteractorImpl(mExecutor, mMainThread, mWeatherRepository, mCallback,
            testCityName);

    interactor.run();

    // Check mWeatherRepository.get(String cityName) was called
    Mockito.verify(mWeatherRepository).get(testCityName);
    Mockito.verifyNoMoreInteractions(mWeatherRepository);

    // Check mCallback.onWeatherRetrieved(WeatherModel weather) was called
    Mockito.verify(mCallback).onWeatherRetrieved(weather);
  }
}
