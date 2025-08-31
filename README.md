## About

**Forty2** is a high-performance Java implementation of the [Nano ID](https://github.com/ai/nanoid)
algorithm, designed for generating compact, unique identifiers efficiently, especially in
multithreaded environments.

It stands out for its **superior performance**, particularly in concurrent scenarios, as
demonstrated by benchmarks against other Java NanoID libraries (`jnanoid-enhanced`, `nanoid-kotlin`)
and `java.util.UUID`. This makes it an excellent choice for high-throughput server applications.

Key features and benefits include:

- 🚀 **High Performance & Multithreading**: Engineered for speed and efficiency, utilizing
  `ThreadLocalRandom` by default for fast, concurrent ID generation. A `SecureRandom` option is
  available for cryptographic needs.
- 🛡️ **Clear Security Options**: Explicitly provides both fast generation (`ThreadLocalRandom`) for
  general use and cryptographically secure generation `(SecureRandom`) for sensitive applications
  like API keys or tokens.
- 🔗 **Compact Identifiers**: Generates short, URL-friendly IDs (default 21 characters) compared
  to UUIDs (36 characters), saving space and improving readability.
- 🧩 **Highly Customizable**: Supports custom ID lengths (between 2 and 48 characters) and allows
  defining your own character sets (`byte[]` or `char[]`) for specific requirements.
- ✅ **Easy Integration**: Offers simple, static methods for common use cases (e.g.,
  `NanoId.urlFriendly()`, `NanoId.alphanumeric()`) alongside flexible methods for advanced
  customization (e.g., `NanoId.create(customAlphabet, customLength)`,
  `NanoId.createSecure(customAlphabet, customLength)`).
- 📦 **Extensive Built-in Character Sets:** Includes all standard dictionaries from
  [nanoid-dictionary](https://github.com/CyberAP/nanoid-dictionary) (e.g., `urlFriendly`,
  `alphanumeric`, `hex`, `noLookALikes`, `cookieSafe`) for immediate use.

The library provides simple, built-in methods for common use cases, making integration quick and
straightforward while still providing the power to customize when needed.

### 📝 Note

While benchmarks clearly demonstrate that this library significantly outperforms `java.util.UUID` in
terms of generation speed, it is **not recommended** as a direct replacement for standard Java UUIDs
in cases where strict compliance with the UUID specification (RFC 4122) is required. This library
generates NanoID-style identifiers, which have a different format and characteristics compared to
UUIDs.

If you are looking for a more efficient, general-purpose unique identifier (but not necessarily a
UUID), this library is an excellent choice. However, for scenarios specifically requiring UUIDs,
please consider using the native `java.util.UUID` or other UUID-specific libraries.

