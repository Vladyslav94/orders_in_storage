package orders.controller;

import orders.model.Order;
import orders.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private static final long CURRENT_TIME = System.currentTimeMillis() / 1000L;

    @RequestMapping("/create")
    public String createOrder(@RequestParam(value = "price") double price,
                              @RequestParam(value = "quantity") int quantity,
                              @RequestParam(value = "item") String item) {
        checkTimeCreationAndDelete();
        Order order = createNewOrderAndFillTheFields(price, quantity, item);
        orderService.createOrderInStorage(order);
        return "order is created";
    }

    @GetMapping("/getAllOrders")
    public ResponseEntity<List<Order>> getAllEmployees(
            @RequestParam(value = "item") String item,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        checkTimeCreationAndDelete();
        List<Order> list = orderService.getAllOrders(item, pageNo, pageSize);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    public void checkTimeCreationAndDelete() {
        orderService.deleteNotValidOrders(CURRENT_TIME - 600L);
    }

    public Order createNewOrderAndFillTheFields(double price, int quantity, String item) {
        Order order = new Order();
        order.setPrice(price);
        order.setQuantity(quantity);
        order.setItem(item);
        order.setTimeStamp(CURRENT_TIME);
        return order;
    }
}
