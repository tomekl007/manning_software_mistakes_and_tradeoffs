package com.tomekl007.CH03;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatchAllTest {
  private static final Logger logger = LoggerFactory.getLogger(CatchAllTest.class);

  @Test
  public void shouldCatchAtNormalGranularity() {
    try {
      methodThatThrowsCheckedException();
    } catch (FileAlreadyExistsException e) {
      logger.error("File already exists: ", e);
    } catch (InterruptedException e) {
      logger.error("Interrupted", e);
    }
  }

  @Test
  public void shouldCatchAtHigherGranularity() {
    try {
      methodThatThrowsCheckedException();
    } catch (IOException e) {
      logger.error("Some IO problem: ", e);
    } catch (InterruptedException e) {
      logger.error("Interrupted", e);
    }
  }

  @Test
  public void shouldCatchAtCatchAll() {
    try {
      methodThatThrowsCheckedException();
    } catch (Exception e) {
      logger.error("Problem ", e);
    }
  }

  @Test
  public void shouldCatchRuntimeAtCatchAll() {
    try {
      methodThatThrowsUncheckedException();
    } catch (Exception e) {
      logger.error("Problem ", e);
    }
  }

  @Test
  public void shouldCatchAtNormalGranularityRuntimeWillBeNotCatch() {
    assertThatThrownBy(
            () -> {
              try {
                methodThatThrowsUncheckedException();
              } catch (FileAlreadyExistsException e) {
                logger.error("File already exists: ", e);
              } catch (InterruptedException e) {
                logger.error("Interrupted", e);
              }
            })
        .isInstanceOf(RuntimeException.class);
  }

  public void methodThatThrowsCheckedException()
      throws FileAlreadyExistsException, InterruptedException {}

  public void methodThatThrowsUncheckedException()
      throws FileAlreadyExistsException, InterruptedException {
    throw new RuntimeException("Unchecked exception!");
  }
}
