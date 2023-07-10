public class User {
    private String name;
    private int favourites;
    private boolean isVerified;
    private int tweetCount;

    public User(String name, int favourites, boolean isVerified) {
        this.name = name;
        this.favourites = favourites;
        this.isVerified = isVerified;
        this.tweetCount = 1;                                        //cuando se crea es porque apareció ya en un tweet
    }
    public void incrementTweetCount() {
        this.tweetCount++;
    }
    public String getName() {
        return name;
    }
    public int getFavourites() {
        return favourites;
    }
    public void setFavourites(int favourites) {
        this.favourites = favourites;
    }
    public boolean isVerified() {
        return isVerified;
    }
    public int getTweetCount() {
        return tweetCount;
    }
}


