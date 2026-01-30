import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * This is a placeholder class for Storage functionality.
 * Future implementations will handle data storage operations.
 */
public class Storage {
    private final String FILEPATH;
    private final File taskFile;
    private static final DateTimeFormatter FILE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public Storage(String filePath) {
        this.FILEPATH = filePath;
        this.taskFile = initiateTaskFile();
    }

    /**
     * Initiates the tasks file. If the file does not exist, it creates a new one.
     *
     * @return File object representing the tasks file.
     */
    public File initiateTaskFile() {
        File tasks = new File(FILEPATH);

        try {
            System.out.println("Loading task file...");
            if (tasks.createNewFile()) {
                System.out.println("File not found! New task file created");
            } else {
                System.out.println("Task file loaded!");
            }
        } catch (IOException e) {
            System.out.println("Error opening/creating tasks file: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Returns a populated TaskList with all tasks from the given file.
     *
     * @throws FileNotFoundException If given file is not found.
     */
    public TaskList parseTasks() throws FileNotFoundException {
        System.out.println("Hold on... I'm reading the tasks...");
        TaskList taskList = new TaskList();
        Scanner s = new Scanner(taskFile);
        while (s.hasNextLine()) {
            String nextLine = s.nextLine();
            String[] taskLine = nextLine.split(" \\| ");
            String taskType = taskLine[0];
            String taskTitle = taskLine[2];
            Boolean isComplete = taskLine[1].equals("1");
            // Parse for each type of task then add to the task list
            try {
                switch (taskType) {
                    case "T" ->  taskList.add(new ToDo(taskTitle, isComplete)); // To Do
                    case "D" ->  {
                        LocalDateTime by = LocalDateTime.parse(taskLine[3], FILE_FORMATTER);
                        taskList.add(new Deadline(taskTitle, by, isComplete)); // Deadline
                    }
                    case "E" ->  {
                        LocalDateTime from = LocalDateTime.parse(taskLine[3], FILE_FORMATTER);
                        LocalDateTime to = LocalDateTime.parse(taskLine[4], FILE_FORMATTER);
                        taskList.add(new Event(taskTitle, from, to, isComplete)); // Event
                    }
                    default -> System.out.println("I can't read this task:\n\t\"" + nextLine + "\"\nSkipping it...");
                }
            } catch (Exception e) {
                System.out.println("I can't read this task:\n\t\"" + nextLine + "\"\nSkipping it...");
            }
        }
        System.out.println("Tasks loaded!");
        return taskList;
    }

    /**
     * Saves all tasks.
     **/
    public void saveTasks(TaskList taskList) {
        try {
            FileWriter fw = new FileWriter(taskFile);
            for (Task task : taskList) {
                fw.write(task.toEncodedString() + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("Error saving task: " + e.getMessage());
        }
    }
}