package com.example.techlog.post.repository;

import com.example.techlog.post.dto.PostSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<PostSimpleResponse> getPostPageWithWriterPage(Pageable pageable);

}
