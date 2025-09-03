package me.kvdpxne.forty2.validator;

import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link AsciiValidator}.
 */
class AsciiValidatorTest {

  // --- validateRandom Tests ---
  @Test
  void testValidateRandomWithValidInstance() {
    Random validRandom = new Random();
    assertDoesNotThrow(() -> AsciiValidator.validateRandom(validRandom));
  }

  @Test
  void testValidateRandomWithNull() {
    NullPointerException exception = assertThrows(NullPointerException.class, () -> AsciiValidator.validateRandom(null));
    assertEquals("Random object instance cannot be null!", exception.getMessage());
  }

  // --- validateCharacters Tests ---
  @Test
  void testValidateCharactersWithValidArray() {
    char[] validChars = {'A', 'B', 'C', '1', '2', '3', 'a', 'b', 'c', '-', '_'};
    assertDoesNotThrow(() -> AsciiValidator.validateCharacters(validChars));
  }

  @Test
  void testValidateCharactersWithNull() {
    NullPointerException exception = assertThrows(NullPointerException.class, () -> AsciiValidator.validateCharacters(null));
    assertEquals("Character array cannot be null!", exception.getMessage());
  }

  @Test
  void testValidateCharactersWithEmptyArray() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> AsciiValidator.validateCharacters(new char[0]));
    assertEquals("Character array cannot be empty!", exception.getMessage());
  }

  @ParameterizedTest
  @ValueSource(chars = {0x20, 0x21, 0x7E, 0x7F})
    // 0x20 is space, 0x7F is DEL
  void testValidateCharactersWithBoundaryChars(char boundaryChar) {
    char[] chars = {'A', boundaryChar, 'Z'};
    if (boundaryChar == 0x20 || boundaryChar == 0x7F) {
      // 0x20 (space) and 0x7F (DEL) are outside the VALID range 0x21-0x7E
      assertThrows(IllegalArgumentException.class, () -> AsciiValidator.validateCharacters(chars));
    } else {
      assertDoesNotThrow(() -> AsciiValidator.validateCharacters(chars));
    }
  }

  @ParameterizedTest
  @ValueSource(chars = {0x00, 0x1F, 0x80, 0xFF, 0x100})
    // Non-printable or non-ASCII
  void testValidateCharactersWithInvalidChars(char invalidChar) {
    char[] chars = {'A', invalidChar, 'Z'};
    assertThrows(IllegalArgumentException.class, () -> AsciiValidator.validateCharacters(chars));
  }

  @Test
  void testValidateCharactersWithDuplicates() {
    char[] charsWithDup = {'A', 'B', 'A'}; // 'A' is duplicated
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> AsciiValidator.validateCharacters(charsWithDup));
    assertTrue(exception.getMessage().contains("Character array cannot have duplicates"));
    assertTrue(exception.getMessage().contains("Duplicate character: 'A'"));
  }

  // --- validateSize Tests ---
  @ParameterizedTest
  @ValueSource(ints = {AsciiValidator.MIN_ACCEPTABLE_SIZE, 25, AsciiValidator.MAX_ACCEPTABLE_SIZE})
  void testValidateSizeWithValidSizes(int validSize) {
    assertDoesNotThrow(() -> AsciiValidator.validateSize(validSize));
  }

  @ParameterizedTest
  @ValueSource(ints = {AsciiValidator.MIN_ACCEPTABLE_SIZE - 1, AsciiValidator.MAX_ACCEPTABLE_SIZE + 1, -1, 0, 1})
  void testValidateSizeWithInvalidSizes(int invalidSize) {
    assertThrows(IllegalArgumentException.class, () -> AsciiValidator.validateSize(invalidSize));
  }

  @Test
  void testValidateSizeErrorMessageForTooSmall() {
    int tooSmall = AsciiValidator.MIN_ACCEPTABLE_SIZE - 1;
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> AsciiValidator.validateSize(tooSmall));
    assertTrue(exception.getMessage().contains("cannot be less than " + AsciiValidator.MIN_ACCEPTABLE_SIZE));
    assertTrue(exception.getMessage().contains("Provided: " + tooSmall));
  }

  @Test
  void testValidateSizeErrorMessageForTooLarge() {
    int tooLarge = AsciiValidator.MAX_ACCEPTABLE_SIZE + 1;
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> AsciiValidator.validateSize(tooLarge));
    assertTrue(exception.getMessage().contains("cannot be greater than " + AsciiValidator.MAX_ACCEPTABLE_SIZE));
    assertTrue(exception.getMessage().contains("Provided: " + tooLarge));
  }
}