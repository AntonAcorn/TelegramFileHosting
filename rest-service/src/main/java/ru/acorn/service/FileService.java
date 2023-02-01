package ru.acorn.service;

import ru.acorn.entity.AppDocument;
import ru.acorn.entity.AppPhoto;

public interface FileService {
    AppDocument findDocById(String docId);
    AppPhoto findPhotoById(String photoId);
}
