package com.example.JPA.controller.restApiControllers;

import com.example.JPA.dto.user.UserPersonalDetailsDto;
import com.example.JPA.dto.OrdersDto;
import com.example.JPA.model.Orders;
import com.example.JPA.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping()
    public ResponseEntity<Void> createOrder(@RequestBody UserPersonalDetailsDto dto){
        System.out.println(dto);
        orderService.createOrder(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Orders>> getOrders(){
        return new ResponseEntity<>(orderService.getAllOrders(),HttpStatus.OK);
    }

    @GetMapping(value = "{startDate}/{endDate}")
    public ResponseEntity<List<OrdersDto>> getOrdersByDates(@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate){
        System.out.println(orderService.getOrdersByDates(startDate,endDate));
        return new ResponseEntity<>(orderService.getOrdersByDates(startDate,endDate),HttpStatus.OK);
    }
}
