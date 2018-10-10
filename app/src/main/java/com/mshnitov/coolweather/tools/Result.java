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

package com.mshnitov.coolweather.tools;

/**
 * A helping class, that can contain a result object of T-type or it can contain Failure object
 * with some issue information
 *
 * @param <T> - type of expected object
 */
public class Result<T> {

  private final Failure mFailure;
  private final T mSuccess;

  public Result(Failure failure) {
    mFailure = failure;
    mSuccess = null;
  }

  public Result(T success) {
    mFailure = null;
    mSuccess = success;
  }

  public boolean isSuccess() {
    return mFailure == null && mSuccess != null;
  }

  public Failure getFailure() {
    return mFailure;
  }

  public T getSuccess() {
    return mSuccess;
  }
}
