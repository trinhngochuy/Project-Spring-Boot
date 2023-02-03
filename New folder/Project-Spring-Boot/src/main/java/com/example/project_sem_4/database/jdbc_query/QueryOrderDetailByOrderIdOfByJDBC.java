package com.example.project_sem_4.database.jdbc_query;

import com.example.project_sem_4.database.dto.order.OrderDetailByOrderIdDTO;
import com.example.project_sem_4.database.dto.order.OrderSearchDTO;
import com.example.project_sem_4.database.entities.Account;
import com.example.project_sem_4.database.entities.Booking;
import com.example.project_sem_4.database.entities.Branch;
import com.example.project_sem_4.database.entities.Voucher;
import com.example.project_sem_4.database.search_body.OrderSearchBody;
import com.example.project_sem_4.util.HelpConvertDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class QueryOrderDetailByOrderIdOfByJDBC {
    @Autowired
    JdbcTemplate jdbcTemplate;

//    public List<OrderDetailByOrderIdDTO> filterWithPaging(Integer id) {
//        return jdbcTemplate.query(stringQuery(id)
//                , new ResultSetExtractor<List<OrderDetailByOrderIdDTO>>() {
//                    @Override
//                    public List<OrderDetailByOrderIdDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
//                        List<OrderSearchDTO>  list = new ArrayList<OrderSearchDTO>();
//
//                        while (rs.next()) {
//                            OrderSearchDTO orderSearchDTO = new OrderSearchDTO();
//                            Integer order_id = rs.getInt("orders.id");
//                            orderSearchDTO.setId(order_id);
//
//                            Booking booking = new Booking();
//                            booking.setId(rs.getString("bookings.id"));
//                            booking.setDate(rs.getString("bookings.date"));
//                            booking.setDate_booking(rs.getString("bookings.date_booking"));
//                            booking.setTime_booking(rs.getString("bookings.time_booking"));
//                            booking.setTime_booking(rs.getString("bookings.phone"));
//                            booking.setTime_booking(rs.getString("bookings.user_id"));
//                            booking.setTime_booking(rs.getString("bookings.status"));
//                            booking.setEmail(rs.getString("bookings.email"));
//                            booking.setTime_booking(rs.getString("bookings.branch_id"));
//                            orderSearchDTO.setBooking(booking);
//
//                            Branch branch = new Branch();
//                            branch.setId(rs.getInt("branchs.id"));
//                            branch.setName(rs.getString("branchs.name"));
//                            branch.setAddress(rs.getString("branchs.address"));
//                            branch.setStatus(rs.getInt("branchs.status"));
//                            branch.setHot_line(rs.getString("branchs.hot_line"));
//                            booking.setBranch(branch);
//
//                            Account customer = new Account();
//                            customer.setId(rs.getInt("accounts.id"));
//                            customer.setEmail(rs.getString("accounts.email"));
//                            orderSearchDTO.setCustomer(customer);
//
//                            if (rs.getString("vouchers.id") != null){
//                                Voucher voucher = Voucher.builder()
//                                        .voucherCode(rs.getString("vouchers.voucher_code"))
//                                        .discount(rs.getDouble("vouchers.discount"))
//                                        .build();
//                                orderSearchDTO.setVoucher(voucher);
//                            }
//                            list.add(orderSearchDTO);
//                        }
//
//                        return list;
//                    }
//                });
//    }

    public String stringQuery(Integer id) {
        StringBuilder sqlQuery = new StringBuilder("Select order_details.*,  orders.*, services.*");
        sqlQuery.append(" FROM order_details " +
                " JOIN orders ON order_details.order_id = orders.id " +
                " JOIN services ON order_details.service_id = services.id ");

        sqlQuery.append(" Where 1=1 ");

        sqlQuery.append(" AND order_details.order_id = " + id);

        sqlQuery.append(" ORDER BY order_details.service_id ASC ");
        return sqlQuery.toString();
    }
}
