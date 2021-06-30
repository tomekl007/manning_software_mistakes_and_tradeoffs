package com.tomekl007.CH06.deprecating_and_removing_setting.client.library.auth;

import static com.tomekl007.CH06.deprecating_and_removing_setting.client.library.auth.UsernamePasswordHashedAuthStrategy.toHash;
import static org.assertj.core.api.Assertions.assertThat;

import com.tomekl007.CH06.Request;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class UsernamePasswordHashedAuthStrategyTest {

  @Test
  public void validateUsingHashedPassword() {
    // given
    UsernamePasswordHashedAuthStrategy usernamePasswordHashedAuthStrategy =
        new UsernamePasswordHashedAuthStrategy("user", toHash("password"));

    // when
    boolean result =
        usernamePasswordHashedAuthStrategy.authenticate(
            new Request(null, "user", "password", Collections.emptyList()));

    // then
    assertThat(result).isTrue();
  }
}
