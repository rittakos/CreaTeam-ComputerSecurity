package hu.bme.hit.vihima06.caffshop.backend.model;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "Comment")
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false)
    private Integer id;

    @Column(name = "comment")
    private String comment;

    @Column(name = "date", nullable = false, updatable = false)
    private Date date = new Date();

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "caff_file_data_id")
    private CaffFileData caffFileData;

    public Comment() {
    }

    public Comment(String comment, User user, CaffFileData caffFileData) {
        this.comment = comment;
        this.user = user;
        this.caffFileData = caffFileData;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CaffFileData getCaffFileData() {
        return caffFileData;
    }

    public void setCaffFileData(CaffFileData caffFileData) {
        this.caffFileData = caffFileData;
    }
}
