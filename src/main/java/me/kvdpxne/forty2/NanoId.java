package me.kvdpxne.forty2;

import java.util.Random;
import me.kvdpxne.forty2.config.CharacterDictionaries;
import me.kvdpxne.forty2.generator.NanoIdStringGenerator;
import me.kvdpxne.forty2.random.SecureRandomProvider;
import me.kvdpxne.forty2.random.ThreadLocalRandomProvider;

/**
 * A utility class for generating compact, URL-friendly unique identifiers based on the NanoID
 * algorithm.
 * <p>
 * This class serves as the primary public API for the library. It provides static factory methods
 * for generating identifiers with common character sets (uppercase, lowercase, numbers,
 * alphanumeric, hex, etc.) or completely custom ones. It offers two main generation strategies:
 * </p>
 * <ul>
 *   <li><b>Fast (default):</b> Uses {@link java.util.concurrent.ThreadLocalRandom} for high performance.
 *       Suitable for most general-purpose ID generation.</li>
 *   <li><b>Secure:</b> Uses {@link java.security.SecureRandom} for cryptographically strong randomness.
 *       Recommended when unpredictability of the ID is critical (e.g., for security tokens).</li>
 * </ul>
 * <p>
 * Identifiers are generated using a carefully selected alphabet and a size that provides a high
 * degree of uniqueness. The default size is {@value #DEFAULT_SIZE} characters.
 * </p>
 * <p>
 * This class is stateless and safe to use concurrently from multiple threads. It internally manages
 * thread-safe generator instances.
 * </p>
 *
 * @see <a href="https://github.com/ai/nanoid">NanoID</a>
 * @see me.kvdpxne.forty2.config.CharacterDictionaries
 * @see me.kvdpxne.forty2.generator.NanoIdStringGenerator
 * @since 0.2.0
 */
public final class NanoId {

  /**
   * Default size of the generated random identifiers. This size provides a good balance between
   * uniqueness and compactness.
   *
   * @since 0.2.0
   */
  public static final int DEFAULT_SIZE = 21;

  /**
   * Private constructor to prevent instantiation of this utility class.
   *
   * @throws AssertionError always thrown to indicate this class should not be instantiated.
   * @since 0.2.0
   */
  private NanoId() {
    throw new AssertionError("NanoId is a utility class and should not be instantiated.");
  }

  /**
   * Gets the shared instance of the fast NanoID string generator.
   * <p>
   * This generator uses {@link ThreadLocalRandomProvider} for high performance.
   * </p>
   *
   * @return The shared fast {@link NanoIdStringGenerator} instance.
   * @since 0.2.0
   */
  private static NanoIdStringGenerator getFastGenerator() {
    return FastGeneratorHolder.INSTANCE;
  }

  /**
   * Gets the shared instance of the secure NanoID string generator.
   * <p>
   * This generator uses {@link SecureRandomProvider} for cryptographically strong randomness.
   * </p>
   *
   * @return The shared secure {@link NanoIdStringGenerator} instance.
   * @since 0.2.0
   */
  private static NanoIdStringGenerator getSecureGenerator() {
    return SecureGeneratorHolder.INSTANCE;
  }

  /**
   * Generates a random string of specified size using the provided characters and a custom
   * {@link Random} number generator.
   * <p>
   * This method allows full control over the generation process, specifying the source of
   * randomness, the character set, and the size.
   * </p>
   *
   * @param random     The {@link Random} object used to generate random bytes. Must not be
   *                   {@code null}.
   * @param characters The characters to be used for generating the random string. Must not be
   *                   {@code null}, not empty, contain only printable ASCII characters (0x21 to
   *                   0x7E), and have no duplicates.
   * @param size       The desired length of the generated string. Must be between
   *                   {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *                   {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE}
   *                   inclusive.
   * @return A newly generated random string of the specified size.
   * @throws NullPointerException     if {@code random} or {@code characters} is {@code null}.
   * @throws IllegalArgumentException if {@code characters} is empty, contains non-printable ASCII
   *                                  characters (outside 0x21-0x7E), contains duplicates, or if
   *                                  {@code size} is outside the acceptable range.
   * @since 0.2.0
   */
  public static String create(
    final Random random,
    final char[] characters,
    final int size
  ) {
    return getFastGenerator().create(random, characters, size);
  }

