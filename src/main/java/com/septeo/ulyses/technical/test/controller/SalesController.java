package com.septeo.ulyses.technical.test.controller;

import com.septeo.ulyses.technical.test.dto.BestSellingVehicleDto;
import com.septeo.ulyses.technical.test.dto.PaginatedResponse;
import com.septeo.ulyses.technical.test.entity.Sales;
import com.septeo.ulyses.technical.test.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    @Autowired
    private SalesService salesService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<Sales>> getAllSales(@RequestParam(defaultValue = "1") int page) {
        int pageSize = 10;
        PaginatedResponse<Sales> response = salesService.getSalesPaginated(page, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sales> getSalesById(@PathVariable Long id) {
        return salesService.getSalesById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/brands/{brandId}")
    public ResponseEntity<List<Sales>> getSalesByBrandId(@PathVariable Long brandId) {
        List<Sales> salesList = salesService.getSalesByBrandId(brandId);
        if (salesList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(salesList);
    }

   @GetMapping("/vehicles/{vehicleId}")
    public ResponseEntity<List<Sales>> getSalesVehicleId(@PathVariable Long vehicleId) {
        List<Sales> salesList = salesService.getSalesByVehicleId(vehicleId);
        if (salesList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(salesList);
    }

    @GetMapping("/vehicles/bestSelling")
    public ResponseEntity<List<BestSellingVehicleDto>> getBestSellingVehicles(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<BestSellingVehicleDto> result = salesService.getBestSellingVehicles(startDate, endDate);
        return ResponseEntity.ok(result);
    }

}
