package com.ivbaranovskii.drones.json;

import com.ivbaranovskii.drones.dto.MedicineDto;
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
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class MedicineDtoJsonTest {

    @Autowired
    private JacksonTester<MedicineDto[]> jsonList;

    private static MedicineDto[] medicines;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @BeforeAll
    static void beforeAll() {
        medicines = Arrays.array(
                new MedicineDto(
                        1L,
                        "a-0",
                        Double.valueOf(50),
                        "A_0",
                        "base64StringOfImageA"
                ),
                new MedicineDto(
                        2L,
                        "b-0",
                        Double.valueOf(20),
                        "B_0",
                        "base64StringOfImageB"
                )
        );
    }

    @Test
    void medicineDtoListSerializationTest() throws IOException {
        assertThat(jsonList.write(medicines)).isStrictlyEqualToJson("/json/medicine/listOfMedicineSerialization.json");
    }

    @Test
    void medicineDtoListDeserializationTest() throws IOException {
        String expected = """
                [
                  {
                    "id": 1,
                    "name": "a-0",
                    "weight": 50,
                    "code": "A_0",
                    "image": "base64StringOfImageA"
                  },
                  {
                    "id": 2,
                    "name": "b-0",
                    "weight": 20,
                    "code": "B_0",
                    "image": "base64StringOfImageB"
                  }
                ]
                """;
        assertArrayEquals(medicines, jsonList.parse(expected).getObject());
    }

    @Test
    void when_medicineDto_correct_then_no_constraint_violations() {
        MedicineDto medicineDto = new MedicineDto(
                1L,
                "Ab-C_D1",
                0.0,
                "A_B_09",
                "base64StringOfImageA"
        );
        Set<ConstraintViolation<MedicineDto>> violations = validator.validate(medicineDto);

        assertEquals(0, violations.size());
    }

    @Test
    void when_droneDto_incorrect_then_constraint_violations() {
        MedicineDto medicineDto = new MedicineDto(
                1L,
                "!!!",
                -1.0,
                "A-B-09",
                null
        );
        Set<ConstraintViolation<MedicineDto>> violations = validator.validate(medicineDto);

        assertEquals(4, violations.size());
    }
}
