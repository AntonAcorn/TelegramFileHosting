package ru.acorn.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.core.io.FileSystemResource;
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
    public ResponseEntity<?> getDoc (@RequestParam (name = "id") Long id) {
        var doc = fileService.findDocById(id);
        if(doc == null) {
            log.error("Doc is not found");
            return ResponseEntity.badRequest().body("Doc is not found");
        }

        var binaryContent = doc.getBinaryContent();
        FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);

        if(!(fileSystemResource.isFile())){
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok()
                .header("Content-disposition", "attachment; filename=" + doc.getFilename())
                .body(fileSystemResource);
    }
}