package hr.fer.handMadeShopBackend.service;

import hr.fer.handMadeShopBackend.domain.*;

import java.util.List;

public interface OrderService {

    List<AdOrder> listAllNotManaged();
    AdOrder manageOrder(Long orderId, boolean isAlowed, Double price);
    AdOrder buyOrder(AdOrder adOrder, User user);
    AdOrder orderObjectDecoration(AdOrder adOrder, User user);
}
