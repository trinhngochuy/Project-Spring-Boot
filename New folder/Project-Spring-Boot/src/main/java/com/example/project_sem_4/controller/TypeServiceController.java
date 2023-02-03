package com.example.project_sem_4.controller;

import com.example.project_sem_4.database.dto.ServiceDTO;
import com.example.project_sem_4.database.entities.TypeService;
import com.example.project_sem_4.database.search_body.ServiceSearchBody;
import com.example.project_sem_4.database.search_body.TypeServiceSearchBody;
import com.example.project_sem_4.service.service.TypeServiceHair;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/type_service")
public class TypeServiceController {
    @Autowired
    private TypeServiceHair typeServiceHair;

    @PostMapping("/create")
    public ResponseEntity createService(@RequestBody @Valid TypeService typeService) {
        return new ResponseEntity<>(typeServiceHair.create(typeService), HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity deleteService(@PathVariable int id) {
        return new ResponseEntity<>(typeServiceHair.delete(id), HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity updateService(@PathVariable int id, @RequestBody TypeService typeService) {
        return new ResponseEntity<>(typeServiceHair.update(id, typeService), HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity searchService(@RequestBody TypeServiceSearchBody typeServiceSearchBody) {
        return new ResponseEntity(typeServiceHair.findTypeService(typeServiceSearchBody), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable int id) {
        return new ResponseEntity(typeServiceHair.findById(id), HttpStatus.OK);
    }

    @GetMapping("/findWithTypeServiceForOrderDTO")
    public ResponseEntity findWithTypeServiceForOrderDTO() {
        return new ResponseEntity(typeServiceHair.findWithTypeServiceForOrderDTO(), HttpStatus.OK);
    }
}
