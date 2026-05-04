package com.session16.service;

import com.session16.entity.Order;
import com.session16.entity.OrderDetail;
import com.session16.entity.Product;
import com.session16.exception.InsufficientStockException;
import com.session16.model.cart.CartItem;
import com.session16.repository.OrderDetailRepository;
import com.session16.repository.OrderRepository;
import com.session16.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order placeOrder(String customerName, String customerEmail,
                            String customerPhone, String shippingAddress,
                            List<CartItem> cartItems) {
        BigDecimal total = cartItems.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .customerName(customerName)
                .customerEmail(customerEmail)
                .customerPhone(customerPhone)
                .shippingAddress(shippingAddress)
                .totalAmount(total)
                .build();
        orderRepository.save(order);

        List<OrderDetail> details = new ArrayList<>();
        for (CartItem item : cartItems) {
            Optional<Product> productOptional = productRepository.findById(item.getProductId());
            if (productOptional.isEmpty()) {
                throw new RuntimeException("San pham khong ton tai");
            }

            Product product = productOptional.get();
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new InsufficientStockException(product.getName());
            }

            int newStock = product.getStockQuantity() - item.getQuantity();
            product.setStockQuantity(newStock);
            productRepository.save(product);

            OrderDetail detail = OrderDetail.builder()
                    .order(order)
                    .product(product)
                    .quantity(item.getQuantity())
                    .unitPrice(item.getUnitPrice())
                    .build();
            details.add(detail);
        }

        orderDetailRepository.saveAll(details);
        order.setStatus("COMPLETED");
        return orderRepository.save(order);
    }
}
