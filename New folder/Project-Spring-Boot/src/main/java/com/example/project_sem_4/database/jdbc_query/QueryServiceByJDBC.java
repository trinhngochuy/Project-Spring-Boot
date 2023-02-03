package com.example.project_sem_4.database.jdbc_query;

import com.example.project_sem_4.database.dto.search.service.ServiceSearchDTO;
import com.example.project_sem_4.database.search_body.ServiceSearchBody;

import lombok.extern.log4j.Log4j2;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Log4j2
public class QueryServiceByJDBC {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<ServiceSearchDTO> filterWithPaging(ServiceSearchBody serviceSearchBody){
        return  jdbcTemplate.query(sqlQuery(serviceSearchBody) + " LIMIT "
                + serviceSearchBody.getLimit() + " OFFSET " + serviceSearchBody.getLimit() * (serviceSearchBody.getPage()-1),new BeanPropertyRowMapper<>(ServiceSearchDTO.class));
    }

    public List<ServiceSearchDTO> filterWithNoPaging(ServiceSearchBody serviceSearchBody){
        return  jdbcTemplate.query(sqlQuery(serviceSearchBody),new BeanPropertyRowMapper<>(ServiceSearchDTO.class));
    }

    public String sqlQuery(ServiceSearchBody serviceSearchBody) {
        StringBuilder sql = new StringBuilder("SELECT s.id as service_id, s.name as service_name, s.description as description, s.thumbnail as thumbnail, s.price as price," +
                "ts.id as typeServiceId FROM services s JOIN type_services ts ON s.type_service_id = ts.id WHERE 1 = 1 ");
        if(serviceSearchBody.getName() != null && serviceSearchBody.getName().trim().length() > 0) {
            sql.append(" AND s.name LIKE '%" + serviceSearchBody.getName() + "%'");
        }
        if(serviceSearchBody.getType_service_id() != null && serviceSearchBody.getType_service_id() > 0) {
            sql.append(" AND ts.id = " + serviceSearchBody.getType_service_id());
        }
        if (serviceSearchBody.getStatus() != null){
            sql.append(" AND s.status = " + serviceSearchBody.getStatus());
        } else {
            sql.append(" AND s.status != -1");
        }
        sql.append(" ORDER BY s.id ");
        if (serviceSearchBody.getSort() != null && serviceSearchBody.getSort().equals("asc")){
            sql.append(" ASC ");
        }
        if (serviceSearchBody.getSort() != null && serviceSearchBody.getSort().equals("desc")){
            sql.append(" DESC ");
        }
        return sql.toString();
    }

    public String stringQueryForChart(String type) {
        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM `order_details` as ordd");
        switch (type) {

            case "bar":
                sqlQuery.append(" join orders as ord  on ord.id = ordd.order_id  join services as sv on sv.id = ordd.service_id  ");
                break;
            default:
                break;
        }
        sqlQuery.append(" where 1=1");
        sqlQuery.append(" order BY ordd.service_id ");
        return sqlQuery.toString();
    }
    public List<Object> filterServiceForChartBar() {
        log.info(stringQueryForChart("bar"));

        return jdbcTemplate.query(stringQueryForChart("bar")
                , new ResultSetExtractor<List<Object>>() {
                    @SneakyThrows
                    @Override
                    public List<Object> extractData(ResultSet rs) throws DataAccessException {

                        Map<Integer, Map<String, Object>> services = new HashMap<>();
                        while (rs.next()) {
                            Integer service_id = rs.getInt("ordd.service_id");
                            Map<String, Object> service = services.get(service_id);
                            if (service == null) {
                                service = new HashMap<>();
                                List<Integer> order_ids = new ArrayList<>();
                                order_ids.add(rs.getInt("ordd.order_id"));

                                service.put("ids", order_ids);
                                service.put("year", rs.getString("sv.name"));
                                service.put("value", order_ids.size());
                                DateFormat formatter = new SimpleDateFormat("YYYY-MM-DD");
                                service.put("created_at",formatter.format(rs.getDate("ord.created_at")));
                                services.put(service_id,service);
                            } else {
                                List<Integer> orderIds = (List<Integer>) service.get("ids");
                                orderIds.add(rs.getInt("ordd.order_id"));
                                service.put("ids", orderIds);
                                service.put("value", orderIds.size());
                                services.put(service_id,service);
                            }
                        }

                return new ArrayList<Object>(services.values());
                    }
                });
    }
    public String countService(){
        StringBuilder sqlQuery = new StringBuilder("SELECT COUNT(*) as count_services FROM `services` WHERE `status` != -1");
        return sqlQuery.toString();
    }

    public String countStaff(){
        StringBuilder sqlQuery = new StringBuilder("SELECT COUNT(*) as count_staffs FROM `accounts_roles` as acr join accounts as acc on acc.id = acr.account_id WHERE acc.status != -1 and acr.role_id = 3");
        return sqlQuery.toString();
    }

    public Object filterCountStaffAndService() {
        Map<String,Integer> list = new HashMap<>();
     Integer count_services =   jdbcTemplate.query(countService()
             ,new ResultSetExtractor<Integer>() {
                 @SneakyThrows
                 @Override
                 public Integer extractData(ResultSet rs) throws DataAccessException {
                     Integer services = 0;
                     while (rs.next()) {
                         services = rs.getInt("count_services");
                     }
                     return  services;
                 }});
        Integer count_staffs =   jdbcTemplate.query(countStaff()
                ,new ResultSetExtractor<Integer>() {
                    @SneakyThrows
                    @Override
                    public Integer extractData(ResultSet rs) throws DataAccessException {
                        Integer staffs = 0;
                        while (rs.next()) {
                            staffs = rs.getInt("count_staffs");
                        }
                        return  staffs;
                    }});
        list.put("count_services",count_services);
        list.put("count_staffs",count_staffs);

        return list;
    }
}
