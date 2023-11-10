package com.EAD.EAD_monolithic.controller;

import com.EAD.EAD_monolithic.dto.OrderDTO;
import com.EAD.EAD_monolithic.dto.OrderRequest;
import com.EAD.EAD_monolithic.dto.UserDelivery;
import com.EAD.EAD_monolithic.entity.Order;
import com.EAD.EAD_monolithic.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/order")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/saveOrder")
    public OrderDTO saveOrder(@RequestBody OrderRequest orderRequest){
        Order order = orderService.saveOrder(orderRequest);
        return modelMapper.map(order, OrderDTO.class);
    }

    @GetMapping("/getOrders")
    public List<OrderDTO> getOrder(){
        return orderService.getAllOrders();
    }

    @GetMapping("/getOrderById/{id}")
    public OrderDTO getOrderById(@PathVariable int id){
        return modelMapper.map(orderService.getOrderById(id), OrderDTO.class);
    }

/*    @PutMapping("/updateOrder/{id}")
    public Order updateOrder(@RequestBody OrderRequest orderRequest, @PathVariable int id){
        return orderService.updateOrder(orderDTO,id);
    }*/

    @DeleteMapping("/deleteOrder/{id}")
    public String deleteOrder(@PathVariable int id){
        return orderService.deleteOrder(id);
    }

    @GetMapping("/getAllUserDelivery")
    public List<UserDelivery> getAllUserDelivery() {
        return orderService.getAllUserDelivery();
    }


}