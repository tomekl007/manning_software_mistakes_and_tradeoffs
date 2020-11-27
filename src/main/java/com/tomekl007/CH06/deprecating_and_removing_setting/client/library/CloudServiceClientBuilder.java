package com.tomekl007.CH06.deprecating_and_removing_setting.client.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.tomekl007.CH06.deprecating_and_removing_setting.client.library.auth.AuthStrategy;
import com.tomekl007.CH06.deprecating_and_removing_setting.client.library.auth.TokenAuthStrategy;
import com.tomekl007.CH06.deprecating_and_removing_setting.client.library.auth.UsernamePasswordHashedAuthStrategy;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CloudServiceClientBuilder {
  private static final String USERNAME_PASSWORD_STRATEGY = "username-password";
  private static final String TOKEN_STRATEGY = "token";
  private static final String USERNAME_PASSWORD_HASHED_STRATEGY = "username-password-hashed";
  private final ObjectMapper mapper;
  private final MapType yamlConfigType;

  public CloudServiceClientBuilder() {
    mapper = new ObjectMapper(new YAMLFactory());
    MapType mapType =
        mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class);
    yamlConfigType =
        mapper
            .getTypeFactory()
            .constructMapType(
                HashMap.class, mapper.getTypeFactory().constructType(String.class), mapType);
  }

  public DefaultCloudServiceClient create(CloudServiceConfiguration cloudServiceConfiguration) {
    return new DefaultCloudServiceClient(cloudServiceConfiguration);
  }

  public DefaultCloudServiceClient create(Path configFilePath) {
    try {

      Map<String, Map<String, Object>> config =
          mapper.readValue(configFilePath.toFile(), yamlConfigType);
      AuthStrategy authStrategy = null;
      Map<String, Object> authConfig = config.get("auth");
      Map<String, Object> timeouts = config.get("timeouts");

      if (authConfig.get("strategy").equals(USERNAME_PASSWORD_HASHED_STRATEGY)) {
        authStrategy =
            new UsernamePasswordHashedAuthStrategy(
                (String) authConfig.get("username"), (String) authConfig.get("password"));

      } else if (authConfig.get("strategy").equals(TOKEN_STRATEGY)) {
        authStrategy = new TokenAuthStrategy((String) authConfig.get("token"));
      } else if (authConfig.get("strategy").equals(USERNAME_PASSWORD_STRATEGY)) {
        throw new UnsupportedOperationException(
            "The " + USERNAME_PASSWORD_STRATEGY + " strategy is no longer supported.");
      }
      return new DefaultCloudServiceClient(
          new CloudServiceConfiguration(authStrategy, (Integer) timeouts.get("connection")));
    } catch (IOException e) {
      throw new UncheckedIOException("Problem when loading file from: " + configFilePath, e);
    }
  }
}
