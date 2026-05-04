package com.session16.service;

import com.session16.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final OrderRepository orderRepository;

    public BigDecimal getTotalRevenue() {
        return orderRepository.getTotalRevenue();
    }

    public List<Map<String, Object>> getTop5Products() {
        List<Object[]> rows = orderRepository.getTop5BestSellingProducts();
        List<Map<String, Object>> products = new ArrayList<>();

        for (Object[] row : rows) {
            Map<String, Object> product = new LinkedHashMap<>();
            product.put("name", row[0]);
            product.put("totalSold", row[1]);
            products.add(product);
        }

        return products;
    }
}
