package com.tomekl007.CH03;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.io.UncheckedIOException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;

public class CloseFinally {

  @Test
  public void shouldCloseClient() {
    assertThatThrownBy(
            () -> {
              CloseableHttpClient client = HttpClients.createDefault();
              try {
                processRequests(client);
              } finally {
                System.out.println("closing");
                client.close();
              }
            })
        .isInstanceOf(UncheckedIOException.class);
  }

  private void processRequests(CloseableHttpClient client) throws UncheckedIOException {
    throw new UncheckedIOException(new IOException("error"));
  }
}
