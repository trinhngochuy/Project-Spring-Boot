package com.example.project_sem_4.database.jdbc_query;

import com.example.project_sem_4.database.entities.Branch;
import com.example.project_sem_4.database.entities.FeedBack;
import com.example.project_sem_4.database.search_body.BranchSearchBody;
import com.example.project_sem_4.database.search_body.BranchSearchBody;
import com.example.project_sem_4.util.HelpConvertDate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Log4j2
@Repository
public class QueryBranchByJDBC {
    @Autowired
    JdbcTemplate jdbcTemplate;
    public List<Branch> filterWithPaging(BranchSearchBody searchBody){
        return  jdbcTemplate.query(stringQuery(searchBody) + " LIMIT "
                + searchBody.getLimit() + " OFFSET " + searchBody.getLimit() * (searchBody.getPage()-1),new BeanPropertyRowMapper<>(Branch.class));
    }

    public List<Branch> filterWithNoPaging(BranchSearchBody searchBody){
        return  jdbcTemplate.query(stringQuery(searchBody),new BeanPropertyRowMapper<>(Branch.class));
    }

    public String stringQuery(BranchSearchBody searchBody){
        StringBuilder sqlQuery = new StringBuilder("Select * ");
        sqlQuery.append(" FROM branchs Where 1=1 ");

        if (searchBody.getName() != null && searchBody.getName().length() > 0){
            sqlQuery.append(" AND branchs.name Like '%" + searchBody.getName() + "%'");
        }

        if (searchBody.getAddress() != null && searchBody.getAddress().length() > 0){
            sqlQuery.append(" AND branchs.address Like '%" + searchBody.getAddress() + "%'");
        }

        if (searchBody.getHot_line() != null && searchBody.getHot_line().length() > 0){
            sqlQuery.append(" AND branchs.hot_line Like '%" + searchBody.getHot_line() + "%'");
        }

        if (searchBody.getStatus() != null){
            sqlQuery.append(" AND branchs.status = " + searchBody.getStatus());
        } else {
            sqlQuery.append(" AND branchs.status != -1");
        }

        if (searchBody.getStart() != null && searchBody.getStart().length() > 0){
            LocalDate date = HelpConvertDate.convertStringToLocalDate(searchBody.getStart());
            sqlQuery.append(" AND branchs.created_at >= '" + date + " 00:00:00' ");
        }

        if (searchBody.getEnd() != null && searchBody.getEnd().length() > 0){
            LocalDate date = HelpConvertDate.convertStringToLocalDate(searchBody.getEnd());
            sqlQuery.append(" AND branchs.created_at <= '" + date + " 23:59:59' ");
        }

        sqlQuery.append(" ORDER BY branchs.id ");
        if (searchBody.getSort() != null && searchBody.getSort().equals("asc")){
            sqlQuery.append(" ASC ");
        }
        if (searchBody.getSort() != null && searchBody.getSort().equals("desc")){
            sqlQuery.append(" DESC ");
        }
        log.info("check query: "+ sqlQuery);
        return sqlQuery.toString();
    }
}
