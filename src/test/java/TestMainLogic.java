import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import orders.StartApplication;
import orders.model.Order;
import orders.repository.OrderRepository;
import orders.service.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartApplication.class)
public class TestMainLogic {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldReturnItemList() throws Exception {
        mockMvc.perform(get("/orders/getAllOrders?item=item"))
                .andExpect(status().isOk());
    }

    @Test
    public void checkReturnItem() {
        var firstOrder = new Order();
        firstOrder.setItem("Apple Watch");
        orderService.createOrderInStorage(firstOrder);

        var secondOrder = new Order();
        secondOrder.setItem("Samsung Galaxy Watch");
        orderService.createOrderInStorage(secondOrder);

        var firstOrderFromDB = orderRepository.findById(1).get();
        var secondOrderFromDB = orderRepository.findById(2).get();

        assertThat(firstOrderFromDB.getItem()).isEqualTo(firstOrder.getItem());
        assertThat(secondOrderFromDB.getItem()).isEqualTo(secondOrder.getItem());
    }

    @Test
    public void shouldCreateOrder() throws Exception {
        var uri = "/orders/create?price=1&quantity=3&item=Belt";
        var order = new Order();
        order.setPrice(1);
        order.setQuantity(3);
        order.setItem("Belt");

        var inputJson = mapToJson(order);
        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        var content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "order is created");
    }

    @Test(expected = NoSuchElementException.class)
    public void deleteMoreThanTenMinutes() {
        var TIME = System.currentTimeMillis();
        var order = new Order();
        order.setTimeStamp(TIME);
        orderService.createOrderInStorage(order);

        TIME = TIME + 700;
        var timeDifference = TIME - orderRepository.findById(1).get().getTimeStamp();
        if (timeDifference > 600) {
            orderRepository.delete(order);
        }

        var order1 = orderRepository.findById(1).get();
    }

    private String mapToJson(Object obj) throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
