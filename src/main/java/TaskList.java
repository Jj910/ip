import java.util.ArrayList;

/*
 * Class that stores the list of tasks for Babby.
 */
public class TaskList extends ArrayList<Task> {

    public TaskList() {
        super();
    }

    /**
     * Lists all tasks in the task list.
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
     * Deletes the given task from the task list.
     *
     * @param index The index of the task to be removed (0-based).
     * @return The task that was removed.
     */
    public Task remove(int index) {
        return super.remove(index);
    }
}
