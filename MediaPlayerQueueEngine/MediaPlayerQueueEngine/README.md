# Media Player Queue Engine (Java)

- `LinkedList<Song>` holds the playback queue (O(1) insert at front/back, no shifting).
- `addFirst` = priority queueing; `addAt(index)` and `playNext` = arbitrary insertion.
- `removeDuplicates()` = one `ListIterator` pass with a `HashSet` (O(n)), keeps first occurrences.
- `next()` / `previous()` use a `ListIterator` cursor; the direction-switch quirk
  (hasNext/previous returning the same element) is handled explicitly.
- Bounded history (`LinkedList`, most-recent-first).
- The iterator is rebuilt after structural edits so no ConcurrentModificationException occurs.

Build & run (JDK 8+):
    mkdir out && javac -d out src/*.java && java -cp out Main
