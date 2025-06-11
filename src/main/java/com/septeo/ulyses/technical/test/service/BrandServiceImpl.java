package com.septeo.ulyses.technical.test.service;

import com.septeo.ulyses.technical.test.cache.SimpleCache;
import com.septeo.ulyses.technical.test.entity.Brand;
import com.septeo.ulyses.technical.test.repository.BrandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the BrandService interface.
 * This class provides the implementation for all brand-related operations.
 */
@Service
@Transactional(readOnly = false)
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final SimpleCache<String, List<Brand>> allBrandsCache;
    private final SimpleCache<Long, Brand> brandByIdCache;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
        this.allBrandsCache = new SimpleCache<>(1 * 60 * 1000); // 1 minute
        this.brandByIdCache = new SimpleCache<>(1 * 60 * 1000); // 1 minute
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Brand> getAllBrands() {

        return allBrandsCache.get("ALL", brandRepository::findAll);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Brand> getBrandById(Long id) {

        Brand result = brandByIdCache.get(id, () ->
                brandRepository.findById(id).orElse(null)
        );
        return Optional.ofNullable(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Brand saveBrand(Brand brand) {
        Brand saved = brandRepository.save(brand);

        allBrandsCache.clear();
        brandByIdCache.invalidate(saved.getId());

        return saved;
    }

    @Override
    public void deleteBrand(Long id) {
        brandRepository.deleteById(id);

        allBrandsCache.clear();
        brandByIdCache.invalidate(id);
    }
}
