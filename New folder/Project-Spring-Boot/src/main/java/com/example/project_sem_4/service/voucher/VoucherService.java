package com.example.project_sem_4.service.voucher;

import com.example.project_sem_4.database.dto.VoucherDTO;
import com.example.project_sem_4.database.dto.search.voucher.VoucherSearchDTO;
import com.example.project_sem_4.database.entities.Branch;
import com.example.project_sem_4.database.entities.Voucher;
import com.example.project_sem_4.database.jdbc_query.QueryBranchByJDBC;
import com.example.project_sem_4.database.jdbc_query.QueryVoucherByJDBC;
import com.example.project_sem_4.database.repository.AccountRepository;
import com.example.project_sem_4.database.repository.VoucherRepository;
import com.example.project_sem_4.database.search_body.BranchSearchBody;
import com.example.project_sem_4.database.search_body.VoucherSearchBody;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.Id;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
@CrossOrigin(origins = "*")
public class VoucherService {
    private final VoucherRepository voucherRepository;

    @Autowired
    QueryVoucherByJDBC queryVoucherByJDBC;

    public boolean saveVoucher(VoucherDTO voucherDTO){
        Voucher newVoucher = new Voucher();
        int voucherCodeInt = getVoucherCodeInt();
        newVoucher.setVoucherCodeInt(voucherCodeInt);
        newVoucher.setVoucherCode(getVoucherCode(voucherCodeInt));
        newVoucher.setName(voucherDTO.getName());
        newVoucher.setDiscount(voucherDTO.getDiscount());
        newVoucher.setExpired_date(voucherDTO.getExpired_date());
        newVoucher.set_used(false);
        Voucher result = voucherRepository.save(newVoucher);
        return result != null;
    };

    public Voucher getVoucherByVoucher_code(String id){
        return voucherRepository.findByVoucherCode(id);
    }

    public boolean deleteVoucherByVoucher_code(String id){
        Voucher voucher = voucherRepository.findByVoucherCode(id);
        if (voucher == null){
            throw new ApiExceptionNotFound("vouchers","id",id);
        }
        voucherRepository.delete(voucher);
        return true;
    }

    public boolean disableVoucher(String voucherCode){
        Voucher checkVoucher = voucherRepository.findByVoucherCode(voucherCode);
        if(checkVoucher == null || checkVoucher.getExpired_date().before(new Date())){return false;}
        checkVoucher.set_used(true);
        Voucher result = voucherRepository.save(checkVoucher);
        return result != null;
    };

    public int getVoucherCodeInt(){
       List<Voucher> listResult =  voucherRepository.findAllByOrderByVoucherCodeIntDesc();
       if (listResult.isEmpty()){return  1;}
        return listResult.get(0).getVoucherCodeInt() + 1;
    }

    public String getVoucherCode(int a){
      String result = "HNVC-QB-" + a;
      return result;
    };

    public Map<String, Object> findAll(VoucherSearchBody voucherSearchBody) {
        List<VoucherSearchDTO> listContentPage = queryVoucherByJDBC.filterWithPaging(voucherSearchBody);
        List<VoucherSearchDTO> listContentNoPage = queryVoucherByJDBC.filterWithNoPaging(voucherSearchBody);

        Map<String, Object> responses = new HashMap<>();
        responses.put("content", listContentPage);
        responses.put("currentPage", voucherSearchBody.getPage());
        responses.put("totalItems", listContentNoPage.size());
        responses.put("totalPage", (int) Math.ceil((double) listContentNoPage.size() / voucherSearchBody.getLimit()));
        return responses;
    }
}
