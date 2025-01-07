package com.bca.cis.seeder;

import com.bca.cis.entity.AppUser;
import com.bca.cis.entity.Post;
import com.bca.cis.repository.AppUserRepository;
import com.bca.cis.repository.PostRepository;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class PostSeeder {

    private final PostRepository postRepository;
    private final AppUserRepository userRepository;

//    @Scheduled(fixedDelay = 50)
    public void run() {
        Faker faker = new Faker();

        Post newPost = new Post();
        newPost.setId(null);
        newPost.setDescription(faker.lorem().sentence(10));
        newPost.setContent(faker.avatar().image());
        newPost.setPostAt(LocalDateTime.now());
        newPost.setCreatedAt(LocalDateTime.now().minusSeconds(LocalDateTime.now().getSecond()));
        newPost.setUpdatedAt(LocalDateTime.now());
        newPost.setUser(getUser(userRepository));

        postRepository.save(newPost);
    }

    private AppUser getUser(AppUserRepository userRepository){
        Faker faker = new Faker();
        Long totalUser= userRepository.count();
        Integer randomUser = faker.random().nextInt(1, totalUser.intValue());

        return userRepository.findById(Long.valueOf(randomUser)).orElse(null);
    }
}
