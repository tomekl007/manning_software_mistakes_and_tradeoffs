package com.tomekl007.CH02.services.sharing.payment;

public class PaymentDto {
	private final String userId;
	private final int amount;

	public PaymentDto(String userId, int amount) {

		this.userId = userId;
		this.amount = amount;
	}
}