  /**
   * Generates a random string of specified size using the provided characters and the default fast
   * random number generator ({@link java.util.concurrent.ThreadLocalRandom}).
   * <p>
   * This method is suitable for general-purpose ID generation where high performance is desired.
   * </p>
   *
   * @param characters The characters to be used for generating the random string. Must not be
   *                   {@code null}, not empty, contain only printable ASCII characters (0x21 to
   *                   0x7E), and have no duplicates.
   * @param size       The desired length of the generated string. Must be between
   *                   {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *                   {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE}
   *                   inclusive.
   * @return A newly generated random string of the specified size.
   * @throws NullPointerException     if {@code characters} is {@code null}.
   * @throws IllegalArgumentException if {@code characters} is empty, contains non-printable ASCII
   *                                  characters (outside 0x21-0x7E), contains duplicates, or if
   *                                  {@code size} is outside the acceptable range.
   * @since 0.2.0
   */
  public static String create(
    final char[] characters,
    final int size
  ) {
    return getFastGenerator().create(characters, size);
  }

  /**
   * Generates a fast, random string using the provided characters and the default size
   * ({@value #DEFAULT_SIZE}) with the default fast random number generator
   * ({@link java.util.concurrent.ThreadLocalRandom}).
   * <p>
   * This is a convenience overload equivalent to calling
   * {@link #create(char[], int) create(characters, DEFAULT_SIZE)}.
   * </p>
   *
   * @param characters The characters to be used for generating the random string. Must not be
   *                   {@code null}, not empty, contain only printable ASCII characters (0x21 to
   *                   0x7E), and have no duplicates.
   * @return A newly generated random string of {@link #DEFAULT_SIZE}.
   * @throws NullPointerException     if {@code characters} is {@code null}.
   * @throws IllegalArgumentException if {@code characters} is empty, contains non-printable ASCII
   *                                  characters (outside 0x21-0x7E), contains duplicates.
   * @since 0.2.0
   */
  public static String create(
    final char[] characters
  ) {
    return create(characters, DEFAULT_SIZE);
  }

  /**
   * Generates a cryptographically secure, random string of specified size using the provided
   * characters and a custom {@link Random} number generator.
   * <p>
   * This method allows full control over the generation process, specifying the source of
   * cryptographically strong randomness, the character set, and the size. It delegates to the
   * secure generator obtained via {@link #getSecureGenerator()}.
   * </p>
   *
   * @param random     The {@link Random} object used to generate random bytes. Must not be
   *                   {@code null}. It is recommended to use {@link java.security.SecureRandom} or
   *                   a similar cryptographically secure source.
   * @param characters The characters to be used for generating the random string. Must not be
   *                   {@code null}, not empty, contain only printable ASCII characters (0x21 to
   *                   0x7E), and have no duplicates.
   * @param size       The desired length of the generated string. Must be between
   *                   {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *                   {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE}
   *                   inclusive.
   * @return A newly generated cryptographically secure random string of the specified size.
   * @throws NullPointerException     if {@code random} or {@code characters} is {@code null}.
   * @throws IllegalArgumentException if {@code characters} is empty, contains non-printable ASCII
   *                                  characters (outside 0x21-0x7E), contains duplicates, or if
   *                                  {@code size} is outside the acceptable range.
   * @since 0.2.0
   */
  public static String createSecure(
    final Random random,
    final char[] characters,
    final int size
  ) {
    return getSecureGenerator().create(random, characters, size);
  }

  /**
   * Generates a cryptographically secure, random string of specified size using the provided
   * characters and the default secure random number generator
   * ({@link java.security.SecureRandom}).
   * <p>
   * This method is suitable for ID generation where unpredictability is critical, such as for
   * security tokens or passwords. It delegates to the secure generator obtained via
   * {@link #getSecureGenerator()}.
   * </p>
   *
   * @param characters The characters to be used for generating the random string. Must not be
   *                   {@code null}, not empty, contain only printable ASCII characters (0x21 to
   *                   0x7E), and have no duplicates.
   * @param size       The desired length of the generated string. Must be between
   *                   {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *                   {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE}
   *                   inclusive.
   * @return A newly generated cryptographically secure random string of the specified size.
   * @throws NullPointerException     if {@code characters} is {@code null}.
   * @throws IllegalArgumentException if {@code characters} is empty, contains non-printable ASCII
   *                                  characters (outside 0x21-0x7E), contains duplicates, or if
   *                                  {@code size} is outside the acceptable range.
   * @since 0.2.0
   */
  public static String createSecure(
    final char[] characters,
    final int size
  ) {
    return getSecureGenerator().create(characters, size);
  }

