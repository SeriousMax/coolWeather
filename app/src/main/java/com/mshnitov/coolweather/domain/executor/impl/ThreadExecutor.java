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

package com.mshnitov.coolweather.domain.executor.impl;

import com.mshnitov.coolweather.domain.executor.Executor;
import com.mshnitov.coolweather.domain.interactors.base.AbstractInteractor;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This singleton class will make sure that each interactor operation gets a background thread.
 */
public class ThreadExecutor implements Executor {

  private static final int CORE_POOL_SIZE = 3;
  private static final int MAX_POOL_SIZE = 5;
  private static final int KEEP_ALIVE_TIME = 120;
  private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
  private static final BlockingQueue<Runnable> WORK_QUEUE = new LinkedBlockingQueue<>();
  private static volatile ThreadExecutor sThreadExecutor;
  private final ThreadPoolExecutor mThreadPoolExecutor;

  private ThreadExecutor() {
    mThreadPoolExecutor =
        new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TIME_UNIT,
            WORK_QUEUE);
  }

  /**
   * Returns a singleton instance of this executor. If the executor is not initialized then
   * it initializes it and returns the instance.
   */
  public static Executor getInstance() {
    if (sThreadExecutor == null) {
      sThreadExecutor = new ThreadExecutor();
    }

    return sThreadExecutor;
  }

  @Override public void execute(final AbstractInteractor interactor) {
    mThreadPoolExecutor.submit(() -> {
      // Naming each thread with name of interactor for easier threads debugging
      final Thread currentThread = Thread.currentThread();
      final String oldName = currentThread.getName();
      currentThread.setName("Processing - " + interactor.getName());

      try {
        // run the main logic
        interactor.run();

        // mark it as finished
        interactor.onFinished();
      } finally {
        currentThread.setName(oldName);
      }
    });
  }
}