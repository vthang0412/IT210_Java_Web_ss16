package com.session16.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutForm {
    @NotBlank(message = "Ho ten khong duoc de trong")
    private String customerName;

    @NotBlank(message = "Email khong duoc de trong")
    @Email(message = "Email khong dung dinh dang")
    private String customerEmail;

    private String customerPhone;

    @NotBlank(message = "Dia chi khong duoc de trong")
    private String shippingAddress;
}
