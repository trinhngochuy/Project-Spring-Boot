package com.example.project_sem_4.controller;

import com.example.project_sem_4.database.entities.FeedBack;
import com.example.project_sem_4.database.search_body.FeedBackSearchBody;
import com.example.project_sem_4.service.feedback.FeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/feedback")
@CrossOrigin(origins = "*")
public class FeedBackController {
    @Autowired
    FeedBackService feedBackService;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody @Valid FeedBack feedBack) {
        return new ResponseEntity<>(feedBackService.create(feedBack), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity findAll(@RequestBody FeedBackSearchBody feedBackSearchBody) {
        return new ResponseEntity<>(feedBackService.findAll(feedBackSearchBody), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity findByid(@PathVariable Integer id) {
        return new ResponseEntity<>(feedBackService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/changeStatus/{id}")
    public ResponseEntity changeStatus(@PathVariable Integer id, @RequestParam Integer status) {
        return new ResponseEntity<>(feedBackService.changeStatus(id,status), HttpStatus.OK);
    }

    @GetMapping("/deleteRead/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        return new ResponseEntity<>(feedBackService.deleteRead(id), HttpStatus.OK);
    }
}
