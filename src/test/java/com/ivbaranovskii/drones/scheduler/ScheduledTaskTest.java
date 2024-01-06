package com.ivbaranovskii.drones.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ScheduledTaskTest {

    @SpyBean
    ScheduledTask task;

    @Test
    public void givenSleepBy100ms_whenGetInvocationCount_thenIsGreaterThanZero() {
        await()
                .atMost(1, TimeUnit.SECONDS)
                .untilAsserted(
                        () -> verify(task, atLeast(1)).checkBatteryLevels()
                );
    }
}
