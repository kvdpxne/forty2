package me.kvdpxne.forty2;

public final class AsciiAlphanumeric {

  /**
   * Default set of alphanumeric characters used for generating random
   * identifiers.
   */
  public static final byte[] DEFAULT_ALPHANUMERIC_CHARACTERS = {
    0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x41, 0x42,
    0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4a, 0x4b, 0x4c, 0x4d, 0x4e,
    0x4f, 0x50, 0x51, 0x52, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59, 0x5a,
    0x61, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6a, 0x6b, 0x6c,
    0x6d, 0x6e, 0x6f, 0x70, 0x71, 0x72, 0x73, 0x74, 0x75, 0x76, 0x77, 0x78,
    0x79, 0x7a
  };

  /**
   * Mask for filtering random values when generating alphanumeric strings.
   */
  public static final int DEFAULT_ALPHANUMERIC_MASK = 0x3f;

  /**
   * Step size for generating random bytes when using alphanumeric characters.
   */
  public static final int DEFAULT_ALPHANUMERIC_STEP = 0x17;

  /**
   * Private constructor to prevent instantiation.
   * @deprecated This constructor is deprecated and throws an error if called.
   */
  @Deprecated
  private AsciiAlphanumeric() {
    throw new InstatiationError(this.getClass());
  }
}
