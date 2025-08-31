package me.kvdpxne.forty2.random;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Provides a shared {@link SecureRandom} instance.
 * <p>
 * This implementation provides a cryptographically strong random number generator. It uses a
 * single, eagerly initialized {@link SecureRandom} instance. {@code SecureRandom} is designed to be
 * thread-safe, making this provider suitable for scenarios where the unpredictability and security
 * of the generated IDs are paramount.
 * </p>
 * <p>
 * This class is stateless and thread-safe.
 * </p>
 *
 * @see java.security.SecureRandom
 * @since 0.2.0
 */
public final class SecureRandomProvider implements RandomProvider {

  /**
   * The shared {@link SecureRandom} instance.
   * <p>
   * Eager initialization is used to ensure the {@code SecureRandom} is fully seeded and ready for
   * use immediately upon the first call to {@link #get()}. {@code SecureRandom} itself is
   * thread-safe.
   * </p>
   *
   * @since 0.2.0
   */
  private static final Random SECURE_RANDOM = new SecureRandom();

  /**
   * Constructs a new {@code SecureRandomProvider}.
   * <p>
   * This constructor is public to allow instantiation, although typically a single instance is used
   * application-wide.
   * </p>
   *
   * @since 0.2.0
   */
  public SecureRandomProvider() {
    // Default constructor
  }

  /**
   * {@inheritDoc}
   * <p>
   * Returns the shared, cryptographically strong {@link SecureRandom} instance. Access to this
   * shared instance is thread-safe.
   * </p>
   *
   * @return The shared {@link SecureRandom} instance.
   * @since 0.2.0
   */
  @Override
  public Random get() {
    return SECURE_RANDOM;
  }
}