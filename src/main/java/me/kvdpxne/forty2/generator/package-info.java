/**
 * Provides the core implementation for generating unique identifiers based on the NanoID
 * algorithm.
 * <p>
 * This package contains the main generator class
 * {@link me.kvdpxne.forty2.generator.NanoIdStringGenerator}, which implements the NanoID string
 * generation logic. It utilizes components from other packages such as
 * {@link me.kvdpxne.forty2.random Random providers},
 * {@link me.kvdpxne.forty2.converter Character converters}, and
 * {@link me.kvdpxne.forty2.validator Input validators} to perform its function efficiently and
 * securely.
 * </p>
 * <p>
 * Performance is optimized through the use of a {@link me.kvdpxne.forty2.generator.PoolManager}
 * which buffers random bytes on a per-thread basis to reduce the frequency of calls to the
 * underlying {@link java.util.Random} source.
 * </p>
 * <p>
 * Helper classes like {@link me.kvdpxne.forty2.generator.NanoIdHelper} contain shared algorithmic
 * logic to ensure consistency and maintainability.
 * </p>
 *
 * @since 0.2.0
 */
package me.kvdpxne.forty2.generator;