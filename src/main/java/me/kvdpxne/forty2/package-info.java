/**
 * The main entry point for the NanoID library, providing a simple and efficient API for generating
 * unique, random identifiers.
 * <p>
 * This package contains the primary facade class {@link me.kvdpxne.forty2.NanoId}, which offers
 * static methods for generating identifiers using various predefined character sets
 * ({@link me.kvdpxne.forty2.config.CharacterDictionaries}) or custom ones. It leverages the core
 * generation logic from the {@link me.kvdpxne.forty2.generator} package and supports both fast
 * ({@link java.util.concurrent.ThreadLocalRandom}) and cryptographically secure
 * ({@link java.security.SecureRandom}) random number sources via the
 * {@link me.kvdpxne.forty2.random} package.
 * </p>
 * <p>
 * Example usage:
 * <blockquote><pre>
 * // Generate a 10-character NanoID using uppercase letters
 * String id2 = NanoId.uppercase(10);
 * // Generate a secure, 15-character NanoID using numbers
 * String id3 = NanoId.numbersSecure(15);
 * // Generate a custom NanoID
 * String id4 = NanoId.create("ABC123".toCharArray(), 8);
 * </pre></blockquote>
 * </p>
 *
 * @see <a href="https://github.com/ai/nanoid">NanoID</a>
 * @since 0.2.0
 */
package me.kvdpxne.forty2;