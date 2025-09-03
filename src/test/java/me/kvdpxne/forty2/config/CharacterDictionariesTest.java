package me.kvdpxne.forty2.config;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link CharacterDictionaries}.
 */
class CharacterDictionariesTest {

  // Helper method to provide dictionary names and their corresponding byte arrays
  static Stream<Object[]> dictionaryProvider() {
    return Stream.of(
      new Object[]{"Uppercase", CharacterDictionaries.getUppercase()},
      new Object[]{"Lowercase", CharacterDictionaries.getLowercase()},
      new Object[]{"Numbers", CharacterDictionaries.getNumbers()},
      new Object[]{"Alphanumeric", CharacterDictionaries.getAlphanumeric()},
      new Object[]{"HexadecimalUppercase", CharacterDictionaries.getHexadecimalUppercase()},
      new Object[]{"HexadecimalLowercase", CharacterDictionaries.getHexadecimalLowercase()},
      new Object[]{"NoLookALikes", CharacterDictionaries.getNoLookALikes()},
      new Object[]{"NoLookALikesSafe", CharacterDictionaries.getNoLookALikesSafe()},
      new Object[]{"CookieSafe", CharacterDictionaries.getCookieSafe()},
      new Object[]{"CookieUnsafe", CharacterDictionaries.getCookieUnsafe()}
    );
  }

  @ParameterizedTest(name = "Dictionary {0} should not be null or empty")
  @MethodSource("dictionaryProvider")
  void testDictionariesNotNullOrEmpty(String name, byte[] dictionary) {
    assertNotNull(dictionary, "Dictionary " + name + " should not be null");
    assertTrue(dictionary.length > 0, "Dictionary " + name + " should not be empty");
  }

  @ParameterizedTest(name = "Dictionary {0} should contain only ASCII bytes")
  @MethodSource("dictionaryProvider")
  void testDictionariesContainOnlyAscii(String name, byte[] dictionary) {
    for (byte b : dictionary) {
      int unsignedByte = b & 0xFF; // Convert to unsigned int for comparison
      assertTrue(unsignedByte >= 0 && unsignedByte <= 127,
        "Dictionary " + name + " contains non-ASCII byte: " + unsignedByte);
    }
  }

  @ParameterizedTest(name = "Dictionary {0} should have no duplicates")
  @MethodSource("dictionaryProvider")
  void testDictionariesHaveNoDuplicates(String name, byte[] dictionary) {
    Set<Byte> seen = new HashSet<>();
    for (byte b : dictionary) {
      assertTrue(seen.add(b), "Dictionary " + name + " contains duplicate byte: " + (b & 0xFF));
    }
  }

  @ParameterizedTest(name = "Dictionary {0} should return a cloned array")
  @MethodSource("dictionaryProvider")
  void testDictionariesReturnClonedArray(String name, byte[] dictionary) {
    byte[] firstCall = dictionary; // Get from provider method
    byte[] secondCall = getDictionaryByName(name); // Get again
    assertNotSame(firstCall, secondCall, "Dictionary " + name + " should return a new clone each time");
    assertArrayEquals(firstCall, secondCall, "Cloned arrays should be equal");
  }

  // Helper to get dictionary by name for the clone test
  private byte[] getDictionaryByName(String name) {
    switch (name) {
      case "Uppercase":
        return CharacterDictionaries.getUppercase();
      case "Lowercase":
        return CharacterDictionaries.getLowercase();
      case "Numbers":
        return CharacterDictionaries.getNumbers();
      case "Alphanumeric":
        return CharacterDictionaries.getAlphanumeric();
      case "HexadecimalUppercase":
        return CharacterDictionaries.getHexadecimalUppercase();
      case "HexadecimalLowercase":
        return CharacterDictionaries.getHexadecimalLowercase();
      case "NoLookALikes":
        return CharacterDictionaries.getNoLookALikes();
      case "NoLookALikesSafe":
        return CharacterDictionaries.getNoLookALikesSafe();
      case "CookieSafe":
        return CharacterDictionaries.getCookieSafe();
      case "CookieUnsafe":
        return CharacterDictionaries.getCookieUnsafe();
      default:
        throw new IllegalArgumentException("Unknown dictionary name: " + name);
    }
  }
}