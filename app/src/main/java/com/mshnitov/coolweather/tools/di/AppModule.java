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

package com.mshnitov.coolweather.tools.di;

import android.app.Application;
import android.content.Context;
import com.mshnitov.coolweather.BuildConfig;
import com.mshnitov.coolweather.data.repository.WeatherRepository;
import com.mshnitov.coolweather.domain.executor.Executor;
import com.mshnitov.coolweather.domain.executor.impl.ThreadExecutor;
import com.mshnitov.coolweather.tools.Env;
import com.mshnitov.coolweather.tools.threading.MainThread;
import com.mshnitov.coolweather.tools.threading.impl.MainThreadImpl;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module public class AppModule {

  private final Application app;

  public AppModule(Application app) {
    this.app = app;
  }

  @Provides @Singleton public Executor providesThreadExecutor() {
    return ThreadExecutor.getInstance();
  }

  @Provides @Singleton public MainThread providesMainThread() {
    return MainThreadImpl.getInstance();
  }

  @Provides @Singleton public Context provideContext() {
    return app.getApplicationContext();
  }

  @Provides @Singleton public Retrofit providesRetrofit() {
    return new Retrofit.Builder().baseUrl(Env.BASE_URL)
        .client(createClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }

  private OkHttpClient createClient() {
    OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
    if (BuildConfig.DEBUG) {
      HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
      logging.setLevel(HttpLoggingInterceptor.Level.BODY);
      httpClientBuilder.addInterceptor(logging);
    }
    return httpClientBuilder.build();
  }

  @Provides @Singleton
  public WeatherRepository providesWeatherRepository(WeatherRepository.Network weatherRepository) {
    return weatherRepository;
  }
}
