package com.example.techlog.s3;

public enum FileExtension {
    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png");

    private final String extension;

    FileExtension(String extension) {
        this.extension = extension;
    }

    public static boolean isValidExtension(String fileName) {
        for (FileExtension ext : FileExtension.values()) {
            if (fileName.toLowerCase().endsWith("." + ext.extension.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
