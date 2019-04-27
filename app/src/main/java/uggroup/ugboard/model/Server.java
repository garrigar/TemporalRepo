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

    private Server() {
        this.fileNames = new ArrayList<>();
        fileNames.add("File 1");
        fileNames.add("File 2");
        fileNames.add("File 3");
    }

    public ArrayList<String> getFileNames() {
        return this.fileNames;
    }
}
