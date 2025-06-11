package com.septeo.ulyses.technical.test.service;

import com.septeo.ulyses.technical.test.dto.BestSellingVehicleDto;
import com.septeo.ulyses.technical.test.dto.PaginatedResponse;
import com.septeo.ulyses.technical.test.entity.Sales;
import com.septeo.ulyses.technical.test.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of the SalesService interface.
 * This class provides the implementation for all sales-related operations.
 */
@Service
@Transactional(readOnly = false)
public class SalesServiceImpl implements SalesService {

    @Autowired
    private SalesRepository salesRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Sales> getAllSales() {
        return salesRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Sales> getSalesById(Long id) {
        return salesRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Sales> getSalesByBrandId(Long brandId) {
        return salesRepository.findByBrandId(brandId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Sales> getSalesByVehicleId(Long vehicleId) {
        return salesRepository.findByVehicleId(vehicleId);
    }

    @Override
    public PaginatedResponse<Sales> getSalesPaginated(int pageNumber, int pageSize) {

        if (pageNumber < 1) {
            throw new IllegalArgumentException("Page number must be 1 or higher.");
        }

        List<Sales> paginatedSales = salesRepository.findAllPaginated(pageNumber, pageSize);
        Long totalItems = salesRepository.countSales();

        return new PaginatedResponse<>(paginatedSales, pageNumber, pageSize, totalItems);
    }

    @Override
    public List<BestSellingVehicleDto> getBestSellingVehicles(LocalDate startDate, LocalDate endDate) {

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate must be before or equal to endDate.");
        }

        List<Sales> allSales = salesRepository.findAll();

        // Filter by optional date range
        List<Sales> filtered = allSales.stream()
                .filter(sale -> {
                    LocalDate saleDate = sale.getSaleDate();
                    boolean afterStart = startDate == null || !saleDate.isBefore(startDate);
                    boolean beforeEnd = endDate == null || !saleDate.isAfter(endDate);
                    return afterStart && beforeEnd;
                })
                .toList();

        // Count sales per vehicle
        Map<Long, Long> salesCount = new HashMap<>();
        for (Sales sale : filtered) {
            if (sale.getVehicle() == null || sale.getVehicle().getId() == null) continue;
            Long vehicleId = sale.getVehicle().getId();
            salesCount.put(vehicleId, salesCount.getOrDefault(vehicleId, 0L) + 1);
        }

        // Maintain a top 5 list in descending order manually
        List<BestSellingVehicleDto> top5 = new ArrayList<>();

        for (Map.Entry<Long, Long> entry : salesCount.entrySet()) {
            BestSellingVehicleDto current = new BestSellingVehicleDto(entry.getKey(), entry.getValue());

            if (top5.size() < 5) {
                insertDescending(top5, current);
            } else {
                // Compare with the weakest (last in the list)
                if (current.getTotalSales() > top5.get(4).getTotalSales()) {
                    top5.remove(4);
                    insertDescending(top5, current);
                }
            }
        }

        return top5;
    }

    // Helper method to keep list sorted descending
    private void insertDescending(List<BestSellingVehicleDto> list, BestSellingVehicleDto dto) {
        int i = 0;
        while (i < list.size() && list.get(i).getTotalSales() >= dto.getTotalSales()) i++;
        list.add(i, dto);
    }


}
