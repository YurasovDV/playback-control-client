package playback.control;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements ErrorDisplay {

    private TextView textView;
    private Sender sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.messages);
        sender = new Sender(this, this);
    }

    public void btnNextClick(View view) {
        sender.handleCommand("next");
    }

    public void btnPauseClick(View view) {
        sender.handleCommand("pause");
    }

    public void btnPrevClick(View view) {
        sender.handleCommand("previous");
    }

    public void btnCancelClick(View view) {
        sender.handleCommand("cancel");
    }

    @Override
    public void showError(String url, String msg) {
        textView.setText(getString(R.string.requestFail, url, msg));
    }

    @Override
    public void showSuccess(String url) {
        textView.setText(getString(R.string.requestSuccess, url));
    }

    @Override
    public void showInProgress(String url) {
        textView.setText(String.format("%s in progress", url));
    }
}
