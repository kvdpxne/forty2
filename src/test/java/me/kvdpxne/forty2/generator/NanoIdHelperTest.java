package me.kvdpxne.forty2.generator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link NanoIdHelper}.
 */
class NanoIdHelperTest {

  @ParameterizedTest
  @CsvSource({
    "1, 0",     // Edge case: single character
    "2, 1",     // 2 chars -> mask 1 (0b1)
    "3, 3",     // 3 chars -> mask 3 (0b11)
    "4, 3",     // 4 chars -> mask 3 (0b11)
    "5, 7",     // 5 chars -> mask 7 (0b111)
    "8, 7",     // 8 chars -> mask 7 (0b111)
    "16, 15",   // 16 chars -> mask 15 (0b1111)
    "32, 31",   // 32 chars -> mask 31 (0b11111)
    "62, 63",   // 62 chars -> mask 63 (0b111111) - typical NanoID alphabet size
    "64, 63",   // 64 chars -> mask 63 (0b111111)
  })
  void testCalculateMask(int alphabetLength, int expectedMask) {
    assertEquals(expectedMask, NanoIdHelper.calculateMask(alphabetLength));
  }

  @Test
  void testCalculateMaskWithZeroOrNegative() {
    assertThrows(IllegalArgumentException.class, () -> NanoIdHelper.calculateMask(0));
    assertThrows(IllegalArgumentException.class, () -> NanoIdHelper.calculateMask(-1));
  }

  @ParameterizedTest
  @CsvSource({
    "15, 21, 62, 9",  // mask=15, size=21, length=62 -> ceil(1.6 * 15 * 21 / 62) = ceil(8.516) = 9
    "63, 21, 64, 34", // mask=63, size=21, length=64 -> ceil(1.6 * 63 * 21 / 64) = ceil(34.725) = 35
    "3, 10, 4, 13",   // mask=3, size=10, length=4 -> ceil(1.6 * 3 * 10 / 4) = ceil(12..02) = 13
  })
  void testCalculateStep(int mask, int size, int alphabetLength, int expectedStep) {
    assertEquals(expectedStep, NanoIdHelper.calculateStep(mask, size, alphabetLength));
  }

  @Test
  void testCalculateStepWithZeroAlphabetLength() {
    assertThrows(IllegalArgumentException.class, () -> NanoIdHelper.calculateStep(15, 21, 0));
  }
}