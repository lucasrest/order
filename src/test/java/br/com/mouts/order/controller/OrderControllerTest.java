package br.com.mouts.order.controller;

import br.com.mouts.order.model.Order;
import br.com.mouts.order.service.OrderFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest {

    @Mock
    private OrderFacade orderFacade;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new OrderController(orderFacade)).build();
    }

    @Test
    public void testCreateOrder() throws Exception {
        Order order = getMock();
        when(orderFacade.createAndProcessOrder(anyString(), anyList())).thenReturn(order);

        mockMvc.perform(post("/orders")
                        .param("refId", order.getRefId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{ \"name\": \"Product 1\", \"price\": 10 }]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.refId").value("refId123"));
    }

    @Test
    public void testGetOrder() throws Exception {
        Order order = getMock();
        when(orderFacade.getOrder(anyLong())).thenReturn(order);

        mockMvc.perform(get("/orders/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.refId").value("refId123"));
    }

    private Order getMock() {
        Order mock = new Order();
        mock.setRefId("refId123");
        return mock;
    }

}
