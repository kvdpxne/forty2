package me.kvdpxne.forty2.generator;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import me.kvdpxne.forty2.converter.CharacterArrayConverter;
import me.kvdpxne.forty2.random.RandomProvider;
import me.kvdpxne.forty2.validator.AsciiValidator;

/**
 * Generates random strings using the NanoID algorithm.
 * <p>
 * This class implements the {@link RandomStringGenerator} interface and provides the core logic for
 * generating unique, random identifiers based on the NanoID specification. It uses a
 * {@link RandomProvider} to obtain a source of randomness and a {@link PoolManager} to optimize
 * performance by buffering random bytes.
 * </p>
 * <p>
 * The generator expects character sets to be provided as {@code byte[]} arrays, assuming ASCII
 * encoding for efficiency. Input validation is performed using {@link AsciiValidator}. Character
 * conversion is handled by {@link CharacterArrayConverter}.
 * </p>
 * <p>
 * This class is thread-safe. Its stateless nature (apart from final dependencies) and the
 * thread-safe design of its dependencies ({@link RandomProvider}, {@link PoolManager}) ensure safe
 * concurrent use.
 * </p>
 *
 * @see <a href="https://github.com/ai/nanoid">NanoID</a>
 * @since 0.2.0
 */
public final class NanoIdStringGenerator implements RandomStringGenerator {

  /**
   * The provider of {@link Random} instances used for generating random bytes.
   *
   * @since 0.2.0
   */
  private final RandomProvider randomProvider;

  /**
   * The pool manager used to buffer random bytes for performance optimization.
   * <p>
   * This manager is inherently thread-safe due to its use of {@link ThreadLocal}.
   * </p>
   *
   * @since 0.2.0
   */
  private final PoolManager poolManager = new PoolManager();

  /**
   * Constructs a new NanoIdStringGenerator with the specified random provider.
   *
   * @param randomProvider The provider of {@link Random} instances. Must not be {@code null}.
   * @throws NullPointerException if {@code randomProvider} is {@code null}.
   * @since 0.2.0
   */
  public NanoIdStringGenerator(
    final RandomProvider randomProvider
  ) {
    if (null == randomProvider) {
      throw new NullPointerException("RandomProvider cannot be null.");
    }
    this.randomProvider = randomProvider;
  }

  /**
   * {@inheritDoc}
   * <p>
   * This implementation uses the {@link Random} instance provided by the configured
   * {@link #randomProvider}.
   * </p>
   *
   * @param characters {@inheritDoc}
   * @param size       {@inheritDoc}
   * @return {@inheritDoc}
   * @throws NullPointerException     {@inheritDoc}
   * @throws IllegalArgumentException {@inheritDoc}
   * @since 0.2.0
   */
  @Override
  public String create(
    final char[] characters,
    final int size
  ) {
    return this.create(this.randomProvider.get(), characters, size);
  }

  /**
   * {@inheritDoc}
   * <p>
   * This implementation performs validation on inputs, converts the character array to bytes,
   * calculates the necessary mask and step size using {@link NanoIdHelper}, and then delegates to
   * the core generation logic.
   * </p>
   *
   * @param random     {@inheritDoc}
   * @param characters {@inheritDoc}
   * @param size       {@inheritDoc}
   * @return {@inheritDoc}
   * @throws NullPointerException     {@inheritDoc}
   * @throws IllegalArgumentException {@inheritDoc}
   * @since 0.2.0
   */
  @Override
  public String create(
    final Random random,
    final char[] characters,
    final int size
  ) {
    AsciiValidator.validateRandom(random);
    AsciiValidator.validateCharacters(characters);
    AsciiValidator.validateSize(size);

    final byte[] byteCharacters = CharacterArrayConverter.toBytes(characters);
    final int mask = NanoIdHelper.calculateMask(characters.length);

    // Delegate to the overloaded method that accepts byte[] and mask
    return this.create(random, byteCharacters, mask, size);
  }

  /**
   * Generates a random string using a pre-converted byte array alphabet and a pre-calculated mask.
   * <p>
   * This method is a convenience overload that calculates the optimal step size and delegates to
   * the most specific {@code create} method. It is useful when the mask has already been computed
   * for the given alphabet.
   * </p>
   *
   * @param random     The {@link Random} object to use. Must not be {@code null}.
   * @param characters The characters (as a {@code byte[]}) to choose from. Must not be {@code null}
   *                   or empty.
   * @param mask       The pre-calculated mask for the alphabet.
   * @param size       The desired size of the generated string. Must be within the acceptable
   *                   range.
   * @return The generated random string.
   * @throws NullPointerException     if {@code random} or {@code characters} is {@code null}.
   * @throws IllegalArgumentException if {@code characters} is empty or if {@code size} is outside
   *                                  the acceptable range.
   * @since 0.2.0
   */
  public String create(
    final Random random,
    final byte[] characters,
    final int mask,
    final int size
  ) {
    final int step = NanoIdHelper.calculateStep(mask, size, characters.length);
    return this.create(random, characters, mask, step, size);
  }

