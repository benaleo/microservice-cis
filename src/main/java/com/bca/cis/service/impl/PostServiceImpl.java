package com.bca.cis.service.impl;

import com.bca.cis.entity.Post;
import com.bca.cis.exception.BadRequestException;
import com.bca.cis.model.PostCreateUpdateRequest;
import com.bca.cis.model.PostDetailResponse;
import com.bca.cis.model.PostIndexResponse;
import com.bca.cis.model.ResultPageResponseDTO;
import com.bca.cis.repository.PostRepository;
import com.bca.cis.service.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.bca.cis.util.PaginationUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private PostRepository repository;

    @Override
        public ResultPageResponseDTO<PostIndexResponse> listDataPostIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
            keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
            Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
            Pageable pageable = PageRequest.of(pages, limit, sort);
            Page<Post> pageResult = repository.findByNameLikeIgnoreCase(keyword, pageable);
            List<PostIndexResponse> dtos = pageResult.stream().map(this::convertToIndexResponse).collect(Collectors.toList());

            int currentPage = pageResult.getNumber() + 1;
            int totalPages = pageResult.getTotalPages();

            return PaginationUtil.createResultPageDTO(
                    pageResult.getTotalElements(), // total items
                    dtos,
                    currentPage, // current page
                    currentPage > 1 ? currentPage - 1 : 1, // prev page
                    currentPage < totalPages - 1 ? currentPage + 1 : totalPages - 1, // next page
                    1, // first page
                    totalPages - 1, // last page
                    pageResult.getSize() // per page
            );
        }

    @Override
    public PostDetailResponse findDataById(String id) throws BadRequestException {
        Post data = repository.findBySecureId(id)
                .orElseThrow(() -> new BadRequestException("Post not found"));

        return convertToDetailResponse(data);
    }

    @Override
    public void saveData(@Valid PostCreateUpdateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        Post data = convertToSave(dto);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(String id, PostCreateUpdateRequest dto) throws BadRequestException {
        // check exist and get
        Post data = getPostEntity(id);

        // update
        convertToUpdateRequest(data, dto);

        // update the updated_at
        data.setUpdatedAt(LocalDateTime.now());

        // save
        repository.save(data);
    }

    @Override
    public void deleteData(String id) throws BadRequestException {
        Post data = getPostEntity(id);
        // delete data
        if (!repository.existsById(data.getId())) {
            throw new BadRequestException("Post not found");
        } else {
            repository.deleteById(data.getId());
        }
    }

    // -- helper --
    private Post getPostEntity(String id){
        return repository.findBySecureId(id).orElseThrow(() -> new BadRequestException("Post not found"));
    }

    private PostIndexResponse convertToIndexResponse(Post data) {
        // convert to dto
        PostIndexResponse dto = new PostIndexResponse();
        dto.setId(data.getSecureId());
        dto.setDescription(data.getDescription());
        dto.setContent(data.getContent());
        return dto;
    }
    private PostDetailResponse convertToDetailResponse(Post data) {
        // convert to dto
        PostDetailResponse dto = new PostDetailResponse();
        dto.setId(data.getSecureId());
        dto.setDescription(data.getDescription());
        dto.setContent(data.getContent());
        return dto;
    }
    private Post convertToSave(PostCreateUpdateRequest dto) {
        Post data = new Post();
        data.setDescription(dto.getDescription());
        data.setContent(dto.getContent());
        return data;
    }
    private void convertToUpdateRequest(Post data, PostCreateUpdateRequest dto) {
        data.setDescription(dto.getDescription());
        data.setContent(dto.getContent());
    }
}
