package hr.fer.handMadeShopBackend.rest;

import hr.fer.handMadeShopBackend.dao.AdvertisementRepository;
import hr.fer.handMadeShopBackend.dao.DimensionRepository;
import hr.fer.handMadeShopBackend.dao.StyleRepository;
import hr.fer.handMadeShopBackend.dao.TransactionRepository;
import hr.fer.handMadeShopBackend.domain.*;
import hr.fer.handMadeShopBackend.service.OrderService;
import hr.fer.handMadeShopBackend.service.StyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@Lazy
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private StyleRepository styleRepository;

    @Autowired
    private StyleService styleService;

    @Autowired
    private DimensionRepository dimensionRepository;

    @PostMapping("/add/{advertisementId}")
    public AdOrder buyOrder(@PathVariable("advertisementId") Long advertisementId,
                            @RequestParam(value="styleId", required=true) Long styleId,
                            @RequestParam(value="dimensionId", required=true) Long dimensionId,
                            @RequestBody User user) {
        Advertisement ad = getAdvertisement(advertisementId);
        Style style = getStyle(styleId);
        Dimension dimension = getDimension(dimensionId);

        AdOrder order = new AdOrder();
        order.setAdvertisement(ad);
        order.setStyle(style);
        order.setDimension(dimension);

        return orderService.buyOrder(order, user);
    }

    @PostMapping("/orderDecoration/{advertisementId}")
    public AdOrder orderObjectDecoration(@PathVariable("advertisementId") Long advertisementId,
                                         @RequestParam(value="styleId", required=true) Long styleId,
                                         @RequestParam(value="styleName", required=false) String styleDescription,
                                         @RequestBody User user) {
        Advertisement ad = getAdvertisement(advertisementId);
        Style style = getStyle(styleId);
        if(style == null) {
            if(styleDescription == null) {
                throw new IllegalArgumentException("The style name must be provided if new style is being created.");
            }
            style = new Style();
            style.setDescription(styleDescription);
            styleService.save(style);
        }

        AdOrder order = new AdOrder();
        order.setAdvertisement(ad);
        order.setStyle(style);

        return orderService.orderObjectDecoration(order, user);
    }

    @PostMapping("/manage")
    public AdOrder manageOrder(@RequestParam(value="orderId", required=true) Long orderId,
                               @RequestParam(value="isAllowed", required=true) boolean isAlowed,
                               @RequestParam(value="price", required=true) Double price) {
        return orderService.manageOrder(orderId, isAlowed, price);
    }

    @GetMapping("")
    public List<AdOrder> listOrdersInEvaluation() {
        return orderService.listAllNotManaged();
    }

    private Style getStyle(Long id) {
        Optional<Style> sOpt = styleRepository.findById(id);
        if(!sOpt.isPresent()) {
//            throw new IllegalArgumentException("The style does not exist");
            return null;
        }
        return sOpt.get();
    }

    private Dimension getDimension(Long id) {
        Optional<Dimension> dOpt = dimensionRepository.findById(id);
        if(!dOpt.isPresent()) {
            throw new IllegalArgumentException("The dimension does not exist");
        }
        return dOpt.get();
    }

    private Advertisement getAdvertisement(Long id) {
        Optional<Advertisement> opt = advertisementRepository.findById(id);
        if(!opt.isPresent()) {
            throw new IllegalArgumentException("Not valid advertisement id.");
        }
        return opt.get();
    }

}
