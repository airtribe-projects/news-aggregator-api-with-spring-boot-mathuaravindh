package org.airtribe.newsaggregator;

import org.airtribe.newsaggregator.entity.Preference;
import org.airtribe.newsaggregator.entity.User;
import org.airtribe.newsaggregator.entity.enums.NewsCategory;
import org.airtribe.newsaggregator.repository.PreferenceRepository;
import org.airtribe.newsaggregator.repository.UserRepository;
import org.airtribe.newsaggregator.dto.UserRegistrationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.airtribe.newsaggregator.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PreferenceRepository preferenceRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_ShouldRegisterNewUser_WhenValidDataProvided() {
        // Arrange
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setUsername("testUser");
        registrationDTO.setPassword("password123");
        registrationDTO.setEmail("test@example.com");
        registrationDTO.setPreferences(List.of("SPORTS", "BUSINESS"));

        when(userRepository.existsByEmail(registrationDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registrationDTO.getPassword())).thenReturn("encodedPassword");
        when(preferenceRepository.findByCategory(NewsCategory.SPORTS)).thenReturn(Optional.of(new Preference(NewsCategory.SPORTS)));
        when(preferenceRepository.findByCategory(NewsCategory.BUSINESS)).thenReturn(Optional.of(new Preference(NewsCategory.BUSINESS)));

        User savedUser = new User();
        savedUser.setUsername("testUser");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.registerUser(registrationDTO);

        // Assert
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailAlreadyExists() {
        // Arrange
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setEmail("duplicate@example.com");
        when(userRepository.existsByEmail(registrationDTO.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(registrationDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void assignPreferencesToUser_ShouldAssignPreferences_WhenValidPreferencesProvided() {
        User user = new User();
        user.setUsername("testUser");
        user.setPreferences(new HashSet<>());

        when(preferenceRepository.findByCategory(NewsCategory.SPORTS)).thenReturn(Optional.of(new Preference(NewsCategory.SPORTS)));
        when(preferenceRepository.findByCategory(NewsCategory.BUSINESS)).thenReturn(Optional.of(new Preference(NewsCategory.BUSINESS)));

        Set<Preference> result = userService.assignPreferencesToUser(user, List.of("SPORTS", "BUSINESS"));

        assertEquals(2, result.size());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getUserByUserName_ShouldReturnUser_WhenUserExists() {
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByUserName("testUser");

        assertTrue(result.isPresent());
        assertEquals("testUser", result.get().getUsername());
    }

    @Test
    void getUserByUserName_ShouldReturnEmptyOptional_WhenUserDoesNotExist() {
        when(userRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByUserName("unknownUser");

        assertFalse(result.isPresent());
    }
}
