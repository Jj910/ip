import java.util.ArrayList;

/*
 * Class that stores the list of tasks for Babby.
 */
public class TaskList extends ArrayList<Task> {
    private ArrayList<Task> taskList;

    public TaskList() {
        this.taskList = new ArrayList<>();
    }
}
