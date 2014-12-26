package edu.wpi.first.wpilib.javainstaller;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * This class is responsible for holding all of the different arguments that the controllers need and allowing them
 * to be passed around. It is also responsible for maintaining the back stack of programs.
 */
public class Arguments {

    private final Stack<Controller> m_backStack = new Stack<>();
    private final Map<Argument, String> m_arguments = new HashMap<>();

    /**
     * Copies the backstack and arguments from an existing arguments instance.
     * <b>THIS WILL OVERWRITE ALL INFORMATION IN THIS INSTANCE!</b>
     *
     * @param args The arguments to copy from
     */
    public void copyFrom(Arguments args) {
        this.m_backStack.removeAllElements();
        this.m_backStack.addAll(args.m_backStack);
        this.m_arguments.clear();
        this.m_arguments.putAll(args.m_arguments);
    }

    /**
     * Pushes a new element onto the backstack.
     *
     * @param controller The new controller to add to the backstack
     */
    public void pushBackstack(Controller controller) {
        m_backStack.push(controller);
    }

    /**
     * Pops most recent controller from the backstack.
     *
     * @return The most recent back controller
     */
    public Controller popBackstack() {
        return m_backStack.pop();
    }

    /**
     * Sets an argument to be a given value. This overwrites the argument if it already exists
     *
     * @param arg   The argument to set
     * @param value The new value
     */
    public void setArgument(Argument arg, String value) {
        m_arguments.put(arg, value);
    }

    /**
     * Gets an argument's value. If the argument has not been set, null will be returned.
     *
     * @param arg The argument to get
     * @return The value, if it exists
     */
    public String getArgument(Argument arg) {
        return m_arguments.get(arg);
    }

    /**
     * This enum represents all of the different arguments that can be passed around.
     */
    public enum Argument {
        JRE_CREATOR_ZIP, JRE_CREATOR_FOLDER, JRE_FOLDER, JRE_TAR
    }

    /**
     * This enum represents all of the different controllers in the project. When a new controller is added, you need
     * to update this enum.
     */
    public enum Controller {
        CONNECT_ROBORIO_CONTROLLER("connect_roborio"),
        CREATE_JRE_CONTROLLER("create_jre"),
        DEPLOY_CONTROLLER("deploy"),
        DOWNLOAD_CONTROLLER("download"),
        DOWNLOAD_PROGRESS_CONTROLLER("download_progress"),
        DOWNLOADED_CONTROLLER("downloaded"),
        ERROR_CONTROLLER("error"),
        INTERNET_CONTROLLER("connect_internet"),
        SUCCESS_CONTROLLER("success"),
        UNTAR_CONTROLLER("untar"),
        WELCOME_CONTROLLER("intro_screen");

        private final String m_path;
        private static final String FORMAT_STRING = "/fxml/%s.fxml";


        Controller(String path) {
            this.m_path = String.format(FORMAT_STRING, path);
        }

        public String getPath() {
            return m_path;
        }
    }
}
