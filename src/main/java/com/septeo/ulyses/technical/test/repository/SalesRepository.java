package com.septeo.ulyses.technical.test.repository;

import com.septeo.ulyses.technical.test.entity.Brand;
import com.septeo.ulyses.technical.test.entity.Sales;
import com.septeo.ulyses.technical.test.entity.Vehicle;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Sales entity.
 */
@Repository
public interface SalesRepository {
    /**
     * Find all sales.
     *
     * @return a list of all sales
     */
    List<Sales> findAll();

    /**
     * Find a sale by its ID.
     *
     * @param id the ID of the sale to find
     * @return an Optional containing the sale if found, or empty if not found
     */
    Optional<Sales> findById(Long id);

    /**
     * Get sales by brand id.
     *
     * @param brandId the ID of the brand
     * @return the list of sales for the given brand
     */
    List<Sales> findByBrandId(Long brandId);

    /**
     * Get sales by vehicle id.
     *
     * @param vehicleId the ID of the vehicle
     * @return the list of sales for the given vehicle
     */
    List<Sales> findByVehicleId(Long vehicleId);

    /**
     * Find page of sale.
     *
     * @param pageNumber the number of the page requested
     * @param pageSize   the size of the page
     * @return the page of sales
     */
    List<Sales> findAllPaginated(int pageNumber, int pageSize);

    Long countSales();

}
