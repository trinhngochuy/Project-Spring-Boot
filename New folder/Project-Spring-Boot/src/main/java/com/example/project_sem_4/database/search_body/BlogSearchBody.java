package com.example.project_sem_4.database.search_body;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class BlogSearchBody {
    private String title;
    private String auth_name;

    private Integer status;
    private String start;
    private String end;
    private Integer limit;
    private Integer page;
    private String sort;

    public BlogSearchBody() {
        this.limit = 4;
        this.page = 1;
    }
}
