package com.ionos.domains.contact.delete;

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
public class DeleteContactServiceBenchmark extends AbstractBenchmark {

    private static DeleteContactService deleteContactService;

    @Autowired
    public void setDeleteContactService(DeleteContactService deleteContactService) {
        DeleteContactServiceBenchmark.deleteContactService = requireNonNull(deleteContactService);
    }

    @Benchmark
    public void delete() {
        // given
        final var instanceId = UUID.randomUUID().toString();
        // when
        deleteContactService.delete(instanceId);
        // then
        Assertions.assertThat(deleteContactService.getCurrentState(instanceId)).isSameAs(DeleteContactState.END);
    }

}