package com.project.utopia.holder.request;

public class UpdateAnnouncementRequestBody {
    private int announcementId;
    private String title;
    private String category;
    private String content;

    public void setAnnouncementId(int announcementId) {
        this.announcementId = announcementId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAnnouncementId() {
        return announcementId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCategory() {
        return category;
    }
}
