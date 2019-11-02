package com.ionos.domains.contact.update;

import com.ionos.domains.contact.benchmark.AbstractBenchmark;
import org.assertj.core.api.Assertions;
import org.openjdk.jmh.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

@SpringBootTest
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class UpdateContactServiceBenchmark extends AbstractBenchmark {

    private static UpdateContactService updateContactService;

    @Autowired
    public void setUpdateContactService(UpdateContactService updateContactService) {
        UpdateContactServiceBenchmark.updateContactService = requireNonNull(updateContactService);
    }

    @Benchmark
    public void update() {
        // given
        final var instanceId = UUID.randomUUID().toString();
        // when
        updateContactService.update(instanceId);
        // then
        Assertions.assertThat(updateContactService.getCurrentState(instanceId)).isSameAs(UpdateContactState.END);
    }

}