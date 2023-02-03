package com.example.project_sem_4.database.search_body;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class FeedBackSearchBody {
    private String title;
    private String email;
    private String phone;
    private Integer status;
    private String start;
    private String end;
    private Integer limit;
    private Integer page;
    private String sort;

    public FeedBackSearchBody() {
        this.limit = 1;
        this.page = 4;
    }
}
