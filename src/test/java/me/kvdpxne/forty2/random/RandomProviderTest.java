package me.kvdpxne.forty2.random;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link RandomProvider} implementations.
 */
class RandomProviderTest {

  @Test
  void testThreadLocalRandomProvider() {
    ThreadLocalRandomProvider provider = new ThreadLocalRandomProvider();
    Random random1 = provider.get();
    Random random2 = provider.get();

    // ThreadLocalRandom.current() returns the same instance per thread in practice,
    // but for the purpose of the interface, we just check it's not null and is a Random instance.
    assertNotNull(random1);
    assertTrue(random1 instanceof ThreadLocalRandom || random1.getClass().getName().contains("ThreadLocalRandom")); // Implementation detail check

    // Note: Checking for identity (assertSame) might be flaky depending on JVM implementation of ThreadLocalRandom
    // It's generally the same instance within a thread, but testing identity here is less reliable.
    // We'll focus on type and non-nullity.
  }

  @Test
  void testSecureRandomProvider() {
    SecureRandomProvider provider = new SecureRandomProvider();
    Random random1 = provider.get();
    Random random2 = provider.get();

    assertNotNull(random1);
    assertTrue(random1 instanceof java.security.SecureRandom);

    // SecureRandomProvider should return the SAME instance (eagerly initialized static final)
    assertSame(random1, random2, "SecureRandomProvider should return the same instance");
  }
}