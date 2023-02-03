package com.example.project_sem_4.service.service;

import com.example.project_sem_4.database.dto.ServiceDTO;
import com.example.project_sem_4.database.dto.search.account.AccountSearchDTO;
import com.example.project_sem_4.database.dto.search.service.ServiceSearchDTO;
import com.example.project_sem_4.database.entities.Role;
import com.example.project_sem_4.database.entities.ServiceModel;
import com.example.project_sem_4.database.entities.TypeService;
import com.example.project_sem_4.database.jdbc_query.QueryServiceByJDBC;
import com.example.project_sem_4.database.repository.ServiceRepository;
import com.example.project_sem_4.database.repository.TypeServiceRepository;
import com.example.project_sem_4.database.search_body.AccountSearchBody;
import com.example.project_sem_4.database.search_body.ServiceSearchBody;
import com.example.project_sem_4.enum_project.StatusEnum;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionBadRequest;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionNotFound;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.Type;
import java.util.*;

@Service
public class ServiceHair {
    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private TypeServiceRepository typeServiceRepository;

    @Autowired
    private QueryServiceByJDBC queryServiceByJDBC;

    public ServiceModel findById(int serviceId) {
        ServiceModel serviceModel = serviceRepository.findById(serviceId).orElse(null);
        if (serviceModel == null) {
            throw new ApiExceptionNotFound("services","id",serviceId);
        }
        return serviceModel;
    }

    public List<TypeService> findAllTypeService(){
        return typeServiceRepository.findAllByStatusNot(-1);
    }

    public boolean deleteService(int serviceId) {
        ServiceModel serviceModel = serviceRepository.findById(serviceId).orElse(null);
        if (serviceModel == null) {
            return false;
        }
        serviceModel.setStatus(StatusEnum.DELETE.status);
        serviceRepository.save(serviceModel);
        return true;
    }

    public boolean updateService(int serviceId, ServiceDTO serviceDTO) {
        if(serviceDTO.getTypeServiceId() == null) {
            return false;
        }
        ServiceModel serviceModel = serviceRepository.findById(serviceId).orElse(null);
        TypeService typeService = typeServiceRepository.findById(serviceDTO.getTypeServiceId()).orElse(null);
        if(serviceModel == null || typeService == null) {
            return false;
        }
        serviceModel.setName(serviceDTO.getName());
        serviceModel.setDescription(serviceDTO.getDescription());
        serviceModel.setType_service_id(serviceDTO.getTypeServiceId());
        serviceModel.setTypeService(typeService);
        serviceModel.setThumbnail(serviceDTO.getThumbnail());
        serviceModel.setUpdated_at(new Date());
        serviceRepository.save(serviceModel);
        return true;
    }

    public ServiceModel createService(ServiceDTO serviceDTO) {
//        if(serviceDTO.getTypeServiceId() == null) {
//            throw new ApiExceptionBadRequest("service","type_service_id","");
//        }
        TypeService typeService = typeServiceRepository.findById(serviceDTO.getTypeServiceId()).orElse(null);
        if(typeService == null) {
            throw new ApiExceptionNotFound("type_services","id",serviceDTO.getTypeServiceId());
        }
        ServiceModel serviceModel = new ServiceModel(serviceDTO,typeService);
        serviceModel.setTypeService(typeService);
        serviceModel.setCreated_at(new Date());
        return serviceRepository.save(serviceModel);
//        return true;
    }

    public Map<String, Object> findService(ServiceSearchBody searchBody){
        List<ServiceSearchDTO> listContentPage = queryServiceByJDBC.filterWithPaging(searchBody);
        for (ServiceSearchDTO service: listContentPage) {
            TypeService typeService = typeServiceRepository.findById(service.getTypeServiceId()).orElse(null);
            service.setType_service(typeService);
        }
        List<ServiceSearchDTO> listContentNoPage = queryServiceByJDBC.filterWithNoPaging(searchBody);

        Map<String, Object> responses = new HashMap<>();
        responses.put("content",listContentPage);
        responses.put("currentPage",searchBody.getPage());
        responses.put("totalItems",listContentNoPage.size());
        responses.put("totalPage",(int) Math.ceil((double) listContentNoPage.size() / searchBody.getLimit()));
        return responses;
    }

    public   List<Object> findChartBar(){
        List<Object> responses = queryServiceByJDBC.filterServiceForChartBar();
        return  responses;
    }

    public   Object countServicesAndStaffs(){
        Object responses = queryServiceByJDBC.filterCountStaffAndService();
        return  responses;
    }
}
