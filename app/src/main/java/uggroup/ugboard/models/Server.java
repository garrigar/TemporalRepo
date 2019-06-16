package uggroup.ugboard.models;

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
    private String currentFolder;

    private Server() {
        this.fileNames = new ArrayList<>();
        this.fileNames.add("picture1.jpg");
        this.fileNames.add("picture2.jpg");
        this.fileNames.add("picture3.jpg");
        this.fileNames.add("picture1.jpg");
        this.fileNames.add("picture2.jpg");
        this.fileNames.add("picture3.jpg");
        this.fileNames.add("picture1.jpg");
        this.fileNames.add("picture2.jpg");
        this.fileNames.add("picture3.jpg");
        this.fileNames.add("picture1.jpg");
        this.fileNames.add("picture2.jpg");
        this.fileNames.add("picture3.jpg");
        this.fileNames.add("picture1.jpg");
        this.fileNames.add("picture2.jpg");
        this.fileNames.add("picture3.jpg");
        this.fileNames.add("picture1.jpg");
        this.fileNames.add("picture2.jpg");
        this.fileNames.add("picture3.jpg");
        this.fileNames.add("picture1.jpg");
        this.fileNames.add("picture2.jpg");
        this.fileNames.add("picture3.jpg");

        this.fileActions = new ArrayList<>();
        this.fileActions.add("Open");
        this.fileActions.add("Rename");
        this.fileActions.add("Delete");

        this.currentFolder = "UGBoardTestFolder";
    }

    public ArrayList<String> getFileNames() {
        return this.fileNames;
    }
    public ArrayList<String> getFileActions() {
        return this.fileActions;
    }
    public String getCurrentFolder() {
        return this.currentFolder;
    }
}
