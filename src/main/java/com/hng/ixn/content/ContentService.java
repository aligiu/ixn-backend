package com.hng.ixn.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public List<Content> saveAllContent(List<ContentDTO> contentDTOs) {
        List<Content> contents = contentDTOs.stream().map(dto -> Content.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .content(dto.getContent())
                .nextId(dto.getNextId())
                .prevId(dto.getPrevId())
                .timestamp(dto.getTimestamp() != null ? dto.getTimestamp() : LocalDateTime.now())
                .build()).collect(Collectors.toList());

//        Content third = contents.get(2);
//        System.out.println("***" + third.toString() + "**");
//        System.out.println("***");
//        System.out.println(third.getId());
//        System.out.println(third.getTitle());
//        System.out.println(third.getDescription());
//        System.out.println(third.getContent().equals(""));
//        System.out.println(third.getNextId());
//        System.out.println(third.getPrevId());
//        System.out.println(third.getTimestamp());
//        System.out.println("***");

//        List<Content> thirdList = new ArrayList<>();
//        thirdList.add(third);
//        System.out.println(third.getContent() + "content **");
//        return contentRepository.saveAll(thirdList);

        return contentRepository.saveAll(contents);
    }
}
