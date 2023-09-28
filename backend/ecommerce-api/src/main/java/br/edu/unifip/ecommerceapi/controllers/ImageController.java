package br.edu.unifip.ecommerceapi.controllers;

import br.edu.unifip.ecommerceapi.utils.FileDownloadUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("api/images")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ImageController {

    @GetMapping("/{imageType}/{fileCode}")
    public ResponseEntity<?> downloadFile(
            @PathVariable("imageType") String imageType,
            @PathVariable("fileCode") String fileCode) {

        String uploadDir;
        if ("product-images".equals(imageType)) {
            uploadDir = "product-images";
        } else if ("user-images".equals(imageType)) {
            uploadDir = "user-images";
        } else {
            return ResponseEntity.notFound().build();
        }

        FileDownloadUtil downloadUtil = new FileDownloadUtil();
        Resource resource;
        try {
            resource = downloadUtil.getFileAsResource(fileCode, uploadDir);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        if (resource == null) {
            return ResponseEntity.notFound().build();
        }

        MediaType contentType;
        String fileExtension = FilenameUtils.getExtension(resource.getFilename());
        if ("jpg".equalsIgnoreCase(fileExtension) || "jpeg".equalsIgnoreCase(fileExtension)) {
            contentType = MediaType.IMAGE_JPEG;
        } else if ("png".equalsIgnoreCase(fileExtension)) {
            contentType = MediaType.IMAGE_PNG;
        } else {
            return ResponseEntity.badRequest().body("Invalid file format");
        }

        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
        return ResponseEntity.ok()
                .contentType(contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }

}
