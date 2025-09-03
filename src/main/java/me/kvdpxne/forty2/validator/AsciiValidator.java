package me.kvdpxne.forty2.validator;

import java.util.Random;

/**
 * Validates inputs for ASCII-based random string generation.
 * <p>
 * This utility class provides static methods to validate the core components required by the NanoID
 * generator: the {@link Random} source, the character alphabet (as a {@code char[]}), and the
 * desired output size. It enforces constraints such as non-nullity, non-emptiness, ASCII character
 * range ({@value #MIN_PRINTABLE_ASCII} to {@value #MAX_PRINTABLE_ASCII}), absence of duplicates in
 * the alphabet, and size limits ({@value #MIN_ACCEPTABLE_SIZE} to {@value #MAX_ACCEPTABLE_SIZE}).
 * </p>
 * <p>
 * This class is stateless and safe to use concurrently from multiple threads.
 * </p>
 *
 * @since 0.2.0
 */
public final class AsciiValidator {

  /**
   * Minimum acceptable size for generated identifiers.
   *
   * @since 0.2.0
   */
  public static final int MIN_ACCEPTABLE_SIZE = 2;

  /**
   * Maximum acceptable size for generated identifiers.
   *
   * @since 0.2.0
   */
  public static final int MAX_ACCEPTABLE_SIZE = 48;

  /**
   * Minimum printable ASCII character (exclamation mark '!').
   *
   * @since 0.2.0
   */
  public static final int MIN_PRINTABLE_ASCII = 0x21; // 33

  /**
   * Maximum printable ASCII character (tilde '~').
   *
   * @since 0.2.0
   */
  public static final int MAX_PRINTABLE_ASCII = 0x7E; // 126

  /**
   * Private constructor to prevent instantiation of this utility class.
   *
   * @throws AssertionError always thrown to indicate this class should not be instantiated.
   * @since 0.2.0
   */
  private AsciiValidator() {
    throw new AssertionError(
      "AsciiValidator is a utility class and should not be instantiated."
    );
  }

  /**
   * Checks if the provided {@link Random} object is not null.
   *
   * @param random The {@link Random} object to be checked. Can be any subclass of {@link Random}.
   * @throws NullPointerException if {@code random} is {@code null}.
   * @since 0.2.0
   */
  public static void validateRandom(
    final Random random
  ) {
    if (null == random) {
      throw new NullPointerException("Random object instance cannot be null!");
    }
  }

  /**
   * Checks if the provided character array is not null, not empty, contains only printable ASCII
   * characters, and has no duplicate characters.
   * <p>
   * Valid characters are those in the range {@value #MIN_PRINTABLE_ASCII} ('!') to
   * {@value #MAX_PRINTABLE_ASCII} ('~') inclusive. This range excludes space (0x20) and control
   * characters (0x00-0x1F).
   * </p>
   *
   * @param characters The character array to be checked. Must not be {@code null}.
   * @throws NullPointerException     if {@code characters} is {@code null}.
   * @throws IllegalArgumentException if {@code characters} is empty, contains non-printable ASCII
   *                                  characters (outside the range
   *                                  {@value #MIN_PRINTABLE_ASCII}-{@value #MAX_PRINTABLE_ASCII}),
   *                                  or contains duplicate characters.
   * @since 0.2.0
   */
  public static void validateCharacters(
    final char[] characters
  ) {
    if (null == characters) {
      throw new NullPointerException("Character array cannot be null!");
    }

    if (0 == characters.length) {
      throw new IllegalArgumentException("Character array cannot be empty!");
    }

    // Optimized size for the duplication check array based on the valid character range.
    final boolean[] duplications = new boolean[MAX_PRINTABLE_ASCII - MIN_PRINTABLE_ASCII + 1];
    for (final char character : characters) {
      // Check if character is within the defined printable ASCII range.
      validatePrintableCharacter(character);

      // Calculate the index for the duplications array using an offset.
      final int index = character - MIN_PRINTABLE_ASCII;
      // Check for duplicates using the boolean array.
      if (duplications[index]) {
        throw new IllegalArgumentException(
          String.format(
            "Character array cannot have duplicates! Duplicate character: '%c' (0x%04X)",
            character, (int) character
          )
        );
      }
      // Mark the character as seen.
      duplications[index] = true;
    }
  }

  /**
   * Checks whether the given character is a printable ASCII character within the allowed range.
   * <p>
   * A character is considered valid if its numeric value lies between {@value #MIN_PRINTABLE_ASCII}
   * ('!') and {@value #MAX_PRINTABLE_ASCII} ('~'), inclusive. Control characters, spaces, and
   * extended ASCII characters are rejected.
   * </p>
   *
   * @param character the character to be validated
   * @throws IllegalArgumentException if the character is not within the valid printable ASCII
   *                                  range
   * @since 0.2.0
   */
  public static void validatePrintableCharacter(
    final char character
  ) {
    if (MIN_PRINTABLE_ASCII > character || MAX_PRINTABLE_ASCII < character) {
      throw new IllegalArgumentException(
        String.format(
          "Character '%c' (0x%04X) is not a printable ASCII character. " +
            "Only characters in range 0x%02X ('%c') to 0x%02X ('%c') are allowed.",
          character, (int) character,
          MIN_PRINTABLE_ASCII, (char) MIN_PRINTABLE_ASCII,
          MAX_PRINTABLE_ASCII, (char) MAX_PRINTABLE_ASCII
        )
      );
    }
  }

  /**
   * Checks if the provided size is within the acceptable range.
   * <p>
   * The size must be greater than or equal to {@value #MIN_ACCEPTABLE_SIZE} and less than or equal
   * to {@value #MAX_ACCEPTABLE_SIZE}.
   * </p>
   *
   * @param size The size to be checked.
   * @throws IllegalArgumentException if {@code size} is less than {@value #MIN_ACCEPTABLE_SIZE} or
   *                                  greater than {@value #MAX_ACCEPTABLE_SIZE}.
   * @since 0.2.0
   */
  public static void validateSize(
    final int size
  ) {
    if (MIN_ACCEPTABLE_SIZE > size) {
      throw new IllegalArgumentException(
        String.format(
          "The length of the generated string cannot be less than %d. Provided: %d",
          MIN_ACCEPTABLE_SIZE, size
        )
      );
    }

    if (MAX_ACCEPTABLE_SIZE < size) {
      throw new IllegalArgumentException(
        String.format(
          "The length of the generated string cannot be greater than %d. Provided: %d",
          MAX_ACCEPTABLE_SIZE, size
        )
      );
    }
  }
}