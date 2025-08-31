package me.kvdpxne.forty2.generator;

import java.util.Random;

/**
 * Defines a contract for generating random strings.
 * <p>
 * This interface specifies the methods that a random string generator must implement. It provides
 * flexibility for different generation strategies or algorithms.
 * </p>
 *
 * @since 0.2.0
 */
public interface RandomStringGenerator {

  /**
   * Generates a random string of specified size using the provided characters and the generator's
   * default random number source.
   *
   * @param characters The characters to choose from for generating the string. Must not be
   *                   {@code null} or empty. The specific implementation may impose additional
   *                   constraints (e.g., ASCII).
   * @param size       The desired length of the generated string. Must be positive. The specific
   *                   implementation may have minimum/maximum limits.
   * @return The generated random string.
   * @throws NullPointerException     if {@code characters} is {@code null}.
   * @throws IllegalArgumentException if {@code characters} is empty or if {@code size} is not valid
   *                                  according to the implementation's constraints.
   * @since 0.2.0
   */
  String create(
    char[] characters,
    int size
  );

  /**
   * Generates a random string of specified size using the provided characters and a custom random
   * number generator.
   *
   * @param random     The {@link Random} object to use for generating random bytes. Must not be
   *                   {@code null}.
   * @param characters The characters to choose from for generating the string. Must not be
   *                   {@code null} or empty. The specific implementation may impose additional
   *                   constraints (e.g., ASCII).
   * @param size       The desired length of the generated string. Must be positive. The specific
   *                   implementation may have minimum/maximum limits.
   * @return The generated random string.
   * @throws NullPointerException     if {@code random} or {@code characters} is {@code null}.
   * @throws IllegalArgumentException if {@code characters} is empty or if {@code size} is not valid
   *                                  according to the implementation's constraints.
   * @since 0.2.0
   */
  String create(
    Random random,
    char[] characters,
    int size
  );
}