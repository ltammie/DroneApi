package com.ivbaranovskii.drones.controller;

import com.ivbaranovskii.drones.dto.MedicineDto;
import com.ivbaranovskii.drones.entity.Drone;
import com.ivbaranovskii.drones.enums.DroneModel;
import com.ivbaranovskii.drones.enums.DroneState;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class DroneControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void should_return_drone_if_exists() {
        ResponseEntity<String> response = restTemplate.getForEntity("/drones/10", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertNotNull(id);
    }

    @Test
    void should_not_return_drone_with_unknown_id() {
        ResponseEntity<String> response = restTemplate.getForEntity("/drones/999", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DirtiesContext
    void should_register_new_drone() {
        Drone newDrone = new Drone(
                55L,
                "serial-number-55",
                DroneModel.LIGHT_WEIGHT,
                250,
                100f,
                DroneState.IDLE
        );
        ResponseEntity<Void> response = restTemplate.postForEntity("/drones", newDrone, Void.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        URI newDroneLocation = response.getHeaders().getLocation();
        ResponseEntity<String> getResponse = restTemplate.getForEntity(newDroneLocation, String.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");
        assertNotNull(id);
    }

    @Test
    void should_return_drones_available_for_loading() {
        ResponseEntity<String> response = restTemplate.getForEntity("/drones/available", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int numberOfDrones = documentContext.read("$.content.length()");
        assertEquals(3, numberOfDrones);

        JSONArray ids = documentContext.read("$.content..id");
        assertThat(ids).containsExactlyInAnyOrder(20, 21, 22);
    }

    @Test
    void should_return_drone_battery_if_drone_exists() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/drones/12/battery", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Double batteryLevel = documentContext.read("$.battery");
        assertEquals(5.8, batteryLevel);
    }

    @Test
    void get_battery_for_nonexistent_drone_should_return_not_found() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/drones/999/battery", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void should_return_cargo_for_the_drone() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/drones/12/medicine", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int numberOfMedicines = documentContext.read("$.length()");
        assertEquals(2, numberOfMedicines);

        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).containsExactlyInAnyOrder(33, 34);

        JSONArray images = documentContext.read("$..image");
        assertEquals(2, images.size());
    }

    @Test
    void should_return_empty_response_if_drone_has_no_cargo() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/drones/21/medicine", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int numberOfMedicines = documentContext.read("$.length()");
        assertEquals(0, numberOfMedicines);
    }

    @Test
    void should_return_not_found_response_if_drone_for_cargo_does_not_exists() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/drones/999/medicine", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DirtiesContext
    void should_load_drone_with_cargo() {
        List<MedicineDto> medicines = List.of(
                new MedicineDto(
                        null,
                        "Test_med_1",
                        20.0,
                        "Test_med_1_code",
                        "base64StringOfImageA"
                ),
                new MedicineDto(
                        null,
                        "Test_med_2",
                        20.0,
                        "Test_med_2_code",
                        "base64StringOfImageB"
                )
        );

        ResponseEntity<Void> response = restTemplate.postForEntity("/drones/20/medicine", medicines, Void.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // check if drone state change to LOADING
        ResponseEntity<String> drone = restTemplate.getForEntity("/drones/20", String.class);
        DocumentContext droneContext = JsonPath.parse(drone.getBody());
        String state = droneContext.read("$.state");
        assertEquals(DroneState.LOADING.toString(), state);

        //check if medicine was saved correctly
        URI savedCargoLocation = response.getHeaders().getLocation();
        ResponseEntity<String> cargo = restTemplate.getForEntity(savedCargoLocation, String.class);
        assertEquals(HttpStatus.OK, cargo.getStatusCode());

        DocumentContext cargoContext = JsonPath.parse(cargo.getBody());
        int numberOfMedicines = cargoContext.read("$.length()");
        assertEquals(2, numberOfMedicines);

        // check if images were encoded back correctly
        String base64StringA = cargoContext.read("$.[0].image");
        assertEquals(medicines.getFirst().image(), base64StringA);

        String base64StringB = cargoContext.read("$.[1].image");
        assertEquals(medicines.getLast().image(), base64StringB);
    }

    @Test
    void should_return_not_found_when_loading_drone_that_does_not_exists() {
        List<MedicineDto> medicines = List.of(
                new MedicineDto(
                        null,
                        "Test_med_1",
                        20.0,
                        "Test_med_1_code",
                        "testImage1"
                )
        );

        ResponseEntity<String> response = restTemplate.postForEntity("/drones/999/cargo", medicines, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void should_return_bad_request_found_when_loading_drone_with_low_battery() {
        List<MedicineDto> medicines = List.of(
                new MedicineDto(
                        null,
                        "Test_med_1",
                        20.0,
                        "Test_med_1_code",
                        "testImage1"
                )
        );

        ResponseEntity<String> response = restTemplate.postForEntity("/drones/10/medicine", medicines, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void should_return_bad_request_found_when_loading_drone_with_empty_medicine_list() {
        List<MedicineDto> medicines = List.of();

        ResponseEntity<String> response = restTemplate.postForEntity("/drones/10/medicine", medicines, String.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }
}