package me.kvdpxne.forty2.generator;

/**
 * Utility class containing common mathematical logic used by the NanoID generator.
 * <p>
 * This class centralizes calculations for the mask and step size used in the NanoID algorithm,
 * ensuring consistency and adherence to the original specification across different parts of the
 * generation process.
 * </p>
 * <p>
 * This is a stateless utility class and is safe to use concurrently from multiple threads.
 * </p>
 *
 * @see <a href="https://github.com/ai/nanoid/blob/main/index.js">NanoID JavaScript
 * Implementation</a>
 * @since 0.2.0
 */
final class NanoIdHelper {

  /**
   * Private constructor to prevent instantiation of this utility class.
   *
   * @throws AssertionError always thrown to indicate this class should not be instantiated.
   * @since 0.2.0
   */
  private NanoIdHelper() {
    throw new AssertionError("NanoIdHelper is a utility class and should not be instantiated.");
  }

  /**
   * Calculates the mask used in the NanoID generation algorithm.
   * <p>
   * The mask is used to filter random bytes so that only values within the range of the character
   * alphabet are selected. The calculation ensures an even distribution of character selection
   * probabilities. The formula used is aligned with the official NanoID implementation.
   * </p>
   * <p>
   * For example, if the alphabet length is 64, the mask will be 63 (0b111111). When a random byte
   * is bitwise ANDed with this mask, the result is a number between 0 and 63, which can be safely
   * used as an index into the alphabet array.
   * </p>
   *
   * @param length The length of the character alphabet used for generation. Must be greater than
   *               0.
   * @return The calculated mask value.
   * @throws IllegalArgumentException if {@code alphabetLength} is not positive.
   * @since 0.2.0
   */
  static int calculateMask(
    final int length
  ) {
    if (0 >= length) {
      throw new IllegalArgumentException("Alphabet length must be positive.");
    }
    // Handle the edge case where the alphabet has only one character.
    // In this case, any mask would work (index 0 is always valid), but 0 is a logical choice.
    if (1 == length) {
      return 0;
    }
    // Calculate the mask according to the NanoID algorithm.
    // Using | 1 ensures the argument for numberOfLeadingZeros is never 0.
    return (2 << (31 - Integer.numberOfLeadingZeros(length - 1 | 1))) - 1;
  }

  /**
   * Calculates the optimal number of random bytes (step) to generate in each iteration of the
   * NanoID algorithm.
   * <p>
   * This calculation aims to balance the trade-off between the number of random bytes generated per
   * iteration and the probability of needing to discard bytes due to the masking process. The
   * factor 1.6 is part of the original NanoID algorithm to optimize performance.
   * </p>
   *
   * @param mask   The mask calculated by {@link #calculateMask(int)}.
   * @param size   The desired size of the generated identifier.
   * @param length The length of the character alphabet used for generation. Must be greater than
   *               0.
   * @return The calculated step size (number of bytes).
   * @throws IllegalArgumentException if {@code alphabetLength} is not positive.
   * @since 0.2.0
   */
  static int calculateStep(
    final int mask,
    final int size,
    final int length
  ) {
    if (0 >= length) {
      throw new IllegalArgumentException("Alphabet length must be positive.");
    }
    // Calculate the step size according to the NanoID algorithm.
    return (int) Math.ceil(1.6 * mask * size / length);
  }
}