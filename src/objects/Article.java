package objects;

import java.util.Date;
import java.util.ArrayList;

public class Article {

    private String title;
    private Date pubDate;
    private String creator;
    private String description;
    private String content;
    private int id;
    private ArrayList<String> tag;
    private ArrayList<String> categories;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getTag() {
        return tag;
    }

    public void setTag(ArrayList<String> tag) {
        this.tag = tag;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", pubDate=" + pubDate +
                ", creator='" + creator + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                ", id=" + id +
                ", tag=" + tag +
                ", categories=" + categories +
                '}';
    }
}
