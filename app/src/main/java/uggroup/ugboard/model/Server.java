package uggroup.ugboard.model;

import java.util.ArrayList;

/*Uses singleton pattern*/
public class Server {
    private static Server singleton = null;

    public static Server getInstance() {
        if(Server.singleton == null)
            Server.singleton = new Server();

        return Server.singleton;
    }

    private ArrayList<String> fileNames;
    private ArrayList<String> fileActions;

    private Server() {
        this.fileNames = new ArrayList<>();
        fileNames.add("File 1");
        fileNames.add("File 2");
        fileNames.add("File 3");

        this.fileActions = new ArrayList<>();
        fileNames.add("Action 1");
        fileNames.add("Action 2");
        fileNames.add("Action 3");
    }

    public ArrayList<String> getFileNames() {
        return this.fileNames;
    }
    public ArrayList<String> getFileActions() {
        return this.fileActions;
    }
}
