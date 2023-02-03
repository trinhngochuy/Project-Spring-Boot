package com.example.project_sem_4.database.search_body;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ServiceSearchBody {
    private String name;
    private Integer type_service_id;
    private List<Integer> idsService;
    private Integer status;
    private String start;
    private String end;
    private Integer limit;
    private Integer page;
    private String sort;

    public ServiceSearchBody() {
        this.limit = 1;
        this.page = 4;
//        this.status = -1;
//        this.sort = "asc";
    }
}