  /**
   * Generates a cryptographically secure, random string using the provided characters and the
   * default size ({@value #DEFAULT_SIZE}) with the default secure random number generator
   * ({@link java.security.SecureRandom}).
   * <p>
   * This is a convenience overload equivalent to calling
   * {@link #createSecure(char[], int) createSecure(characters, DEFAULT_SIZE)}.
   * </p>
   *
   * @param characters The characters to be used for generating the random string. Must not be
   *                   {@code null}, not empty, contain only printable ASCII characters (0x21 to
   *                   0x7E), and have no duplicates.
   * @return A newly generated cryptographically secure random string of {@link #DEFAULT_SIZE}.
   * @throws NullPointerException     if {@code characters} is {@code null}.
   * @throws IllegalArgumentException if {@code characters} is empty, contains non-printable ASCII
   *                                  characters (outside 0x21-0x7E), contains duplicates.
   * @since 0.2.0
   */
  public static String createSecure(
    final char[] characters
  ) {
    return createSecure(characters, DEFAULT_SIZE);
  }

  /**
   * Generates a fast, random string of {@link #DEFAULT_SIZE} using the standard
   * {@link CharacterDictionaries#getUrlFriendly()} character set.
   * <p>
   * This character set includes alphanumeric characters (0-9, a-z, A-Z) and the two URL-safe
   * special characters: hyphen ({@code -}) and underscore ({@code _}). It corresponds to the
   * default alphabet used by the original NanoID JavaScript library.
   * </p>
   *
   * @return A random string of default size using the URL-friendly character set.
   * @see #urlFriendly(int)
   * @see CharacterDictionaries#getUrlFriendly()
   * @since 0.2.0
   */
  public static String urlFriendly() {
    return urlFriendly(DEFAULT_SIZE);
  }

  /**
   * Generates a fast, random string of specified size using the standard
   * {@link CharacterDictionaries#getUrlFriendly()} character set.
   * <p>
   * This character set includes alphanumeric characters (0-9, a-z, A-Z) and the two URL-safe
   * special characters: hyphen ({@code -}) and underscore ({@code _}). It corresponds to the
   * default alphabet used by the original NanoID JavaScript library.
   * </p>
   *
   * @param size The desired length of the generated string. Must be between
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE} inclusive.
   * @return A random string of the specified size using the URL-friendly character set.
   * @throws IllegalArgumentException if {@code size} is outside the acceptable range.
   * @see CharacterDictionaries#getUrlFriendly()
   * @since 0.2.0
   */
  public static String urlFriendly(
    final int size
  ) {
    final byte[] alphabet = CharacterDictionaries.getUrlFriendly();
    return getFastGenerator().create(alphabet, size);
  }

  /**
   * Generates a fast, random string of {@link #DEFAULT_SIZE} using the standard
   * {@link CharacterDictionaries#getUppercase()} character set (A-Z).
   *
   * @return A random string of default size using uppercase letters.
   * @see #uppercase(int)
   * @since 0.2.0
   */
  public static String uppercase() {
    return uppercase(DEFAULT_SIZE);
  }

  /**
   * Generates a fast, random string of specified size using the standard
   * {@link CharacterDictionaries#getUppercase()} character set (A-Z).
   *
   * @param size The desired length of the generated string. Must be between
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE} inclusive.
   * @return A random string of the specified size using uppercase letters.
   * @throws IllegalArgumentException if {@code size} is outside the acceptable range.
   * @since 0.2.0
   */
  public static String uppercase(
    final int size
  ) {
    final byte[] alphabet = CharacterDictionaries.getUppercase();
    return getFastGenerator().create(alphabet, size);
  }

  /**
   * Generates a fast, random string of specified size using the standard
   * {@link CharacterDictionaries#getLowercase()} character set (a-z).
   *
   * @return A random string of default size using lowercase letters.
   * @see #lowercase(int)
   * @since 0.2.0
   */
  public static String lowercase() {
    return lowercase(DEFAULT_SIZE);
  }

  /**
   * Generates a fast, random string of specified size using the standard
   * {@link CharacterDictionaries#getLowercase()} character set (a-z).
   *
   * @param size The desired length of the generated string. Must be between
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE} inclusive.
   * @return A random string of the specified size using lowercase letters.
   * @throws IllegalArgumentException if {@code size} is outside the acceptable range.
   * @since 0.2.0
   */
  public static String lowercase(
    final int size
  ) {
    final byte[] alphabet = CharacterDictionaries.getLowercase();
    return getFastGenerator().create(alphabet, size);
  }

