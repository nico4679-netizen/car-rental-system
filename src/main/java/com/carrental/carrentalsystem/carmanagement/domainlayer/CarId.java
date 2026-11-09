package com.carrental.carrentalsystem.carmanagement.domainlayer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Business identifier of a car (the numeric id stays the database primary key). */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarId {
    @Column(name = "car_id", nullable = false, unique = true)
    private UUID value = UUID.randomUUID();
}
