package me.kvdpxne.forty2;

import java.util.Random;
import java.util.regex.Pattern;
import me.kvdpxne.forty2.config.CharacterDictionaries;
import me.kvdpxne.forty2.validator.AsciiValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link NanoId}.
 */
class NanoIdTest {

  private static final int DEFAULT_SIZE = NanoId.DEFAULT_SIZE;

  // --- Core Custom Creation Methods Tests ---
  @Test
  void testCreateWithRandomCharArray() {
    Random mockRandom = new Random();
    char[] chars = {'a', 'b', 'c'};
    int size = 10;
    String result = NanoId.create(mockRandom, chars, size);
    assertNotNull(result);
    assertEquals(size, result.length());
    assertTrue(result.matches("^[abc]+$")); // Only characters from the set
  }

  @Test
  void testCreateWithCharArray() {
    char[] chars = {'x', 'y', 'z'};
    int size = 15;
    String result = NanoId.create(chars, size);
    assertNotNull(result);
    assertEquals(size, result.length());
    assertTrue(result.matches("^[xyz]+$"));
  }

  @Test
  void testCreateWithCharArrayDefaultSize() {
    char[] chars = {'m', 'n'};
    String result = NanoId.create(chars); // Uses DEFAULT_SIZE
    assertNotNull(result);
    assertEquals(DEFAULT_SIZE, result.length());
    assertTrue(result.matches("^[mn]+$"));
  }

  // --- Convenience Methods Tests (Fast) ---
  @ParameterizedTest
  @CsvSource({
    "uppercase, A-Z",
    "lowercase, a-z",
    "numbers, 0-9"
  })
  void testSimpleConvenienceMethodsDefaultSize(String methodName, String expectedPattern) {
    String result = callSimpleMethod(methodName, new Class[0], new Object[0]);
    assertNotNull(result);
    assertEquals(DEFAULT_SIZE, result.length());
    assertTrue(result.matches("^[" + expectedPattern + "]+$"), "Result: " + result);
  }

  @ParameterizedTest
  @CsvSource({
    "uppercase, 10, A-Z",
    "lowercase, 5, a-z",
    "numbers, 20, 0-9"
  })
  void testSimpleConvenienceMethodsCustomSize(String methodName, int size, String expectedPattern) {
    String result = callSimpleMethod(methodName, new Class[]{int.class}, new Object[]{size});
    assertNotNull(result);
    assertEquals(size, result.length());
    assertTrue(result.matches("^[" + expectedPattern + "]+$"), "Result: " + result);
  }

  // --- Convenience Methods Tests (Secure) ---
  @ParameterizedTest
  @CsvSource({
    "uppercaseSecure, A-Z",
    "lowercaseSecure, a-z",
    "numbersSecure, 0-9"
  })
  void testSimpleSecureConvenienceMethodsDefaultSize(String methodName, String expectedPattern) {
    String result = callSimpleMethod(methodName, new Class[0], new Object[0]);
    assertNotNull(result);
    assertEquals(DEFAULT_SIZE, result.length());
    assertTrue(result.matches("^[" + expectedPattern + "]+$"), "Result: " + result);
  }

  @ParameterizedTest
  @CsvSource({
    "uppercaseSecure, 10, A-Z",
    "lowercaseSecure, 5, a-z",
    "numbersSecure, 20, 0-9"
  })
  void testSimpleSecureConvenienceMethodsCustomSize(String methodName, int size, String expectedPattern) {
    String result = callSimpleMethod(methodName, new Class[]{int.class}, new Object[]{size});
    assertNotNull(result);
    assertEquals(size, result.length());
    assertTrue(result.matches("^[" + expectedPattern + "]+$"), "Result: " + result);
  }

  // --- Specific Dictionary Tests ---
  @Test
  void testAlphanumeric() {
    String result = NanoId.alphanumeric();
    assertEquals(DEFAULT_SIZE, result.length());
    assertTrue(result.matches("^[a-zA-Z0-9]+$"));
  }

  @Test
  void testAlphanumericCustomSize() {
    int size = 30;
    String result = NanoId.alphanumeric(size);
    assertEquals(size, result.length());
    assertTrue(result.matches("^[a-zA-Z0-9]+$"));
  }

  @Test
  void testHexLowercase() {
    String result = NanoId.hexLowercase();
    assertEquals(DEFAULT_SIZE, result.length());
    assertTrue(result.matches("^[a-f0-9]+$"));
  }

  @Test
  void testHexUppercase() {
    String result = NanoId.hexUppercase();
    assertEquals(DEFAULT_SIZE, result.length());
    assertTrue(result.matches("^[A-F0-9]+$"));
  }

  @Test
  void testNoLookALikes() {
    String result = NanoId.noLookALikes();
    assertEquals(DEFAULT_SIZE, result.length());
    // Check it doesn't contain excluded chars (simplified check)
    assertFalse(result.contains("0"));
    assertFalse(result.contains("O"));
    assertFalse(result.contains("I"));
    assertFalse(result.contains("l"));
    assertFalse(result.contains("1"));
  }

