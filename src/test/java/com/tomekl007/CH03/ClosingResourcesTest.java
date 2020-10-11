package com.tomekl007.CH03;

import java.io.IOException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClosingResourcesTest {
  private static final Logger logger = LoggerFactory.getLogger(ClosingResourcesTest.class);

  @Test
  public void shouldCloseClient() {
    CloseableHttpClient client = HttpClients.createDefault();
    try {
      processRequests(client);
      client.close();
    } catch (IOException e) {
      logger.error("Problem when closing the client or processing requests", e);
    }
  }

  @Test
  public void shouldCloseClientInCaseOfAProblemWithProcessRequests() {
    CloseableHttpClient client = HttpClients.createDefault();
    try {
      processRequests(client);
    } catch (IOException e) {
      logger.error("Problem when processing requests", e);
    }
    try {
      client.close();
    } catch (IOException e) {
      logger.error("Problem when closing client", e);
    }
  }

  @Test
  public void shouldCloseClientTryWithResources() {

    try (CloseableHttpClient client = HttpClients.createDefault()) {
      processRequests(client);
    } catch (IOException e) {
      logger.error("Problem when processing requests", e);
    }
  }

  private void processRequests(CloseableHttpClient client) throws IOException {}
}
