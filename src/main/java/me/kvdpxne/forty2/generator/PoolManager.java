package me.kvdpxne.forty2.generator;

import java.util.Random;

/**
 * Manages a thread-local pool of random bytes to optimize performance.
 * <p>
 * Generating random bytes can be a relatively expensive operation. This class mitigates that cost
 * by generating a larger block of random bytes ({@link #POOL_SIZE_MULTIPLIER} times the requested
 * amount) and storing them in a per-thread pool. Subsequent requests for random bytes are served
 * from this pool until it's exhausted, at which point a new block is generated.
 * </p>
 * <p>
 * This class is thread-safe because it uses {@link ThreadLocal} to ensure that each thread has its
 * own independent {@link PoolState} instance, preventing contention.
 * </p>
 *
 * @since 0.2.0
 */
public final class PoolManager {

  /**
   * Pool size multiplier for performance optimization.
   * <p>
   * This multiplier determines how many times the requested number of bytes are generated and
   * stored in the pool. A larger multiplier means fewer calls to {@link Random#nextBytes(byte[])},
   * but also means more bytes are held in memory per thread.
   * </p>
   *
   * @since 0.2.0
   */
  private static final int POOL_SIZE_MULTIPLIER = 128;

  /**
   * ThreadLocal random bytes pool for thread-safe performance optimization.
   * <p>
   * Each thread accessing this manager will have its own {@link PoolState} instance, ensuring that
   * pool operations are isolated and do not require synchronization.
   * </p>
   *
   * @since 0.2.0
   */
  private final ThreadLocal<PoolState> poolStateThreadLocal = ThreadLocal.withInitial(PoolState::new);

  /**
   * Private constructor to prevent external instantiation. Use of {@code public} modifier is
   * intentional for package-private access within the generator package.
   *
   * @since 0.2.0
   */
  PoolManager() {
    // Package-private constructor for internal use by NanoIdStringGenerator
  }

  /**
   * Fills the random bytes pool with new random values if necessary.
   * <p>
   * This method checks if the current pool has enough bytes to satisfy the request. If not, it
   * generates a new block of random bytes (sized by {@link #POOL_SIZE_MULTIPLIER}) and resets the
   * pool offset.
   * </p>
   *
   * @param bytes  The number of bytes needed for the current request.
   * @param random The {@link Random} instance to use for generating new bytes.
   * @param state  The {@link PoolState} for the current thread.
   * @since 0.2.0
   */
  private void fillPool(
    final int bytes,
    final Random random,
    final PoolState state
  ) {
    // Check if the pool needs to be initialized or resized
    if (null == state.pool || bytes > state.pool.length) {
      state.pool = new byte[bytes * POOL_SIZE_MULTIPLIER];
      random.nextBytes(state.pool);
      state.poolOffset = 0;
      // Check if there are not enough bytes left in the current pool
    } else if (state.poolOffset + bytes > state.pool.length) {
      random.nextBytes(state.pool);
      state.poolOffset = 0;
    }
    // Advance the offset to mark the bytes as "used" for the next request
    state.poolOffset += bytes;
  }

  /**
   * Gets a specific number of random bytes from the pool.
   * <p>
   * This method ensures that the requested number of bytes is available by calling
   * {@link #fillPool(int, Random, PoolState)}. It then copies the required bytes from the pool into
   * a new array and returns it.
   * </p>
   *
   * @param bytes  The number of random bytes to retrieve.
   * @param random The {@link Random} instance to use if the pool needs refilling.
   * @return A new {@code byte[]} array containing the requested random bytes.
   * @since 0.2.0
   */
  public byte[] getRandomBytes(
    final int bytes,
    final Random random
  ) {
    final PoolState state = this.poolStateThreadLocal.get();
    this.fillPool(bytes, random, state);
    final byte[] result = new byte[bytes];
    // Copy the bytes from the pool to the result array.
    // The pool offset has already been advanced by fillPool.
    System.arraycopy(state.pool, state.poolOffset - bytes, result, 0, bytes);
    return result;
  }

  /**
   * Pool state holder for each thread.
   * <p>
   * This private static class holds the state of the random byte pool for a single thread. It
   * contains the byte array pool and the current offset within that pool.
   * </p>
   *
   * @since 0.2.0
   */
  private static final class PoolState {

    /**
     * The byte array pool storing pre-generated random bytes. This field can be {@code null}
     * initially or if resized.
     *
     * @since 0.2.0
     */
    byte[] pool;

    /**
     * The current offset within the {@link #pool} array. This indicates the position of the next
     * byte to be used.
     *
     * @since 0.2.0
     */
    int poolOffset = 0;
  }
}