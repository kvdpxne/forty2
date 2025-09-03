package me.kvdpxne.forty2.converter;

import me.kvdpxne.forty2.validator.AsciiValidator;

/**
 * Utility class for converting character arrays ({@code char[]}) to byte arrays ({@code byte[]}),
 * optimized for ASCII characters.
 * <p>
 * This class is specifically designed for the NanoID library's internal use, where character sets
 * are represented as {@code byte[]} for performance and direct compatibility with the generation
 * algorithm. It assumes that the input characters conform to the 7-bit ASCII standard (values
 * 0x00-0x7F).
 * </p>
 * <p>
 * This is a stateless utility class and is safe to use concurrently from multiple threads.
 * </p>
 *
 * @since 0.2.0
 */
public final class CharacterArrayConverter {

  /**
   * Private constructor to prevent instantiation of this utility class.
   *
   * @throws AssertionError always thrown to indicate this class should not be instantiated.
   * @since 0.2.0
   */
  private CharacterArrayConverter() {
    throw new AssertionError(
      "CharacterArrayConverter is a utility class and should not be instantiated."
    );
  }

  /**
   * Converts an array of characters to an array of bytes.
   * <p>
   * This method is optimized for printable ASCII characters (0x21-0x7E). It performs a direct cast
   * from {@code char} to {@code byte}. If any character in the input array is not a printable ASCII
   * character (i.e., outside the range {@value AsciiValidator#MIN_PRINTABLE_ASCII} to
   * {@value AsciiValidator#MAX_PRINTABLE_ASCII}), an {@link IllegalArgumentException} is thrown.
   * This validation ensures data integrity and prevents silent corruption if the input does not
   * conform to the expected printable ASCII standard.
   * </p>
   *
   * @param characters The array of characters to be converted. Must not be {@code null}. All
   *                   characters must be within the printable ASCII range (0x21-0x7E).
   * @return A new {@code byte[]} array where each element is the ASCII byte representation of the
   * corresponding character in the input array.
   * @throws NullPointerException     if {@code characters} is {@code null}.
   * @throws IllegalArgumentException if any character in the {@code characters} array is not a
   *                                  printable ASCII character (i.e., outside the range
   *                                  {@value AsciiValidator#MIN_PRINTABLE_ASCII} to
   *                                  {@value AsciiValidator#MAX_PRINTABLE_ASCII}).
   * @since 1.0.0
   */
  public static byte[] toBytes(
    final char[] characters
  ) {
    // Implementation assumes characters have been pre-validated or are known ASCII.
    // A basic check is performed for robustness.
    final byte[] bytes = new byte[characters.length];
    for (int i = 0; i < characters.length; i++) {
      final char character = characters[i];
      AsciiValidator.validatePrintableCharacter(character);
      bytes[i] = (byte) character;
    }
    return bytes;
  }
}