  /**
   * Generates a fast, random string of {@link #DEFAULT_SIZE} using the standard
   * {@link CharacterDictionaries#getNumbers()} character set (0-9).
   *
   * @return A random string of default size using digits.
   * @see #numbers(int)
   * @since 0.2.0
   */
  public static String numbers() {
    return numbers(DEFAULT_SIZE);
  }

  /**
   * Generates a fast, random string of specified size using the standard
   * {@link CharacterDictionaries#getNumbers()} character set (0-9).
   *
   * @param size The desired length of the generated string. Must be between
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE} inclusive.
   * @return A random string of the specified size using digits.
   * @throws IllegalArgumentException if {@code size} is outside the acceptable range.
   * @since 0.2.0
   */
  public static String numbers(
    final int size
  ) {
    final byte[] alphabet = CharacterDictionaries.getNumbers();
    return getFastGenerator().create(alphabet, size);
  }

  /**
   * Generates a fast, random string of {@link #DEFAULT_SIZE} using the standard
   * {@link CharacterDictionaries#getAlphanumeric()} character set (0-9, a-z, A-Z).
   *
   * @return A random string of default size using alphanumeric characters.
   * @see #alphanumeric(int)
   * @since 0.2.0
   */
  public static String alphanumeric() {
    return alphanumeric(DEFAULT_SIZE);
  }

  /**
   * Generates a fast, random string of specified size using the standard
   * {@link CharacterDictionaries#getAlphanumeric()} character set (0-9, a-z, A-Z).
   *
   * @param size The desired length of the generated string. Must be between
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE} inclusive.
   * @return A random string of the specified size using alphanumeric characters.
   * @throws IllegalArgumentException if {@code size} is outside the acceptable range.
   * @since 0.2.0
   */
  public static String alphanumeric(
    final int size
  ) {
    final byte[] alphabet = CharacterDictionaries.getAlphanumeric();
    return getFastGenerator().create(alphabet, size);
  }

  /**
   * Generates a fast, random string of {@link #DEFAULT_SIZE} using the standard
   * {@link CharacterDictionaries#getHexadecimalUppercase()} character set (0-9, A-F).
   *
   * @return A random string of default size using uppercase hexadecimal digits.
   * @see #hexUppercase(int)
   * @since 0.2.0
   */
  public static String hexUppercase() {
    return hexUppercase(DEFAULT_SIZE);
  }

  /**
   * Generates a fast, random string of specified size using the standard
   * {@link CharacterDictionaries#getHexadecimalUppercase()} character set (0-9, A-F).
   *
   * @param size The desired length of the generated string. Must be between
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE} inclusive.
   * @return A random string of the specified size using uppercase hexadecimal digits.
   * @throws IllegalArgumentException if {@code size} is outside the acceptable range.
   * @since 0.2.0
   */
  public static String hexUppercase(
    final int size
  ) {
    final byte[] alphabet = CharacterDictionaries.getHexadecimalUppercase();
    return getFastGenerator().create(alphabet, size);
  }

  /**
   * Generates a fast, random string of {@link #DEFAULT_SIZE} using the standard
   * {@link CharacterDictionaries#getHexadecimalLowercase()} character set (0-9, a-f).
   *
   * @return A random string of default size using lowercase hexadecimal digits.
   * @see #hexLowercase(int)
   * @since 0.2.0
   */
  public static String hexLowercase() {
    return hexLowercase(DEFAULT_SIZE);
  }

  /**
   * Generates a fast, random string of specified size using the standard
   * {@link CharacterDictionaries#getHexadecimalLowercase()} character set (0-9, a-f).
   *
   * @param size The desired length of the generated string. Must be between
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE} inclusive.
   * @return A random string of the specified size using lowercase hexadecimal digits.
   * @throws IllegalArgumentException if {@code size} is outside the acceptable range.
   * @since 0.2.0
   */
  public static String hexLowercase(
    final int size
  ) {
    final byte[] alphabet = CharacterDictionaries.getHexadecimalLowercase();
    return getFastGenerator().create(alphabet, size);
  }

  /**
   * Generates a fast, random string of {@link #DEFAULT_SIZE} using the standard
   * {@link CharacterDictionaries#getNoLookALikes()} character set, which excludes visually similar
   * characters (0, O, I, l, 1, etc.).
   *
   * @return A random string of default size using less ambiguous characters.
   * @see #noLookALikes(int)
   * @since 0.2.0
   */
  public static String noLookALikes() {
    return noLookALikes(DEFAULT_SIZE);
  }

