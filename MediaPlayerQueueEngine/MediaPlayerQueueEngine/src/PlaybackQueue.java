import java.util.*;

/**
 * Playback queue backed by a LinkedList<Song>.
 *  - addFirst / addLast / addAt : fast insertion (O(1) at ends)
 *  - removeDuplicates           : single ListIterator pass + HashSet, O(n)
 *  - next / previous            : ListIterator navigation, no index lookups
 *  - history                    : bounded LinkedList of recently played songs
 */
public class PlaybackQueue {
    private enum Dir { NONE, FORWARD, BACKWARD }

    private final LinkedList<Song> queue = new LinkedList<>();
    private final LinkedList<Song> history = new LinkedList<>();   // most recent first
    private final int historyLimit;

    private ListIterator<Song> cursor = queue.listIterator();
    private Dir lastDir = Dir.NONE;
    private Song current;

    public PlaybackQueue() { this(10); }
    public PlaybackQueue(int historyLimit) { this.historyLimit = historyLimit; }

    // ---------- insertion ----------
    public void addLast(Song s) { queue.addLast(s); refreshCursor(); }

    /** Priority queueing: put the song at the very front. */
    public void addFirst(Song s) { queue.addFirst(s); refreshCursor(); }

    /** Insert at an arbitrary position. */
    public void addAt(int index, Song s) { queue.add(index, s); refreshCursor(); }

    /** Insert immediately after the currently playing song ("play next"). */
    public void playNext(Song s) {
        int pos = positionOfCurrent();      // -1 when nothing is playing => front
        queue.add(pos + 1, s);
        refreshCursor();
    }

    // ---------- removal ----------
    /** Removes later duplicates, keeping first occurrences. @return number removed. */
    public int removeDuplicates() {
        Set<Song> seen = new HashSet<>();
        int removed = 0;
        ListIterator<Song> it = queue.listIterator();
        while (it.hasNext()) {
            Song s = it.next();
            if (!seen.add(s)) {
                if (s == current) current = firstEqual(s);   // keep "now playing" valid
                it.remove();
                removed++;
            }
        }
        refreshCursor();
        return removed;
    }

    public boolean remove(Song s) {
        boolean ok = queue.remove(s);
        if (ok) { if (s.equals(current)) current = null; refreshCursor(); }
        return ok;
    }

    public void clear() { queue.clear(); current = null; lastDir = Dir.NONE; cursor = queue.listIterator(); }

    // ---------- navigation ----------
    /** Advances to the next song; returns null at the end of the queue. */
    public Song next() {
        if (lastDir == Dir.BACKWARD && cursor.hasNext()) cursor.next();  // skip element already returned
        if (!cursor.hasNext()) return null;
        return play(cursor.next(), Dir.FORWARD);
    }

    /** Moves back to the previous song; returns null at the start of the queue. */
    public Song previous() {
        if (lastDir == Dir.FORWARD && cursor.hasPrevious()) cursor.previous();
        if (!cursor.hasPrevious()) return null;
        return play(cursor.previous(), Dir.BACKWARD);
    }

    public boolean hasNext() {
        if (lastDir == Dir.BACKWARD) return queue.size() > positionOfCurrent() + 1;
        return cursor.hasNext();
    }
    public boolean hasPrevious() {
        if (lastDir == Dir.FORWARD) return positionOfCurrent() > 0;
        return cursor.hasPrevious();
    }

    public Song getCurrent() { return current; }

    /** Restart from the beginning. */
    public void rewind() { cursor = queue.listIterator(); lastDir = Dir.NONE; current = null; }

    // ---------- history ----------
    public List<Song> getHistory() { return Collections.unmodifiableList(history); }

    private Song play(Song s, Dir dir) {
        current = s;
        lastDir = dir;
        history.addFirst(s);
        if (history.size() > historyLimit) history.removeLast();
        return s;
    }

    // ---------- helpers ----------
    /** Structural changes invalidate iterators, so rebuild one positioned just after the current song. */
    private void refreshCursor() {
        int pos = positionOfCurrent();
        cursor = queue.listIterator(pos + 1);
        lastDir = (current == null) ? Dir.NONE : Dir.FORWARD;
    }

    /** Index of the current song (identity first, then equality); -1 if none. */
    private int positionOfCurrent() {
        if (current == null) return -1;
        int i = 0;
        for (Song s : queue) { if (s == current) return i; i++; }
        return queue.indexOf(current);
    }

    private Song firstEqual(Song target) {
        for (Song s : queue) if (s.equals(target)) return s;
        return target;
    }

    public int size() { return queue.size(); }
    public List<Song> getQueue() { return Collections.unmodifiableList(queue); }

    public void print() {
        System.out.println("Queue (" + queue.size() + "):");
        int i = 1;
        for (Song s : queue) System.out.printf("  %2d. %s%s%n", i++, s, s == current ? "   <-- now playing" : "");
    }
}
