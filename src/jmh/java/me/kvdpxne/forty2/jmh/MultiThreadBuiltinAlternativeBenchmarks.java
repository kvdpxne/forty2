package me.kvdpxne.forty2.jmh;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

@Fork(value = 1, warmups = 1)
@Threads(-1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 4, time = 1)
@Measurement(iterations = 10, time = 3)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class MultiThreadBuiltinAlternativeBenchmarks {

  @Benchmark
  public UUID uuid() {
    return UUID.randomUUID();
  }

  @Benchmark
  public String uuid_toString() {
    return UUID.randomUUID().toString();
  }

  @Benchmark
  public void nothing() {
  }
}