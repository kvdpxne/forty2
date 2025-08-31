package me.kvdpxne.forty2.config;

/**
 * Utility class providing predefined sets of ASCII characters commonly used for generating NanoID
 * identifiers.
 * <p>
 * Character sets are defined as {@code byte[]} arrays for direct and efficient use with the NanoID
 * generator, assuming ASCII encoding. This avoids the overhead of string conversions during ID
 * generation.
 * </p>
 * <p>
 * Character sets are initialized lazily on first access using the Initialization-on-Demand Holder
 * idiom. This optimizes resource usage if only a subset of the available dictionaries is needed by
 * the application.
 * </p>
 * <p>
 * <b>Note on Special Characters:</b> Some character sets (e.g., {@link #getCookieSafe()},
 * {@link #getCookieUnsafe()}) include special characters. These are intended for specific use cases
 * where those characters are valid and safe in the target environment (e.g., HTTP headers). Care
 * must be taken to ensure compatibility and security (e.g., against injection) when using them.
 * </p>
 *
 * @see <a href="https://github.com/ai/nanoid/blob/main/url-alphabet/index.js">NanoID URL
 * Alphabet</a>
 * @see <a href="https://github.com/CyberAP/nanoid-dictionary">NanoID Dictionary</a>
 * @since 0.2.0
 */
public final class CharacterDictionaries {

  /**
   * Private constructor to prevent instantiation of this utility class.
   *
   * @throws AssertionError always thrown to indicate this class should not be instantiated.
   */
  private CharacterDictionaries() {
    throw new AssertionError("CharacterDictionaries is a utility class and should not be instantiated.");
  }

  /**
   * Gets the standard URL-friendly character set.
   * <p>
   * This set includes alphanumeric characters (0-9, a-z, A-Z) and the two URL-safe special
   * characters: hyphen ({@code -}) and underscore ({@code _}). It corresponds to the default
   * alphabet used by the original NanoID JavaScript library.
   * </p>
   *
   * @return A cloned {@code byte[]} array containing the URL-friendly ASCII characters.
   * @see <a href="https://github.com/ai/nanoid/blob/main/url-alphabet/index.js">NanoID URL
   * Alphabet</a>
   * @since 0.2.0
   */
  public static byte[] getUrlFriendly() {
    return UrlFriendlyHolder.URL_FRIENDLY.clone();
  }

  /**
   * Gets the standard uppercase English letters (A-Z).
   *
   * @return A cloned {@code byte[]} array containing uppercase ASCII letters (A-Z).
   * @since 0.2.0
   */
  public static byte[] getUppercase() {
    return UppercaseHolder.UPPERCASE.clone();
  }

  /**
   * Gets the standard lowercase English letters (a-z).
   *
   * @return A cloned {@code byte[]} array containing lowercase ASCII letters (a-z).
   * @since 0.2.0
   */
  public static byte[] getLowercase() {
    return LowercaseHolder.LOWERCASE.clone();
  }

  /**
   * Gets the standard decimal digits (0-9).
   *
   * @return A cloned {@code byte[]} array containing ASCII digits (0-9).
   * @since 0.2.0
   */
  public static byte[] getNumbers() {
    return NumbersHolder.NUMBERS.clone();
  }

  /**
   * Gets the combined alphanumeric character set (0-9, a-z, A-Z).
   * <p>
   * This combines the sets returned by {@link #getNumbers()}, {@link #getLowercase()}, and
   * {@link #getUppercase()}.
   * </p>
   *
   * @return A cloned {@code byte[]} array containing alphanumeric ASCII characters.
   * @since 0.2.0
   */
  public static byte[] getAlphanumeric() {
    return AlphanumericHolder.ALPHANUMERIC.clone();
  }

  /**
   * Gets the uppercase hexadecimal digits (0-9, A-F).
   *
   * @return A cloned {@code byte[]} array containing uppercase hexadecimal ASCII digits.
   * @since 0.2.0
   */
  public static byte[] getHexadecimalUppercase() {
    return HexadecimalUppercaseHolder.HEXADECIMAL_UPPERCASE.clone();
  }

