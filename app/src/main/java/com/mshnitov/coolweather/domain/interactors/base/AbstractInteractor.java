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

package com.mshnitov.coolweather.domain.interactors.base;

import com.mshnitov.coolweather.domain.executor.Executor;
import com.mshnitov.coolweather.domain.interactors.Interactor;
import com.mshnitov.coolweather.tools.threading.MainThread;

/**
 * This abstract class implements some common methods for all interactors. Cancelling an interactor,
 * check if its running and finishing an interactor has mostly the same code throughout so that is
 * why this class was created.
 *
 * Field methods are declared volatile as we might use these methods from different threads
 * (mainly from UI).
 *
 * For example, when an activity is getting destroyed then we should probably cancel an interactor
 * but the request will come from the UI thread unless the request was specifically assigned to a
 * background thread.
 */
public abstract class AbstractInteractor implements Interactor {

  protected final Executor mThreadExecutor;
  protected final MainThread mMainThread;

  protected volatile boolean mIsCanceled;
  protected volatile boolean mIsRunning;

  protected String name;

  public AbstractInteractor(Executor threadExecutor, MainThread mainThread, String interactorName) {
    mThreadExecutor = threadExecutor;
    mMainThread = mainThread;
    name = interactorName;
  }

  /**
   * This method contains the actual business logic of the interactor. It SHOULD NOT BE USED
   * DIRECTLY but, instead, a developer should call the execute() method of an interactor
   * to make sure the operation is done on a background thread.
   *
   * This method should only be called directly while doing unit/integration tests. That is the only
   * reason it is declared public as to help with easier testing.
   */
  public abstract void run();

  public void cancel() {
    mIsCanceled = true;
    mIsRunning = false;
  }

  public boolean isRunning() {
    return mIsRunning;
  }

  public boolean isCanceled() {
    return mIsCanceled;
  }

  public void onFinished() {
    mIsRunning = false;
    mIsCanceled = false;
  }

  public String getName() {
    return name;
  }

  public void execute() {

    // mark this interactor as running
    this.mIsRunning = true;

    // start running this interactor in a background thread
    mThreadExecutor.execute(this);
  }
}
