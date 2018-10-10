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

import android.app.Application;
import com.mshnitov.coolweather.tools.di.AppComponent;
import com.mshnitov.coolweather.tools.di.AppModule;
import com.mshnitov.coolweather.tools.di.DaggerAppComponent;
import com.squareup.leakcanary.LeakCanary;

public class AndroidApplication extends Application {

  private AppComponent component;

  public AppComponent getComponent() {
    return component;
  }

  @Override public void onCreate() {
    super.onCreate();

    component = buildComponent();

    injectMembers();
    initLeakDetection();
  }

  private void injectMembers() {
    component.inject(this);
  }

  private void initLeakDetection() {
    if (BuildConfig.DEBUG) {
      if (LeakCanary.isInAnalyzerProcess(this)) {
        return;
      }
      LeakCanary.install(this);
    }
  }

  private AppComponent buildComponent() {
    return DaggerAppComponent.builder().appModule(new AppModule(this)).build();
  }
}
