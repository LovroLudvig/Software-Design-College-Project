package hr.fer.handMadeShopBackend.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Story {

    @Id
    @GeneratedValue
    private Long Id;

    private String text;
    private String imageUrl;
    private String videoUrl;

    @ManyToOne
    private StoryStatus status;

    @OneToMany
    private List<Comment> comments;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public StoryStatus getStatus() {
        return status;
    }

    public void setStatus(StoryStatus status) {
        this.status = status;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
