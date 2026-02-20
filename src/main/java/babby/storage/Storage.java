package babby.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import babby.task.Deadline;
import babby.task.Event;
import babby.task.Friend;
import babby.task.Task;
import babby.task.TaskList;
import babby.task.ToDo;

/**
 * This is a placeholder class for Storage functionality.
 * Future implementations will handle data storage operations.
 */
public class Storage {
    private static final String DEFAULT_FILEPATH = "data/tasks.txt";
    private static final DateTimeFormatter FILE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final String filepath;
    private final File taskFile;

    /**
     * Creates a Storage object with the specified file path.
     *
     * @param filePath Path to the tasks file.
     */
    public Storage(String filePath) {
        this.filepath = filePath;
        this.taskFile = initiateTaskFile();
    }

    /**
     * Creates a Storage object with the default file path.
     */
    public Storage() {
        this.filepath = DEFAULT_FILEPATH;
        this.taskFile = initiateTaskFile();
    }

    /**
     * Initiates the tasks file. If the file does not exist, it creates a new one.
     *
     * @return File object representing the tasks file.
     */
    public File initiateTaskFile() {
        File tasksFile = new File(filepath);

        try {
            System.out.println("Loading task file: " + tasksFile.getAbsolutePath());
            if (tasksFile.createNewFile()) {
                System.out.println("File not found! New task file created");
            } else {
                System.out.println("Task file loaded!");
            }
        } catch (IOException e) {
            System.out.println("Error opening/creating tasks file: " + e.getMessage());
        }
        return tasksFile;
    }

    /**
     * Returns a populated TaskList with all tasks from the given file.
     *
     * @throws FileNotFoundException If given file is not found.
     */
    public TaskList parseTasks() throws FileNotFoundException {
        System.out.println("Hold on... I'm reading the tasks from: " + taskFile.getAbsolutePath());
        TaskList taskList = new TaskList();
        Scanner s = new Scanner(taskFile);
        int loaded = 0;
        while (s.hasNextLine()) {
            String nextLine = s.nextLine();
            String[] taskLine = nextLine.split(" \\| ");

            // Basic validation: at minimum we expect three parts (type | isComplete | title)
            if (taskLine.length < 3) {
                System.out.println("I can't read this task:\n\t\"" + nextLine + "\"\nSkipping it...");
                continue;
            }

            String taskType = taskLine[0];
            String taskTitle = taskLine[2];
            boolean isComplete = taskLine[1].equals("1");
            // Parse for each type of task then add to the task list
            try {
                switch (taskType) {
                case "T" -> {
                    taskList.add(new ToDo(taskTitle, isComplete));
                    loaded++;
                }
                case "D" -> {
                    // Expect an additional field for the deadline time
                    if (taskLine.length < 4) {
                        System.out.println("I can't read this task:\n\t\"" + nextLine + "\"\nSkipping it...");
                        break;
                    }
                    LocalDateTime by = LocalDateTime.parse(taskLine[3], FILE_FORMATTER);
                    taskList.add(new Deadline(taskTitle, by, isComplete));
                    loaded++;
                }
                case "E" -> {
                    // Expect two additional fields for from and to
                    if (taskLine.length < 5) {
                        System.out.println("I can't read this task:\n\t\"" + nextLine + "\"\nSkipping it...");
                        break;
                    }
                    LocalDateTime from = LocalDateTime.parse(taskLine[3], FILE_FORMATTER);
                    LocalDateTime to = LocalDateTime.parse(taskLine[4], FILE_FORMATTER);
                    taskList.add(new Event(taskTitle, from, to, isComplete));
                    loaded++;
                }
                case "F" -> {
                    if (taskLine.length < 4) {
                        System.out.println("I can't read this task:\n\t\"" + nextLine + "\"\nSkipping it...");
                        break;
                    }
                    int number = Integer.parseInt(taskLine[3]);
                    taskList.add(new Friend(taskTitle, number, isComplete));
                    loaded++;
                }
                default -> System.out.println("I can't read this task:\n\t\"" + nextLine + "\"\nSkipping it...");
                }
            } catch (Exception e) {
                System.out.println("I can't read this task:\n\t\"" + nextLine + "\"\nSkipping it...");
            }
        }
        System.out.println("Tasks loaded: " + loaded);
        return taskList;
    }

    /**
     * Saves all tasks.
     **/
    public void saveTasks(TaskList taskList) {
        try {
            // Safety: avoid overwriting an existing non-empty file with an empty list
            if ((taskList == null || taskList.size() == 0) && taskFile.exists() && taskFile.length() > 0) {
                System.out.println("Skipping save: in-memory task list empty but " + taskFile.getAbsolutePath() + " contains data. Preventing accidental overwrite.");
                return;
            }
            System.out.println("Saving " + (taskList == null ? 0 : taskList.size()) + " tasks to: " + taskFile.getAbsolutePath());
            FileWriter fw = new FileWriter(taskFile);
            for (Task task : taskList) {
                fw.write(task.toEncodedString() + System.lineSeparator());
            }
            fw.close();
            System.out.println("Save complete.");
        } catch (IOException e) {
            System.out.println("Error saving task: " + e.getMessage());
        }
    }
}
