package com.example.project_sem_4.database.jdbc_query;

import com.example.project_sem_4.database.dto.booking.BookingSearchDTO;
import com.example.project_sem_4.database.entities.Booking;
import com.example.project_sem_4.database.entities.Role;
import com.example.project_sem_4.database.search_body.BookingSearchBody;
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Repository
public class QueryBookingByJDBC {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<BookingSearchDTO> filterWithPaging(BookingSearchBody searchBody) {
        return jdbcTemplate.query(stringQuery(searchBody)
                , new ResultSetExtractor<List<BookingSearchDTO>>() {
            @Override
            public List<BookingSearchDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<BookingSearchDTO>  ListEmpForBooking = new ArrayList<BookingSearchDTO>();
                Map<Integer,BookingSearchDTO.Employee> liszt = new HashMap<Integer, BookingSearchDTO.Employee>();
                Map<Integer, Map<String,Booking>> bookingsMaper = new HashMap<Integer, Map<String,Booking>>();
                Map<Integer, Map<Integer,Role>> roleMaper = new HashMap<Integer, Map<Integer,Role>>();
                int count = 1;
                while (rs.next()) {
                    count++;
                    // Lấy ra tài khoản nhân viên
                    Integer keyAccount = rs.getInt("accounts.id");

                    BookingSearchDTO.Employee employee = liszt.get(keyAccount);

                    if (employee == null) {
                        employee = new BookingSearchDTO.Employee();
                        employee.setEmployee_name(rs.getString("accounts.name"));
                        liszt.put(keyAccount, employee);
                    }

                    // Lấy ra danh sách lịch đặt theo account nếu booking.employee_id = accounts.id
                    Integer keyBookingEmployee = rs.getInt("bookings.employee_id");
                    Map<String,Booking> bookingList = bookingsMaper.get(keyBookingEmployee);
                    Booking booking = new Booking();
                    booking.setId(rs.getString("bookings.id"));
                    booking.setEmployee_id(rs.getInt("bookings.employee_id"));
                    booking.setDate_booking(rs.getString("bookings.date_booking"));
                    booking.setBranch_id(rs.getInt("bookings.branch_id"));
                    booking.setTime_booking(rs.getString("bookings.time_booking"));
                    if (bookingList == null ){
                        bookingList = new HashMap<>();
                    }
                    bookingList.put(booking.getId(),booking);
                    bookingsMaper.put(keyBookingEmployee,bookingList);
                    employee.setBookingByTime_bookings(new ArrayList<>(bookingList.values()));

                    Integer keyRolesEmployee = rs.getInt("accounts.id");
                    Map<Integer,Role> roles = roleMaper.get(keyRolesEmployee);
                    Role role = new Role();
                    role.setId(rs.getInt("roles.id"));
                    role.setName(rs.getString("roles.name"));

                    if (roles == null ){
                        roles = new HashMap<>();
                    }
                    roles.put(role.getId(),role);
                    roleMaper.put(keyRolesEmployee,roles);

                    employee.setRoles(new ArrayList<>(roles.values()));
                }

                for ( Map.Entry<Integer, BookingSearchDTO.Employee> emp : liszt.entrySet()) {
                    BookingSearchDTO brschDTO = new BookingSearchDTO(emp.getValue());

                    ListEmpForBooking.add(brschDTO);
                }

                return  ListEmpForBooking;
            }
        });
    }

    public List<BookingSearchDTO> filterWithNoPaging(BookingSearchBody searchBody) {
        return jdbcTemplate.query(stringQuery(searchBody), new BeanPropertyRowMapper<>(BookingSearchDTO.class));
    }

    public String stringQuery(BookingSearchBody searchBody) {
        StringBuilder sqlQuery = new StringBuilder("Select *");
        sqlQuery.append(" FROM bookings " +
                " Join accounts ON bookings.employee_id = accounts.id" +
                " Join accounts_roles ON accounts.id = accounts_roles.account_id " +
                " Join roles ON accounts_roles.role_id = roles.id");

        sqlQuery.append(" Where 1=1");


        if (searchBody.getIdsBooking() != null && searchBody.getIdsBooking().size() > 0){

            sqlQuery.append(" AND bookings.id in (");
            for(int i = 0; i < searchBody.getIdsBooking().size(); i++) {
                String Id = String.valueOf(searchBody.getIdsBooking().get(i));

                if(i != 0) {
                    sqlQuery.append(',');
                }
                sqlQuery.append("'");
                sqlQuery.append(Id);
                sqlQuery.append("'");
            }

            sqlQuery.append(")");
        }

        if (searchBody.getBranch_id() != null) {
            sqlQuery.append(" AND bookings.branch_id = " + searchBody.getBranch_id());
        }

        if (searchBody.getEmployee_id() != null) {
            sqlQuery.append(" AND accounts.id = " + searchBody.getEmployee_id());
        }
        if (searchBody.getEmployee_name() != null && searchBody.getEmployee_name().length() > 0) {
            sqlQuery.append(" AND accounts.name LIKE '%" + searchBody.getEmployee_name() + "%'");
        }

        if (searchBody.getDate_booking() != null && searchBody.getDate_booking().length() > 0) {
            sqlQuery.append(" AND bookings.date_booking = '" + searchBody.getDate_booking() + "'");
        }

        if (searchBody.getTime_booking() != null && searchBody.getTime_booking().length() > 0) {
            sqlQuery.append(" AND bookings.time_booking = '" + searchBody.getTime_booking() + "'");
        }

        if (searchBody.getStatus() != null) {
            sqlQuery.append(" AND bookings.status = " + searchBody.getStatus());
        } else {
            sqlQuery.append(" AND bookings.status != -1");
        }

        if (searchBody.getStart() != null && searchBody.getStart().length() > 0) {
            LocalDate date = HelpConvertDate.convertStringToLocalDate(searchBody.getStart());
            sqlQuery.append(" AND bookings.created_at >= '" + date + " 00:00:00' ");
        }

        if (searchBody.getEnd() != null && searchBody.getEnd().length() > 0) {
            LocalDate date = HelpConvertDate.convertStringToLocalDate(searchBody.getEnd());
            sqlQuery.append(" AND bookings.created_at <= '" + date + " 23:59:59' ");
        }

        switch (searchBody.getSort()){
            case "asc":
                sqlQuery.append(" ORDER BY bookings.id ASC ");
                break;
            case "desc":
                sqlQuery.append(" ORDER BY bookings.id DESC ");
                break;
            case "time_bookingASC":
                sqlQuery.append(" ORDER BY bookings.time_booking ASC ");
                break;
            case "time_bookingDESC":
                sqlQuery.append(" ORDER BY bookings.time_booking DESC ");
                break;
            default:
                sqlQuery.append(" ORDER BY bookings.time_booking ASC ");
                break;
        }
        log.info("check query: " + sqlQuery);
        return sqlQuery.toString();
    }
    public String stringQueryForChart(String type) {
        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM bookings as bk");
        switch (type) {

            case "pei":
                sqlQuery.append(" join branchs as br on br.id = bk.branch_id ");
                break;

            case "heat":
                sqlQuery.append(" join accounts as acc on acc.id = bk.employee_id ");
                break;

            default:
                break;

        }
        sqlQuery.append(" where 1=1");
        sqlQuery.append(" and bk.status != -1 ORDER BY bk.date_booking ASC ");
        return sqlQuery.toString();
    }
    public String stringQueryForRange(){
        return "SELECT (SELECT COUNT(bookings.user_id) from bookings where user_id != 0)*100/(COUNT(user_id)) as total_booking FROM bookings as bk";
    }
    public List<Object> filterBookingForChartPei() {
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
                                List<String> booking_ids = new ArrayList<>();
                                booking_ids.add(rs.getString("bk.id"));
                                Integer total_booking = 0;
                                total_booking ++;
                                branch.put("ids", booking_ids);
                                branch.put("type", rs.getString("br.name"));
                                branch.put("branch_id", branch_id);
                                branch.put("value", total_booking);
                                branches.put(branch_id,branch);
                            } else {
                                List<String> bookingBranchIds = (List<String>) branch.get("ids");
                                bookingBranchIds.add(rs.getString("bk.id"));
                                Integer totalNumberBooking = (Integer) branch.get("value");
                                totalNumberBooking ++;
                                branch.put("ids", bookingBranchIds);
                                branch.put("value", totalNumberBooking);
                                branches.put(branch_id,branch);
                            }
                        }

                        list.add(branches.values());
                        return list;
                    }
                });
    }
    public List<Object> filterBookingForChartRange() {
        return jdbcTemplate.query(stringQueryForRange()
                , new ResultSetExtractor<List<Object>>() {
                    @SneakyThrows
                    @Override
                    public List<Object> extractData(ResultSet rs) throws DataAccessException {
                        List<Object> list = new ArrayList<Object>();
                        Float total_booking = (float) 0;
                        while (rs.next()) {
                             total_booking = rs.getFloat("total_booking");
                        }
                        Map<String,Object> values =
                                new HashMap<String, Object>();
                        values.put("total_booking",total_booking);
                        list.add(values );
                        return list;
                    }
                });
    }
    public List<Object> filterBookingForChartColumn(){
        return jdbcTemplate.query(stringQueryForChart("column")
                , new ResultSetExtractor<List<Object>>() {
                    @SneakyThrows
                    @Override
                    public List<Object> extractData(ResultSet rs) throws DataAccessException {
//                        List<Object> list = new ArrayList<Object>();
                        Map<String, Map<String, Object>> days = new HashMap<>();
                        while (rs.next()) {
                            String date_booking = rs.getString("bk.date_booking");
                            Map<String, Object> day = days.get(date_booking);
                            if (day == null) {
                                day = new HashMap<>();
                                List<String> booking_ids = new ArrayList<>();
                                booking_ids.add(rs.getString("bk.id"));
                                Integer total_booking = 0;
                                total_booking ++;
                                day.put("ids", booking_ids);
                                day.put("release", date_booking);
                                day.put("count", total_booking);
                                days.put(date_booking,day);
                            } else {
                                List<String> bookingIds = (List<String>) day.get("ids");
                                bookingIds.add(rs.getString("bk.id"));
                                Integer totalNumberBooking = (Integer) day.get("count");
                                totalNumberBooking ++;
                                day.put("ids", bookingIds);
                                day.put("count", totalNumberBooking);
                                days.put(date_booking,day);
                            }
                        }

//                        list.add(days.values());
                        return new ArrayList<Object>(days.values());
//                        return list;
                    }
                });
    }

    public List<Object> filterBookingForChartHeat(){
        return jdbcTemplate.query(stringQueryForChart("heat")
                , new ResultSetExtractor<List<Object>>() {
                    @SneakyThrows
                    @Override
                    public List<Object> extractData(ResultSet rs) throws DataAccessException {
                        List<Object> list = new ArrayList<Object>();
                        Map<String,  Map<Integer, Map<String, Object>>> days = new HashMap<>();
                        while (rs.next()) {
                            String date_booking = rs.getString("bk.date_booking");
                            Integer employee_id = rs.getInt("bk.employee_id");
                            Map<Integer, Map<String, Object>> employees_in_day = days.get(date_booking);
                            if (employees_in_day == null) {
                                employees_in_day= new HashMap<>();
                            }
                            Map<String, Object>  employee_in_day = PutEmployeeToList(date_booking,employees_in_day,employee_id,rs);
                            employees_in_day.put(employee_id,employee_in_day);
                            days.put(date_booking,employees_in_day);
                        }

                        days.forEach((k,e) -> {
                            e.values().forEach(element ->{
                                list.add(element);
                            });
                        });

                        return list;

                    }
                });
    }
    public Map<String, Object> PutEmployeeToList(String date_booking,Map<Integer, Map<String, Object>> employees_in_day,Integer employee_id,ResultSet rs) throws SQLException {
        Map<String, Object>  employee_in_day =  employees_in_day.get(employee_id);

        if (employee_in_day == null){
            employee_in_day = new HashMap<>();
            List<String> booking_ids = new ArrayList<>();
            booking_ids.add(rs.getString("bk.id"));
            Double total_booking = Double.valueOf(0.0);
            total_booking += 0.7639;
            employee_in_day.put("ids", booking_ids);
            employee_in_day.put("year", date_booking);
            employee_in_day.put("District", rs.getString("acc.name"));
            employee_in_day.put("AQHI", total_booking);
        }else{
            List<String> bookingIds = (List<String>) employee_in_day.get("ids");
            bookingIds.add(rs.getString("bk.id"));
            Double totalNumberBooking = (Double) employee_in_day.get("AQHI");
            totalNumberBooking  += 0.7639;
            employee_in_day.put("ids", bookingIds);
            employee_in_day.put("AQHI", totalNumberBooking);
        }
        return  employee_in_day;
    }
}
