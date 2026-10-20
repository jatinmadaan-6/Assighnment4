import java.util.*;

public class Student {
    private final String id;
    private final String name;
    private final Set<String> completedCourses = new HashSet<>();

    public Student(String id, String name, String... completed) {
        this.id = id;
        this.name = name;
        this.completedCourses.addAll(Arrays.asList(completed));
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public boolean hasCompleted(String code) { return completedCourses.contains(code); }
    public void markCompleted(String code) { completedCourses.add(code); }
    public Set<String> getCompletedCourses() { return Collections.unmodifiableSet(completedCourses); }
}
