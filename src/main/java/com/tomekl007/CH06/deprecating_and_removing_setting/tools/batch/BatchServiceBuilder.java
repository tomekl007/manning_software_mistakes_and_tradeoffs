package com.tomekl007.CH06.deprecating_and_removing_setting.tools.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.tomekl007.CH06.deprecating_and_removing_setting.client.library.CloudServiceClient;
import com.tomekl007.CH06.deprecating_and_removing_setting.client.library.CloudServiceClientBuilder;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class BatchServiceBuilder {

  private final ObjectMapper mapper;
  private final MapType yamlConfigType;

  public BatchServiceBuilder() {
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

      // the raw configuration is passed to the underlying cloud client library
      // when the client specifies deprecated strategy: strategy: username-password
      // it will throw exception to the client. All clients need to migrate to the new type!
      // To have the backward compatibility we need to provide hacky workaround:
      // 1. Scan the config file under configFilePath path
      // 2. Locate the entry for auth.strategy
      // 3. Modify the file by replacing username-password with username-password-hashed
      // 4. Pass the new file location to the CloudServiceClientBuilder
      // Such a solution is very bad: it tweaks the original file, change the config value without
      // user's knowledge and
      // may introduce bugs.
      CloudServiceClient cloudServiceClient =
          new CloudServiceClientBuilder().create(configFilePath);
      return new BatchService(batchServiceConfiguration, cloudServiceClient);
    } catch (IOException e) {
      throw new UncheckedIOException("Problem when loading file from: " + configFilePath, e);
    }
  }
}
