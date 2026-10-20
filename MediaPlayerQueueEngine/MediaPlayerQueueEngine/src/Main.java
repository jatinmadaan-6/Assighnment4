public class Main {
    public static void main(String[] args) {
        PlaybackQueue q = new PlaybackQueue(5);
        q.addLast(new Song("Tum Hi Ho", "Arijit Singh", 262));
        q.addLast(new Song("Kun Faya Kun", "A. R. Rahman", 468));
        q.addLast(new Song("Tum Hi Ho", "Arijit Singh", 262));      // duplicate
        q.addLast(new Song("Channa Mereya", "Arijit Singh", 289));
        q.addLast(new Song("kun faya kun", "A. R. Rahman", 468));   // duplicate (case-insensitive)
        q.addLast(new Song("Ik Onkar", "Harshdeep Kaur", 240));
        q.print();

        System.out.println("\n-- Priority queue: add to front --");
        q.addFirst(new Song("Lag Ja Gale", "Lata Mangeshkar", 255));
        q.addAt(3, new Song("Jag Ghoomeya", "Rahat Fateh Ali Khan", 250));
        q.print();

        System.out.println("\n-- Remove duplicates --");
        System.out.println("Removed: " + q.removeDuplicates());
        q.print();

        System.out.println("\n-- Forward traversal --");
        for (int i = 0; i < 3; i++) System.out.println("next     : " + q.next());

        System.out.println("\n-- Backward traversal (direction switch handled) --");
        System.out.println("previous : " + q.previous());
        System.out.println("previous : " + q.previous());
        System.out.println("next     : " + q.next());

        System.out.println("\n-- Play next (insert after current) --");
        q.playNext(new Song("Kesariya", "Arijit Singh", 268));
        q.print();
        System.out.println("next     : " + q.next());

        System.out.println("\n-- Play to the end --");
        while (q.hasNext()) System.out.println("next     : " + q.next());
        System.out.println("next at end: " + q.next());

        System.out.println("\n-- Recently played (latest first, max 5) --");
        q.getHistory().forEach(s -> System.out.println("  " + s));
    }
}
