package com.ivbaranovskii.drones.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DroneMedicineKey implements Serializable {

    @Column(name = "drone_id")
    Long droneId;

    @Column(name = "medicine_id")
    Long medicineId;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        DroneMedicineKey that = (DroneMedicineKey) o;
        return getDroneId() != null && Objects.equals(getDroneId(), that.getDroneId())
                && getMedicineId() != null && Objects.equals(getMedicineId(), that.getMedicineId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(droneId, medicineId);
    }
}
