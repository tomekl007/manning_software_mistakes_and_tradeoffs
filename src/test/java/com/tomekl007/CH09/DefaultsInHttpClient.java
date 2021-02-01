package com.tomekl007.CH09;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.tomakehurst.wiremock.WireMockServer;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DefaultsInHttpClient {
  private static WireMockServer wireMockServer;
  private static final int PORT = 9999;
  private static String HOST;

  @BeforeAll
  public static void setup() {
    wireMockServer = new WireMockServer(options().port(PORT));
    wireMockServer.start();
    HOST = String.format("http://localhost:%s", PORT);
    wireMockServer.stubFor(
        get(urlEqualTo("/data")).willReturn(aResponse().withStatus(200).withBody("some-data")));

    wireMockServer.stubFor(
        get(urlEqualTo("/slow-data"))
            .willReturn(aResponse().withStatus(200).withBody("some-data").withFixedDelay(5000)));
  }

  @AfterAll
  public static void cleanup() {
    if (wireMockServer != null) {
      wireMockServer.stop();
    }
  }

  @Test
  public void shouldExecuteGetRequestsWithDefaults() throws IOException {
    Request request = new Request.Builder().url(HOST + "/data").build();

    OkHttpClient client = new OkHttpClient.Builder().build();
    Call call = client.newCall(request);
    Response response = call.execute();

    assertThat(response.code()).isEqualTo(200);
    assertThat(response.body().string()).isEqualTo("some-data");
  }

  @Test
  public void shouldExecuteGetRequestsToSlowEndpointWithDefaults() throws IOException {
    Request request = new Request.Builder().url(HOST + "/slow-data").build();

    OkHttpClient client = new OkHttpClient.Builder().build();
    Call call = client.newCall(request);

    long start = System.currentTimeMillis();
    Response response = call.execute();
    long totalTime = System.currentTimeMillis() - start;

    // the request took 5 seconds
    assertThat(totalTime).isGreaterThanOrEqualTo(5000);
    assertThat(response.code()).isEqualTo(200);
    assertThat(response.body().string()).isEqualTo("some-data");
  }

  @Test
  public void shouldFailRequestAfterTimeout() throws IOException {
    Request request = new Request.Builder().url(HOST + "/slow-data").build();

    OkHttpClient client = new OkHttpClient.Builder().readTimeout(Duration.ofMillis(100)).build();
    Call call = client.newCall(request);

    long start = System.currentTimeMillis();
    assertThatThrownBy(call::execute).isInstanceOf(SocketTimeoutException.class);
    long totalTime = System.currentTimeMillis() - start;

    // the request will fail fast
    assertThat(totalTime).isLessThan(5000);
  }
}
