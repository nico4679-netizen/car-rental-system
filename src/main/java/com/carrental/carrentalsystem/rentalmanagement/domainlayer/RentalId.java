package com.carrental.carrentalsystem.rentalmanagement.domainlayer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Business identifier of a rental. */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentalId {
    @Column(name = "rental_id", nullable = false, unique = true)
    private UUID value = UUID.randomUUID();
}
