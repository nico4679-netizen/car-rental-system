package com.carrental.carrentalsystem.carmanagement.domainlayer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Model year of a car ("year" is a reserved SQL word, hence car_year). */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModelYear {
    @Column(name = "car_year", nullable = false)
    private Integer value;
}
