package com.example.techlog.post.repository;

import com.example.techlog.post.dto.PostSimpleResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<PostSimpleResponse> getPostPageWithWriterPage(Pageable pageable);

    Page<PostSimpleResponse> searchByIds(List<Long> ids, Pageable pageable);

}
