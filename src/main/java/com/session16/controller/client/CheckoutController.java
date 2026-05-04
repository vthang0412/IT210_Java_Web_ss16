package com.session16.controller.client;

import com.session16.exception.InsufficientStockException;
import com.session16.model.cart.CartItem;
import com.session16.service.CheckoutService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;

    @GetMapping
    public String showCheckout(HttpSession session, Model model) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("cartItems", cart);
        model.addAttribute("total", cart.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        return "client/checkout";
    }

    @PostMapping
    public String processCheckout(@RequestParam String customerName,
                                  @RequestParam String customerEmail,
                                  @RequestParam String customerPhone,
                                  @RequestParam String shippingAddress,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }

        try {
            var order = checkoutService.placeOrder(
                    customerName, customerEmail, customerPhone, shippingAddress, cart);
            session.removeAttribute("cart");
            redirectAttributes.addFlashAttribute("orderId", order.getId());
            return "redirect:/checkout/success";
        } catch (InsufficientStockException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart";
        }
    }

    @GetMapping("/success")
    public String success() {
        return "client/checkout-success";
    }
}
