package com.example.project_sem_4.controller;

import com.example.project_sem_4.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/cloud")
@CrossOrigin(origins = "*")
public class CloudController {
    @Autowired
    private CloudinaryService cloudinaryService;

//    @GetMapping("/")
//    public String listUploadedFiles(Model model) throws IOException {
//
//        return "uploadForm";
//    }

    @GetMapping("/{asset_id}")
    public ResponseEntity<?> findImage(@PathVariable String asset_id) throws IOException {
        return new ResponseEntity<>(cloudinaryService.findFile(asset_id),HttpStatus.OK);
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(@RequestParam Integer limit, @RequestParam String next_cursor) throws IOException {
        return new ResponseEntity<>(cloudinaryService.findAll(limit,next_cursor),HttpStatus.OK);
    }

    @GetMapping("/delete")
    public ResponseEntity<?> deleteImage(@RequestParam String public_id) throws IOException {
        return new ResponseEntity<>(cloudinaryService.deleteFile(public_id),HttpStatus.OK);
    }

    @RequestMapping(value = "/uploads",method = RequestMethod.POST)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        String url = cloudinaryService.uploadFile(file);
        return new ResponseEntity<>(url,HttpStatus.OK);
    }
}
