package com.example.project_sem_4.controller;

import com.example.project_sem_4.database.dto.ServiceDTO;
import com.example.project_sem_4.database.entities.ServiceModel;
import com.example.project_sem_4.database.search_body.ServiceSearchBody;
import com.example.project_sem_4.service.service.ServiceHair;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/service")
public class ServiceController {
    @Autowired
    private ServiceHair serviceHair;

    @PostMapping("/create")
    public ResponseEntity createService(@RequestBody @Valid ServiceDTO serviceDTO) {
        return new ResponseEntity<>(serviceHair.createService(serviceDTO), HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity deleteService(@PathVariable int id) {
        return new ResponseEntity<>(serviceHair.deleteService(id), HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity updateService(@PathVariable int id, @RequestBody ServiceDTO serviceDTO) {
        return new ResponseEntity<>(serviceHair.updateService(id, serviceDTO), HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity searchService(@RequestBody ServiceSearchBody serviceSearchBody) {
        return new ResponseEntity(serviceHair.findService(serviceSearchBody), HttpStatus.OK);
    }

    @PostMapping("/dashboard-bar")
    public ResponseEntity dashBoardBar() {
        return new ResponseEntity(serviceHair.findChartBar(), HttpStatus.OK);
    }

    @PostMapping("/dashboard-count")
    public ResponseEntity countServicesAndStaffs() {
        return new ResponseEntity(serviceHair.countServicesAndStaffs(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable int id) {
        return new ResponseEntity(serviceHair.findById(id), HttpStatus.OK);
    }

    @GetMapping("/findAllTypeService")
    public ResponseEntity findAllTypeService() {
        return new ResponseEntity(serviceHair.findAllTypeService(), HttpStatus.OK);
    }
}
