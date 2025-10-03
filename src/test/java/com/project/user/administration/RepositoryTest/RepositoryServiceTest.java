package com.project.user.administration.RepositoryTest;

import com.project.user.administration.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RepositoryServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindUser() {
        User user = User.builder()
                .userName("anna")
                .password("pass123")
                .email("anna@example.com")
                .build();

        userRepository.save(user);

        User found = userRepository.findById(user.getUserId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getUserName()).isEqualTo("anna");
    }
}
