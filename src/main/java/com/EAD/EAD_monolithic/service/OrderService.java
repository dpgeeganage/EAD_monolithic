package com.EAD.EAD_monolithic.service;


import com.EAD.EAD_monolithic.Exception.InsufficientProductQuantityException;
import com.EAD.EAD_monolithic.Exception.OrderNotFoundException;
import com.EAD.EAD_monolithic.Exception.ProductNotFoundException;
import com.EAD.EAD_monolithic.dto.*;
import com.EAD.EAD_monolithic.entity.Delivery;
import com.EAD.EAD_monolithic.entity.Inventory;
import com.EAD.EAD_monolithic.entity.Order;
import com.EAD.EAD_monolithic.entity.OrderItem;
import com.EAD.EAD_monolithic.repo.DeliveryRepo;
import com.EAD.EAD_monolithic.repo.OrderItemRepo;
import com.EAD.EAD_monolithic.repo.OrderRepo;
import com.EAD.EAD_monolithic.repo.InventoryRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private DeliveryRepo deliveryRepo;

    @Autowired
    private ModelMapper modelMapper;


    public OrderDTO orderReqToOrderDTO(OrderRequest orderRequest) {
        Order newOrder = new Order();

        List<OrderItemRequest> orderRequestItems = orderRequest.getOrderItems();

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest orderItemRequest : orderRequestItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setItemId(orderItemRequest.getItemId());
            orderItem.setQuantity(orderItemRequest.getQuantity());
            orderItem.setUnitPrice(orderItemRequest.getUnitPrice());
            orderItem.setTotalUnitPrice(orderItemRequest.getTotalUnitPrice());
            orderItems.add(orderItem);
        }



    }

   /* public boolean checkProductQuantity(int itemId, int quantity) {
        Inventory inventory = inventoryRepo.getProductByProductID(itemId);
        if(inventory == null){
            throw new ProductNotFoundException("Product not found with id " + itemId);
        }
        return inventory.getQuantity() >= quantity;
    }

    public double getProductUnitPrice(int itemId) {
        Inventory inventory = inventoryRepo.getProductByProductID(itemId);
        if(inventory == null){
            throw new ProductNotFoundException("Product not found with id " + itemId);
        }
        return inventory.getUnitPrice();
    }

    public Order saveOrder(OrderRequest orderRequest) {

        // Check if all order items have enough quantity in stock
        for (OrderItemRequest orderItemRequest : orderRequest.getOrderItems()) {
            if (!checkProductQuantity(orderItemRequest.getItemId(), orderItemRequest.getQuantity())) {
                throw new InsufficientProductQuantityException("Insufficient quantity for product with id " + orderItemRequest.getItemId() + " in stock");
            }
        }

        //Reduce the quantity of each product in the database
        for (OrderItemRequest orderItemRequest : orderRequest.getOrderItems()) {
            Inventory inventory = inventoryRepo.getProductByProductID(orderItemRequest.getItemId());
            inventory.setQuantity(inventory.getQuantity() - orderItemRequest.getQuantity());
            inventoryRepo.save(inventory);
        }

        Order newOrder = new Order();
        newOrder.setUserId(orderRequest.getUserId());
        newOrder.setIsPrepared(false);

        // Create a list to store the OrderItem objects
        List<OrderItem> newOrderItems = new ArrayList<>();

       //Iterate over the OrderItemRequest objects and create a new OrderItem object for each one
        for (OrderItemRequest orderItemRequest : orderRequest.getOrderItems()) {
            OrderItem newOrderItem = new OrderItem();
            newOrderItem.setOrder(newOrder);
            newOrderItem.setItemId(orderItemRequest.getItemId());
            newOrderItem.setQuantity(orderItemRequest.getQuantity());

            // Set the unit price on the OrderItem object
            newOrderItem.setUnitPrice(getProductUnitPrice(orderItemRequest.getItemId()));

            // Calculate the total unit price for the OrderItem object
            newOrderItem.setTotalUnitPrice(newOrderItem.getQuantity() * newOrderItem.getUnitPrice());

            // Add the OrderItem object to the list
            newOrderItems.add(newOrderItem);
        }
        // Set the OrderItem objects on the Order object
        newOrder.setOrderItems(newOrderItems);

        double totalPrice = 0;
        for (OrderItem orderItem : newOrder.getOrderItems()) {
            totalPrice += orderItem.getTotalUnitPrice();
        }

        // Set the total price on the Order object
        newOrder.setTotalPrice(totalPrice);

        // Save the Order object
        return orderRepo.save(newOrder);
    }



    public List<OrderDTO> getAllOrders() {
        List<Order> orderList = orderRepo.findAll();
        return modelMapper.map(orderList, new TypeToken<List<OrderDTO>>() {
        }.getType());
    }


    public Order getOrderById(int id) {
        Order order = orderRepo.findById(id).orElse(null);
        if (order == null) {
            throw new OrderNotFoundException("Order not found with id " + id);
        }
        return order;
    }

    public Order updateOrder(OrderUpdateRequest orderUpdateRequest, int id) {
        Order existingOrder = orderRepo.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id " + id));


        // Update order-level properties
        existingOrder.setUserId(orderUpdateRequest.getUserId());
        existingOrder.setIsPrepared(orderUpdateRequest.isPrepared());

        // Handle order items
        List<OrderItem> updatedOrderItems = new ArrayList<>();

        for (OrderItemRequest orderItemRequest : orderUpdateRequest.getOrderItems()) {
            // Check if the item already exists in the order
            Optional<OrderItem> existingOrderItemOptional = existingOrder.getOrderItems().stream()
                    .filter(orderItem -> orderItem.getItemId() == orderItemRequest.getItemId())
                    .findFirst();

            if (existingOrderItemOptional.isPresent()) {
                // If the item exists, update its properties
                OrderItem existingOrderItem = existingOrderItemOptional.get();
                existingOrderItem.setQuantity(orderItemRequest.getQuantity());
                existingOrderItem.setUnitPrice(getProductUnitPrice(orderItemRequest.getItemId()));
                existingOrderItem.setTotalUnitPrice(orderItemRequest.getQuantity() * existingOrderItem.getUnitPrice());

                // Add the updated item to the list
                updatedOrderItems.add(existingOrderItem);

                // Update the product quantity in the database
                Inventory inventory = inventoryRepo.getProductByProductID(orderItemRequest.getItemId());
                inventory.setQuantity(inventory.getQuantity() + existingOrderItem.getQuantity() - orderItemRequest.getQuantity());
                inventoryRepo.save(inventory);
            } else {
                throw new ProductNotFoundException("Product not found with id " + orderItemRequest.getItemId());
            }
        }

        // Manually remove orphaned items
        List<OrderItem> itemsToRemove = existingOrder.getOrderItems().stream()
                .filter(existingItem ->
                        updatedOrderItems.stream()
                                .noneMatch(updatedItem -> updatedItem.getOrderItemId() == existingItem.getOrderItemId()))
                .collect(Collectors.toList());

        existingOrder.getOrderItems().removeAll(itemsToRemove);

        // Clear existing items before adding updated items
        existingOrder.getOrderItems().clear();

        // Set the updated order items
        existingOrder.getOrderItems().addAll(updatedOrderItems);

        double totalPrice = 0;
        for (OrderItem orderItem : existingOrder.getOrderItems()) {
            totalPrice += orderItem.getTotalUnitPrice();
        }

        // Set the total price on the Order object
        existingOrder.setTotalPrice(totalPrice);

        // Save and return the updated order
        return orderRepo.save(existingOrder);
    }




    public String deleteOrder(int id){
        Order order = orderRepo.findById(id).orElse(null);
        if (order == null) {
            throw new OrderNotFoundException("Order not found with id " + id);
        }
        orderRepo.deleteById(id);
        return "order deleted with id " + id;
    }
*/
    public List<UserDelivery> getAllUserDelivery() {
        List<Order> orderList = orderRepo.findAll();
        List<UserDelivery> userDeliveryList = new ArrayList<>();
        for (Order order : orderList) {
            Delivery delivery = deliveryRepo.findByOrder(order);
            UserDelivery userDelivery = new UserDelivery();
            userDelivery.setDeliveryId(delivery.getDeliveryId());
            userDelivery.setOrderId(order.getOrderId());
            userDelivery.setDeliveryStatus(delivery.getStatus());
            userDelivery.setTotalPrice(order.getTotalPrice());
            userDelivery.setIsPrepared(order.getIsPrepared());

            userDeliveryList.add(userDelivery);
        }
        return userDeliveryList;
    }
}

