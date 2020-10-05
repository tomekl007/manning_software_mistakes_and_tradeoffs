package com.tomekl007.CH02.services.sharing.payment;

public class AuthService {

  // validate if token is correct. In the real live system this logic will be complex
  public boolean isTokenValid(String token) {
    return token.equals("secret");
  }
}
