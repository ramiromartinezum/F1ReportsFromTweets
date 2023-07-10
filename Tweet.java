import uy.edu.um.prog2.adt.LinkedList.MyLinkedListImpl;

import java.time.LocalDate;

public class Tweet {
    private long id;
    private LocalDate date;
    private String content;
    private MyLinkedListImpl<HashTag> tweetHashtags;
    private static long tweetID = 0;

    public Tweet(LocalDate date, String content) {
        this.id = tweetID++;
        this.date = date;
        this.content = content;

        this.tweetHashtags = new MyLinkedListImpl<>();
    }

    public void addHashTag(HashTag hashTag) {
        this.tweetHashtags.add(hashTag);
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public MyLinkedListImpl<HashTag> getTweetHashtags() {
        return tweetHashtags;
    }

    public LocalDate getDate() {
        return date;
    }
}