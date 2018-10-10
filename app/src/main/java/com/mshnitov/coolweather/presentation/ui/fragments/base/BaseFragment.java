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

package com.mshnitov.coolweather.presentation.ui.fragments.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.mshnitov.coolweather.AndroidApplication;
import com.mshnitov.coolweather.presentation.ui.activities.base.BaseActivity;
import com.mshnitov.coolweather.tools.di.AppComponent;

public abstract class BaseFragment extends Fragment {

  //region Properties
  protected AppComponent app;
  //endregion

  //region LifeCycle
  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Activity activity = getActivity();
    if (activity != null) {
      app = ((AndroidApplication) activity.getApplication()).getComponent();
    }
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(layoutId(), container, false);
  }
  //endregion

  protected abstract int layoutId();

  public void onBackPressed() {

  }

  protected void showToast(String message) {
    Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
    toast.setGravity(Gravity.CENTER, 0, 0);
    toast.show();
  }

  protected void showProgress() {
    progressBarStatus(View.VISIBLE);
  }

  protected void hideProgress() {
    progressBarStatus(View.INVISIBLE);
  }

  //region Private methods
  private void progressBarStatus(int viewStatus) {
    Activity activity = getActivity();
    if (activity instanceof BaseActivity) {
      ((BaseActivity) activity).progress.setVisibility(viewStatus);
    }
  }
  //endregion
}