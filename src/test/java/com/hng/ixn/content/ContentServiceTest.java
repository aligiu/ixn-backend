package com.hng.ixn.content;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyList;


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


    @Test
    void testGetAllContentAsAuthorised() {

        when(contentRepository.findAll()).thenReturn(contentListSet1);

        // Create a spy to override the hasRequiredRole method
        ContentService spyContentService = spy(contentService);
        doReturn(true).when(spyContentService).hasRequiredRole();

        List<Content> result = spyContentService.getAllContent();

        assertEquals(3, result.size());
        assertEquals("Old secret 1", result.get(0).getSecret()); // Authorised roles (admins) should see the secret
        assertEquals("Old secret 2", result.get(1).getSecret()); // Authorised roles (admins) should see the secret
        assertEquals("Old secret 3", result.get(2).getSecret()); // Authorised roles (admins) should see the secret
    }

    @Test
    void testGetAllContentUnauthorised() {

        when(contentRepository.findAll()).thenReturn(contentListSet1);

        // Create a spy to override the hasRequiredRole method
        ContentService spyContentService = spy(contentService);
        doReturn(false).when(spyContentService).hasRequiredRole();

        List<Content> result = spyContentService.getAllContent();

        assertEquals(3, result.size());
        assertNull(result.get(0).getSecret()); // Unauthorised roles (users and guests) should not see the secret
        assertNull(result.get(1).getSecret()); // Unauthorised roles (users and guests) should not see the secret
        assertNull(result.get(2).getSecret()); // Unauthorised roles (users and guests) should not see the secret
    }

    @Test
    void testGetLatestContentAsAuthorised() {

        when(contentRepository.findAllWithMaxTimestamp()).thenReturn(contentListSet2);

        // Create a spy to override the hasRequiredRole method
        ContentService spyContentService = spy(contentService);
        doReturn(true).when(spyContentService).hasRequiredRole();

        List<Content> result = spyContentService.getLatestContent();

        assertEquals(3, result.size());
        assertEquals("New secret 1", result.get(0).getSecret()); // Authorised roles (admins) should see the secret
        assertEquals("New secret 2", result.get(1).getSecret()); // Authorised roles (admins) should see the secret
        assertEquals("New secret 3", result.get(2).getSecret()); // Authorised roles (admins) should see the secret
    }

    @Test
    void testGetLatestContentUnauthorised() {

        when(contentRepository.findAllWithMaxTimestamp()).thenReturn(contentListSet2);

        // Create a spy to override the hasRequiredRole method
        ContentService spyContentService = spy(contentService);
        doReturn(false).when(spyContentService).hasRequiredRole();

        List<Content> result = spyContentService.getLatestContent();

        assertEquals(3, result.size());
        assertNull(result.get(0).getSecret()); // Unauthorised roles (users and guests) should not see the secret
        assertNull(result.get(1).getSecret()); // Unauthorised roles (users and guests) should not see the secret
        assertNull(result.get(2).getSecret()); // Unauthorised roles (users and guests) should not see the secret
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

    // Helper method to setup SecurityContext with mocked Authentication
    private void setupSecurityContextWithRoles(List<String> roles) {
        Authentication authentication = mock(Authentication.class);

        // Create a collection of GrantedAuthority
        Collection<GrantedAuthority> authorities = roles.stream()
                .map(role -> (GrantedAuthority) () -> role)
                .collect(Collectors.toList());

        // Use doReturn to avoid type inference issues
        doReturn(authorities).when(authentication).getAuthorities();

        SecurityContext securityContext = mock(SecurityContext.class);
        doReturn(authentication).when(securityContext).getAuthentication();

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testHasRequiredRole_withAdminRole() {
        setupSecurityContextWithRoles(List.of("ROLE_ADMIN"));

        ContentService service = new ContentService(mock(ContentRepository.class));
        assertTrue(service.hasRequiredRole(), "The method should return true for ROLE_ADMIN");
    }

    @Test
    void testHasRequiredRole_withUserRole() {
        setupSecurityContextWithRoles(List.of("ROLE_USER"));

        ContentService service = new ContentService(mock(ContentRepository.class));
        assertTrue(service.hasRequiredRole(), "The method should return true for ROLE_USER");
    }

    @Test
    void testHasRequiredRole_withAdminAndUserRoles() {
        setupSecurityContextWithRoles(List.of("ROLE_ADMIN", "ROLE_USER"));

        ContentService service = new ContentService(mock(ContentRepository.class));
        assertTrue(service.hasRequiredRole(), "The method should return true for having both ROLE_ADMIN and ROLE_USER");
    }

    @Test
    void testHasRequiredRole_withOtherRole() {
        setupSecurityContextWithRoles(List.of("ROLE_GUEST"));

        ContentService service = new ContentService(mock(ContentRepository.class));
        assertFalse(service.hasRequiredRole(), "The method should return false for roles other than ROLE_ADMIN and ROLE_USER");
    }

    @Test
    void testHasRequiredRole_withNoRoles() {
        setupSecurityContextWithRoles(List.of());

        ContentService service = new ContentService(mock(ContentRepository.class));
        assertFalse(service.hasRequiredRole(), "The method should return false when no roles are present");
    }


}


