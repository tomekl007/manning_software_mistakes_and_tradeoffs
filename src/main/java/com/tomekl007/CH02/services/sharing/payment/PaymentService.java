package com.tomekl007.CH02.services.sharing.payment;

import java.util.Collections;
import java.util.List;

public class PaymentService {
  public List<PaymentDto> getAllPayments() {
    return Collections.singletonList(new PaymentDto("user_1", 100));
  }
}
