package com.tomekl007.CH06.client.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.tomekl007.CH06.client.library.auth.AuthStrategy;
import com.tomekl007.CH06.client.library.auth.TokenAuthStrategy;
import com.tomekl007.CH06.client.library.auth.UsernamePasswordAuthStrategy;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CloudServiceClientBuilder {
  private static final String USERNAME_PASSWORD_STRATEGY = "username-password";
  private static final String TOKEN_STRATEGY = "token";
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

  public DefaultCloudServiceClient create(Path configFilePath) {
    try {

      Map<String, Map<String, Object>> config =
          mapper.readValue(configFilePath.toFile(), yamlConfigType);
      AuthStrategy authStrategy = null;
      Map<String, Object> authConfig = config.get("auth");

      if (authConfig.get("strategy").equals(USERNAME_PASSWORD_STRATEGY)) {
        authStrategy =
            new UsernamePasswordAuthStrategy(
                (String) authConfig.get("username"), (String) authConfig.get("password"));

      } else if (authConfig.get("strategy").equals(TOKEN_STRATEGY)) {
        authStrategy = new TokenAuthStrategy((String) authConfig.get("token"));
      }
      return new DefaultCloudServiceClient(new CloudServiceConfiguration(authStrategy));
    } catch (IOException e) {
      throw new UncheckedIOException("Problem when loading file from: " + configFilePath, e);
    }
  }
}
