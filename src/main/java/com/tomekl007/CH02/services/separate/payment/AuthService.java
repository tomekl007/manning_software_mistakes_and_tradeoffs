package com.tomekl007.CH02.services.separate.payment;

import java.io.IOException;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class AuthService {

  // send request to a separate service
  public boolean isTokenValid(String token) throws IOException {
    CloseableHttpClient client = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet("http://auth-service/auth/validate/" + token);
    CloseableHttpResponse response = client.execute(httpGet);
    return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
  }
}
