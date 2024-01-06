package com.ivbaranovskii.drones.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Cargo {

    @EmbeddedId
    private DroneMedicineKey id;

    @ManyToOne
    @JoinColumn(name = "drone_id", insertable = false, updatable = false)
    @ToString.Exclude
    private Drone drone;

    @ManyToOne
    @JoinColumn(name = "medicine_id", insertable = false, updatable = false)
    @ToString.Exclude
    private Medicine medicine;

    public Cargo(Drone drone, Medicine medicine) {
        this.id = new DroneMedicineKey(drone.getId(), medicine.getId());
        this.drone = drone;
        this.medicine = medicine;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Cargo cargo = (Cargo) o;
        return getId() != null && Objects.equals(getId(), cargo.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}
