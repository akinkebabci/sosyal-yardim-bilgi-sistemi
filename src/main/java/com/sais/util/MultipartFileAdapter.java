package com.sais.util;

import org.primefaces.model.file.UploadedFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class MultipartFileAdapter implements MultipartFile {

    private final UploadedFile uploadedFile;
    private final byte[] bytes;

    public MultipartFileAdapter(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
        this.bytes = uploadedFile.getContent();
    }

    @Override
    public String getName() {
        return uploadedFile.getFileName();
    }

    @Override
    public String getOriginalFilename() {
        return uploadedFile.getFileName();
    }

    @Override
    public String getContentType() {
        return uploadedFile.getContentType();
    }

    @Override
    public boolean isEmpty() {
        return bytes == null || bytes.length == 0;
    }

    @Override
    public long getSize() {
        return bytes.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return bytes;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        throw new UnsupportedOperationException("transferTo not supported");
    }
}

