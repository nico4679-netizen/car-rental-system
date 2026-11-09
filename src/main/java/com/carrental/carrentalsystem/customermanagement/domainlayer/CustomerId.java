package com.carrental.carrentalsystem.customermanagement.domainlayer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Business identifier of a customer. */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerId {
    @Column(name = "customer_id", nullable = false, unique = true)
    private UUID value = UUID.randomUUID();
}