  /**
   * Generates a fast, random string of specified size using the standard
   * {@link CharacterDictionaries#getNoLookALikes()} character set, which excludes visually similar
   * characters (0, O, I, l, 1, etc.).
   *
   * @param size The desired length of the generated string. Must be between
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE} inclusive.
   * @return A random string of the specified size using less ambiguous characters.
   * @throws IllegalArgumentException if {@code size} is outside the acceptable range.
   * @since 0.2.0
   */
  public static String noLookALikes(
    final int size
  ) {
    final byte[] alphabet = CharacterDictionaries.getNoLookALikes();
    return getFastGenerator().create(alphabet, size);
  }

  /**
   * Generates a fast, random string of {@link #DEFAULT_SIZE} using the standard
   * {@link CharacterDictionaries#getNoLookALikesSafe()} character set, which is an even stricter
   * subset of {@link #noLookALikes()} for maximum clarity.
   *
   * @return A random string of default size using a very safe character set.
   * @see #noLookALikesSafe(int)
   * @since 0.2.0
   */
  public static String noLookALikesSafe() {
    return noLookALikesSafe(DEFAULT_SIZE);
  }

  /**
   * Generates a fast, random string of specified size using the standard
   * {@link CharacterDictionaries#getNoLookALikesSafe()} character set, which is an even stricter
   * subset of {@link #noLookALikes()} for maximum clarity.
   *
   * @param size The desired length of the generated string. Must be between
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE} inclusive.
   * @return A random string of the specified size using a very safe character set.
   * @throws IllegalArgumentException if {@code size} is outside the acceptable range.
   * @since 0.2.0
   */
  public static String noLookALikesSafe(
    final int size
  ) {
    final byte[] alphabet = CharacterDictionaries.getNoLookALikesSafe();
    return getFastGenerator().create(alphabet, size);
  }

  /**
   * Generates a fast, random string of {@link #DEFAULT_SIZE} using the standard
   * {@link CharacterDictionaries#getCookieSafe()} character set, designed for HTTP cookie
   * names/values.
   *
   * @return A random string of default size using RFC 6265-compliant characters.
   * @see #cookieSafe(int)
   * @since 0.2.0
   */
  public static String cookieSafe() {
    return cookieSafe(DEFAULT_SIZE);
  }

  /**
   * Generates a fast, random string of specified size using the standard
   * {@link CharacterDictionaries#getCookieSafe()} character set, designed for HTTP cookie
   * names/values.
   *
   * @param size The desired length of the generated string. Must be between
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE} inclusive.
   * @return A random string of the specified size using RFC 6265-compliant characters.
   * @throws IllegalArgumentException if {@code size} is outside the acceptable range.
   * @since 0.2.0
   */
  public static String cookieSafe(
    final int size
  ) {
    final byte[] alphabet = CharacterDictionaries.getCookieSafe();
    return getFastGenerator().create(alphabet, size);
  }

  /**
   * Generates a fast, random string of {@link #DEFAULT_SIZE} using the standard
   * {@link CharacterDictionaries#getCookieUnsafe()} character set, a broader set for cookies.
   *
   * @return A random string of default size using a practical cookie character set.
   * @see #cookieUnsafe(int)
   * @since 0.2.0
   */
  public static String cookieUnsafe() {
    return cookieUnsafe(DEFAULT_SIZE);
  }

  /**
   * Generates a fast, random string of specified size using the standard
   * {@link CharacterDictionaries#getCookieUnsafe()} character set, a broader set for cookies.
   *
   * @param size The desired length of the generated string. Must be between
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE} inclusive.
   * @return A random string of the specified size using a practical cookie character set.
   * @throws IllegalArgumentException if {@code size} is outside the acceptable range.
   * @since 0.2.0
   */
  public static String cookieUnsafe(
    final int size
  ) {
    final byte[] alphabet = CharacterDictionaries.getCookieUnsafe();
    return getFastGenerator().create(alphabet, size);
  }

  /**
   * Generates a cryptographically secure, random string of {@link #DEFAULT_SIZE} using the standard
   * {@link CharacterDictionaries#getUrlFriendly()} character set.
   * <p>
   * This character set includes alphanumeric characters (0-9, a-z, A-Z) and the two URL-safe
   * special characters: hyphen ({@code -}) and underscore ({@code _}). It corresponds to the
   * default alphabet used by the original NanoID JavaScript library.
   * </p>
   *
   * @return A cryptographically secure random string of default size using the URL-friendly
   * character set.
   * @see #urlFriendlySecure(int)
   * @see CharacterDictionaries#getUrlFriendly()
   * @since 0.2.0
   */
  public static String urlFriendlySecure() {
    return urlFriendlySecure(DEFAULT_SIZE);
  }

