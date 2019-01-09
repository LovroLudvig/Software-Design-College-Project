package hr.fer.handMadeShopBackend.service;

import hr.fer.handMadeShopBackend.Constants.Constants;
import hr.fer.handMadeShopBackend.dao.*;
import hr.fer.handMadeShopBackend.domain.*;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TownRepository townRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<AdOrder> listAllNotManaged() {
        Long inEvaluationId = orderStatusRepository.findByName(Constants.ORDER_STATUS_IN_EVALUATION).getId();
        return orderRepository.findAll().stream().filter(o -> o.getStatus().getId() == inEvaluationId).collect(Collectors.toList());
    }

    @Override
    public AdOrder manageOrder(Long orderId, boolean isAllowed, Double price) {
        Optional<AdOrder> opt = orderRepository.findById(orderId);
        if(!opt.isPresent()) {
            throw new IllegalArgumentException("The order does not exist.");
        }
        AdOrder order = opt.get();
        if(price != null) {
            order.getStyle().setPrice(price);
            order.setPrice(calculatePrice(order));
        }

        if(isAllowed) {
            OrderStatus status = orderStatusRepository.findByName(Constants.ORDER_STATUS_ALLOWED);
            order.setStatus(status);

            // this line adds the style to the list of available styles for that advetisement
            // if admin approves the order
            order.getAdvertisement().getStyles().add(order.getStyle());

            return orderRepository.save(order);
        } else {
            orderRepository.delete(order);
            return order;
        }
    }

    @Override
    public AdOrder buyOrder(AdOrder adOrder, User user) {
        validate(adOrder);
        validateUser(user);

        OrderStatus status = orderStatusRepository.findByName(Constants.ORDER_STATUS_ALLOWED);
        adOrder.setStatus(status);
        adOrder.setPrice(calculatePrice(adOrder));

        adOrder.setName(user.getName());
        adOrder.setLastName(user.getLastName());
        adOrder.setCardNumber(user.getCardNumber());
        adOrder.setTown(user.getTown());
        adOrder.setAddress(user.getAddress());

        AdOrder o = orderRepository.save(adOrder);

        Transaction t = new Transaction();
        t.setAdOrder(o);

        transactionRepository.save(t);

        return o;
    }

    @Override
    public AdOrder orderObjectDecoration(AdOrder adOrder, User user) {
        validateDecorationOrder(adOrder);
        validateUser(user);

        OrderStatus status = orderStatusRepository.findByName(Constants.ORDER_STATUS_IN_EVALUATION);
        adOrder.setStatus(status);
        adOrder.setPrice(adOrder.getStyle().getPrice());

        Town town = checkTown(user.getTown());

        adOrder.setName(user.getName());
        adOrder.setLastName(user.getLastName());
        adOrder.setCardNumber(user.getCardNumber());
        adOrder.setTown(town);
        adOrder.setAddress(user.getAddress());

        return orderRepository.save(adOrder);
    }

    private void validate(AdOrder adOrder) {
        if(adOrder == null) {
            throw new IllegalArgumentException("The order must be provided!");
        }
        if(adOrder.getAdvertisement() == null) {
            throw new IllegalArgumentException("The advertisement you want to buy must be provided.");
        }
        Optional<Advertisement> ad = advertisementRepository.findById(adOrder.getAdvertisement().getId());
        if(!ad.isPresent()) {
            throw new IllegalArgumentException("The advertisement does not exist!");
        }
        if(adOrder.getDimension() == null) {
            throw new IllegalArgumentException("The dimension of the object must be provided.");
        }
        if(adOrder.getStyle() == null) {
            throw new IllegalArgumentException("The style for the decoration must be provided.");
        }
    }

    private void validateDecorationOrder(AdOrder adOrder) {
        if(adOrder == null) {
            throw new IllegalArgumentException("The order must be provided!");
        }
        if(adOrder.getAdvertisement() == null) {
            throw new IllegalArgumentException("The advertisement you want to buy must be provided.");
        }
        Optional<Advertisement> ad = advertisementRepository.findById(adOrder.getAdvertisement().getId());
        if(!ad.isPresent()) {
            throw new IllegalArgumentException("The advertisement does not exist!");
        }
        if(adOrder.getStyle() == null) {
            throw new IllegalArgumentException("The style for the decoration must be provided.");
        }
    }

    private void validateUser(User user) {
        if(user == null) {  throw new IllegalArgumentException("User object must be given"); }
        if(user.getCardNumber() == null) {
            throw new IllegalArgumentException("The card number must be provided to make an order.");
        }
    }

    private Double calculatePrice(AdOrder adOrder) {
        Double price = 0.0;

        price += adOrder.getAdvertisement().getPrice();
        price += adOrder.getStyle().getPrice();

        return price;
    }

    private Town checkTown(Town town) {
        if(town == null) return null;
        Optional<Town> t = townRepository.findById(town.getPostCode());
        if(!t.isPresent()) {
            return townRepository.save(town);
        }
        return t.get();
    }

}
