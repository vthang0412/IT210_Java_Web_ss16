package com.session16.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String productName) {
        super("Sản phẩm \"" + productName + "\" không đủ số lượng trong kho.");
    }
}
