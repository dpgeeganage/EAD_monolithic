package com.EAD.EAD_monolithic.service;

import com.EAD.EAD_monolithic.Exception.OrderNotFoundException;
import com.EAD.EAD_monolithic.dto.DeliveryDTO;
import com.EAD.EAD_monolithic.dto.DeliveryPerson;
import com.EAD.EAD_monolithic.entity.Delivery;
import com.EAD.EAD_monolithic.entity.Order;
import com.EAD.EAD_monolithic.repo.DeliveryRepo;
import com.EAD.EAD_monolithic.repo.OrderRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DeliveryService {
    @Autowired
    private DeliveryRepo deliveryRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrderService orderService;

    public List<Delivery> getAllDeliveries(){
        List<Delivery>deliveryList = deliveryRepo.findAll();
        return modelMapper.map(deliveryList, new TypeToken<List<Delivery>>(){}.getType());
    }

    public Delivery newDelivery(DeliveryDTO deliveryDTO){
        Delivery delivery = new Delivery();
        delivery.setDeliveryId(deliveryDTO.getDeliveryId());
        delivery.setOrder(orderService.getOrderById(deliveryDTO.getOrderId()));
        delivery.setStatus(deliveryDTO.getStatus());
        deliveryRepo.save(delivery);
        return delivery;
    }

    public Delivery getDeliveryById(int deliveryId) {
        Delivery delivery = deliveryRepo.findById(deliveryId).orElse(null);
        if (delivery == null) {
            throw new OrderNotFoundException("Delivery not found with id " + deliveryId);
        }
        return delivery;
    }


    public Delivery editDelivery(int deliveryId, DeliveryDTO deliveryDTO){
        Delivery delivery = getDeliveryById(deliveryId);
        //delivery.setDeliveryId(deliveryDTO.getDeliveryId());
        delivery.setOrder(orderService.getOrderById(deliveryDTO.getOrderId()));
        delivery.setStatus(deliveryDTO.getStatus());
        deliveryRepo.save(delivery);
        return delivery;
    }

    public boolean deleteDelivery(int deliveryId){
        deliveryRepo.deleteById(deliveryId);
        return true;
    }

//    public Delivery getDeliveryById (int id) {
//        Delivery delivery = deliveryRepo.findById(id).orElse(null);
//        if (delivery == null) {
//            throw new OrderNotFoundException("Order not found with id " + id);
//        }
//        return delivery;
//    }

    public List<DeliveryPerson> getDeliveryPerson() {
        List<Order> orderList = orderRepo.findAll();
        List<DeliveryPerson> deliveryPersonList = new ArrayList<>();
        for (Order order : orderList) {
            Delivery delivery = deliveryRepo.findByOrder(order);
            DeliveryPerson deliveryPerson = new DeliveryPerson();
            deliveryPerson.setDeliveryId(delivery.getDeliveryId());
            deliveryPerson.setDeliveryStatus(delivery.getStatus());
            deliveryPerson.setTotalPrice(order.getTotalPrice());
            deliveryPerson.setIsPrepared(order.getIsPrepared());

            deliveryPersonList.add(deliveryPerson);
        }
        return deliveryPersonList;
    }
}