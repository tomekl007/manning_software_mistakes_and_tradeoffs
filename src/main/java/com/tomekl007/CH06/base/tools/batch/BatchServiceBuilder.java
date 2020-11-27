package com.tomekl007.CH06.base.tools.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.tomekl007.CH06.base.client.library.CloudServiceClient;
import com.tomekl007.CH06.base.client.library.CloudServiceClientBuilder;
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
      CloudServiceClient cloudServiceClient =
          new CloudServiceClientBuilder().create(configFilePath);
      return new BatchService(batchServiceConfiguration, cloudServiceClient);
    } catch (IOException e) {
      throw new UncheckedIOException("Problem when loading file from: " + configFilePath, e);
    }
  }
}
