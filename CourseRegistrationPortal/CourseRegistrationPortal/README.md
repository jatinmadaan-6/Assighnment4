# Course Registration Portal (Java)

Collections used:
- `HashMap<String, Course>`  – O(1) course lookup by code (`RegistrationPortal`)
- `HashSet<String>`          – unique enrolled student IDs (`Course`)
- `ArrayList<String>`        – ordered prerequisite codes (`Course`)
- `LinkedList<String>`       – FIFO waitlist queue (`Course`)

Logic: prerequisite check -> duplicate check -> capacity check -> enroll or waitlist.
Dropping a seat automatically promotes the earliest eligible waitlisted student.

Build & run (JDK 8+):
    mkdir out && javac -d out src/*.java && java -cp out Main
