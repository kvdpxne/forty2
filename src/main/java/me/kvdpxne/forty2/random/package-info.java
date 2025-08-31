/**
 * Provides abstractions and implementations for sources of randomness used in NanoID generation.
 * <p>
 * This package defines the {@link me.kvdpxne.forty2.random.RandomProvider} interface for obtaining
 * {@link java.util.Random} instances. It includes implementations for different requirements:
 * </p>
 * <ul>
 *   <li>{@link me.kvdpxne.forty2.random.ThreadLocalRandomProvider}: Provides fast, thread-local
 *       random numbers suitable for general-purpose, non-cryptographic ID generation.</li>
 *   <li>{@link me.kvdpxne.forty2.random.SecureRandomProvider}: Provides cryptographically
 *       strong random numbers suitable for generating IDs where unpredictability is critical.</li>
 * </ul>
 * <p>
 * These providers allow the NanoID generator to be decoupled from specific random number
 * generation strategies, enabling flexibility and ensuring thread safety where required.
 * </p>
 *
 * @since 0.2.0
 */
package me.kvdpxne.forty2.random;