package ru.acorn.service.impl;

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
import ru.acorn.utils.CryptoTool;

import java.io.File;
import java.io.IOException;

@Service
@Log4j
public class FileServiceImpl implements FileService {
    private final AppDocumentRepository appDocumentRepository;
    private final AppPhotoRepository appPhotoRepository;
    private final CryptoTool cryptoTool;


    public FileServiceImpl(AppDocumentRepository appDocumentRepository,
                           AppPhotoRepository appPhotoRepository,
                           CryptoTool cryptoTool)
    {
        this.appDocumentRepository = appDocumentRepository;
        this.appPhotoRepository = appPhotoRepository;
        this.cryptoTool = cryptoTool;
    }

    @Override
    public AppDocument findDocById(String docId) {
        var id = cryptoTool.idFromHash(docId);
        if (id == null){
            return null;
        }
        return appDocumentRepository.findById(id).orElse(null);
    }

    @Override
    public AppPhoto findPhotoById(String photoId) {
        var id = cryptoTool.idFromHash(photoId);
        if (id == null){
            return null;
        }
        return appPhotoRepository.findById(id).orElse(null);
    }

    @Override
    public FileSystemResource getFileSystemResource(BinaryContent binaryContent) {
        try {
            File  temp = File.createTempFile("tempFile", "bin");
            temp.deleteOnExit();
            FileUtils.writeByteArrayToFile(temp, binaryContent.getFileArraysOfBytes());
            return new FileSystemResource(temp);
        } catch (IOException e) {
            log.error(e);
            return null;
        }
    }
}
