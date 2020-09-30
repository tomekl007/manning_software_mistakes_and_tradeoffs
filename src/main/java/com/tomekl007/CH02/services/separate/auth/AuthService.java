package com.tomekl007.CH02.services.separate.auth;

// this is exposed via REST now using separate service
public class AuthService {

	// validate if token is correct. In the real live system this logic will be complex
	public boolean validToken(String token) {
		return token.equals("secret");
	}
}
