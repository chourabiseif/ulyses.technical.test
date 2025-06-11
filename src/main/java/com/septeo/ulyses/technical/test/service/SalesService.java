package com.septeo.ulyses.technical.test.service;

import com.septeo.ulyses.technical.test.dto.BestSellingVehicleDto;
import com.septeo.ulyses.technical.test.dto.PaginatedResponse;
import com.septeo.ulyses.technical.test.entity.Sales;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Sales operations.
 */
public interface SalesService {

    /**
     * Get all sales.
     *
     * @return a list of all sales
     */
    List<Sales> getAllSales();

    /**
     * Get a sales by its ID.
     *
     * @param id the ID of the sales to find
     * @return an Optional containing the sales if found, or empty if not found
     */
    Optional<Sales> getSalesById(Long id);

    /**
     * Get sales by brand id.
     *
     * @param brandId the ID of the brand
     * @return the list of sales for the given brand
     */
    List<Sales> getSalesByBrandId(Long brandId);

    /**
     * Get sales by vehicle id.
     *
     * @param vehicleId the ID of the vehicle
     * @return the list of sales for the given vehicle
     */
    List<Sales> getSalesByVehicleId(Long vehicleId);

    PaginatedResponse<Sales> getSalesPaginated(int pageNumber, int pageSize);

    List<BestSellingVehicleDto> getBestSellingVehicles(LocalDate startDate, LocalDate endDate);


}
