package com.bca.cis.service;

import com.bca.cis.model.PostCreateUpdateRequest;
import com.bca.cis.model.PostDetailResponse;
import com.bca.cis.model.PostIndexResponse;
import com.bca.cis.model.ResultPageResponseDTO;

public interface PostService {
    ResultPageResponseDTO<PostIndexResponse> listDataPostIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    PostDetailResponse findDataById(String id);

    void saveData(PostCreateUpdateRequest dto);

    void updateData(String id, PostCreateUpdateRequest dto);

    void deleteData(String id);
}