  /**
   * The core method implementing the NanoID generation algorithm.
   * <p>
   * This method performs the main loop of the NanoID algorithm:
   * <ol>
   *   <li>Obtain a batch of random bytes from the {@link #poolManager}.</li>
   *   <li>Iterate through the bytes, applying the {@code mask}.</li>
   *   <li>If the masked value is a valid index into the {@code characters} array, use it.</li>
   *   <li>Repeat until the {@code result} array of {@code size} is filled.</li>
   * </ol>
   * </p>
   *
   * @param random     The {@link Random} object to use for refilling the pool if needed. Must not
   *                   be {@code null}.
   * @param characters The characters (as a {@code byte[]}) to choose from. Must not be {@code null}
   *                   or empty.
   * @param mask       The mask to apply to random bytes.
   * @param step       The number of random bytes to request from the pool in each iteration.
   * @param size       The desired size of the generated string. Must be within the acceptable
   *                   range.
   * @return The generated random string.
   * @throws NullPointerException     if {@code random} or {@code characters} is {@code null}.
   * @throws IllegalArgumentException if {@code characters} is empty or if {@code size} is outside
   *                                  the acceptable range.
   * @since 0.2.0
   */
  public String create(
    final Random random,
    final byte[] characters,
    final int mask,
    final int step,
    final int size
  ) {
    final int length = characters.length;
    // Validate inputs that are used directly in this method's logic
    if (0 == length) {
      throw new IllegalArgumentException("Character array cannot be empty.");
    }
    if (0 >= size) {
      throw new IllegalArgumentException("Size must be positive.");
    }
    // Result array to hold the generated identifier bytes
    final byte[] result = new byte[size];
    int j = 0; // Counter for filled characters in the result array
    int i; // Counter for bytes consumed from the current pool batch
    // Main generation loop: continue until the result array is full
    while (j < size) {
      // Get a batch of random bytes from the pool manager
      final byte[] bytes = this.poolManager.getRandomBytes(step, random);
      i = 0;
      // Process bytes from the current batch
      while (step > i && size > j) {
        // Apply the mask to get a value within a potential range
        final int k = bytes[i] & mask;
        ++i; // Move to the next byte in the batch
        // Check if the masked value is a valid index into the character array
        if (length > k) {
          // Valid index: use the character and add it to the result
          result[j] = characters[k];
          ++j; // Move to the next position in the result array
        }
        // If k >= length, the byte is discarded, and we proceed to the next one.
        // This ensures uniform distribution across the alphabet.
      }
    }
    // Convert the resulting byte array to a String using US-ASCII encoding
    return new String(result, 0, size, StandardCharsets.US_ASCII);
  }

  /**
   * Generates a random string using a pre-converted byte array alphabet.
   * <p>
   * This method is a convenience overload that calculates the mask and step size and delegates to
   * the core generation logic. It uses the {@link Random} instance provided by the configured
   * {@link #randomProvider}.
   * </p>
   *
   * @param characters The characters (as a {@code byte[]}) to choose from. Must not be {@code null}
   *                   or empty.
   * @param size       The desired size of the generated string. Must be within the acceptable
   *                   range.
   * @return The generated random string.
   * @throws NullPointerException     if {@code characters} is {@code null}.
   * @throws IllegalArgumentException if {@code characters} is empty or if {@code size} is outside
   *                                  the acceptable range.
   * @since 0.2.0
   */
  public String create(
    final byte[] characters,
    final int size
  ) {
    final int mask = NanoIdHelper.calculateMask(characters.length);
    return this.create(characters, mask, size);
  }

  /**
   * Generates a random string using a pre-converted byte array alphabet and a pre-calculated mask.
   * <p>
   * This method is a convenience overload that calculates the step size and delegates to the core
   * generation logic. It uses the {@link Random} instance provided by the configured
   * {@link #randomProvider}.
   * </p>
   *
   * @param characters The characters (as a {@code byte[]}) to choose from. Must not be {@code null}
   *                   or empty.
   * @param mask       The pre-calculated mask for the alphabet.
   * @param size       The desired size of the generated string. Must be within the acceptable
   *                   range.
   * @return The generated random string.
   * @throws NullPointerException     if {@code characters} is {@code null}.
   * @throws IllegalArgumentException if {@code characters} is empty or if {@code size} is outside
   *                                  the acceptable range.
   * @since 0.2.0
   */
  public String create(
    final byte[] characters,
    final int mask,
    final int size
  ) {
    final int step = NanoIdHelper.calculateStep(mask, size, characters.length);
    return this.create(characters, mask, step, size);
  }

  /**
   * The core method implementing the NanoID generation algorithm, using the default random
   * provider.
   * <p>
   * This method performs the main loop of the NanoID algorithm using the {@link Random} instance
   * obtained from {@link #randomProvider}.
   * </p>
   *
   * @param characters The characters (as a {@code byte[]}) to choose from. Must not be {@code null}
   *                   or empty.
   * @param mask       The mask to apply to random bytes.
   * @param step       The number of random bytes to request from the pool in each iteration.
   * @param size       The desired size of the generated string. Must be within the acceptable
   *                   range.
   * @return The generated random string.
   * @throws NullPointerException     if {@code characters} is {@code null}.
   * @throws IllegalArgumentException if {@code characters} is empty or if {@code size} is outside
   *                                  the acceptable range.
   * @since 0.2.0
   */
  public String create(
    final byte[] characters,
    final int mask,
    final int step,
    final int size
  ) {
    return this.create(this.randomProvider.get(), characters, mask, step, size);
  }
}