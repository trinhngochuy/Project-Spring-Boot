package com.example.project_sem_4.database.jdbc_query;

import com.example.project_sem_4.database.dto.search.account.AccountSearchDTO;
import com.example.project_sem_4.database.search_body.AccountSearchBody;
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
public class QueryAccountByJDBC {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<AccountSearchDTO> filterWithPaging(AccountSearchBody searchBody){
        return  jdbcTemplate.query(stringQuery(searchBody) + " LIMIT "
                + searchBody.getLimit() + " OFFSET " + searchBody.getLimit() * (searchBody.getPage()-1),new BeanPropertyRowMapper<>(AccountSearchDTO.class));
    }

    public List<AccountSearchDTO> filterWithNoPaging(AccountSearchBody searchBody){
//        log.info("check: "+ stringQuery(searchBody));
        return  jdbcTemplate.query(stringQuery(searchBody),new BeanPropertyRowMapper<>(AccountSearchDTO.class));
    }

    public String stringQuery(AccountSearchBody searchBody){
        StringBuilder sqlQuery = new StringBuilder("Select accounts.id as accounts_id,accounts.name as accounts_name,accounts.email as accounts_email,accounts.phone,accounts.gender,accounts.address,accounts.thumbnail,accounts.total_payment,accounts.created_at as account_created_at,accounts.description as account_description ");

        sqlQuery.append(" ,(CONCAT(\"[\",GROUP_CONCAT(CONCAT('{\"id\":\"', roles.id, '\", \"name\":\"',roles.name,'\"}')),']')) as rolesListBefore")
                .append(" FROM accounts ")
                .append(" Join accounts_roles ").append(" ON accounts.id = accounts_roles.account_id ")
                .append(" Join roles ").append(" ON accounts_roles.role_id = roles.id ");

            if (searchBody.getMember_ship_class_id() != null){
                sqlQuery.append(" Join membership_classes ").append(" ON accounts.member_ship_class_id = membership_classes.id ");
            }
            sqlQuery.append(" Where 1=1 ");

            if (searchBody.getName() != null && searchBody.getName().length() > 0){
                sqlQuery.append(" AND accounts.name Like '%" + searchBody.getName() + "%'");
            }

            if (searchBody.getEmail() != null && searchBody.getEmail().length() > 0){
                sqlQuery.append(" AND accounts.email Like '%" + searchBody.getEmail() + "%'");
            }

            if (searchBody.getPhone() != null && searchBody.getPhone().length() > 0){
                sqlQuery.append(" AND accounts.phone Like '%" + searchBody.getPhone() + "%'");
            }

            if (searchBody.getGender() != null && searchBody.getGender().length() > 0){
                sqlQuery.append(" AND accounts.gender = '" + searchBody.getGender() +"'");
            }

            if (searchBody.getRole_id() != null){
                sqlQuery.append(" AND roles.id = " + searchBody.getRole_id());
            }

            if (searchBody.getStatus() != null){
                sqlQuery.append(" AND accounts.status = " + searchBody.getStatus());
            } else {
                sqlQuery.append(" AND accounts.status != -1");
            }

            if (searchBody.getMember_ship_class_id() != null){
                sqlQuery.append(" AND accounts.member_ship_class_id = " + searchBody.getMember_ship_class_id());
            }

            if (searchBody.getStart() != null && searchBody.getStart().length() > 0){
                LocalDate date = HelpConvertDate.convertStringToLocalDate(searchBody.getStart());
                sqlQuery.append(" AND accounts.created_at >= '" + date + " 00:00:00' ");
            }

            if (searchBody.getEnd() != null && searchBody.getEnd().length() > 0){
                LocalDate date = HelpConvertDate.convertStringToLocalDate(searchBody.getEnd());
                sqlQuery.append(" AND accounts.created_at <= '" + date + " 23:59:59' ");
            }

        sqlQuery.append(" GROUP BY accounts_id ");
        sqlQuery.append(" ORDER BY accounts.id ");
        if (searchBody.getSort() != null && searchBody.getSort().equals("asc")){
            sqlQuery.append(" ASC ");
        }
        if (searchBody.getSort() != null && searchBody.getSort().equals("desc")){
            sqlQuery.append(" DESC ");
        }
        log.info("check query: "+ sqlQuery);
        return sqlQuery.toString();
    }

//    private boolean checkAllSearch(AccountSearchBody searchBody) {
//        return (searchBody.getName() != null && searchBody.getName().length() > 0) ||
//                (searchBody.getEmail() != null && searchBody.getEmail().length() > 0) ||
//                (searchBody.getPhone() != null && searchBody.getPhone().length() > 0) ||
//                (searchBody.getGender() != null && searchBody.getGender().length() > 0) ||
//                (searchBody.getRole_id() != -1) ||
//                (searchBody.getStatus() != -1) ||
//                (searchBody.getMember_ship_class_id() != -1) ||
//                (searchBody.getStart() != null && searchBody.getStart().length() > 0) ||
//                (searchBody.getEnd() != null && searchBody.getEnd().length() > 0);
//    }
}
