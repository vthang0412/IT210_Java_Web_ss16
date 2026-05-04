package com.session16.repository;

import com.session16.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.status = 'COMPLETED'")
    BigDecimal getTotalRevenue();

    @Query(value = """
        SELECT p.name, SUM(od.quantity) AS total_sold
        FROM order_details od
        JOIN products p ON od.product_id = p.id
        JOIN orders o ON od.order_id = o.id
        WHERE o.status = 'COMPLETED'
        GROUP BY p.id, p.name
        ORDER BY total_sold DESC
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> getTop5BestSellingProducts();
}
