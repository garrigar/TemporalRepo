package uggroup.ugboard;

public interface CoreView extends MVPView, LoggedInstance {
    void showToast(String msg, int length);
}
