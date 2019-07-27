package playback.control;

public interface ErrorDisplay {

    void showError(String url, String msg);

    void showSuccess(String url);

    void showInProgress(String url);
}
