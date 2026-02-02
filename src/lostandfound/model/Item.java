package lostfound.model;

import java.time.LocalDateTime;

public class Item {
    private int id;
    private String name;
    private String type;
    private String description;
    private int postedBy;
    private String postedByUsername;
    private LocalDateTime postedAt;

    public Item() {}

    public Item(int id, String name, String type, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public Item(String name, String type, String description, int postedBy) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.postedBy = postedBy;
    }

    public Item(int id, String name, String type, String description, int postedBy, String postedByUsername, LocalDateTime postedAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.postedBy = postedBy;
        this.postedByUsername = postedByUsername;
        this.postedAt = postedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getPostedBy() { return postedBy; }
    public void setPostedBy(int postedBy) { this.postedBy = postedBy; }

    public String getPostedByUsername() { return postedByUsername; }
    public void setPostedByUsername(String postedByUsername) { this.postedByUsername = postedByUsername; }

    public LocalDateTime getPostedAt() { return postedAt; }
    public void setPostedAt(LocalDateTime postedAt) { this.postedAt = postedAt; }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", postedBy=" + postedBy +
                ", postedByUsername='" + postedByUsername + '\'' +
                ", postedAt=" + postedAt +
                '}';
    }
}