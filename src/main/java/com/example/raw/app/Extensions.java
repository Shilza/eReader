package com.example.raw.app;

public enum Extensions {
    PDF(".pdf"),
    //FB2(".fb2"),
    TXT(".txt"),
    XML(".xml"),
    HTML(".html"),
    JSON(".json");

    private String description;

    Extensions(String description) {
        this.description = description;
    }

    public static Extensions[] searchableExtensions() {
        Extensions[] arr = new Extensions[1];
        arr[0] = PDF;
        return arr;
    }

    public static Extensions[] simpleTextExtensions() {
        Extensions[] arr = new Extensions[4];
        arr[0] = TXT;
        arr[1] = XML;
        arr[2] = HTML;
        arr[3] = JSON;
        return arr;
    }

    public String getDescription() {
        return description;
    }
}