Additionally, if you are seeking a more efficient alternative to `java.util.UUID` that offers
non-String representations (e.g., `long`, `UUID` object) for better storage or performance
characteristics, the [TSID Creator](https://github.com/f4b6a3/tsid-creator) library is highly
recommended.

## Installation

You can easily integrate `forty2` into your Java project using [JitPack](https://jitpack.io/).

### Using Gradle

1. Add the JitPack repository to your `build.gradle` file:
   ```groovy
   repositories {
      maven {
        url = "https://jitpack.io"
        content {
          includeGroup("com.github.kvdpxne")
        }
      }
   }
   ```
2. Add the `forty2` dependency to your `dependencies` section:
   ```groovy
   dependencies {
     implementation("com.github.kvdpxne:forty2:<VERSION>")
   }
   ```

Replace `<VERSION>` with the specific version of `forty2` you wish to use (e.g., `v0.2.0`).

### Using Maven

1. Add the JitPack repository to your `pom.xml` file:
   ```xml
   <repositories>
     <repository>
       <id>jitpack.io</id>
       <url>https://jitpack.io</url>
     </repository>
   </repositories>
   ```
2. Add the `forty2` dependency to your `dependencies` section::
   ```xml
   <dependencies>
     <dependency>
       <groupId>com.github.kvdpxne</groupId>
       <artifactId>forty2</artifactId>
       <version>VERSION</version> <!-- Replace VERSION with the desired release tag -->
     </dependency>
   </dependencies>
   ```

After adding the repository and dependency, your build tool will fetch `forty2` from JitPack. For
more information on JitPack, see the [JitPack documentation](https://docs.jitpack.io/).

## Usage

Generating identifiers with `forty2` is designed to be both simple for common cases and powerful for
advanced customization. The library primarily exposes its functionality through the static methods
of the `NanoId` class.

### Quick Start: Common Use Cases

For typical scenarios, `forty2` provides convenient static methods that use predefined, safe
character sets with a default length (21 characters).

```java
import me.kvdpxne.forty2.NanoId;

public class Example {

  public static void main(String[] args) {
    // Generate a default-length, URL-friendly NanoID (e.g., "V1StGXR8_Z5jdHi6B-myT")
    String id1 = NanoId.urlFriendly();

    // Generate a default-length, alphanumeric ID (e.g., "a1B2c3D4e5F6g7H8i9J0")
    String id2 = NanoId.alphanumeric();

    // Generate a default-length, hexadecimal ID (lowercase) (e.g., "a1b2c3d4e5f67890")
    String id3 = NanoId.hexLowercase();

    // Generate a default-length, "no look-a-likes" ID (e.g., "6789BCDFGHJKMPQRTWbcdfghjkmnpqrtwz")
    String id4 = NanoId.noLookALikes();
  }
}
```

### Custom Length

All the built-in convenience methods have overloads that accept a custom `size` parameter, allowing
you to tailor the ID length to your needs (between 2 and 48 characters).

```java
// Generate a 10-character URL-friendly ID
String shortId = NanoId.urlFriendly(10);

// Generate a 32-character hexadecimal ID
String longHexId = NanoId.hexLowercase(32);
```

### Cryptographically Secure Generation

For use cases requiring cryptographically strong randomness (e.g., security tokens, API keys), use
the `*Secure` variants. These methods utilize `java.security.SecureRandom` instead of the default
fast generator.

```java
// Generate a default-length, cryptographically secure URL-friendly ID
String secureId = NanoId.urlFriendlySecure();

// Generate a 16-character, cryptographically secure alphanumeric ID
String secureAlphaId = NanoId.alphanumericSecure(16);

// Generate a secure ID using the 'no look-a-likes' set
String secureSafeId = NanoId.noLookALikesSecure(12);
```

### Advanced Usage: Full Customization

For complete control over the ID generation process, use the core `create` methods. This allows you
to define your own character set and provide a specific `java.util.Random` instance.

1. Custom Alphabet and Size:
   You can define your own character set. The library accepts `char[]` or `byte[]` (assuming ASCII)
   for the alphabet.

   ```java
   // Define a custom character set
   char[] mySymbols = {'!', '@', '#', '$', '%', '^', '&', '*'};
    
   // Generate a 12-character ID using the custom alphabet with fast randomness (default)
   String customIdFast = NanoId.create(mySymbols, 12);
    
   // Generate a 12-character ID using the custom alphabet with secure randomness
   String customIdSecure = NanoId.createSecure(mySymbols, 12);
   ```

2. Providing Your Own Random Instance:
   If you need specific control over the random number generator (e.g., using a seeded `Random` for
   testing, or a specific `SecureRandom` instance), you can pass it directly.

   ```java
   import java.util.Random;
   import java.security.SecureRandom;
  
   // Example: Using a standard Random (fast, but not cryptographically secure)
   Random myFastRandom = new Random();
   char[] alphabet = {'a', 'b', 'c', 'd', 'e'};
  
   String idWithMyFastRandom = NanoId.create(myFastRandom, alphabet, 10);
  
   // Example: Using a specific SecureRandom instance
   SecureRandom mySecureRandom = new SecureRandom(); // Or one initialized with a specific seed
   String idWithMySecureRandom = NanoId.create(mySecureRandom, alphabet, 10);
  
   // Example: Using a seeded Random for reproducible results (use SecureRandom for seeding if 
   // security is needed)
   Random seededRandom = new Random(12345L); // Not suitable for security tokens!
   String reproducibleId = NanoId.create(seededRandom, NanoId.getUrlFriendlyAlphabet(), 8);
   // reproducibleId will be the same sequence every time the program runs with this seed.
   ```

This structured approach allows you to easily choose the right method for your specific
requirements, balancing simplicity, performance, security, and customization.

## Benchmark

Performance comparison of identifier generation methods (higher `ops/ms` is better). Benchmarks were
conducted using JMH (Java Microbenchmark Harness) on the following system:

- **CPU**: AMD Ryzen 5 2600
- **RAM**: 16 GB
- **OS**: Windows 10 22H2
- **JVM**: Java 21

### Settings:

- `@Fork`: 1 (1 warmup fork)
- `@Warmup`: 4 iterations, 1 second each
- `@Measurement`: 10 iterations, 3 seconds each
- `@BenchmarkMode`: Throughput (`Mode.Throughput`)
- `@OutputTimeUnit`: Milliseconds (`TimeUnit.MILLISECONDS`)
- `@Param`: `size = {8, 16, 24, 32, 40, 48}` (for relevant benchmarks)

> The benchmark code is located in the `src/jmh/java/me/kvdpxne/forty2/jmh` directory of this
> repository.

### Summary Comparison (Key Sizes)

#### Multi-Thread Performance (`@Threads(-1)` - Max CPU threads)

| **BENCHMARK**                  | **LIBRARY**                | **SCORE (OPS/MS)** | **ERROR (± OPS/MS)** |
|--------------------------------|----------------------------|--------------------|----------------------|
| `url_friendly_fast`            | forty2 (fast)              | 46955.882*         | 1401.685*            |
| `url_friendly_secure`          | forty2 (secure)            | 4847.428*          | 17.617*              |
| `wosher_url_friendly_default`  | jnanoid-enhanced (default) | 1105.709*          | 4.704*               |
| `viascom_url_friendly_default` | nanoid-kotlin (default)    | 744.845*           | 6.509*               |
| `uuid`                         | `java.util.UUID` (object)  | 1514.653           | 12.569               |
| `uuid_toString`                | `java.util.UUID`           | 1480.313           | 18.003               |

> *Value for `size=24` (highlighting the comparison with other NanoID libraries). Full results for
> sizes 8, 16, 24, 32, 40, 48 shown in the detailed table below.

#### Single-Thread Performance (`@Threads(1)`)

| **BENCHMARK**                  | **LIBRARY**                | **SCORE (OPS/MS)** | **ERROR (± OPS/MS)** |
|--------------------------------|----------------------------|--------------------|----------------------|
| `url_friendly_fast`            | forty2 (fast)              | 7613.188*          | 70.842*              |
| `url_friendly_secure`          | forty2 (secure)            | 4157.029*          | 14.685*              |
| `wosher_url_friendly_default`  | jnanoid-enhanced (default) | 1422.072*          | 19.864*              |
| `viascom_url_friendly_default` | nanoid-kotlin (default)    | 427.013*           | 2.835*               |
| `uuid`                         | `java.util.UUID` (object)  | 2216.578           | 15.314               |
| `uuid_toString`                | `java.util.UUID`           | 2102.173           | 15.622               |

> *Value for `size=24` (highlighting the comparison with other NanoID libraries). Full results for
> sizes 8, 16, 24, 32, 40, 48 shown in the detailed table below.

#### Detailed Benchmark Results (All Sizes)

```text
Benchmark                                               (size)   Mode  Cnt         Score        Error   Units
MultiThreadBenchmarks.url_friendly_fast                      8  thrpt   10     62578.968 ±   1592.786  ops/ms
MultiThreadBenchmarks.url_friendly_fast                     16  thrpt   10     54393.984 ±   1294.885  ops/ms
MultiThreadBenchmarks.url_friendly_fast                     24  thrpt   10     46955.882 ±   1401.685  ops/ms
MultiThreadBenchmarks.url_friendly_fast                     32  thrpt   10     41579.864 ±    745.690  ops/ms
MultiThreadBenchmarks.url_friendly_fast                     40  thrpt   10     29372.719 ±    136.443  ops/ms
MultiThreadBenchmarks.url_friendly_fast                     48  thrpt   10     28063.916 ±     87.599  ops/ms
MultiThreadBenchmarks.url_friendly_secure                    8  thrpt   10     15525.578 ±    124.975  ops/ms
MultiThreadBenchmarks.url_friendly_secure                   16  thrpt   10      8211.777 ±    131.254  ops/ms
MultiThreadBenchmarks.url_friendly_secure                   24  thrpt   10      4847.428 ±     17.617  ops/ms
MultiThreadBenchmarks.url_friendly_secure                   32  thrpt   10      4079.344 ±     14.344  ops/ms
MultiThreadBenchmarks.url_friendly_secure                   40  thrpt   10      3432.501 ±     18.306  ops/ms
MultiThreadBenchmarks.url_friendly_secure                   48  thrpt   10      2969.304 ±     16.829  ops/ms
MultiThreadBenchmarks.viascom_url_friendly_default           8  thrpt   10       726.636 ±      1.030  ops/ms
MultiThreadBenchmarks.viascom_url_friendly_default          16  thrpt   10       752.200 ±      6.744  ops/ms
MultiThreadBenchmarks.viascom_url_friendly_default          24  thrpt   10       744.845 ±      6.509  ops/ms
MultiThreadBenchmarks.viascom_url_friendly_default          32  thrpt   10       681.492 ±      0.743  ops/ms
MultiThreadBenchmarks.viascom_url_friendly_default          40  thrpt   10       727.801 ±      1.769  ops/ms
MultiThreadBenchmarks.viascom_url_friendly_default          48  thrpt   10       719.226 ±      2.956  ops/ms
MultiThreadBenchmarks.wosher_url_friendly_default            8  thrpt   10      1347.109 ±     15.185  ops/ms
MultiThreadBenchmarks.wosher_url_friendly_default           16  thrpt   10      1411.585 ±     29.057  ops/ms
MultiThreadBenchmarks.wosher_url_friendly_default           24  thrpt   10      1105.709 ±      4.704  ops/ms
MultiThreadBenchmarks.wosher_url_friendly_default           32  thrpt   10      1110.769 ±      8.698  ops/ms
MultiThreadBenchmarks.wosher_url_friendly_default           40  thrpt   10      1093.577 ±     11.698  ops/ms
MultiThreadBenchmarks.wosher_url_friendly_default           48  thrpt   10       937.665 ±      3.876  ops/ms
MultiThreadBuiltinAlternativeBenchmarks.nothing            N/A  thrpt   10  14432900.106 ± 142325.905  ops/ms
MultiThreadBuiltinAlternativeBenchmarks.uuid               N/A  thrpt   10      1514.653 ±     12.569  ops/ms
MultiThreadBuiltinAlternativeBenchmarks.uuid_toString      N/A  thrpt   10      1480.313 ±     18.003  ops/ms
SingleThreadBenchmarks.url_friendly_fast                     8  thrpt   10     10736.756 ±    233.303  ops/ms
SingleThreadBenchmarks.url_friendly_fast                    16  thrpt   10      9975.788 ±     90.538  ops/ms
SingleThreadBenchmarks.url_friendly_fast                    24  thrpt   10      7613.188 ±     70.842  ops/ms
SingleThreadBenchmarks.url_friendly_fast                    32  thrpt   10      6894.765 ±    122.887  ops/ms
SingleThreadBenchmarks.url_friendly_fast                    40  thrpt   10      5836.908 ±    259.161  ops/ms
SingleThreadBenchmarks.url_friendly_fast                    48  thrpt   10      5174.604 ±    167.926  ops/ms
SingleThreadBenchmarks.url_friendly_secure                   8  thrpt   10      8255.269 ±    101.151  ops/ms
SingleThreadBenchmarks.url_friendly_secure                  16  thrpt   10      5757.289 ±     42.594  ops/ms
SingleThreadBenchmarks.url_friendly_secure                  24  thrpt   10      4157.029 ±     14.685  ops/ms
SingleThreadBenchmarks.url_friendly_secure                  32  thrpt   10      3654.940 ±     31.181  ops/ms
SingleThreadBenchmarks.url_friendly_secure                  40  thrpt   10      2700.359 ±     35.446  ops/ms
SingleThreadBenchmarks.url_friendly_secure                  48  thrpt   10      2421.522 ±     16.734  ops/ms
SingleThreadBenchmarks.viascom_url_friendly_default          8  thrpt   10       435.434 ±      5.569  ops/ms
SingleThreadBenchmarks.viascom_url_friendly_default         16  thrpt   10       411.775 ±      1.366  ops/ms
SingleThreadBenchmarks.viascom_url_friendly_default         24  thrpt   10       427.013 ±      2.835  ops/ms
SingleThreadBenchmarks.viascom_url_friendly_default         32  thrpt   10       346.879 ±      2.712  ops/ms
SingleThreadBenchmarks.viascom_url_friendly_default         40  thrpt   10       422.192 ±      2.104  ops/ms
SingleThreadBenchmarks.viascom_url_friendly_default         48  thrpt   10       395.614 ±      3.027  ops/ms
SingleThreadBenchmarks.wosher_url_friendly_default           8  thrpt   10      1991.867 ±     19.873  ops/ms
SingleThreadBenchmarks.wosher_url_friendly_default          16  thrpt   10      1810.880 ±     19.825  ops/ms
SingleThreadBenchmarks.wosher_url_friendly_default          24  thrpt   10      1422.072 ±     19.864  ops/ms
SingleThreadBenchmarks.wosher_url_friendly_default          32  thrpt   10      1344.590 ±     13.438  ops/ms
SingleThreadBenchmarks.wosher_url_friendly_default          40  thrpt   10      1310.616 ±      6.258  ops/ms
SingleThreadBenchmarks.wosher_url_friendly_default          48  thrpt   10      1065.654 ±      9.182  ops/ms
SingleThreadBuiltinAlternativeBenchmarks.nothing           N/A  thrpt   10   2508142.068 ±  27734.390  ops/ms
SingleThreadBuiltinAlternativeBenchmarks.uuid              N/A  thrpt   10      2216.578 ±     15.314  ops/ms
SingleThreadBuiltinAlternativeBenchmarks.uuid_toString     N/A  thrpt   10      2102.173 ±     15.622  ops/ms
```

These results clearly demonstrate that the `forty2` library's default `url_friendly` method
significantly outperforms other available NanoID implementations for Java (`jnanoid-enhanced`) and
Kotlin (`nanoid-kotlin`), especially in multithreaded scenarios. The cryptographically secure
version (`url_friendly_secure`) also maintains a substantial performance lead over the alternatives
while providing stronger randomness guarantees. The `nothing` benchmark serves as a baseline to
illustrate the overhead of the generation process itself.

## License

This project is licensed under the **MIT License**.

`forty2` is an independent implementation of the Nano ID algorithm. The original Nano ID algorithm
and its reference implementations are also licensed under the MIT License.

- **`forty2` License**: MIT License (see
  [LICENSE](https://github.com/kvdpxne/forty2/blob/master/LICENSE) file in this repository)
- **Nano ID Origin**: Based on the [Nano ID](https://github.com/ai/nanoid) algorithm by Andrey
  Sitnik, also licensed under the MIT License.

The MIT License is a permissive open-source license that allows for use, modification, and
distribution of the code, provided the original copyright notice and license text are included in
all copies or substantial portions of the software.