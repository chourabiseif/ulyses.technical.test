package com.septeo.ulyses.technical.test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BestSellingVehicleDto {
    private Long vehicleId;
    private Long totalSales;
}
