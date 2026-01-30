package babby.task;

import java.util.ArrayList;

/*
 * Class that stores the list of tasks for Babby.
 */
public class TaskList extends ArrayList<Task> {

    /**
     * Creates an empty TaskList.
     */
    public TaskList() {
        super();
    }

    /**
     * Print a human-readable listing of tasks to stdout.
     */
    public void list() {
        if (this.isEmpty()) {
            System.out.println("\tYour task list is empty! Add some tasks first :)");
            return;
        }

        int i = 1;
        System.out.println("\tHere are your tasks:");
        for (Task task : this) {
            System.out.println("\t\t" + i + ") " + task);
            i++;
        }
    }

    /**
     * Find the tasks containing the input string.
     */
    public void find(String input) {
        if (this.isEmpty()) {
            System.out.println("\tYour task list is empty! Add some tasks first :)");
            return;
        }

        String query = (input == null) ? "" : input.trim();
        if (query.isEmpty()) {
            System.out.println("\tOopsie! You didn't provide a search query! :<");
            return;
        }

        String lowerQuery = query.toLowerCase();
        int matchIndex = 1;
        System.out.println("\tHere are the matching tasks in your list:");
        for (Task task : this) {
            // match against the title and the full toString() for flexibility
            String title = (task.getTitle() == null) ? "" : task.getTitle().toLowerCase();
            String full = (task.toString() == null) ? "" : task.toString().toLowerCase();
            if (title.contains(lowerQuery) || full.contains(lowerQuery)) {
                System.out.println("\t\t" + matchIndex + ") " + task);
                matchIndex++;
            }
        }

        if (matchIndex == 1) {
            System.out.println("\tNo matching tasks found.");
        }
    }

    /**
     * Adds the given task to the list.
     *
     * @param task The task to be added.
     * @return True if the task was successfully added, false otherwise.
     */
    public boolean add(Task task) {
        return super.add(task);
    }

    /**
     * Deletes the given task from the task list.
     *
     * @param index The index of the task to be removed (0-based).
     * @return The task that was removed.
     */
    public Task remove(int index) {
        return super.remove(index);
    }
}
