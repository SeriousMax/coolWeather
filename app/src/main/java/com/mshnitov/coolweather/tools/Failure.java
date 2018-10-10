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

import androidx.annotation.NonNull;
import androidx.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Failure object
 */
public class Failure {

  public static final String NETWORK_ERROR = "Some network problems";
  public static final String DATA_ERROR = "Can't parse received data";
  public static final String SERVER_ERROR = "Server error";
  public static final String UNDEFINED_ERROR = "Undefined error";

  private final String message;

  public Failure(@FailureType String failureType) {
    System.out.println("FailureType : " + failureType);
    this.message = failureType;
  }

  public Failure(@FailureType String failureType, String customMessage) {
    System.out.println("FailureType : " + failureType);
    this.message = failureType + ": " + customMessage;
  }

  @NonNull @Override public String toString() {
    return message;
  }

  @StringDef({ NETWORK_ERROR, DATA_ERROR, SERVER_ERROR, UNDEFINED_ERROR })
  @Retention(RetentionPolicy.SOURCE) public @interface FailureType {
  }
}