  @Test
  void testCookieSafe() {
    String result = NanoId.cookieSafe();
    assertEquals(DEFAULT_SIZE, result.length());
    byte[] cookieSafeChars = CharacterDictionaries.getCookieSafe();
    String allowedChars = new String(cookieSafeChars, java.nio.charset.StandardCharsets.US_ASCII);
    String regex = "^[" + Pattern.quote(allowedChars) + "]+$";
    assertTrue(result.matches(regex), "Result '" + result + "' should match pattern " + regex);
  }

  // --- URL Friendly Tests ---
  @Test
  void testUrlFriendly() {
    String result = NanoId.urlFriendly();
    assertEquals(DEFAULT_SIZE, result.length());
    byte[] urlFriendlyChars = CharacterDictionaries.getUrlFriendly();
    String allowedChars = new String(urlFriendlyChars, java.nio.charset.StandardCharsets.US_ASCII);
    String regex = "^[" + Pattern.quote(allowedChars) + "]+$";
    assertTrue(result.matches(regex), "Result '" + result + "' should match pattern " + regex);
    // Specific check for hyphen and underscore
    assertTrue(result.contains("-") || result.contains("_") || result.matches("^[a-zA-Z0-9]+$"),
      "Result should only contain URL-friendly chars");
  }

  @Test
  void testUrlFriendlyCustomSize() {
    int size = 12;
    String result = NanoId.urlFriendly(size);
    assertEquals(size, result.length());
    byte[] urlFriendlyChars = CharacterDictionaries.getUrlFriendly();
    String allowedChars = new String(urlFriendlyChars, java.nio.charset.StandardCharsets.US_ASCII);
    String regex = "^[" + Pattern.quote(allowedChars) + "]+$";
    assertTrue(result.matches(regex));
  }

  @Test
  void testUrlFriendlySecure() {
    String result = NanoId.urlFriendlySecure();
    assertEquals(DEFAULT_SIZE, result.length());
    byte[] urlFriendlyChars = CharacterDictionaries.getUrlFriendly();
    String allowedChars = new String(urlFriendlyChars, java.nio.charset.StandardCharsets.US_ASCII);
    String regex = "^[" + Pattern.quote(allowedChars) + "]+$";
    assertTrue(result.matches(regex));
  }

  // --- Secure Convenience Methods Tests ---
  @Test
  void testCreateSecureWithRandomCharArray() {
    Random mockRandom = new Random();
    char[] chars = {'s', 'e', 'c'};
    int size = 8;
    String result = NanoId.createSecure(mockRandom, chars, size);
    assertNotNull(result);
    assertEquals(size, result.length());
    assertTrue(result.matches("^[sec]+$"));
  }

  @Test
  void testCreateSecureWithCharArray() {
    char[] chars = {'S', 'E', 'C'};
    int size = 12;
    String result = NanoId.createSecure(chars, size);
    assertNotNull(result);
    assertEquals(size, result.length());
    assertTrue(result.matches("^[SEC]+$"));
  }

  @Test
  void testCreateSecureWithCharArrayDefaultSize() {
    char[] chars = {'D', 'E', 'F'};
    String result = NanoId.createSecure(chars); // Uses DEFAULT_SIZE
    assertNotNull(result);
    assertEquals(DEFAULT_SIZE, result.length());
    assertTrue(result.matches("^[DEF]+$"));
  }


  // --- Error Handling Tests ---
  @ParameterizedTest
  @ValueSource(ints = {1, AsciiValidator.MAX_ACCEPTABLE_SIZE + 1})
  // Invalid sizes
  void testCreateMethodsThrowOnInvalidSize(int invalidSize) {
    char[] chars = {'a', 'b'};
    Random mockRandom = new Random();
    assertThrows(IllegalArgumentException.class, () -> NanoId.create(mockRandom, chars, invalidSize));
    assertThrows(IllegalArgumentException.class, () -> NanoId.create(chars, invalidSize));
    assertThrows(IllegalArgumentException.class, () -> NanoId.createSecure(mockRandom, chars, invalidSize));
    assertThrows(IllegalArgumentException.class, () -> NanoId.createSecure(chars, invalidSize));
  }

  @Test
  void testCreateMethodsThrowOnNullInputs() {
    char[] chars = {'a', 'b'};
    Random mockRandom = new Random();
    assertThrows(NullPointerException.class, () -> NanoId.create(null, chars, 10));
    assertThrows(NullPointerException.class, () -> NanoId.create(null, 10));
    assertThrows(NullPointerException.class, () -> NanoId.create(mockRandom, null, 10));

    // Secure versions
    assertThrows(NullPointerException.class, () -> NanoId.createSecure(null, chars, 10));
    assertThrows(NullPointerException.class, () -> NanoId.createSecure(null, 10));
    assertThrows(NullPointerException.class, () -> NanoId.createSecure(mockRandom, null, 10));
  }

  // --- Helper method to call methods via reflection for parameterized tests ---
  private String callSimpleMethod(String methodName, Class<?>[] paramTypes, Object[] args) {
    try {
      return (String) NanoId.class.getMethod(methodName, paramTypes).invoke(null, args);
    } catch (Exception e) {
      throw new RuntimeException("Failed to invoke method " + methodName, e);
    }
  }
}