  /**
   * Gets the lowercase hexadecimal digits (0-9, a-f).
   *
   * @return A cloned {@code byte[]} array containing lowercase hexadecimal ASCII digits.
   * @since 0.2.0
   */
  public static byte[] getHexadecimalLowercase() {
    return HexadecimalLowercaseHolder.HEXADECIMAL_LOWERCASE.clone();
  }

  /**
   * Gets a character set designed to minimize visual similarity between characters.
   * <p>
   * This set removes characters that are often confused with each other, such as 0, O, I, l, 1. It
   * is useful for identifiers that might be read by humans or manually entered.
   * </p>
   *
   * @return A cloned {@code byte[]} array containing less ambiguous ASCII characters.
   * @since 0.2.0
   */
  public static byte[] getNoLookALikes() {
    return NoLookALikesHolder.NO_LOOK_A_LIKES.clone();
  }

  /**
   * Gets a stricter subset of {@link #getNoLookALikes()} for enhanced clarity.
   * <p>
   * This set is even more conservative, excluding additional characters like '0', 'O', 'o' to
   * further reduce potential for confusion.
   * </p>
   *
   * @return A cloned {@code byte[]} array containing a very safe subset of ASCII characters.
   * @since 0.2.0
   */
  public static byte[] getNoLookALikesSafe() {
    return NoLookALikesSafeHolder.NO_LOOK_A_LIKES_SAFE.clone();
  }

  /**
   * Gets a character set designed to be safe for use in HTTP cookie names and values according to
   * RFC 6265.
   * <p>
   * Includes alphanumeric characters and the symbols: {@code !#$%&'*+-.^_`|~}.
   * </p>
   * <p>
   * <b>Warning:</b> While these characters are RFC 6265 compliant, the actual
   * safety can depend on the specific HTTP library or browser implementation.
   * </p>
   *
   * @return A cloned {@code byte[]} array containing RFC 6265-compliant ASCII characters.
   * @since 0.2.0
   */
  public static byte[] getCookieSafe() {
    return CookieSafeHolder.COOKIE_SAFE.clone();
  }

  /**
   * Gets a broader set of characters often found to work in browser cookies in practice.
   * <p>
   * Includes characters from {@link #getAlphanumeric()} and additional symbols:
   * {@code !#$%&'()*+-./:<=>?@[]^_`{|}~}.
   * </p>
   * <p>
   * <b>Warning:</b> This set includes many special characters. Using it requires
   * careful handling, especially regarding character encoding and potential injection
   * vulnerabilities. It is recommended for advanced users who understand the implications.
   * </p>
   * <p>
   * <b>Security Note:</b> Characters like {@code <}, {@code >}, {@code "}, {@code \}
   * are intentionally excluded from this representation to avoid common security pitfalls, even
   * though they might technically work in some contexts. Always validate and sanitize user input.
   * </p>
   *
   * @return A cloned {@code byte[]} array containing a practical set of ASCII characters for
   * cookies.
   * @since 0.2.0
   */
  public static byte[] getCookieUnsafe() {
    return CookieUnsafeHolder.COOKIE_UNSAFE.clone();
  }

  // Holder classes for lazy initialization
  // Internal implementation detail for lazy loading. No Javadoc needed for private static classes.

  private static final class UrlFriendlyHolder {
    private static final byte[] URL_FRIENDLY = {
      0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x61, 0x62,
      0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A, 0x6B, 0x6C, 0x6D, 0x6E,
      0x6F, 0x70, 0x71, 0x72, 0x73, 0x74, 0x75, 0x76, 0x77, 0x78, 0x79, 0x7A,
      0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4A, 0x4B, 0x4C,
      0x4D, 0x4E, 0x4F, 0x50, 0x51, 0x52, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58,
      0x59, 0x5A, 0x2D, 0x5F
    };
  }

  private static final class UppercaseHolder {
    private static final byte[] UPPERCASE = {
      0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4A, 0x4B, 0x4C,
      0x4D, 0x4E, 0x4F, 0x50, 0x51, 0x52, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58,
      0x59, 0x5A
    };
  }

