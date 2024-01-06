package com.ivbaranovskii.drones.service;

import com.ivbaranovskii.drones.entity.Medicine;
import com.ivbaranovskii.drones.repository.MedicineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MedicineService {

    private final MedicineRepository medicineRepository;

    public MedicineService(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Transactional
    public Optional<Medicine> getMedicineByName(String name) {
        return medicineRepository.findByName(name);
    }

}
