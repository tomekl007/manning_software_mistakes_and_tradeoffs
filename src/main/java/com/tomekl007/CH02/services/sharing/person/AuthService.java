package com.tomekl007.CH02.services.sharing.person;

// auth service is duplicated between payment and person microservices
public class AuthService {

  // validate if token is correct. In the real live system this logic will be complex
  public boolean validToken(String token) {
    return token.equals("secret");
  }
}