  /**
   * Generates a cryptographically secure, random string of specified size using the standard
   * {@link CharacterDictionaries#getUrlFriendly()} character set.
   * <p>
   * This character set includes alphanumeric characters (0-9, a-z, A-Z) and the two URL-safe
   * special characters: hyphen ({@code -}) and underscore ({@code _}). It corresponds to the
   * default alphabet used by the original NanoID JavaScript library.
   * </p>
   *
   * @param size The desired length of the generated string. Must be between
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE} inclusive.
   * @return A cryptographically secure random string of the specified size using the URL-friendly
   * character set.
   * @throws IllegalArgumentException if {@code size} is outside the acceptable range.
   * @see CharacterDictionaries#getUrlFriendly()
   * @since 0.2.0
   */
  public static String urlFriendlySecure(
    final int size
  ) {
    final byte[] alphabet = CharacterDictionaries.getUrlFriendly();
    return getSecureGenerator().create(alphabet, size);
  }

  /**
   * Generates a cryptographically secure, random string of {@link #DEFAULT_SIZE} using the standard
   * {@link CharacterDictionaries#getUppercase()} character set (A-Z).
   *
   * @return A random string of default size using uppercase letters.
   * @see #uppercaseSecure(int)
   * @since 0.2.0
   */
  public static String uppercaseSecure() {
    return uppercaseSecure(DEFAULT_SIZE);
  }

  /**
   * Generates a cryptographically secure, random string of specified size using the standard
   * {@link CharacterDictionaries#getUppercase()} character set (A-Z).
   *
   * @param size The desired length of the generated string. Must be between
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE} inclusive.
   * @return A random string of the specified size using uppercase letters.
   * @throws IllegalArgumentException if {@code size} is outside the acceptable range.
   * @since 0.2.0
   */
  public static String uppercaseSecure(
    final int size
  ) {
    final byte[] alphabet = CharacterDictionaries.getUppercase();
    return getSecureGenerator().create(alphabet, size);
  }

  /**
   * Generates a cryptographically secure, random string of {@link #DEFAULT_SIZE} using the standard
   * {@link CharacterDictionaries#getLowercase()} character set (a-z).
   *
   * @return A random string of default size using lowercase letters.
   * @see #lowercaseSecure(int)
   * @since 0.2.0
   */
  public static String lowercaseSecure() {
    return lowercaseSecure(DEFAULT_SIZE);
  }

  /**
   * Generates a cryptographically secure, random string of specified size using the standard
   * {@link CharacterDictionaries#getLowercase()} character set (a-z).
   *
   * @param size The desired length of the generated string. Must be between
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE} inclusive.
   * @return A random string of the specified size using lowercase letters.
   * @throws IllegalArgumentException if {@code size} is outside the acceptable range.
   * @since 0.2.0
   */
  public static String lowercaseSecure(
    final int size
  ) {
    final byte[] alphabet = CharacterDictionaries.getLowercase();
    return getSecureGenerator().create(alphabet, size);
  }

  /**
   * Generates a cryptographically secure, random string of {@link #DEFAULT_SIZE} using the standard
   * {@link CharacterDictionaries#getNumbers()} character set (0-9).
   *
   * @return A random string of default size using digits.
   * @see #numbersSecure(int)
   * @since 0.2.0
   */
  public static String numbersSecure() {
    return numbersSecure(DEFAULT_SIZE);
  }

  /**
   * Generates a cryptographically secure, random string of specified size using the standard
   * {@link CharacterDictionaries#getNumbers()} character set (0-9).
   *
   * @param size The desired length of the generated string. Must be between
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE} inclusive.
   * @return A random string of the specified size using digits.
   * @throws IllegalArgumentException if {@code size} is outside the acceptable range.
   * @since 0.2.0
   */
  public static String numbersSecure(
    final int size
  ) {
    final byte[] alphabet = CharacterDictionaries.getNumbers();
    return getSecureGenerator().create(alphabet, size);
  }

