package ru.acorn.utils;

public enum LinkValue {
    DOC_VALUE ("/file/get-doc"),
    PHOTO_VALUE("/file/get-photo");

    private final String link;

    LinkValue(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return link;
    }
}
