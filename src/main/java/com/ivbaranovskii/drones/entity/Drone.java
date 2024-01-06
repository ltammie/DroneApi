package com.ivbaranovskii.drones.entity;

import com.ivbaranovskii.drones.enums.DroneModel;
import com.ivbaranovskii.drones.enums.DroneState;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "SERIAL_NUMBER")
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "MODEL", columnDefinition = "enum")
    private DroneModel model;

    @Column(name = "WEIGHT_LIMIT")
    private Integer weightLimit;

    @Column(name = "BATTERY")
    private Float battery;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATE", columnDefinition = "enum")
    private DroneState state;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Drone drone = (Drone) o;
        return getId() != null && Objects.equals(getId(), drone.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
