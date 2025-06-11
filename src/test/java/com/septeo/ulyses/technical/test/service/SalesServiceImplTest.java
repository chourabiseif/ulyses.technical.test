package com.septeo.ulyses.technical.test.service;

import com.septeo.ulyses.technical.test.dto.BestSellingVehicleDto;
import com.septeo.ulyses.technical.test.entity.Sales;
import com.septeo.ulyses.technical.test.entity.Vehicle;
import com.septeo.ulyses.technical.test.repository.SalesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SalesServiceImplTest {

    @Mock
    private SalesRepository salesRepository;

    @InjectMocks
    private SalesServiceImpl salesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Sales createSale(Long vehicleId, LocalDate date) {
        Sales sale = new Sales();
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);
        sale.setVehicle(vehicle);
        sale.setSaleDate(date);
        return sale;
    }

    @Test
    void testGetBestSellingVehicles_noDateFilter_returnsTop5() {
        // Given
        List<Sales> mockSales = Arrays.asList(
                createSale(1L, LocalDate.of(2024, 1, 1)),
                createSale(1L, LocalDate.of(2024, 1, 2)),
                createSale(2L, LocalDate.of(2024, 1, 3)),
                createSale(3L, LocalDate.of(2024, 1, 4)),
                createSale(3L, LocalDate.of(2024, 1, 5)),
                createSale(3L, LocalDate.of(2024, 1, 6)),
                createSale(4L, LocalDate.of(2024, 1, 7)),
                createSale(5L, LocalDate.of(2024, 1, 8)),
                createSale(6L, LocalDate.of(2024, 1, 9))
        );

        when(salesRepository.findAll()).thenReturn(mockSales);

        // When
        List<BestSellingVehicleDto> result = salesService.getBestSellingVehicles(null, null);

        // Then
        assertEquals(5, result.size());
        assertEquals(3L, result.get(0).getVehicleId()); // 3 sales
        assertEquals(1L, result.get(1).getVehicleId()); // 2 sales
        assertEquals(2L, result.get(2).getVehicleId()); // 1 sale
        assertTrue(result.stream().anyMatch(v -> v.getVehicleId() == 4L));
        assertTrue(result.stream().anyMatch(v -> v.getVehicleId() == 5L));

        verify(salesRepository, times(1)).findAll();
    }
}
