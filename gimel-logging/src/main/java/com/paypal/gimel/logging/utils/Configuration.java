/*
 * Copyright 2018 PayPal Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.paypal.gimel.logging.utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.paypal.gimel.logging.Constants;

public class Configuration {

  private static Configuration instance = null;
  private Properties properties = null;
  /**
   * It's a singleton class. Use {@link #getInstance()}
   */
  private Configuration() {
    readConfiguration();
  }

  public static synchronized Configuration getInstance() {
    if (instance == null) {
      instance = new Configuration();
    }
    return instance;
  }

  /**
   * Read the configuration file  scaasConfig.properties, and load to properties
   */

  public void readConfiguration() {
    properties = new Properties();
    try {
      String fileName = "/scaasConfig.properties";
      final InputStream configStream = this.getClass().getResourceAsStream(fileName);
      properties.load(configStream);
      configStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Object get(String key) {
    return properties.get(key);
  }

  /**
   * From the configuration file scaasConfig.properties, it reads the kafka properties and returns as properties
   */
  public Properties getKafkaProperties() {
    Properties props = new Properties();
    for (Object key : properties.keySet()) {
      if (key.toString().indexOf(Constants.GIMEL_LOGGER_PROPERTY_PREFIX) != -1) {
        props.put(key.toString().substring(Constants.GIMEL_LOGGER_PROPERTY_PREFIX.length() + 1, key.toString().length()), properties.get(key));
      }

    }
    return props;
  }

  /**
   * From the configuration file scaasConfig.properties, it reads the topics and returns as properties
   */
  public Properties getKafkaTopics() {
    Properties props = new Properties();
    for (Object key : properties.keySet()) {
      if (key.toString().contains(".topic")) {
        props.put(key.toString(), properties.get(key));
      }

    }
    return props;
  }


}
