package com.example.project_sem_4.database.jdbc_query;
import com.example.project_sem_4.database.entities.TypeService;
import com.example.project_sem_4.database.search_body.TypeServiceSearchBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QueryTypeServiceByJDBC {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<TypeService> filterWithPaging(TypeServiceSearchBody searchBody){
        return  jdbcTemplate.query(sqlQuery(searchBody) + " LIMIT "
                + searchBody.getLimit() + " OFFSET " + searchBody.getLimit() * (searchBody.getPage()-1),new BeanPropertyRowMapper<>(TypeService.class));
    }

    public List<TypeService> filterWithNoPaging(TypeServiceSearchBody searchBody){
        return  jdbcTemplate.query(sqlQuery(searchBody),new BeanPropertyRowMapper<>(TypeService.class));
    }

    public String sqlQuery(TypeServiceSearchBody searchBody) {
        StringBuilder sql = new StringBuilder("SELECT * FROM type_services WHERE 1 = 1 ");
        if(searchBody.getName() != null && searchBody.getName().trim().length() > 0) {
            sql.append(" AND type_services.name LIKE '%" + searchBody.getName() + "%'");
        }
        if (searchBody.getStatus() != null){
            sql.append(" AND type_services.status = " + searchBody.getStatus());
        } else {
            sql.append(" AND type_services.status != -1");
        }
        sql.append(" ORDER BY type_services.id ");
        if (searchBody.getSort() != null && searchBody.getSort().equals("asc")){
            sql.append(" ASC ");
        }
        if (searchBody.getSort() != null && searchBody.getSort().equals("desc")){
            sql.append(" DESC ");
        }
        return sql.toString();
    }
}
