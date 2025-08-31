package me.kvdpxne.forty2.random;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Provides {@link ThreadLocalRandom} instances.
 * <p>
 * This implementation offers a very fast, thread-safe source of pseudo-random numbers by leveraging
 * {@link ThreadLocalRandom}. Because {@code ThreadLocalRandom} is inherently thread-local, this
 * provider is suitable for high-performance scenarios where cryptographic security is not a
 * requirement.
 * </p>
 * <p>
 * This class is stateless and thread-safe.
 * </p>
 *
 * @see java.util.concurrent.ThreadLocalRandom
 * @since 0.2.0
 */
public final class ThreadLocalRandomProvider implements RandomProvider {

  /**
   * Constructs a new {@code ThreadLocalRandomProvider}.
   * <p>
   * This constructor is public to allow instantiation, although typically a single instance is used
   * application-wide.
   * </p>
   *
   * @since 0.2.0
   */
  public ThreadLocalRandomProvider() {
    // Default constructor
  }

  /**
   * {@inheritDoc}
   * <p>
   * Returns the {@link ThreadLocalRandom} instance for the current thread. This call is extremely
   * fast and does not involve synchronization.
   * </p>
   *
   * @return The {@link ThreadLocalRandom} instance for the calling thread.
   * @see java.util.concurrent.ThreadLocalRandom#current()
   * @since 0.2.0
   */
  @Override
  public Random get() {
    return ThreadLocalRandom.current();
  }
}