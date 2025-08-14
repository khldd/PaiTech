package com.example.paitech.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public final class FileStorageUtil {
    private FileStorageUtil() {}

    public static String saveToTemp(MultipartFile file) throws IOException {
        String name = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File dest = new File(System.getProperty("java.io.tmpdir"), name);
        file.transferTo(dest);
        return dest.getAbsolutePath();
    }
}
