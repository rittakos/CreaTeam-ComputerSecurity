package hu.bme.hit.vihima06.caffshop.backend.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "CaffFileData")
@Table(name = "caff_file_data")
public class CaffFileData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "stored_file_name", nullable = false, unique = true)
    private String storedFileName;

    @Column(name = "upload_date", nullable = false, updatable = false)
    private Date uploadDate = new Date();

    @ManyToOne()
    private User creator;

    @ElementCollection
    @CollectionTable(name = "file_tags", joinColumns = @JoinColumn(name = "file_id"))
    @JoinColumn(name = "file_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name = "tags")
    private List<String> tags = new ArrayList();

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "width", nullable = false)
    private Integer width;

    @Column(name = "height", nullable = false)
    private Integer height;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "duration", nullable = false)
    private Double duration;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "caff_file_data_id")
    private List<Comment> comments = new ArrayList();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_bought_caffs",
            joinColumns = @JoinColumn(name = "caff_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> usersPurchased = new ArrayList();

    public CaffFileData() {
    }

    public CaffFileData(String name, String storedFileName, User creator, List<String> tags, String description, Integer width, Integer height, Double price, Double duration) {
        this.name = name;
        this.storedFileName = storedFileName;
        this.creator = creator;
        this.tags = tags;
        this.description = description;
        this.width = width;
        this.height = height;
        this.price = price;
        this.duration = duration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStoredFileName() {
        return storedFileName;
    }

    public void setStoredFileName(String caffFileName) {
        this.storedFileName = caffFileName;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<User> getUsersPurchased() {
        return usersPurchased;
    }

    public void setUsersPurchased(List<User> usersPurchased) {
        this.usersPurchased = usersPurchased;
    }
}
