package com.ivbaranovskii.drones.mapper;

import com.ivbaranovskii.drones.dto.DroneDto;
import com.ivbaranovskii.drones.dto.MedicineDto;
import com.ivbaranovskii.drones.entity.Drone;
import com.ivbaranovskii.drones.entity.Medicine;
import org.mapstruct.Mapper;

import java.util.Base64;

@Mapper(componentModel = "spring")
public interface DroneMapper {
    DroneDto toDto(Drone drone);

    Drone toEntity(DroneDto droneDto);

    MedicineDto toDto(Medicine medicine);

    Medicine toEntity(MedicineDto medicineDto);

    default byte[] getBytes(String string) {
        return string != null ? Base64.getDecoder().decode(string) : null;
    }

    default String getString(byte[] bytes) {
        return bytes != null ? Base64.getEncoder().encodeToString(bytes) : null;
    }
}
