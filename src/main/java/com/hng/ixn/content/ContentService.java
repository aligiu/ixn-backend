package com.hng.ixn.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContentService {
    private final ContentRepository contentRepository;

    @Autowired
    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public List<Content> getAllContent() {
        List<Content> contents = contentRepository.findAll();
        return filterSecrets(contents);
    }

    public List<Content> getLatestContent() {
        List<Content> contents = contentRepository.findAllWithMaxTimestamp();
        return filterSecrets(contents);
    }

    public List<Content> saveMultipleContent(List<ContentDTO> contentDTOs) {
        List<Content> contents = contentDTOs.stream().map(dto -> Content.builder()
                .title(dto.getTitle())
                .id(dto.getId())
                .description(dto.getDescription())
                .content(dto.getContent())
                .nextId(dto.getNextId())
                .prevId(dto.getPrevId())
                .secret(dto.getSecret())
                .timestamp(dto.getTimestamp() != null ? dto.getTimestamp() : LocalDateTime.now())
                .build()).collect(Collectors.toList());
        return contentRepository.saveAll(contents);
    }

    private List<Content> filterSecrets(List<Content> contents) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasRequiredRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN") || role.equals("ROLE_USER"));

        if (!hasRequiredRole) {
            contents.forEach(content -> content.setSecret(null));
        }

        return contents;
    }
}
