package com.hng.ixn.content;

import com.hng.ixn.user.Role;
import com.hng.ixn.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class ContentServiceTest {

    private final LocalDateTime dateTimeOld = LocalDateTime.of(2020, 8, 1, 10, 0);
    private final LocalDateTime dateTimeNew = LocalDateTime.of(2024, 8, 2, 12, 0);
    @Mock
    private ContentRepository contentRepository;
    @InjectMocks
    private ContentService contentService;
    private List<Content> contentListSet1, contentListSet2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Content content1Old = Content.builder().globalId(1).id(1).title("Old title 1").description(
                "Old description 1").content("Old content 1").nextId(2).prevId(null).timestamp(
                dateTimeOld).secret("Old secret 1").build();

        Content content2Old = Content.builder().globalId(2).id(2).title("Old title 2").description(
                "Old description 2").content("Old content 2").nextId(3).prevId(1).timestamp(
                dateTimeOld).secret("Old secret 2").build();

        Content content3Old = Content.builder().globalId(3).id(3).title("Old title 3").description(
                "Old description 3").content("Old content 3").nextId(null).prevId(2).timestamp(
                dateTimeOld).secret("Old secret 3").build();

        Content content1New = Content.builder().globalId(4).id(1).title("New title 1").description(
                "New description 1").content("New content 1").nextId(2).prevId(null).timestamp(
                dateTimeNew).secret("New secret 1").build();

        Content content2New = Content.builder().globalId(5).id(2).title("New title 2").description(
                "New description 2").content("New content 2").nextId(3).prevId(1).timestamp(
                dateTimeNew).secret("New secret 2").build();

        Content content3New = Content.builder().globalId(6).id(3).title("New title 3").description(
                "New description 3").content("New content 3").nextId(null).prevId(2).timestamp(
                dateTimeNew).secret("New secret 3").build();

        contentListSet1 = Arrays.asList(content1Old, content2Old, content3Old);
        contentListSet2 = Arrays.asList(content1New, content2New, content3New);
    }

    private void setAuthentication(String role) {
        UserDetails userDetails = User.builder().email("username@example.com").password("password")
                .role(Role.valueOf(role)) // Assuming Role is an enum
                .build();

        Authentication authentication =
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetAllContentAsAdmin() {
        setAuthentication("ROLE_ADMIN");

        when(contentRepository.findAll()).thenReturn(contentListSet1);

        List<Content> result = contentService.getAllContent();

        assertEquals(3, result.size());
        assertEquals("Old secret 1", result.get(0).getSecret()); // Admin should see the secret
        assertEquals("Old secret 2", result.get(1).getSecret()); // Admin should see the secret
        assertEquals("Old secret 3", result.get(2).getSecret()); // Admin should see the secret
    }

    @Test
    void testGetAllContentAsUser() {
        setAuthentication("ROLE_ADMIN");

        when(contentRepository.findAll()).thenReturn(contentListSet1);

        List<Content> result = contentService.getAllContent();

        assertEquals(3, result.size());
        assertNull(result.get(0).getSecret()); // User should not see the secret
        assertNull(result.get(1).getSecret()); // User should not see the secret
        assertNull(result.get(2).getSecret()); // User should not see the secret
    }

    @Test
    void testGetLatestContentAsAdmin() {
        setAuthentication("ROLE_ADMIN");

        when(contentRepository.findAllWithMaxTimestamp()).thenReturn(contentListSet2);

        List<Content> result = contentService.getLatestContent();

        assertEquals(3, result.size());
        assertEquals("New secret 1", result.get(0).getSecret()); // Admin should see the secret
        assertEquals("New secret 2", result.get(1).getSecret()); // Admin should see the secret
        assertEquals("New secret 3", result.get(2).getSecret()); // Admin should see the secret
    }

    @Test
    void testGetLatestContentAsUser() {
        setAuthentication("ROLE_USER");

        when(contentRepository.findAllWithMaxTimestamp()).thenReturn(contentListSet2);

        List<Content> result = contentService.getLatestContent();

        assertEquals(3, result.size());
        assertNull(result.get(0).getSecret()); // User should not see the secret
        assertNull(result.get(1).getSecret()); // User should not see the secret
        assertNull(result.get(2).getSecret()); // User should not see the secret
    }

    @Test
    void testSaveMultipleContent() {
        ContentDTO dto1 = new ContentDTO(1, "Title1", "Description1", "Content1", 2, null,
                "SecretContent1", LocalDateTime.now());

        ContentDTO dto2 = new ContentDTO(2, "Title2", "Description2", "Content2", 3, 1,
                "SecretContent2", LocalDateTime.now());

        when(contentRepository.saveAll(anyList())).thenAnswer(
                invocation -> invocation.getArgument(0));

        List<Content> result = contentService.saveMultipleContent(Arrays.asList(dto1, dto2));

        assertEquals(2, result.size());
        assertEquals("Title1", result.get(0).getTitle());
        assertEquals("Description1", result.get(0).getDescription());
        assertEquals("Content1", result.get(0).getContent());
        assertEquals("SecretContent1", result.get(0).getSecret());

        assertEquals("Title2", result.get(1).getTitle());
        assertEquals("Description2", result.get(1).getDescription());
        assertEquals("Content2", result.get(1).getContent());
        assertEquals("SecretContent2", result.get(1).getSecret());

        verify(contentRepository, times(1)).saveAll(anyList());
    }
}
