package me.kvdpxne.forty2.converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link CharacterArrayConverter}.
 */
class CharacterArrayConverterTest {

  @Test
  void testToBytesWithValidAsciiChars() {
    char[] input = {'A', 'B', 'C', '1', '2', '3', 'a', 'b', 'c', '-', '_'};
    byte[] expected = {0x41, 0x42, 0x43, 0x31, 0x32, 0x33, 0x61, 0x62, 0x63, 0x2D, 0x5F};
    byte[] result = CharacterArrayConverter.toBytes(input);
    assertArrayEquals(expected, result);
  }

  @Test
  void testToBytesWithEmptyArray() {
    char[] input = {};
    byte[] expected = {};
    byte[] result = CharacterArrayConverter.toBytes(input);
    assertArrayEquals(expected, result);
  }

  @ParameterizedTest
  @ValueSource(chars = {0x00, 0x1F, 0x7F, 0x80, 0xFF, 0x100})
    // Non-ASCII or edge cases
  void testToBytesThrowsOnNonAscii(char nonAsciiChar) {
    char[] input = {'A', nonAsciiChar, 'B'};
    assertThrows(IllegalArgumentException.class, () -> CharacterArrayConverter.toBytes(input));
  }

  @Test
  void testToBytesReturnsNewArray() {
    char[] input = {'A', 'B', 'C'};
    byte[] result1 = CharacterArrayConverter.toBytes(input);
    byte[] result2 = CharacterArrayConverter.toBytes(input);
    assertNotSame(result1, result2); // Should be different instances
    assertArrayEquals(result1, result2); // But contents should be equal
  }
}