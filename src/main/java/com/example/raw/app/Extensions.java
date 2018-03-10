package com.example.raw.app;

public enum Extensions {
    PDF(".pdf"),
    //FB2(".fb2"),
    TXT(".txt"),
    JSON(".json");

    private String description;

    Extensions(String description) {
        this.description = description;
    }

    static Extensions[] searchableExtensions(){
        Extensions[] arr = new Extensions[1];
        arr[0] = PDF;
        return arr;
    }

    public String getDescription() {return description;}
}
