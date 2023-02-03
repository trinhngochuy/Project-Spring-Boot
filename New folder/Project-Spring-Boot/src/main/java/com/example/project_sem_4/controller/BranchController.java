package com.example.project_sem_4.controller;

import com.example.project_sem_4.database.dto.BranchDTO;
import com.example.project_sem_4.database.entities.Branch;
import com.example.project_sem_4.database.repository.ResponeRepository;
import com.example.project_sem_4.database.search_body.BranchSearchBody;
import com.example.project_sem_4.database.search_body.ServiceSearchBody;
import com.example.project_sem_4.service.branch.BranchService;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.pool.TypePool;

import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/branch")
public class BranchController {
    private final BranchService branchService;
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    //@RequestBody @Valid BranchDTO branchDTO
    public ResponseEntity<Object> create(@RequestBody @Valid BranchDTO branchDTO) {
        return new ResponseEntity<>(branchService.saveBranch(branchDTO), HttpStatus.OK);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public ResponseEntity<Object> update(@RequestBody @Valid BranchDTO branchDTO,@PathVariable Integer id) {
        return ResponseEntity.ok().body(branchService.updateBranch(branchDTO,id));
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        return ResponseEntity.ok().body(branchService.deleteBranch(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable int id) {
        return new ResponseEntity(branchService.findById(id), HttpStatus.OK);
    }


    @PostMapping("/search")
    public ResponseEntity searchBranch(@RequestBody BranchSearchBody branchSearchBody) {
        return new ResponseEntity(branchService.findAll(branchSearchBody), HttpStatus.OK);
    }
}
