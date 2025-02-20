package duke;

import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.ToDo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents the storage class that handles loading and saving the tasks in a list.
 */
public class Storage {
    /** The specified file path to be checked and stored in. */
    private String filePath;

    /**
     * Constructor for a Storage object.
     * 
     * @param filePath Specified file path to check and store tasks.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Returns an array list of tasks after loading the cached list.
     * Otherwise, returns an empty array list.
     * 
     * @return Array list of tasks from reading the list.
     * @throws DukeException If the cached list is invalid.
     */
    public ArrayList<Task> loadList() throws DukeException {
        ArrayList<Task> taskList = new ArrayList<>();
        try {
            File listFile = new File(filePath);
            if (!listFile.exists()) {
                return taskList;
            }
            BufferedReader br = new BufferedReader(new FileReader(listFile));

            String input;
            while ((input = br.readLine()) != null) {
                String[] splitInput = input.split(" \\| ");
                switch (splitInput[0]) {
                case "T":
                    ToDo newToDo = new ToDo(splitInput[2]);
                    if (Integer.parseInt(splitInput[1]) == 1) {
                        newToDo.changeStatus();
                    }
                    taskList.add(newToDo);
                    break;
                case "D":
                    Deadline newDeadline = new Deadline(splitInput[2], splitInput[3]);
                    if (Integer.parseInt(splitInput[1]) == 1) {
                        newDeadline.changeStatus();
                    }
                    taskList.add(newDeadline);
                    break;
                
                case "E":
                    String[] timeInput = splitInput[3].split(" to ");
                    Event newEvent = new Event(splitInput[2], timeInput[0], timeInput[1]);
                    if (Integer.parseInt(splitInput[1]) == 1) {
                        newEvent.changeStatus();
                    }
                    taskList.add(newEvent);
                    break;
                default:
                    throw new DukeException("OOPS! Unexpected type of task found!");
                }
            }
        } catch (IOException e) {
            throw new DukeException("An IOException has occurred!");
        } catch (NumberFormatException e) {
            throw new DukeException("OOPS! Unexpected done value occurred!");
        }
        return taskList;
    }

    /**
     * Saves the list in the memory after inputting into the chatbot.
     * 
     * @param taskList Task List to be saved in the memory.
     * @throws DukeException If IOException has occurred.
     */
    public void saveList(ArrayList<Task> taskList) throws DukeException {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (Task t : taskList) {
                bw.append(t.getOutputString());
                bw.append("\n");
            }
            bw.close();
        } catch (IOException e) {
            throw new DukeException("An IOException has occurred!"
            + e);
        }
    }

}
