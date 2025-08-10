package com.collab.user.service;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.collab.user.model.User;
import com.collab.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repo;

    @InjectMocks
    private UserService service;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("alice");
        user.setPassword("secret");
        user.setEmail("a@example.com");
    }

    @Test
    void register_savesUser() {
        when(repo.save(any(User.class))).thenReturn(user);
        User saved = service.register(user);
        assertThat(saved.getUsername()).isEqualTo("alice");
    }

    @Test
    void login_returnsUserWhenPasswordMatches() {
        when(repo.findByUsername("alice")).thenReturn(Optional.of(user));
        assertThat(service.login("alice", "secret")).isPresent();
    }

    @Test
    void login_emptyWhenPasswordMismatch() {
        when(repo.findByUsername("alice")).thenReturn(Optional.of(user));
        assertThat(service.login("alice", "wrong")).isEmpty();
    }

    @Test
    void updateProfile_updatesAndSaves() {
        when(repo.findById(1L)).thenReturn(Optional.of(user));
        when(repo.save(any(User.class))).thenReturn(user);

        User patch = new User();
        patch.setEmail("new@example.com");
        patch.setPassword("newpass");

        User updated = service.updateProfile(1L, patch);
        assertThat(updated.getEmail()).isEqualTo("new@example.com");
        assertThat(updated.getPassword()).isEqualTo("newpass");
    }

    @Test
    void updateProfile_whenNotFound_throws() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
    Exception ex = assertThrows(Exception.class, () -> service.updateProfile(99L, new User()));
    assertThat(ex).isNotNull();
    }
}
