package hr.fer.handMadeShopBackend.service;

import hr.fer.handMadeShopBackend.dao.OrderStatusRepository;
import hr.fer.handMadeShopBackend.domain.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusServiceImpl implements OrderStatusService {

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Override
    public OrderStatus findByName(String name) {
        return orderStatusRepository.findByName(name);
    }

    @Override
    public OrderStatus saveOrderStatusWithName(String name) {
        OrderStatus status = new OrderStatus();
        status.setName(name);

        return orderStatusRepository.save(status);
    }

}
