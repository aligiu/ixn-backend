package com.hng.ixn.s3;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class FileDetails {
    private final String folderId;
    private final String fileName;
    private final String downloadRoute;
}