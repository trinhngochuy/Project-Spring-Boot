package com.example.project_sem_4.service.authen;

import com.example.project_sem_4.database.entities.Role;
import com.example.project_sem_4.database.repository.RoleRepository;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;
    public Role getRole(String roleName){
        Role role = roleRepository.findByName(roleName).orElse(null);
        if(role == null) {
            throw new ApiExceptionNotFound("roles","name","Không thấy role " + roleName);
        }
        return role;
    }
}
