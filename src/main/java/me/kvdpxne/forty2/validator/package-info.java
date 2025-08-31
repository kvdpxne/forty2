/**
 * Provides input validation utilities for the NanoID generator.
 * <p>
 * This package contains classes responsible for validating the parameters used in the NanoID
 * generation process, such as the {@link java.util.Random} source, the character alphabet, and the
 * desired identifier size. These validators ensure that the inputs conform to the assumptions and
 * requirements of the NanoID algorithm, preventing errors and ensuring correct behavior.
 * </p>
 * <p>
 * The primary validator, {@link me.kvdpxne.forty2.validator.AsciiValidator}, enforces constraints
 * specific to ASCII-based generation, including character set bounds and duplicate checks.
 * </p>
 *
 * @since 0.2.0
 */
package me.kvdpxne.forty2.validator;