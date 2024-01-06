package com.ivbaranovskii.drones.json;

import com.ivbaranovskii.drones.dto.DroneDto;
import com.ivbaranovskii.drones.enums.DroneModel;
import com.ivbaranovskii.drones.enums.DroneState;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
public class DroneDtoJsonTest {

    @Autowired
    private JacksonTester<DroneDto> json;

    @Autowired
    private JacksonTester<DroneDto[]> jsonList;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private static DroneDto[] drones;

    @BeforeAll
    static void beforeAll() {
        drones = Arrays.array(
                new DroneDto(
                        10L,
                        "serial-number-10",
                        DroneModel.LIGHT_WEIGHT,
                        200,
                        10.1f,
                        DroneState.IDLE
                ),
                new DroneDto(
                        11L,
                        "serial-number-11",
                        DroneModel.CRUISER_WEIGHT,
                        400,
                        100f,
                        DroneState.LOADING
                ),
                new DroneDto(
                        12L,
                        "serial-number-12",
                        DroneModel.HEAVY_WEIGHT,
                        500,
                        5.8f,
                        DroneState.RETURNING
                )
        );
    }

    @Test
    void droneDtoSerializationTest() throws IOException {
        assertThat(json.write(drones[0])).isStrictlyEqualToJson("/json/drone/singleDrone.json");
    }

    @Test
    void droneDtoDeserializationTest() throws IOException {
        DroneDto droneDto = json.read("/json/drone/singleDrone.json").getObject();

        assertAll(
                () -> assertEquals(drones[0].id(), droneDto.id()),
                () -> assertEquals(drones[0].serialNumber(), droneDto.serialNumber()),
                () -> assertEquals(drones[0].model(), droneDto.model()),
                () -> assertEquals(drones[0].weightLimit(), droneDto.weightLimit()),
                () -> assertEquals(drones[0].battery(), droneDto.battery()),
                () -> assertEquals(drones[0].state(), droneDto.state())
        );
    }

    @Test
    void droneDtoListSerializationTest() throws IOException {
        assertThat(jsonList.write(drones)).isStrictlyEqualToJson("/json/drone/listOfDrones.json");
    }

    @Test
    void droneDtoListDeserializationTest() throws IOException {
        DroneDto[] dtos = jsonList.read("/json/drone/listOfDrones.json").getObject();
        assertArrayEquals(drones, dtos);
    }

    @Test
    void when_droneDto_correct_then_no_constraint_violations() {
        DroneDto droneDto = new DroneDto(
                1L,
                "Abc",
                DroneModel.LIGHT_WEIGHT,
                250,
                55.6f,
                DroneState.IDLE
        );
        Set<ConstraintViolation<DroneDto>> violations = validator.validate(droneDto);

        assertEquals(0, violations.size());
    }

    @Test
    void when_droneDto_incorrect_then_constraint_violations() {
        DroneDto droneDto = new DroneDto(
                1L,
                "",
                null,
                -10,
                101f,
                null
        );
        Set<ConstraintViolation<DroneDto>> violations = validator.validate(droneDto);

        assertEquals(5, violations.size());
    }
}
