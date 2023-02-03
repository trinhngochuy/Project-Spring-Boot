package com.example.project_sem_4.service.branch;

import com.example.project_sem_4.database.dto.BranchDTO;
import com.example.project_sem_4.database.entities.*;
import com.example.project_sem_4.database.jdbc_query.QueryBranchByJDBC;
import com.example.project_sem_4.database.repository.BranchRepository;
import com.example.project_sem_4.database.search_body.BranchSearchBody;
import com.example.project_sem_4.enum_project.StatusEnum;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.transaction.Transactional;
import java.sql.SQLDataException;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@CrossOrigin()
@Log4j2
public class BranchService {

    private final BranchRepository branchRepository;

    @Autowired
    QueryBranchByJDBC queryBranchByJDBC;


    public Branch findById(Integer id){
        return branchRepository.findById(id).orElse(null);
    }
    public Branch saveBranch(BranchDTO branchDTO) {
        Branch branch = new Branch();
        try {
            branch.setAddress(branchDTO.getAddress());
            branch.setHot_line(branchDTO.getHot_line());
            branch.setThumbnail(branchDTO.getThumbnail());
            branch.setName(branchDTO.getName());
            branch.setStatus(StatusEnum.ACTIVE.status);
            branch.setCreated_at(new Date());
            return branchRepository.save(branch);
        } catch (Exception ex) {
            return null;
        }
    }

    @Transactional
    public Branch updateBranch(BranchDTO branchDTO, Integer Id) {

        Branch branch = branchRepository.findById(Id).orElse(null);
        if (branch == null) {
            throw new ApiExceptionNotFound("branchs", "id", Id);
        }
        branch.setAddress(branchDTO.getAddress());
        branch.setHot_line(branchDTO.getHot_line());
        branch.setThumbnail(branchDTO.getThumbnail());
        branch.setName(branchDTO.getName());
        branch.setUpdated_at(new Date());
        return branchRepository.save(branch);

    }

    @Transactional
    public Branch deleteBranch(Integer Id) {
        Branch branch = branchRepository.findById(Id).orElse(null);
        if (branch == null) {
            throw new ApiExceptionNotFound("branchs", "id", Id);
        }
        branch.setStatus(StatusEnum.DELETE.status);
        return branchRepository.save(branch);
    }

    public Map<String, Object> findAll(BranchSearchBody branchSearchBody) {
        List<Branch> listContentPage = queryBranchByJDBC.filterWithPaging(branchSearchBody);
        List<Branch> listContentNoPage = queryBranchByJDBC.filterWithNoPaging(branchSearchBody);

        Map<String, Object> responses = new HashMap<>();
        responses.put("content", listContentPage);
        responses.put("currentPage", branchSearchBody.getPage());
        responses.put("totalItems", listContentNoPage.size());
        responses.put("totalPage", (int) Math.ceil((double) listContentNoPage.size() / branchSearchBody.getLimit()));
        return responses;
    }

}
