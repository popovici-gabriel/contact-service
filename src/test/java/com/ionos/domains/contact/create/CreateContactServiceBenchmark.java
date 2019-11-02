package com.ionos.domains.contact.create;

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
public class CreateContactServiceBenchmark extends AbstractBenchmark {

    private static CreateContactService createContactService;

    @Autowired
    public void setCreateContactService(CreateContactService createContactService) {
        CreateContactServiceBenchmark.createContactService = requireNonNull(createContactService);
    }

    @Benchmark
    public void create() {
        // given
        final var instanceId = UUID.randomUUID().toString();
        // when
        createContactService.create(instanceId);
        // then
        Assertions.assertThat(createContactService.getCurrentState(instanceId)).isSameAs(CreateContactState.END);
    }
}