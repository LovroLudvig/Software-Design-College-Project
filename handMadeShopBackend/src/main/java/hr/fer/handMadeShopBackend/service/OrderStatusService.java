package hr.fer.handMadeShopBackend.service;

import hr.fer.handMadeShopBackend.domain.OrderStatus;

public interface OrderStatusService {

    OrderStatus findByName(String name);
    OrderStatus saveOrderStatusWithName(String name);
}
