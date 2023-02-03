package com.example.project_sem_4.database.jdbc_query;

import java.text.ParseException;

import com.example.project_sem_4.database.dto.booking.BookingSearchDTO;
import com.example.project_sem_4.database.dto.order.OrderSearchDTO;
import com.example.project_sem_4.database.entities.*;
import com.example.project_sem_4.database.search_body.BookingSearchBody;
import com.example.project_sem_4.database.search_body.OrderSearchBody;
import com.example.project_sem_4.util.HelpConvertDate;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Log4j2
@Repository
public class QueryOrderByJDBC {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<OrderSearchDTO> filterWithPaging(OrderSearchBody searchBody) {
        
        return jdbcTemplate.query(stringQuery(searchBody) + " LIMIT "
                        + searchBody.getLimit() + " OFFSET " + searchBody.getLimit() * (searchBody.getPage() - 1)
                , new ResultSetExtractor<List<OrderSearchDTO>>() {
                    @Override
                    public List<OrderSearchDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        List<OrderSearchDTO> list = new ArrayList<OrderSearchDTO>();

                        while (rs.next()) {
                            OrderSearchDTO orderSearchDTO = new OrderSearchDTO();
                            Integer order_id = rs.getInt("orders.id");
                            Integer total_price = rs.getInt("orders.total_price");
                            String created_at = rs.getString("orders.created_at");
                            Integer status = rs.getInt("orders.status");
                            Integer user_id = rs.getInt("bookings.user_id");
                            orderSearchDTO.setId(order_id);
                            orderSearchDTO.setTotal_price(total_price);
                            orderSearchDTO.setStatus(status);
                            orderSearchDTO.setUser_id(user_id);
                            orderSearchDTO.setCreated_at(created_at);

                            Booking booking = new Booking();
                            booking.setId(rs.getString("bookings.id"));
                            booking.setDate(rs.getString("bookings.date"));
                            booking.setDate_booking(rs.getString("bookings.date_booking"));
                            booking.setTime_booking(rs.getString("bookings.time_booking"));
                            booking.setPhone(rs.getString("bookings.phone"));
                            booking.setUser_id(rs.getInt("bookings.user_id"));
                            booking.setStatus(rs.getInt("bookings.status"));
                            booking.setEmail(rs.getString("bookings.email"));
                            booking.setBranch_id(rs.getInt("bookings.branch_id"));
                            orderSearchDTO.setBooking(booking);

                            Branch branch = new Branch();
                            branch.setId(rs.getInt("branchs.id"));
                            branch.setName(rs.getString("branchs.name"));
                            branch.setAddress(rs.getString("branchs.address"));
                            branch.setStatus(rs.getInt("branchs.status"));
                            branch.setHot_line(rs.getString("branchs.hot_line"));
                            booking.setBranch(branch);

                            Account customer = new Account();
                            customer.setId(rs.getInt("accounts.id"));
                            customer.setEmail(rs.getString("accounts.email"));
                            orderSearchDTO.setCustomer(customer);

                            if (rs.getString("vouchers.id") != null) {
                                Voucher voucher = Voucher.builder()
                                        .voucherCode(rs.getString("vouchers.voucher_code"))
                                        .discount(rs.getDouble("vouchers.discount"))
                                        .build();
                                orderSearchDTO.setVoucher(voucher);
                            }
                            list.add(orderSearchDTO);
                        }

                        return list;
                    }
                });
    }

    public List<OrderSearchDTO> filterWithNoPaging(OrderSearchBody searchBody) {
        return jdbcTemplate.query(stringQuery(searchBody), new ResultSetExtractor<List<OrderSearchDTO>>() {
            @Override
            public List<OrderSearchDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<OrderSearchDTO> list = new ArrayList<OrderSearchDTO>();

                while (rs.next()) {
                    OrderSearchDTO orderSearchDTO = new OrderSearchDTO();
                    Integer order_id = rs.getInt("orders.id");
                    Integer total_price = rs.getInt("orders.total_price");
                    String created_at = rs.getString("orders.created_at");
                    Integer status = rs.getInt("orders.status");
                    Integer user_id = rs.getInt("bookings.user_id");
                    orderSearchDTO.setId(order_id);
                    orderSearchDTO.setTotal_price(total_price);
                    orderSearchDTO.setStatus(status);
                    orderSearchDTO.setUser_id(user_id);
                    orderSearchDTO.setCreated_at(created_at);

                    Booking booking = new Booking();
                    booking.setId(rs.getString("bookings.id"));
                    booking.setDate(rs.getString("bookings.date"));
                    booking.setDate_booking(rs.getString("bookings.date_booking"));
                    booking.setTime_booking(rs.getString("bookings.time_booking"));
                    booking.setPhone(rs.getString("bookings.phone"));
                    booking.setUser_id(rs.getInt("bookings.user_id"));
                    booking.setStatus(rs.getInt("bookings.status"));
                    booking.setEmail(rs.getString("bookings.email"));
                    booking.setBranch_id(rs.getInt("bookings.branch_id"));
                    orderSearchDTO.setBooking(booking);

                    Branch branch = new Branch();
                    branch.setId(rs.getInt("branchs.id"));
                    branch.setName(rs.getString("branchs.name"));
                    branch.setAddress(rs.getString("branchs.address"));
                    branch.setStatus(rs.getInt("branchs.status"));
                    branch.setHot_line(rs.getString("branchs.hot_line"));
                    booking.setBranch(branch);

                    Account customer = new Account();
                    customer.setId(rs.getInt("accounts.id"));
                    customer.setEmail(rs.getString("accounts.email"));
                    orderSearchDTO.setCustomer(customer);

                    if (rs.getString("vouchers.id") != null) {
                        Voucher voucher = Voucher.builder()
                                .voucherCode(rs.getString("vouchers.voucher_code"))
                                .discount(rs.getDouble("vouchers.discount"))
                                .build();
                        orderSearchDTO.setVoucher(voucher);
                    }
                    list.add(orderSearchDTO);
                }

                return list;
            }
        });
    }

    public String stringQuery(OrderSearchBody searchBody) {
        StringBuilder sqlQuery = new StringBuilder("Select orders.*, bookings.*,  accounts.*, branchs.*, vouchers.*");
        sqlQuery.append(" FROM orders " +
                " JOIN bookings ON orders.booking_id = bookings.id " +
                " JOIN accounts ON orders.customer_id = accounts.id " +
                " JOIN branchs ON bookings.branch_id = branchs.id " +
                " LEFT JOIN vouchers ON orders.voucher_id = vouchers.id ");

        sqlQuery.append(" Where 1=1");

        if (searchBody.getBooking_id() != null && searchBody.getBooking_id().length() > 0) {
            sqlQuery.append(" AND orders.booking_id = '" + searchBody.getBooking_id() + "'");
        }

        if (searchBody.getIdsOrder() != null && searchBody.getIdsOrder().size() > 0){

            sqlQuery.append(" AND orders.id in (");
            for(int i = 0; i < searchBody.getIdsOrder().size(); i++) {
                Integer Id = Integer.valueOf(searchBody.getIdsOrder().get(i));

                if(i != 0) {
                    sqlQuery.append(',');
                }
                sqlQuery.append(Id);
            }

            sqlQuery.append(")");
        }

        if (searchBody.getTime_booking() != null && searchBody.getTime_booking().length() > 0) {
            sqlQuery.append(" AND bookings.time_booking = '" + searchBody.getTime_booking()+"'");
        }


        if (searchBody.getVoucher_id() != null && searchBody.getVoucher_id().length() > 0) {
            sqlQuery.append(" AND orders.voucher_id = '" + searchBody.getVoucher_id() + "'");
        }

        if (searchBody.getCustomer_id() != null) {
            sqlQuery.append(" AND orders.customer_id = " + searchBody.getCustomer_id());
        }

        if (searchBody.getRangeTotalPriceStart() != null) {
            if (searchBody.getRangeTotalPriceEnd() != null) {
                sqlQuery.append(" AND orders.total_price >= " + searchBody.getRangeTotalPriceStart());
                sqlQuery.append(" AND orders.total_price <= " + searchBody.getRangeTotalPriceEnd());
            } else {
                sqlQuery.append(" AND orders.total_price = " + searchBody.getRangeTotalPriceStart());
            }
        }

        if (searchBody.getStatus() != null) {
            sqlQuery.append(" AND orders.status = " + searchBody.getStatus());
        } else {
            sqlQuery.append(" AND orders.status != -1");
        }

        if (searchBody.getStart() != null && searchBody.getStart().length() > 0) {
            LocalDate date = HelpConvertDate.convertStringToLocalDate(searchBody.getStart());
            sqlQuery.append(" AND orders.created_at >= '" + date + " 00:00:00' ");
        }

        if (searchBody.getEnd() != null && searchBody.getEnd().length() > 0) {
            LocalDate date = HelpConvertDate.convertStringToLocalDate(searchBody.getEnd());
            sqlQuery.append(" AND orders.created_at <= '" + date + " 23:59:59' ");
        }

        sqlQuery.append(" ORDER BY orders.id ");
        if (searchBody.getSort() != null && searchBody.getSort().equals("asc")) {
            sqlQuery.append(" ASC ");
        }
        if (searchBody.getSort() != null && searchBody.getSort().equals("desc")) {
            sqlQuery.append(" DESC ");
        }
//        log.info("check query: "+ sqlQuery);
        return sqlQuery.toString();
    }

    public String stringQueryForChart(String type) {
        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM orders as ord");
        switch (type) {
            case "line":
                break;
            case "pei":
                sqlQuery.append(" join bookings as bk on bk.id = ord.booking_id join branchs as br on br.id = bk.branch_id ");
                break;
            case "column":
                sqlQuery.append(" join accounts as acc on acc.id = ord.customer_id ");
                break;
            default:
                break;
        }
        sqlQuery.append(" where 1=1");
        sqlQuery.append(" and ord.status != -1 ORDER BY ord.created_at ASC ");
        return sqlQuery.toString();
    }
    public String stringQueryForStatus() {
        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM orders as ord");
        sqlQuery.append(" ORDER BY ord.created_at ASC ");
        return sqlQuery.toString();
    }
    public List<Object> filterOrderForStatus(){
        return jdbcTemplate.query(stringQueryForStatus()
                , new ResultSetExtractor<List<Object>>() {
                    @SneakyThrows
                    @Override
                    public List<Object> extractData(ResultSet rs) throws DataAccessException {
                        List<Object> list = new ArrayList<Object>();
                        Map<Integer, Map<String,Object>> orderKeyByStatus = new HashMap<>();
                        while (rs.next()) {
                            Integer KeyStatus = rs.getInt("ord.status");
                            Map<String, Object> jo = orderKeyByStatus.get(KeyStatus);
                            if(jo == null){
                                Double price = rs.getDouble("ord.total_price");
                                int count = 0;
                                jo = new HashMap<>();
                                jo.put("status", rs.getInt("ord.status"));
                                jo.put("price",price);
                                jo.put("count",++count);
                            }else {
                                Double price = (Double) jo.get("price");
                                int count = (int) jo.get("count");
                                price +=rs.getDouble("ord.total_price");
                                jo.put("price",price);
                                jo.put("count",++count);
                            }
                            orderKeyByStatus.put(KeyStatus,jo);
                        }
                    list.add(orderKeyByStatus);
                        return list;
                    }
                });
    }
    public List<Object> filterOrderForChartLine() {
        return jdbcTemplate.query(stringQueryForChart("line")
                , new ResultSetExtractor<List<Object>>() {
                    @SneakyThrows
                    @Override
                    public List<Object> extractData(ResultSet rs) throws DataAccessException {
                        List<Object> list = new ArrayList<Object>();
                        while (rs.next()) {
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = dateFormat.parse(rs.getString("created_at"));
                            Map<String, Object> jo = new HashMap<>();
                            String dateValue = dateFormat.format(date);
                            jo.put("Date", dateValue);
                            jo.put("id", rs.getInt("id"));
                            jo.put("scales", rs.getDouble("total_price"));
                            list.add(jo);
                        }

                        return list;
                    }
                });
    }

    public List<Object> filterOrderForChartPei() {
        return jdbcTemplate.query(stringQueryForChart("pei")
                , new ResultSetExtractor<List<Object>>() {
                    @SneakyThrows
                    @Override
                    public List<Object> extractData(ResultSet rs) throws DataAccessException {
                        List<Object> list = new ArrayList<Object>();
                        Map<Integer, Map<String, Object>> branches = new HashMap<>();
                        while (rs.next()) {
                            Integer branch_id = rs.getInt("bk.branch_id");
                            Map<String, Object> branch = branches.get(branch_id);
                            if (branch == null) {
                                branch = new HashMap<>();
                                List<Integer> order_ids = new ArrayList<>();
                                order_ids.add(rs.getInt("ord.id"));
                                Double total_prices = Double.valueOf(0);
                                total_prices += rs.getDouble("total_price");
                                branch.put("ids", order_ids);
                                branch.put("type", rs.getString("br.name"));
                                branch.put("branch_id", branch_id);
                                branch.put("value", total_prices);
                                branches.put(branch_id,branch);
                            } else {
                                List<Integer> orderBranchIds = (List<Integer>) branch.get("ids");
                                orderBranchIds.add(rs.getInt("ord.id"));
                                Double totalPricesBranch = (Double) branch.get("value");
                                totalPricesBranch += rs.getDouble("total_price");
                                branch.put("ids", orderBranchIds);
                                branch.put("value", totalPricesBranch);
                                branches.put(branch_id,branch);
                            }
                        }

                        list.add(branches.values());
                        return list;
                    }
                });
    }

    public List<Object> filterOrderForChartColumn() {
        return jdbcTemplate.query(stringQueryForChart("column")
                , new ResultSetExtractor<List<Object>>() {
                    @SneakyThrows
                    @Override
                    public List<Object> extractData(ResultSet rs) throws DataAccessException {
                        List<Object> list = new ArrayList<Object>();
                        Map<Integer, Map<String, Object>> accounts = new HashMap<>();
                        while (rs.next()) {
                            Integer account_id = rs.getInt("acc.id");
                            Map<String, Object> account = accounts.get(account_id);
                            if (account == null) {
                                account = new HashMap<>();
                                List<Integer> order_ids = new ArrayList<>();
                                order_ids.add(rs.getInt("ord.id"));
                                Double total_prices = Double.valueOf(0);
                                total_prices += rs.getDouble("total_price");
                                account.put("ids", order_ids);
                                account.put("type", rs.getString("acc.name"));
                                account.put("account_id", account_id);
                                account.put("sales", total_prices);
                                accounts.put(account_id,account);
                            } else {
                                List<Integer> orderAccountIds = (List<Integer>) account.get("ids");
                                orderAccountIds.add(rs.getInt("ord.id"));
                                Double totalPricesAccount = (Double) account.get("sales");
                                totalPricesAccount += rs.getDouble("total_price");
                                account.put("ids", orderAccountIds);
                                account.put("sales", totalPricesAccount);
                                accounts.put(account_id,account);
                            }
                        }

                        list.add(accounts.values());
                        return list;
                    }
                });
    }
}
