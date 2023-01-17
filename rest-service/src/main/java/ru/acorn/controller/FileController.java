package ru.acorn.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.acorn.service.FileService;

@RestController
@RequestMapping("/file")
@Log4j
public class FileController {

    private final FileService fileService;
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/get-doc")
    public ResponseEntity<?> getDoc (@RequestParam (name = "id") String id) {
        var doc = fileService.findDocById(id);
        if(doc == null) {
            log.error("Doc is not found");
            return ResponseEntity.badRequest().body("Doc is not found");
        }

        var binaryContent = doc.getBinaryContent();
        FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);

        if(!(fileSystemResource.isFile())){
            log.error("Server problem");
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getMimeType()))
                .header("Content-disposition", "attachment; filename=" + doc.getFilename())
                .body(fileSystemResource);
    }
    @GetMapping("/get-photo")
    public ResponseEntity<?> getPhoto (@RequestParam(name = "id") String id){
        var photo = fileService.findPhotoById(id);
        if(photo == null){
            log.error("Doc is not found");
            return ResponseEntity.badRequest().body("Doc is not found");
        }

        var binaryContent = photo.getBinaryContent();
        FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);

        if(fileSystemResource == null){
            return ResponseEntity.internalServerError().body("Server problem");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header("Content-disposition", "attachment;")
                .body(fileSystemResource);
    }
}
