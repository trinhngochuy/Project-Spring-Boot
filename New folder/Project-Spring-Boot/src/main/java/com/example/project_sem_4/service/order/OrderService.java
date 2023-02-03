package com.example.project_sem_4.service.order;

import com.example.project_sem_4.database.dto.booking.BookingSearchDTO;
import com.example.project_sem_4.database.dto.order.OrderDTO;
import com.example.project_sem_4.database.dto.order.OrderDetailByOrderIdDTO;
import com.example.project_sem_4.database.dto.order.OrderSearchDTO;
import com.example.project_sem_4.database.entities.*;
import com.example.project_sem_4.database.jdbc_query.QueryOrderByJDBC;
import com.example.project_sem_4.database.repository.*;
import com.example.project_sem_4.database.search_body.BookingSearchBody;
import com.example.project_sem_4.database.search_body.OrderSearchBody;
import com.example.project_sem_4.enum_project.StatusEnum;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionBadRequest;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionCustomNotFound;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private QueryOrderByJDBC queryOrderByJDBC;

    @Transactional
    public Order create(OrderDTO orderDTO){
        //Kiểm tra tài khoản
        Account checkCustomer = accountRepository.findById(orderDTO.getCustomer_id()).orElse(null);
        if (checkCustomer == null){
            throw new ApiExceptionNotFound("orders","customer_id",orderDTO.getCustomer_id());
        }
        //Kiểm tra voucher
        if (orderDTO.getVoucher_id() != null && orderDTO.getVoucher_id().length() > 0){
            Voucher voucher = voucherRepository.findByVoucherCode(orderDTO.getVoucher_id());
            if (voucher == null){
                throw new ApiExceptionNotFound("orders","voucher_id",orderDTO.getVoucher_id());
            }
            if (voucher.getExpired_date().after(new Date())){
                throw new ApiExceptionBadRequest("orders","voucher_id", "Voucher hết hạn " + orderDTO.getVoucher_id());
            }
            if (voucher.is_used()){
                throw new ApiExceptionBadRequest("orders","voucher_id", "Voucher đã được sử dụng " + orderDTO.getVoucher_id());
            }
        }
        //Kiểm tra mã Booking và trạng thái
        Booking checkBooking = bookingRepository.findById(orderDTO.getBooking_id()).orElse(null);
        if (checkBooking == null){
            throw new ApiExceptionNotFound("orders","booking_id",orderDTO.getBooking_id());
        }
        if (checkBooking.getStatus() == StatusEnum.ACTIVE.status){
            throw new ApiExceptionBadRequest("orders","booking_id","Lịch đã được đặt " + orderDTO.getBooking_id());
        }

        if (checkCustomer.getId() == 1){
            checkBooking.setStatus(StatusEnum.ACTIVE.status);
            checkBooking.setEmail(orderDTO.getEmail());
            checkBooking.setPhone(orderDTO.getPhone());
            checkBooking.setName_booking(orderDTO.getName_booking());
            checkBooking.setUser_id(orderDTO.getCustomer_id());
            checkBooking.setUpdated_at(new Date());
        } else {
            checkBooking.setStatus(StatusEnum.ACTIVE.status);
            checkBooking.setEmail(checkCustomer.getEmail());
            checkBooking.setPhone(checkCustomer.getPhone());
            checkBooking.setName_booking(checkCustomer.getName());
            checkBooking.setUser_id(checkCustomer.getId());
            checkBooking.setUpdated_at(new Date());
        }
        bookingRepository.save(checkBooking);

        Order order = orderRepository.save(Order.builder()
                .booking_id(orderDTO.getBooking_id())
                .customer(checkCustomer)
                .voucher_id(orderDTO.getVoucher_id())
                .build());
        order.setCreated_at(new Date());
        return orderRepository.save(order);
    }

    public List<OrderDetailByOrderIdDTO> findAllByOrder(int id){
        List<OrderDetail> orderDetailList = orderDetailRepository.findAllByOrder_id(id);
        List<OrderDetailByOrderIdDTO> list = new ArrayList<>();
        for (OrderDetail orderDetail: orderDetailList) {
            OrderDetailByOrderIdDTO orderDetailByOrderIdDTO  = new OrderDetailByOrderIdDTO();
            Order order = orderRepository.findById(orderDetail.getPk().getOrder_id()).orElse(null);
            ServiceModel serviceModel = serviceRepository.findById(orderDetail.getPk().getService_id()).orElse(null);
            if (order == null){
                throw new ApiExceptionCustomNotFound("Không tìm thấy order có id là: " + orderDetail.getPk().getOrder_id());
            }

            if (serviceModel == null){
                throw new ApiExceptionCustomNotFound("Không tìm thấy service có id là: " + orderDetail.getPk().getService_id());
            }
            orderDetailByOrderIdDTO.setOrder_id(orderDetail.getPk().getOrder_id());
            orderDetailByOrderIdDTO.setOrder(order);
            orderDetailByOrderIdDTO.setService_id(orderDetail.getPk().getService_id());
            orderDetailByOrderIdDTO.setServiceModel(serviceModel);
            orderDetailByOrderIdDTO.setUnit_price((int) orderDetail.getUnit_price());
            list.add(orderDetailByOrderIdDTO);
        }

        return list;
    }

    public Order updateOrderStatus(int id, int status){
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null){
            throw new ApiExceptionNotFound("order","id",id);
        }
        boolean checkStatus = false;

        for (StatusEnum statusEnum: StatusEnum.values()) {
            if (statusEnum.status == status){
                checkStatus = true;
            }
        }
        if (!checkStatus){
            throw new ApiExceptionBadRequest("order","status", "Không tồn tại trạng thái là" + status);
        }
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public Map<String, Object> findAll(OrderSearchBody searchBody) {
        List<OrderSearchDTO> listContentPage = queryOrderByJDBC.filterWithPaging(searchBody);
        List<OrderSearchDTO> listContentNoPage = queryOrderByJDBC.filterWithNoPaging(searchBody);

        Map<String, Object> responses = new HashMap<>();
        responses.put("content",listContentPage);
        responses.put("currentPage",searchBody.getPage());
        responses.put("totalItems",listContentNoPage.size());
        responses.put("totalPage",(int) Math.ceil((double) listContentNoPage.size() / searchBody.getLimit()));
        return responses;
    }

    public   List<Object> findChartLine(){

        List<Object> responses = queryOrderByJDBC.filterOrderForChartLine();
        return  responses;
    }
    public   List<Object> findChartPie(){
        List<Object> responses = queryOrderByJDBC.filterOrderForChartPei();
        return  responses;
    }
    public   List<Object> findChartColumn(){

        List<Object> responses = queryOrderByJDBC.filterOrderForChartColumn();
        return  responses;
    }
    public   List<Object> findOrderbyStatuses(){
        List<Object> responses = queryOrderByJDBC.filterOrderForStatus();
        return  responses;
    }
}
