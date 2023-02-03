package com.example.project_sem_4.service.feedback;

import com.example.project_sem_4.database.entities.FeedBack;

import com.example.project_sem_4.database.jdbc_query.QueryFeedBackByJDBC;
import com.example.project_sem_4.database.repository.FeedBackRepository;
import com.example.project_sem_4.database.search_body.FeedBackSearchBody;
import com.example.project_sem_4.enum_project.FeedBackEnum;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionBadRequest;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FeedBackService {
    @Autowired
    FeedBackRepository feedBackRepository;

    @Autowired
    QueryFeedBackByJDBC queryFeedBackByJDBC;

    public FeedBack create(FeedBack feedBack){
        feedBack.setCreated_at(new Date());
        return feedBackRepository.save(feedBack);
    }

    public Map<String, Object> findAll(FeedBackSearchBody feedBackSearchBody){
        List<FeedBack> listContentPage = queryFeedBackByJDBC.filterWithPaging(feedBackSearchBody);
        List<FeedBack> listContentNoPage = queryFeedBackByJDBC.filterWithNoPaging(feedBackSearchBody);

        Map<String, Object> responses = new HashMap<>();
        responses.put("content",listContentPage);
        responses.put("currentPage",feedBackSearchBody.getPage());
        responses.put("totalItems",listContentNoPage.size());
        responses.put("totalPage",(int) Math.ceil((double) listContentNoPage.size() / feedBackSearchBody.getLimit()));
        return responses;
    }

    public FeedBack findById(int id){
        FeedBack feedBack = feedBackRepository.findById(id).orElse(null);
        if (feedBack == null){
            throw new ApiExceptionNotFound("feedbacks","id",id);
        }
        return feedBack;
    }

    public FeedBack changeStatus(int id,int status){
        FeedBack checkFeedBack = feedBackRepository.findById(id).orElse(null);
        if (checkFeedBack == null){
            throw new ApiExceptionNotFound("feed_backs","id",id);
        }
        checkFeedBack.setUpdated_at(new Date());
        switch (status){
            case 1:
                checkFeedBack.setStatus(FeedBackEnum.READED.status);
                break;
            case 0:
                checkFeedBack.setStatus(FeedBackEnum.NOT_READED.status);
                break;
            case -1:
                checkFeedBack.setStatus(FeedBackEnum.DELETE.status);
                break;
            default:
                throw new ApiExceptionBadRequest("feedbacks","status","Không tìm thấy trạng thái: " + status);
        }
        return feedBackRepository.save(checkFeedBack);
    }

    public FeedBack deleteRead(int id){
        FeedBack checkFeedBack = feedBackRepository.findById(id).orElse(null);
        if (checkFeedBack == null){
            throw new ApiExceptionNotFound("feed_backs","id",id);
        }
        checkFeedBack.setUpdated_at(new Date());
        checkFeedBack.setStatus(FeedBackEnum.DELETE.status);
        return feedBackRepository.save(checkFeedBack);
    }
}
