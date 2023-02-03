package com.example.project_sem_4.database.search_body;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
    @AllArgsConstructor
    @Getter
    @Setter
    public class BranchSearchBody {
        private String address;
        private String hot_line;
        private String name;
        private Integer status;
        private String start;
        private String end;
        private Integer limit;
        private Integer page;
        private String sort;

        public BranchSearchBody() {
            this.limit = 4;
            this.page = 1;
        }
}
