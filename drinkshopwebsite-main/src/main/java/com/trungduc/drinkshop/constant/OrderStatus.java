package com.trungduc.drinkshop.constant;

public interface OrderStatus {
    String PENDING = "PENDING";         // Đang chờ xử lý
    String PROCESSING = "PROCESSING";     // Đang xử lý
    String DELIVERING = "DELIVERING";        // Đang giao hàng
    String DELIVERED = "DELIVERED";      // Đã giao thành công
    String CANCELLED = "CANCELLED";       // Đã hủy
    String UNPAID = "UNPAID"; // chưa hoàn tất việc thanh toán
}

