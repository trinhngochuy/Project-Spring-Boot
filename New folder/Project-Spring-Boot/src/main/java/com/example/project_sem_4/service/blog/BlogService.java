package com.example.project_sem_4.service.blog;

import com.example.project_sem_4.database.dto.BlogDTO;
import com.example.project_sem_4.database.entities.Account;
import com.example.project_sem_4.database.entities.Blog;
import com.example.project_sem_4.database.jdbc_query.QueryBlogByJDBC;
import com.example.project_sem_4.database.repository.BlogRepository;
import com.example.project_sem_4.database.search_body.BlogSearchBody;
import com.example.project_sem_4.enum_project.StatusEnum;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@CrossOrigin()
@Log4j2
public class BlogService {
    private final BlogRepository blogRepository;
    @Autowired
    QueryBlogByJDBC queryBlogByJDBC;


   public Blog findById(Integer Id){
       return blogRepository.findById(Id).orElse(null);
   }
    public Blog saveBlog(BlogDTO blogDTO, Account account) {
        Blog blog = new Blog();
        try {
            blog.setContent(blogDTO.getContent());
            blog.setTitle(blogDTO.getTitle());
            blog.setThumbnail(blogDTO.getThumbnail());
            blog.setDescription(blogDTO.getDescription());
            blog.setAccount(account);
            blog.setStatus(StatusEnum.ACTIVE.status);
            blog.setCreated_at(new Date());
            return blogRepository.save(blog);
        } catch (Exception ex) {
            return null;
        }
    }
    public Blog updateBlog(BlogDTO blogDTO, Account account,Integer Id) {
        Blog blog = blogRepository.findById(Id).orElse(null);
        if (blog == null) {
            throw new ApiExceptionNotFound("blogs", "id", Id);
        }
            blog.setContent(blogDTO.getContent());
            blog.setTitle(blogDTO.getTitle());
            blog.setThumbnail(blogDTO.getThumbnail());
            blog.setDescription(blogDTO.getDescription());
            blog.setAccount(account);
            blog.setUpdated_at(new Date());
            return blogRepository.save(blog);
    }
    public Blog deleteBlog(Integer Id) {
        Blog blog = blogRepository.findById(Id).orElse(null);
        if (blog == null) {
            throw new ApiExceptionNotFound("blogs", "id", Id);
        }
        blog.setStatus(StatusEnum.DELETE.status);
        blog.setUpdated_at(new Date());
        return blogRepository.save(blog);
    }
    public Map<String, Object> findAll(BlogSearchBody blogSearchBody) {
        List<Blog> listContentPage = queryBlogByJDBC.filterWithPaging(blogSearchBody);
        List<Blog> listContentNoPage = queryBlogByJDBC.filterWithNoPaging(blogSearchBody);

        Map<String, Object> responses = new HashMap<>();
        responses.put("content", listContentPage);
        responses.put("currentPage", blogSearchBody.getPage());
        responses.put("totalItems", listContentNoPage.size());
        responses.put("totalPage", (int) Math.ceil((double) listContentNoPage.size() / blogSearchBody.getLimit()));
        return responses;
    }
}
