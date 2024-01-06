package com.ivbaranovskii.drones.scheduler;

import com.ivbaranovskii.drones.entity.Drone;
import com.ivbaranovskii.drones.repository.DroneRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ScheduledTask {

    private final Logger LOGGER = LoggerFactory.getLogger(ScheduledTask.class);

    private final DroneRepository droneRepository;
    private final MeterRegistry meterRegistry;

    @Scheduled(fixedDelayString = "${battery-level.task.delay}")
    public void checkBatteryLevels() {
        LOGGER.info("Started battery check task");

        List<Drone> drones = droneRepository.findAll();
        Map<Long, Float> batteryChargeMap = drones
                .stream()
                .collect(Collectors.toMap(Drone::getId, Drone::getBattery));

        batteryChargeMap.forEach((droneId, batteryCharge) ->
                Gauge.builder("drone.battery.charge", () -> batteryCharge)
                        .tag("droneId", String.valueOf(droneId))
                        .register(meterRegistry)
        );

        LOGGER.info("Battery check task result {}", batteryChargeMap);
        LOGGER.info("Ended battery check task");
    }
}
