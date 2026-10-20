import java.util.*;

/** Central registry: HashMap lookups by course code + registration / drop / waitlist logic. */
public class RegistrationPortal {
    private final Map<String, Course> courses = new HashMap<>();
    private final Map<String, Student> students = new HashMap<>();

    public void addCourse(Course c) { courses.put(c.getCode(), c); }
    public void addStudent(Student s) { students.put(s.getId(), s); }
    public Course getCourse(String code) { return courses.get(code); }   // O(1)

    /** Returns missing prerequisite codes (in declared order). */
    public List<String> missingPrerequisites(Student s, Course c) {
        List<String> missing = new ArrayList<>();
        for (String p : c.getPrerequisites())
            if (!s.hasCompleted(p)) missing.add(p);
        return missing;
    }

    public RegistrationResult register(String studentId, String courseCode) {
        Student s = students.get(studentId);
        if (s == null) return RegistrationResult.STUDENT_NOT_FOUND;
        Course c = courses.get(courseCode);
        if (c == null) return RegistrationResult.COURSE_NOT_FOUND;
        if (c.isEnrolled(studentId)) return RegistrationResult.ALREADY_ENROLLED;
        if (c.isWaitlisted(studentId)) return RegistrationResult.ALREADY_WAITLISTED;
        if (!missingPrerequisites(s, c).isEmpty()) return RegistrationResult.PREREQUISITES_NOT_MET;

        if (!c.isFull()) {
            c.enroll(studentId);
            return RegistrationResult.ENROLLED;
        }
        c.addToWaitlist(studentId);
        return RegistrationResult.WAITLISTED;
    }

    /**
     * Drops a student (from the class or the waitlist). If a seat opens,
     * the earliest waitlisted student is promoted automatically.
     * @return list of student IDs promoted as a result.
     */
    public List<String> drop(String studentId, String courseCode) {
        Course c = courses.get(courseCode);
        if (c == null) return Collections.emptyList();
        if (c.removeFromWaitlist(studentId)) return Collections.emptyList();
        if (c.unenroll(studentId)) return promoteFromWaitlist(c);
        return Collections.emptyList();
    }

    /** Fills open seats FIFO; skips (and removes) students who no longer qualify. */
    private List<String> promoteFromWaitlist(Course c) {
        List<String> promoted = new ArrayList<>();
        LinkedList<String> q = c.waitlistInternal();
        while (!c.isFull() && !q.isEmpty()) {
            String next = q.pollFirst();
            Student s = students.get(next);
            if (s == null || !missingPrerequisites(s, c).isEmpty()) continue;
            c.enroll(next);
            promoted.add(next);
        }
        return promoted;
    }

    public int waitlistPosition(String studentId, String courseCode) {
        Course c = courses.get(courseCode);
        if (c == null) return -1;
        int idx = c.getWaitlist().indexOf(studentId);
        return idx < 0 ? -1 : idx + 1;
    }

    public void printAll() {
        new TreeMap<>(courses).values().forEach(System.out::println);
    }
}
