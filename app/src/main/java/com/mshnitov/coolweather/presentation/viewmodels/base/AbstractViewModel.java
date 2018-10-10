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

package com.mshnitov.coolweather.presentation.viewmodels.base;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.mshnitov.coolweather.presentation.viewmodels.BaseViewModel;
import com.mshnitov.coolweather.tools.Failure;

public abstract class AbstractViewModel extends ViewModel
    implements BaseViewModel, LifecycleObserver {

  private final MutableLiveData<Failure> failure = new MutableLiveData<>();

  protected void handleFailure(Failure failure) {
    this.failure.setValue(failure);
  }

  public LiveData<Failure> getFailure() {
    return failure;
  }
}