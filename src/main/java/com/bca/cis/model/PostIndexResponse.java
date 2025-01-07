package com.bca.cis.model;

import lombok.Data;

@Data
public class PostIndexResponse {

    private String id;
    private String description;
    private String content;
    private PostOwner postOwner;

    @Data
    public static class PostOwner {
        private String id;
        private String name;
    }

}