  /**
   * Generates a cryptographically secure, random string of {@link #DEFAULT_SIZE} using the standard
   * {@link CharacterDictionaries#getAlphanumeric()} character set (0-9, a-z, A-Z).
   *
   * @return A random string of default size using alphanumeric characters.
   * @see #alphanumericSecure(int)
   * @since 0.2.0
   */
  public static String alphanumericSecure() {
    return alphanumericSecure(DEFAULT_SIZE);
  }

  /**
   * Generates a cryptographically secure, random string of specified size using the standard
   * {@link CharacterDictionaries#getAlphanumeric()} character set (0-9, a-z, A-Z).
   *
   * @param size The desired length of the generated string. Must be between
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE} inclusive.
   * @return A random string of the specified size using alphanumeric characters.
   * @throws IllegalArgumentException if {@code size} is outside the acceptable range.
   * @since 0.2.0
   */
  public static String alphanumericSecure(
    final int size
  ) {
    final byte[] alphabet = CharacterDictionaries.getAlphanumeric();
    return getSecureGenerator().create(alphabet, size);
  }

  /**
   * Generates a cryptographically secure, random string of {@link #DEFAULT_SIZE} using the standard
   * {@link CharacterDictionaries#getHexadecimalUppercase()} character set (0-9, A-F).
   *
   * @return A random string of default size using uppercase hexadecimal digits.
   * @see #hexUppercaseSecure(int)
   * @since 0.2.0
   */
  public static String hexUppercaseSecure() {
    return hexUppercaseSecure(DEFAULT_SIZE);
  }

  /**
   * Generates a cryptographically secure, random string of specified size using the standard
   * {@link CharacterDictionaries#getHexadecimalUppercase()} character set (0-9, A-F).
   *
   * @param size The desired length of the generated string. Must be between
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE} inclusive.
   * @return A random string of the specified size using uppercase hexadecimal digits.
   * @throws IllegalArgumentException if {@code size} is outside the acceptable range.
   * @since 0.2.0
   */
  public static String hexUppercaseSecure(
    final int size
  ) {
    final byte[] alphabet = CharacterDictionaries.getHexadecimalUppercase();
    return getSecureGenerator().create(alphabet, size);
  }

  /**
   * Generates a cryptographically secure, random string of {@link #DEFAULT_SIZE} using the standard
   * {@link CharacterDictionaries#getHexadecimalLowercase()} character set (0-9, a-f).
   *
   * @return A random string of default size using lowercase hexadecimal digits.
   * @see #hexLowercaseSecure(int)
   * @since 0.2.0
   */
  public static String hexLowercaseSecure() {
    return hexLowercaseSecure(DEFAULT_SIZE);
  }

  /**
   * Generates a cryptographically secure, random string of specified size using the standard
   * {@link CharacterDictionaries#getHexadecimalLowercase()} character set (0-9, a-f).
   *
   * @param size The desired length of the generated string. Must be between
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE} inclusive.
   * @return A random string of the specified size using lowercase hexadecimal digits.
   * @throws IllegalArgumentException if {@code size} is outside the acceptable range.
   * @since 0.2.0
   */
  public static String hexLowercaseSecure(
    final int size
  ) {
    final byte[] alphabet = CharacterDictionaries.getHexadecimalLowercase();
    return getSecureGenerator().create(alphabet, size);
  }

  /**
   * Generates a cryptographically secure, random string of {@link #DEFAULT_SIZE} using the standard
   * {@link CharacterDictionaries#getNoLookALikes()} character set, which excludes visually similar
   * characters (0, O, I, l, 1, etc.).
   *
   * @return A random string of default size using less ambiguous characters.
   * @see #noLookALikesSecure(int)
   * @since 0.2.0
   */
  public static String noLookALikesSecure() {
    return noLookALikesSecure(DEFAULT_SIZE);
  }

  /**
   * Generates a cryptographically secure, random string of specified size using the standard
   * {@link CharacterDictionaries#getNoLookALikes()} character set, which excludes visually similar
   * characters (0, O, I, l, 1, etc.).
   *
   * @param size The desired length of the generated string. Must be between
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE} inclusive.
   * @return A random string of the specified size using less ambiguous characters.
   * @throws IllegalArgumentException if {@code size} is outside the acceptable range.
   * @since 0.2.0
   */
  public static String noLookALikesSecure(
    final int size
  ) {
    final byte[] alphabet = CharacterDictionaries.getNoLookALikes();
    return getSecureGenerator().create(alphabet, size);
  }

