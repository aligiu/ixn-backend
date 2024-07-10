package com.hng.ixn.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return contentRepository.findAll();
    }

    public List<Content> getLatestContent() {
        return contentRepository.findAllWithMaxTimestamp();
    }

    public List<Content> saveMultipleContent(List<ContentDTO> contentDTOs) {
        List<Content> contents = contentDTOs.stream().map(dto -> Content.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .content(dto.getContent())
                .nextId(dto.getNextId())
                .prevId(dto.getPrevId())
                .timestamp(dto.getTimestamp() != null ? dto.getTimestamp() : LocalDateTime.now())
                .build()).collect(Collectors.toList());
        return contentRepository.saveAll(contents);
    }
}
