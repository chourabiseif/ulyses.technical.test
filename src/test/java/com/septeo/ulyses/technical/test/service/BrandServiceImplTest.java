package com.septeo.ulyses.technical.test.service;

import com.septeo.ulyses.technical.test.entity.Brand;
import com.septeo.ulyses.technical.test.repository.BrandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BrandServiceImplTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandServiceImpl brandService;

    private Brand brand1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        brand1 = new Brand();
        brand1.setId(1L);
        brand1.setName("Renault");
    }

    @Test
    void saveBrand_shouldInvalidateCacheAndReturnSavedBrand() {
        when(brandRepository.save(any(Brand.class))).thenReturn(brand1);

        Brand saved = brandService.saveBrand(brand1);

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        verify(brandRepository, times(1)).save(brand1);
    }

    @Test
    void deleteBrand_shouldInvalidateCache() {
        doNothing().when(brandRepository).deleteById(1L);

        brandService.deleteBrand(1L);

        verify(brandRepository, times(1)).deleteById(1L);
    }
}