  private static final class LowercaseHolder {
    private static final byte[] LOWERCASE = {
      0x61, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A, 0x6B, 0x6C,
      0x6D, 0x6E, 0x6F, 0x70, 0x71, 0x72, 0x73, 0x74, 0x75, 0x76, 0x77, 0x78,
      0x79, 0x7A
    };
  }

  private static final class NumbersHolder {
    private static final byte[] NUMBERS = {
      0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39
    };
  }

  private static final class AlphanumericHolder {
    private static final byte[] ALPHANUMERIC = {
      0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x61, 0x62,
      0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A, 0x6B, 0x6C, 0x6D, 0x6E,
      0x6F, 0x70, 0x71, 0x72, 0x73, 0x74, 0x75, 0x76, 0x77, 0x78, 0x79, 0x7A,
      0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4A, 0x4B, 0x4C,
      0x4D, 0x4E, 0x4F, 0x50, 0x51, 0x52, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58,
      0x59, 0x5A
    };
  }

  private static final class HexadecimalUppercaseHolder {
    private static final byte[] HEXADECIMAL_UPPERCASE = {
      0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x41, 0x42,
      0x43, 0x44, 0x45, 0x46
    };
  }

  private static final class HexadecimalLowercaseHolder {
    private static final byte[] HEXADECIMAL_LOWERCASE = {
      0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x61, 0x62,
      0x63, 0x64, 0x65, 0x66
    };
  }

  private static final class NoLookALikesHolder {
    private static final byte[] NO_LOOK_A_LIKES = {
      0x33, 0x34, 0x36, 0x37, 0x38, 0x39, 0x41, 0x42, 0x43, 0x44, 0x45, 0x46,
      0x47, 0x48, 0x4A, 0x4B, 0x4C, 0x4D, 0x4E, 0x50, 0x51, 0x52, 0x54, 0x55,
      0x56, 0x57, 0x58, 0x59, 0x61, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68,
      0x69, 0x6A, 0x6B, 0x6D, 0x6E, 0x70, 0x71, 0x72, 0x74, 0x77, 0x78, 0x79,
      0x7A
    };
  }

  private static final class NoLookALikesSafeHolder {
    private static final byte[] NO_LOOK_A_LIKES_SAFE = {
      0x36, 0x37, 0x38, 0x39, 0x42, 0x43, 0x44, 0x46, 0x47, 0x48, 0x4A, 0x4B,
      0x4C, 0x4D, 0x4E, 0x50, 0x51, 0x52, 0x54, 0x57, 0x62, 0x63, 0x64, 0x66,
      0x67, 0x68, 0x6A, 0x6B, 0x6D, 0x6E, 0x70, 0x71, 0x72, 0x74, 0x77, 0x7A
    };
  }

  private static final class CookieSafeHolder {
    private static final byte[] COOKIE_SAFE = {
      0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x61, 0x62,
      0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A, 0x6B, 0x6C, 0x6D, 0x6E,
      0x6F, 0x70, 0x71, 0x72, 0x73, 0x74, 0x75, 0x76, 0x77, 0x78, 0x79, 0x7A,
      0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4A, 0x4B, 0x4C,
      0x4D, 0x4E, 0x4F, 0x50, 0x51, 0x52, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58,
      0x59, 0x5A, 0x21, 0x23, 0x24, 0x25, 0x26, 0x27, 0x2A, 0x2B, 0x2D, 0x2E,
      0x5E, 0x5F, 0x60, 0x7C, 0x7E
    };
  }

  private static final class CookieUnsafeHolder {
    private static final byte[] COOKIE_UNSAFE = {
      0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x61, 0x62,
      0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A, 0x6B, 0x6C, 0x6D, 0x6E,
      0x6F, 0x70, 0x71, 0x72, 0x73, 0x74, 0x75, 0x76, 0x77, 0x78, 0x79, 0x7A,
      0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4A, 0x4B, 0x4C,
      0x4D, 0x4E, 0x4F, 0x50, 0x51, 0x52, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58,
      0x59, 0x5A, 0x21, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B,
      0x2D, 0x2E, 0x2F, 0x3A, 0x3C, 0x3D, 0x3E, 0x3F, 0x40, 0x5B, 0x5D, 0x5E,
      0x5F, 0x60, 0x7B, 0x7C, 0x7D, 0x7E
    };
  }
}