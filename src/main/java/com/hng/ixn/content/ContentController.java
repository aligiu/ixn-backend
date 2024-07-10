package com.hng.ixn.content;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/content")
@AllArgsConstructor
public class ContentController {

    private final ContentService contentService;
    private final ContentRepository contentRepository;

    @GetMapping("/some")
    public String home() {
        return "You are viewing content";
    }

    @GetMapping("")
    public List<Content> getAllContent() {
        return contentService.getAllContent();
    }

    @PostMapping("")
    public ResponseEntity<List<Content>> createContent(@RequestBody List<ContentDTO> contentDTOs) {
        List<Content> createdContents = contentService.saveAllContent(contentDTOs);
        return new ResponseEntity<>(createdContents, HttpStatus.CREATED);
    }

}
