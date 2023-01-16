package ru.acorn.service;

import org.springframework.core.io.FileSystemResource;
import ru.acorn.entity.AppDocument;
import ru.acorn.entity.AppPhoto;
import ru.acorn.entity.BinaryContent;

public interface FileService {
    AppDocument findDocById(Long docId);
    AppPhoto findPhotoById(Long photoId);
    FileSystemResource getFileSystemResource (BinaryContent binaryContent);
}
