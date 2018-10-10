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

package com.mshnitov.coolweather.domain.entities;

import com.mshnitov.coolweather.tools.Env;
import java.util.List;

/**
 * This class is for mapping JSON answer data from OpenWeatherMap API.
 */
public class WeatherResponse {

  private int id;
  private int cod;
  private List<Weather> weather;
  private Main main;
  private String name;

  public float getTemperature() {
    return main.getTemp();
  }

  public String getCityName() {
    return name;
  }

  public int getStatus() {
    return cod;
  }

  public String getIconUrl() {
    if (!weather.isEmpty()) {
      return Env.BASE_URL + "/img/w/" + weather.get(0).getIcon() + ".png";
    } else {
      return "";
    }
  }

  private class Weather {

    private String icon;

    String getIcon() {
      return icon;
    }
  }

  private class Main {

    private float temp;

    float getTemp() {
      return temp;
    }
  }
}