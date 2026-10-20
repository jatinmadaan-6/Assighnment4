import java.util.List;

public class Main {
    public static void main(String[] args) {
        RegistrationPortal portal = new RegistrationPortal();
        portal.addCourse(new Course("CS101", "Intro to Programming", 2));
        portal.addCourse(new Course("CS201", "Data Structures", 2, "CS101"));
        portal.addCourse(new Course("CS301", "Algorithms", 1, "CS101", "CS201"));

        portal.addStudent(new Student("S1", "Asha", "CS101", "CS201"));
        portal.addStudent(new Student("S2", "Bikram", "CS101"));
        portal.addStudent(new Student("S3", "Charu", "CS101", "CS201"));
        portal.addStudent(new Student("S4", "Dev", "CS101", "CS201"));
        portal.addStudent(new Student("S5", "Esha"));

        System.out.println("== Registrations ==");
        String[][] reqs = {{"S1","CS301"},{"S3","CS301"},{"S4","CS301"},{"S2","CS301"},
                           {"S1","CS301"},{"S5","CS201"},{"S2","CS201"},{"S9","CS201"},{"S1","CS999"}};
        for (String[] r : reqs)
            System.out.printf("%s -> %s : %s%n", r[0], r[1], portal.register(r[0], r[1]));

        System.out.println("\nS4 waitlist position in CS301: " + portal.waitlistPosition("S4", "CS301"));
        System.out.println("\n== State ==");
        portal.printAll();

        System.out.println("\n== S1 drops CS301 (auto-promotion) ==");
        List<String> promoted = portal.drop("S1", "CS301");
        System.out.println("Promoted: " + promoted);
        portal.printAll();

        System.out.println("\n== S3 drops CS301 ==");
        System.out.println("Promoted: " + portal.drop("S3", "CS301"));
        portal.printAll();
    }
}
