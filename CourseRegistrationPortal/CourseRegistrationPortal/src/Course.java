import java.util.*;

/** A course with capacity, unique enrolled IDs, ordered prerequisites and a FIFO waitlist. */
public class Course {
    private final String code;
    private final String title;
    private final int capacity;
    private final Set<String> enrolledStudents = new HashSet<>();      // unique IDs, O(1) lookup
    private final List<String> prerequisites = new ArrayList<>();      // ordered prerequisite codes
    private final LinkedList<String> waitlist = new LinkedList<>();    // first-come, first-served queue

    public Course(String code, String title, int capacity, String... prerequisiteCodes) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive");
        this.code = code;
        this.title = title;
        this.capacity = capacity;
        this.prerequisites.addAll(Arrays.asList(prerequisiteCodes));
    }

    public String getCode() { return code; }
    public String getTitle() { return title; }
    public int getCapacity() { return capacity; }
    public boolean isFull() { return enrolledStudents.size() >= capacity; }
    public int getSeatsLeft() { return capacity - enrolledStudents.size(); }

    public boolean isEnrolled(String id) { return enrolledStudents.contains(id); }
    public boolean isWaitlisted(String id) { return waitlist.contains(id); }

    /** @return true if added (HashSet guarantees uniqueness). */
    boolean enroll(String id) { return enrolledStudents.add(id); }
    boolean unenroll(String id) { return enrolledStudents.remove(id); }
    void addToWaitlist(String id) { waitlist.addLast(id); }
    boolean removeFromWaitlist(String id) { return waitlist.remove(id); }

    public Set<String> getEnrolledStudents() { return Collections.unmodifiableSet(enrolledStudents); }
    public List<String> getPrerequisites() { return Collections.unmodifiableList(prerequisites); }
    LinkedList<String> waitlistInternal() { return waitlist; }
    public List<String> getWaitlist() { return Collections.unmodifiableList(waitlist); }

    @Override
    public String toString() {
        return String.format("%s - %s [%d/%d enrolled, waitlist=%s, prereq=%s]",
                code, title, enrolledStudents.size(), capacity, waitlist, prerequisites);
    }
}
