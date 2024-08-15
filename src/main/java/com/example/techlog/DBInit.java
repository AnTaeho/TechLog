//package com.example.techlog;
//
//import com.example.techlog.post.domain.Post;
//import com.example.techlog.post.repository.PostRepository;
//import com.example.techlog.user.domain.User;
//import com.example.techlog.user.repository.UserRepository;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//@Component
//@RequiredArgsConstructor
//public class DBInit {
//
//    private final UserRepository userRepository;
//    private final PostRepository postRepository;
//
//
////    @PostConstruct
//    @Transactional
//    public void init() {
//        User user = new User("bb", "bb", "bb");
//        User savedUser = userRepository.save(user);
//        for (int i = 0; i < 10000; i++) {
//            Post post = new Post("title" + i, "yoyak" + i, "content" + i, null, savedUser);
//            postRepository.save(post);
//        }
//    }
//
//}
