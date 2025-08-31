/**
 * Provides utilities for converting between different character representations, optimized for the
 * requirements of NanoID generation (e.g., ASCII byte arrays).
 * <p>
 * This package contains classes that handle the efficient conversion of standard Java character
 * types (like {@code char[]}) to formats suitable for internal processing by the NanoID generator,
 * primarily {@code byte[]} assuming ASCII encoding. This avoids the overhead of more general string
 * encoding/decoding mechanisms.
 * </p>
 *
 * @since 0.2.0
 */
package me.kvdpxne.forty2.converter;