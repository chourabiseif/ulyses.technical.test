package com.septeo.ulyses.technical.test.repository;

import com.septeo.ulyses.technical.test.entity.Brand;
import com.septeo.ulyses.technical.test.entity.Sales;
import com.septeo.ulyses.technical.test.entity.Vehicle;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the SalesRepository interface.
 * This class provides the implementation for all sales-related operations.
 */
@Repository
public class SalesRepositoryImpl implements SalesRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Sales> findAll() {
        String stringQuery = "SELECT s FROM Sales s";
        Query query = entityManager.createQuery(stringQuery);
        return query.getResultList();
    }

    @Override
    public Optional<Sales> findById(Long id) {
        String stringQuery = "SELECT s FROM Sales s WHERE s.id = :id";
        Query query = entityManager.createQuery(stringQuery);
        query.setParameter("id", id);

        try {
            return Optional.of((Sales) query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Sales> findByBrandId(Long brandId) {
        String queryStr = "SELECT s FROM Sales s WHERE s.vehicle.brand.id = :brandId";
        return entityManager.createQuery(queryStr, Sales.class)
                .setParameter("brandId", brandId)
                .getResultList();
    }

    @Override
    public List<Sales> findByVehicleId(Long vehicleId) {
        String queryStr = "SELECT s FROM Sales s WHERE s.vehicle.id = :vehicleId";
        return entityManager.createQuery(queryStr, Sales.class)
                .setParameter("vehicleId", vehicleId)
                .getResultList();
    }

    @Override
    public List<Sales> findAllPaginated(int pageNumber, int pageSize) {
        String queryStr = "SELECT s FROM Sales s";
        return entityManager.createQuery(queryStr, Sales.class)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public Long countSales() {
        String queryStr = "SELECT COUNT(s) FROM Sales s";
        return entityManager.createQuery(queryStr, Long.class).getSingleResult();
    }
}
