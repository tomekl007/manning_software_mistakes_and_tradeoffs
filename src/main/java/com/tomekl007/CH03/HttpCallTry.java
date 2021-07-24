package com.tomekl007.CH03;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpCallTry {
  private static final Logger logger = LoggerFactory.getLogger(HttpCallTry.class);
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public String getId() {
    CloseableHttpClient client = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet("http://external-service/resource");
    Try<HttpResponse> response = Try.of(() -> client.execute(httpGet));
    return response
        .mapTry(this::extractStringBody)
        .mapTry(this::toEntity)
        .map(this::extractUserId)
        .onFailure(ex -> logger.error("The getId() failed.", ex))
        .getOrElse("DEFAULT_ID");
  }

  public String getIdExceptions() {
    CloseableHttpClient client = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet("http://external-service/resource");
    try {
      CloseableHttpResponse response = client.execute(httpGet);
      String body = extractStringBody(response);
      EntityObject entityObject = toEntity(body);
      return extractUserId(entityObject);
    } catch (IOException ex) {
      logger.error("The getId() failed", ex);
      return "DEFAULT_ID";
    }
  }

  private String extractUserId(EntityObject entityObject) {
    return entityObject.id;
  }

  private String extractStringBody(HttpResponse r) throws IOException {
    return new BufferedReader(
            new InputStreamReader(r.getEntity().getContent(), StandardCharsets.UTF_8))
        .lines()
        .collect(Collectors.joining("\n"));
  }

  private EntityObject toEntity(String content) throws IOException {
    return OBJECT_MAPPER.readValue(content, EntityObject.class);
  }

  static class EntityObject {
    String id;

    public EntityObject(String id) {
      this.id = id;
    }
  }
}
