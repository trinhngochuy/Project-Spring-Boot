package com.example.project_sem_4.controller;

import com.example.project_sem_4.database.dto.BlogDTO;
import com.example.project_sem_4.database.entities.Account;
import com.example.project_sem_4.database.repository.AccountRepository;
import com.example.project_sem_4.database.search_body.BlogSearchBody;
import com.example.project_sem_4.service.blog.BlogService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/blog")
public class BlogController {

    private final BlogService blogService;

    private final AccountRepository accountRepository;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestBody @Valid BlogDTO blogDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String  EmailAccount = authentication.getPrincipal().toString();
      Account account = accountRepository.findByEmail(EmailAccount).orElse(null);
      if (account == null){
          return new ResponseEntity<>("Account không được tìm thấy",HttpStatus.NOT_FOUND);
      }
        return new ResponseEntity<>(blogService.saveBlog(blogDTO,account), HttpStatus.OK);
    }
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public ResponseEntity<Object> update(@RequestBody @Valid BlogDTO blogDTO,@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String  EmailAccount = authentication.getPrincipal().toString();
        Account account = accountRepository.findByEmail(EmailAccount).orElse(null);
        return new ResponseEntity<>(blogService.updateBlog(blogDTO,account,id), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        return new ResponseEntity<>(blogService.deleteBlog(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> detail(@PathVariable Integer id) {
        return new ResponseEntity<>(blogService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity searchBlog(@RequestBody BlogSearchBody blogSearchBody) {
        return new ResponseEntity(blogService.findAll(blogSearchBody), HttpStatus.OK);
    }
}