  /**
   * Generates a cryptographically secure, random string of {@link #DEFAULT_SIZE} using the standard
   * {@link CharacterDictionaries#getNoLookALikesSafe()} character set, which is an even stricter
   * subset of {@link #noLookALikes()} for maximum clarity.
   *
   * @return A random string of default size using a very safe character set.
   * @see #noLookALikesSafeSecure(int)
   * @since 0.2.0
   */
  public static String noLookALikesSafeSecure() {
    return noLookALikesSafeSecure(DEFAULT_SIZE);
  }

  /**
   * Generates a cryptographically secure, random string of specified size using the standard
   * {@link CharacterDictionaries#getNoLookALikesSafe()} character set, which is an even stricter
   * subset of {@link #noLookALikes()} for maximum clarity.
   *
   * @param size The desired length of the generated string. Must be between
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE} inclusive.
   * @return A random string of the specified size using a very safe character set.
   * @throws IllegalArgumentException if {@code size} is outside the acceptable range.
   * @since 0.2.0
   */
  public static String noLookALikesSafeSecure(
    final int size
  ) {
    final byte[] alphabet = CharacterDictionaries.getNoLookALikesSafe();
    return getSecureGenerator().create(alphabet, size);
  }

  /**
   * Generates a cryptographically secure, random string of {@link #DEFAULT_SIZE} using the standard
   * {@link CharacterDictionaries#getCookieSafe()} character set, designed for HTTP cookie
   * names/values.
   *
   * @return A random string of default size using RFC 6265-compliant characters.
   * @see #cookieSafeSecure(int)
   * @since 0.2.0
   */
  public static String cookieSafeSecure() {
    return cookieSafeSecure(DEFAULT_SIZE);
  }

  /**
   * Generates a cryptographically secure, random string of specified size using the standard
   * {@link CharacterDictionaries#getCookieSafe()} character set, designed for HTTP cookie
   * names/values.
   *
   * @param size The desired length of the generated string. Must be between
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE} inclusive.
   * @return A random string of the specified size using RFC 6265-compliant characters.
   * @throws IllegalArgumentException if {@code size} is outside the acceptable range.
   * @since 0.2.0
   */
  public static String cookieSafeSecure(
    final int size
  ) {
    final byte[] alphabet = CharacterDictionaries.getCookieSafe();
    return getSecureGenerator().create(alphabet, size);
  }

  /**
   * Generates a cryptographically secure, random string of {@link #DEFAULT_SIZE} using the standard
   * {@link CharacterDictionaries#getCookieUnsafe()} character set, a broader set for cookies.
   *
   * @return A random string of default size using a practical cookie character set.
   * @see #cookieUnsafeSecure(int)
   * @since 0.2.0
   */
  public static String cookieUnsafeSecure() {
    return cookieUnsafeSecure(DEFAULT_SIZE);
  }

  /**
   * Generates a cryptographically secure, random string of specified size using the standard
   * {@link CharacterDictionaries#getCookieUnsafe()} character set, a broader set for cookies.
   *
   * @param size The desired length of the generated string. Must be between
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MIN_ACCEPTABLE_SIZE} and
   *             {@value me.kvdpxne.forty2.validator.AsciiValidator#MAX_ACCEPTABLE_SIZE} inclusive.
   * @return A random string of the specified size using a practical cookie character set.
   * @throws IllegalArgumentException if {@code size} is outside the acceptable range.
   * @since 0.2.0
   */
  public static String cookieUnsafeSecure(
    final int size
  ) {
    final byte[] alphabet = CharacterDictionaries.getCookieUnsafe();
    return getSecureGenerator().create(alphabet, size);
  }

  /**
   * Holder class for the fast NanoIdStringGenerator instance. Uses the Initialization-on-Demand
   * Holder idiom for lazy, thread-safe initialization.
   *
   * @since 0.2.0
   */
  private static final class FastGeneratorHolder {

    /**
     * The singleton instance of the fast generator.
     *
     * @since 0.2.0
     */
    private static final NanoIdStringGenerator INSTANCE =
      new NanoIdStringGenerator(new ThreadLocalRandomProvider());
  }

  /**
   * Holder class for the secure NanoIdStringGenerator instance. Uses the Initialization-on-Demand
   * Holder idiom for lazy, thread-safe initialization.
   *
   * @since 0.2.0
   */
  private static final class SecureGeneratorHolder {

    /**
     * The singleton instance of the secure generator.
     *
     * @since 0.2.0
     */
    private static final NanoIdStringGenerator INSTANCE =
      new NanoIdStringGenerator(new SecureRandomProvider());
  }
}