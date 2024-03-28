package com.example.pc_builder;

public class SettingsItem {
    private int icon;
    private String title;
    private String body;
    private String link;

    public SettingsItem(int icon, String title, String body, String link) {
        this.icon = icon;
        this.title = title;
        this.body = body;
        this.link = link;
    }

    public int getIcon() {
        return icon;
    }
    public String getTitle() {
        return title;
    }
    public String getBody() {
        return body;
    }
    public String getLink() {
        return link;
    }
}

