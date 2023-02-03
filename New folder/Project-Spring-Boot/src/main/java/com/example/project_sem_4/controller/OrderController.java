package com.example.project_sem_4.controller;

import com.example.project_sem_4.database.dto.order.OrderDTO;
import com.example.project_sem_4.database.dto.order.OrderDetailDTO;
import com.example.project_sem_4.database.search_body.OrderSearchBody;
import com.example.project_sem_4.service.order.OrderDetailService;
import com.example.project_sem_4.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @Autowired
    OrderDetailService orderDetailService;

    @PostMapping("/create")
    public ResponseEntity createOrder(@RequestBody @Valid OrderDTO orderDTO) {
        return new ResponseEntity<>(orderService.create(orderDTO), HttpStatus.OK);
    }
    @PostMapping("/createOrderDetail")
    public ResponseEntity createOrderDetail(@RequestBody @Valid OrderDetailDTO orderDetailDTO) {
        return new ResponseEntity<>(orderDetailService.create(orderDetailDTO), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity detail(@PathVariable Integer id) {
        return new ResponseEntity<>(orderService.findAllByOrder(id), HttpStatus.OK);
    }

    @GetMapping("/update/status/{id}")
    public ResponseEntity updateOrderStatus(@PathVariable Integer id, @RequestParam Integer status) {
        return new ResponseEntity<>(orderService.updateOrderStatus(id,status), HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity createOrderDetail(@RequestBody OrderSearchBody orderSearchBody) {
        return new ResponseEntity<>(orderService.findAll(orderSearchBody), HttpStatus.OK);
    }

    @PostMapping("/dashboard-line")
    public ResponseEntity dashBoardLine() {
        return new ResponseEntity<>(orderService.findChartLine(), HttpStatus.OK);
    }

    @PostMapping("/dashboard-pei")
    public ResponseEntity dashBoardPie() {
        return new ResponseEntity<>(orderService.findChartPie(), HttpStatus.OK);
    }

    @PostMapping("/dashboard-column")
    public ResponseEntity dashBoardColumn() {
        return new ResponseEntity<>(orderService.findChartColumn(), HttpStatus.OK);
    }

    @PostMapping("/dashboard-status")
    public ResponseEntity dashBoardStatus() {
        return new ResponseEntity<>(orderService.findOrderbyStatuses(), HttpStatus.OK);
    }

}
