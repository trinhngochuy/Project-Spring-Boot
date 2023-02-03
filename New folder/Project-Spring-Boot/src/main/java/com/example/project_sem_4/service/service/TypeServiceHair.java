package com.example.project_sem_4.service.service;

import com.example.project_sem_4.database.dto.search.service.ServiceSearchDTO;
import com.example.project_sem_4.database.dto.typeService.TypeServiceForOrderDTO;
import com.example.project_sem_4.database.entities.TypeService;
import com.example.project_sem_4.database.jdbc_query.QueryTypeServiceByJDBC;
import com.example.project_sem_4.database.repository.TypeServiceRepository;
import com.example.project_sem_4.database.search_body.ServiceSearchBody;
import com.example.project_sem_4.database.search_body.TypeServiceSearchBody;
import com.example.project_sem_4.enum_project.StatusEnum;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionBadRequest;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionCustomBadRequest;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionCustomNotFound;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TypeServiceHair {
    @Autowired
    TypeServiceRepository typeServiceRepository;

    @Autowired
    QueryTypeServiceByJDBC queryTypeServiceByJDBC;

    public TypeService findById(int id){
        TypeService typeService = typeServiceRepository.findById(id).orElse(null);
        if (typeService == null){
            throw new ApiExceptionCustomNotFound("Không tìm thấy loại dịch vụ");
        }
        return typeService;
    }

    public TypeService create(TypeService typeService){
        TypeService checkTypeService = typeServiceRepository.findByName(typeService.getName().trim());
        if (checkTypeService != null){
            throw new ApiExceptionCustomBadRequest("Trùng tên loại dịch vụ");
        }
        typeService.setStatus(StatusEnum.ACTIVE.status);
        typeService.setName(typeService.getName().trim());
        typeService.setCreated_at(new Date());
        return typeServiceRepository.save(typeService);
    }

    public TypeService update(int id,TypeService typeService){
        TypeService updateTypeService = typeServiceRepository.findById(id).orElse(null);
        if (updateTypeService == null){
            throw new ApiExceptionCustomNotFound("Không tìm thấy loại dịch vụ");
        }
        updateTypeService.setStatus(StatusEnum.ACTIVE.status);
        updateTypeService.setName(typeService.getName().trim());
        updateTypeService.setUpdated_at(new Date());
        return typeServiceRepository.save(updateTypeService);
    }

    public TypeService delete(int id){
        TypeService deleteTypeService = typeServiceRepository.findById(id).orElse(null);
        if (deleteTypeService == null){
            throw new ApiExceptionCustomNotFound("Không tìm thấy loại dịch vụ");
        }
        deleteTypeService.setStatus(-1);
        return typeServiceRepository.save(deleteTypeService);
    }

    public Map<String, Object> findTypeService(TypeServiceSearchBody searchBody){
        List<TypeService> listContentPage = queryTypeServiceByJDBC.filterWithPaging(searchBody);
        List<TypeService> listContentNoPage = queryTypeServiceByJDBC.filterWithNoPaging(searchBody);

        Map<String, Object> responses = new HashMap<>();
        responses.put("content",listContentPage);
        responses.put("currentPage",searchBody.getPage());
        responses.put("totalItems",listContentNoPage.size());
        responses.put("totalPage",(int) Math.ceil((double) listContentNoPage.size() / searchBody.getLimit()));
        return responses;
    }

    public List<TypeServiceForOrderDTO> findWithTypeServiceForOrderDTO(){
        List<TypeService> typeServiceList = typeServiceRepository.findAll();
        List<TypeServiceForOrderDTO> list = new ArrayList<>();
        for (TypeService typeService: typeServiceList) {
            TypeServiceForOrderDTO typeServiceForOrderDTO = new TypeServiceForOrderDTO();
            typeServiceForOrderDTO.setId(typeService.getId());
            typeServiceForOrderDTO.setName(typeService.getName());
            typeServiceForOrderDTO.setServices(typeService.getServices());
            list.add(typeServiceForOrderDTO);
        }
        return list;
    }
}
