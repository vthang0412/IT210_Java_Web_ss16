package com.session16.controller.client;

import com.session16.exception.InsufficientStockException;
import com.session16.model.CheckoutForm;
import com.session16.model.cart.CartItem;
import com.session16.service.CheckoutService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        model.addAttribute("total", getTotal(cart));
        model.addAttribute("checkoutForm", new CheckoutForm());
        return "client/checkout";
    }

    @PostMapping
    public String processCheckout(@Valid @ModelAttribute CheckoutForm checkoutForm,
                                  BindingResult bindingResult,
                                  HttpSession session,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("cartItems", cart);
            model.addAttribute("total", getTotal(cart));
            return "client/checkout";
        }

        try {
            var order = checkoutService.placeOrder(
                    checkoutForm.getCustomerName(),
                    checkoutForm.getCustomerEmail(),
                    checkoutForm.getCustomerPhone(),
                    checkoutForm.getShippingAddress(),
                    cart);
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

    private BigDecimal getTotal(List<CartItem> cart) {
        return cart.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
