package me.kvdpxne.forty2.random;

import java.util.Random;

/**
 * Defines a contract for providing instances of {@link Random}.
 * <p>
 * This interface allows the NanoID generator to be decoupled from specific {@link Random}
 * implementations. Different providers can offer varying characteristics (e.g., speed vs.
 * cryptographic strength) or scoping (e.g., thread-local vs. shared).
 * </p>
 * <p>
 * Implementations of this interface are expected to be thread-safe if they are intended for use in
 * a multithreaded environment.
 * </p>
 *
 * @see java.util.Random
 * @since 0.2.0
 */
public interface RandomProvider {

  /**
   * Gets a {@link Random} instance.
   * <p>
   * The specifics of the returned instance (e.g., whether it's a new instance, a shared instance,
   * or a thread-local instance) are determined by the implementation.
   * </p>
   *
   * @return A {@link Random} instance. Implementations should define the characteristics and
   * lifecycle of the returned object.
   * @since 0.2.0
   */
  Random get();
}