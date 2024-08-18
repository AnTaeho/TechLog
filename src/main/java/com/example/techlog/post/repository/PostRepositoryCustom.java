package com.example.techlog.post.repository;

import com.example.techlog.common.RestPage;
import com.example.techlog.post.dto.PostSimpleResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    RestPage<PostSimpleResponse> getPostPageWithWriterPage(Pageable pageable);

    RestPage<PostSimpleResponse> searchByIds(List<Long> ids, Pageable pageable);

}
