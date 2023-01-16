package ru.acorn.service.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import ru.acorn.entity.AppDocument;
import ru.acorn.entity.AppPhoto;
import ru.acorn.entity.BinaryContent;
import ru.acorn.exception.UploadFileException;
import ru.acorn.repository.AppDocumentRepository;
import ru.acorn.repository.AppPhotoRepository;
import ru.acorn.repository.BinaryContentRepository;
import ru.acorn.service.FileService;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class FileServiceImpl implements FileService {
    @Value("${token}")
    private String token;
    @Value("${service.file_info.uri}")
    private String fileInfoUri;
    @Value("${service.file_storage.uri}")
    private String fileStorageUri;

    private final BinaryContentRepository binaryContentRepository;
    private final AppDocumentRepository appDocumentRepository;
    private final AppPhotoRepository appPhotoRepository;

    public FileServiceImpl(BinaryContentRepository binaryContentRepository,
                           AppDocumentRepository appDocumentRepository,
                           AppPhotoRepository appPhotoRepository) {
        this.binaryContentRepository = binaryContentRepository;
        this.appDocumentRepository = appDocumentRepository;
        this.appPhotoRepository = appPhotoRepository;
    }

    @Override
    public AppDocument processDoc(Message telegramMessage) {
        Document telegramDoc = telegramMessage.getDocument();
        String fileId = telegramDoc.getFileId();
        ResponseEntity<String> response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            String filePath = getStorageFilePath(response);
            BinaryContent persistentBinaryContent = getPersistentBinaryContent(filePath);

            AppDocument transientAppDoc = enrichAppDoc(telegramDoc, persistentBinaryContent);
            return appDocumentRepository.save(transientAppDoc);
        } else {
            throw new UploadFileException ("Bad response " + response);
        }
    }

    @Override
    public AppPhoto processPhoto(Message telegramMessage) {
        var photoSizeCount = telegramMessage.getPhoto().size();
        var photoIndex =
                photoSizeCount > 1 ? telegramMessage.getPhoto().size() - 1 : 0;
        PhotoSize photo  = telegramMessage.getPhoto().get(photoIndex);
        String fileId = photo.getFileId();
        ResponseEntity<String> response = getFilePath(fileId);
        if(response.getStatusCode() == HttpStatus.OK){
            String filePath = getStorageFilePath(response);
            BinaryContent persistentBinaryContent = getPersistentBinaryContent(filePath);

            AppPhoto transientAppPhoto = enrichAppPhoto(photo, persistentBinaryContent);
            return appPhotoRepository.save(transientAppPhoto);
        } else {
         throw new UploadFileException ("Bad response " + response);
        }
    }

    private AppDocument enrichAppDoc(Document telegramDocument, BinaryContent persistentBinaryContent) {
        return   AppDocument.builder()
                .telegramDocId(telegramDocument.getFileId())
                .mimeType(telegramDocument.getMimeType())
                .fileSize(Math.toIntExact(telegramDocument.getFileSize()))
                .binaryContent(persistentBinaryContent)
                .filename(telegramDocument.getFileName())
                .build();
    }

    private AppPhoto enrichAppPhoto(PhotoSize telegramPhoto, BinaryContent persistentBinaryContent) {
        return   AppPhoto.builder()
                .telegramFileId(telegramPhoto.getFileId())
                .fileSize(telegramPhoto.getFileSize())
                .binaryContent(persistentBinaryContent)
                .build();
    }

    private BinaryContent getPersistentBinaryContent(String filePath) {
        byte[] fileAsBytes = downloadFile(filePath);
        BinaryContent binaryContent = BinaryContent.builder()
                .fileArraysOfBytes(fileAsBytes)
                .build();
        return binaryContentRepository.save(binaryContent);
    }

    private byte[] downloadFile(String storageFilePath) {
        String fullUri = fileStorageUri.replace("{token}", token)
                .replace("{filePath}", storageFilePath);
        URL url = null;
        try {
            url = new URL(fullUri);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try (InputStream is = url.openStream()){
           return is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getStorageFilePath(ResponseEntity<String> response) {
        JSONObject jsonObject = new JSONObject(response.getBody());
        return String.valueOf(jsonObject.getJSONObject("result")
                .get("file_path"));
    }

    private ResponseEntity<String> getFilePath(String telegramFileId){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity <String> request = new HttpEntity<>(headers);
        return  restTemplate.exchange(fileInfoUri,
                HttpMethod.GET,
                request,
                String.class,
                token,
                telegramFileId
                );
    }
}
