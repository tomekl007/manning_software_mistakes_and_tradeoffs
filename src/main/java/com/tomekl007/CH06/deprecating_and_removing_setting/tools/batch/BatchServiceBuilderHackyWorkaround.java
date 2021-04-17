package com.tomekl007.CH06.deprecating_and_removing_setting.tools.batch;

import static com.tomekl007.CH06.deprecating_and_removing_setting.client.library.CloudServiceClientBuilder.USERNAME_PASSWORD_HASHED_STRATEGY;
import static com.tomekl007.CH06.deprecating_and_removing_setting.client.library.CloudServiceClientBuilder.USERNAME_PASSWORD_STRATEGY;
import static com.tomekl007.CH06.deprecating_and_removing_setting.client.library.auth.UsernamePasswordHashedAuthStrategy.toHash;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.tomekl007.CH06.deprecating_and_removing_setting.client.library.CloudServiceClient;
import com.tomekl007.CH06.deprecating_and_removing_setting.client.library.CloudServiceClientBuilder;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class BatchServiceBuilderHackyWorkaround {

  private final ObjectMapper mapper;
  private final MapType yamlConfigType;

  public BatchServiceBuilderHackyWorkaround() {
    mapper = new ObjectMapper(new YAMLFactory());
    MapType mapType =
        mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class);
    yamlConfigType =
        mapper
            .getTypeFactory()
            .constructMapType(
                HashMap.class, mapper.getTypeFactory().constructType(String.class), mapType);
  }

  public BatchService create(Path configFilePath) {
    try {
      Map<String, Map<String, Object>> config =
          mapper.readValue(configFilePath.toFile(), yamlConfigType);
      Map<String, Object> batchConfig = config.get("batch");
      BatchServiceConfiguration batchServiceConfiguration =
          new BatchServiceConfiguration((Integer) batchConfig.get("size"));

      Map<String, Object> authConfig = config.get("auth");
      if (authConfig.get("strategy").equals(USERNAME_PASSWORD_STRATEGY)) {
        authConfig.put("strategy", USERNAME_PASSWORD_HASHED_STRATEGY);
      }
      String password = (String) authConfig.get("password");
      String hashedPassword = toHash(password);
      authConfig.put("password", hashedPassword);
      Path tempFile = Files.createTempFile(null, null);
      Files.write(tempFile, mapper.writeValueAsBytes(config));

      CloudServiceClient cloudServiceClient = new CloudServiceClientBuilder().create(tempFile);
      return new BatchService(batchServiceConfiguration, cloudServiceClient);
    } catch (IOException e) {
      throw new UncheckedIOException("Problem when loading file from: " + configFilePath, e);
    }
  }
}
