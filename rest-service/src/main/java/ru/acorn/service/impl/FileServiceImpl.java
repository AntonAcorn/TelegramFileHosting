package ru.acorn.service.impl;

import com.google.common.io.Files;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import ru.acorn.entity.AppDocument;
import ru.acorn.entity.AppPhoto;
import ru.acorn.entity.BinaryContent;
import ru.acorn.repository.AppDocumentRepository;
import ru.acorn.repository.AppPhotoRepository;
import ru.acorn.service.FileService;

import javax.ws.rs.NotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@Log4j
public class FileServiceImpl implements FileService {
    private final AppDocumentRepository appDocumentRepository;
    private final AppPhotoRepository appPhotoRepository;

    public FileServiceImpl(AppDocumentRepository appDocumentRepository,
                           AppPhotoRepository appPhotoRepository) {
        this.appDocumentRepository = appDocumentRepository;
        this.appPhotoRepository = appPhotoRepository;
    }

    @Override
    public AppDocument findDocById(Long id) {
        Optional <AppDocument> appDocumentToFind = appDocumentRepository.findById(id);
        if (appDocumentToFind.isEmpty()){
            log.error("File is not found");
            throw new NotFoundException("Document is not found");
        }
        return appDocumentToFind.get();
    }

    @Override
    public AppPhoto findPhotoById(Long id) {
        Optional <AppPhoto> appPhotoToFind = appPhotoRepository.findById(id);
        if (appPhotoToFind.isEmpty()){
            log.error("File is not found");
            throw new NotFoundException("Photo is not found");
        }
        return appPhotoToFind.get();
    }

    @Override
    public FileSystemResource getFileSystemResource(BinaryContent binaryContent) {
        try {
            File  temp = File.createTempFile("tempFile", "bin");
            temp.deleteOnExit();
            FileUtils.writeByteArrayToFile(temp, binaryContent.getFileArraysOfBytes());
            return new FileSystemResource(temp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
