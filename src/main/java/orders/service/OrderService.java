package orders.service;

import orders.model.Order;
import orders.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public void createOrderInStorage(Order order) {
        orderRepository.save(order);
    }

    public List<Order> getAllOrders(String item, Integer pageNo, Integer pageSize) {
        var paging = PageRequest.of(pageNo, pageSize, Sort.by("price").ascending());

        var pagedResult = orderRepository.findByItem(item, paging);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public void deleteNotValidOrders(long currentTime){
        orderRepository.deleteEveryTenMinutes(currentTime);
    }
}
