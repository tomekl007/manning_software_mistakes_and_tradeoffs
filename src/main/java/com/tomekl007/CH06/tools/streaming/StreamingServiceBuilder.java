package com.tomekl007.CH06.tools.streaming;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.tomekl007.CH06.client.library.CloudServiceClientBuilder;
import com.tomekl007.CH06.client.library.CloudServiceConfiguration;
import com.tomekl007.CH06.client.library.auth.UsernamePasswordAuthStrategy;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class StreamingServiceBuilder {

  private final ObjectMapper mapper;
  private final MapType yamlConfigType;

  public StreamingServiceBuilder() {
    mapper = new ObjectMapper(new YAMLFactory());
    MapType mapType =
        mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class);
    yamlConfigType =
        mapper
            .getTypeFactory()
            .constructMapType(
                HashMap.class, mapper.getTypeFactory().constructType(String.class), mapType);
  }

  public StreamingService create(Path configFilePath) {
    try {

      Map<String, Map<String, Object>> config =
          mapper.readValue(configFilePath.toFile(), yamlConfigType);
      Map<String, Object> streamingConfig = config.get("streaming");

      StreamingServiceConfiguration streamingServiceConfiguration =
          new StreamingServiceConfiguration((Integer) streamingConfig.get("maxTimeMs"));

      // the internal cloud library UsernamePasswordAuthStrategy is abstracted away from the user.
      // The client of StreamingService does not know nothing about its configuration mechanism.
      CloudServiceConfiguration cloudServiceConfiguration =
          new CloudServiceConfiguration(
              new UsernamePasswordAuthStrategy(
                  (String) streamingConfig.get("username"),
                  (String) streamingConfig.get("password")));
      return new StreamingService(
          streamingServiceConfiguration,
          new CloudServiceClientBuilder().create(cloudServiceConfiguration));
    } catch (IOException e) {
      throw new UncheckedIOException("Problem when loading file from: " + configFilePath, e);
    }
  }
}
