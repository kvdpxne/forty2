package me.kvdpxne.forty2.jmh;

import java.util.concurrent.TimeUnit;
import me.kvdpxne.forty2.NanoId;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

@Fork(value = 1, warmups = 1)
@Threads(1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 4, time = 1)
@Measurement(iterations = 10, time = 3)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SingleThreadBenchmarks {

  @Param({"8", "16", "24", "32", "40", "48"})
  public int size;

  @Benchmark
  public String url_friendly_fast() {
    return NanoId.urlFriendly(this.size);
  }

  @Benchmark
  public String url_friendly_secure() {
    return NanoId.urlFriendlySecure(this.size);
  }

  @Benchmark
  public String wosher_url_friendly_default() {
    return com.soundicly.jnanoidenhanced.jnanoid.NanoIdUtils.randomNanoId(this.size);
  }

  @Benchmark
  public String viascom_url_friendly_default() {
    return io.viascom.nanoid.NanoId.generate(this.size);
  }
}
