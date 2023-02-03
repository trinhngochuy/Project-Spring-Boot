package com.example.project_sem_4.database.jdbc_query;

import com.example.project_sem_4.database.dto.search.voucher.VoucherSearchDTO;
import com.example.project_sem_4.database.entities.Branch;
import com.example.project_sem_4.database.entities.Voucher;
import com.example.project_sem_4.database.search_body.BranchSearchBody;
import com.example.project_sem_4.database.search_body.VoucherSearchBody;
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
public class QueryVoucherByJDBC {
    @Autowired
    JdbcTemplate jdbcTemplate;
    public List<VoucherSearchDTO> filterWithPaging(VoucherSearchBody searchBody){
        return  jdbcTemplate.query(stringQuery(searchBody) + " LIMIT "
                + searchBody.getLimit() + " OFFSET " + searchBody.getLimit() * (searchBody.getPage()-1),new BeanPropertyRowMapper<>(VoucherSearchDTO.class));
    }

    public List<VoucherSearchDTO> filterWithNoPaging(VoucherSearchBody searchBody){
        return  jdbcTemplate.query(stringQuery(searchBody),new BeanPropertyRowMapper<>(VoucherSearchDTO.class));
    }

    public String stringQuery(VoucherSearchBody searchBody){
        StringBuilder sqlQuery = new StringBuilder("Select *, vouchers.is_used ");
        sqlQuery.append(" FROM vouchers Where 1=1 ");

        if (searchBody.getName() != null && searchBody.getName().length() > 0){
            sqlQuery.append(" AND vouchers.name Like '%" + searchBody.getName() + "%'");
        }

        if (searchBody.getVoucher_code() != null && searchBody.getVoucher_code().length() > 0){
            sqlQuery.append(" AND vouchers.voucher_code Like '%" + searchBody.getVoucher_code() + "%'");
        }

        if (searchBody.getIs_used() != null){
            sqlQuery.append(" AND vouchers.is_used = " + searchBody.getIs_used());
        } else {
            sqlQuery.append(" AND vouchers.is_used != -1");
        }

        if (searchBody.getStatus() != null){
            sqlQuery.append(" AND vouchers.status = " + searchBody.getStatus());
        } else {
            sqlQuery.append(" AND vouchers.status != -1");
        }

        sqlQuery.append(" ORDER BY vouchers.id ");
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
