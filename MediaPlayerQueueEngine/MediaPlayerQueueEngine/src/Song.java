import java.util.Objects;

public class Song {
    private final String title;
    private final String artist;
    private final int durationSeconds;

    public Song(String title, String artist, int durationSeconds) {
        this.title = title;
        this.artist = artist;
        this.durationSeconds = durationSeconds;
    }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public int getDurationSeconds() { return durationSeconds; }

    /** Two songs are duplicates when title and artist match (case-insensitive). */
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song)) return false;
        Song s = (Song) o;
        return title.equalsIgnoreCase(s.title) && artist.equalsIgnoreCase(s.artist);
    }
    @Override public int hashCode() { return Objects.hash(title.toLowerCase(), artist.toLowerCase()); }
    @Override public String toString() {
        return String.format("%s - %s (%d:%02d)", title, artist, durationSeconds / 60, durationSeconds % 60);
    }
}
