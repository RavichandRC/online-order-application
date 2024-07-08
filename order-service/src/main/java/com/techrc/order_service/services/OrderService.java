package com.techrc.order_service.services;

import com.techrc.order_service.dtos.InventoryResponse;
import com.techrc.order_service.dtos.OrderLineItemsDto;
import com.techrc.order_service.dtos.OrderRequest;
import com.techrc.order_service.model.Order;
import com.techrc.order_service.model.OrderLineItems;
import com.techrc.order_service.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    @Autowired
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems =  orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();
        //call to inventory service and place order if product is in stock
        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                                .uri("http://inventory-service/api/inventory",
                                uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                                .retrieve()
                                .bodyToMono(InventoryResponse[].class)
                                .block();
        boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);
        if(allProductsInStock){
            orderRepository.save(order);
        }
        else{
            throw new IllegalArgumentException("Product is not in stock, check later");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
