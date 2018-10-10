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

package com.mshnitov.coolweather.tools.threading.impl;

import android.os.Handler;
import android.os.Looper;
import com.mshnitov.coolweather.tools.threading.MainThread;

/**
 * This class makes sure that the runnable we provide will be run on the main UI thread.
 */
public class MainThreadImpl implements MainThread {
  private static MainThread sMainThread;

  private final Handler mHandler;

  private MainThreadImpl() {
    mHandler = new Handler(Looper.getMainLooper());
  }

  public static MainThread getInstance() {
    if (sMainThread == null) {
      sMainThread = new MainThreadImpl();
    }

    return sMainThread;
  }

  @Override public boolean post(Runnable runnable) {
    return mHandler.post(runnable);
  }
}
