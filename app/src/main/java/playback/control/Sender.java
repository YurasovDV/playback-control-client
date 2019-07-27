package playback.control;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RequestQueue.RequestFilter;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

class Sender {
    private RequestQueue queue;
    private ErrorDisplay display;
    private AddressBuilder addressBuilder;
    private boolean wasReset = false;

    Sender(@NonNull Context context, @NonNull ErrorDisplay display) {
        queue = Volley.newRequestQueue(context);
        this.display = display;
        addressBuilder = new AddressBuilder();
    }

    void handleCommand(String command) {
        if (command.equals("cancel")) {
            wasReset = true;
            queue.cancelAll(new RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
            addressBuilder.reset();
        } else {
            wasReset = false;
            StringRequest stringRequest = buildStringRequest(command);
            addToQueue(stringRequest);
        }
    }

    private void addToQueue(StringRequest stringRequest) {
        display.showInProgress(stringRequest.getUrl());
        queue.add(stringRequest);
    }

    private StringRequest buildStringRequest(final String command) {
        String ip = addressBuilder.getNextUrl();
        final String url = ip + "/?command=" + command;
        return new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("MEDIA_CONTROL_INFO", "network success");
                        display.showSuccess(url);
                        addressBuilder.onSuccess();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String msg = error.getMessage() == null ? "no message" : error.getMessage();
                        Log.d("MEDIA_CONTROL_ERROR", msg);
                        display.showError(url, msg);
                        addressBuilder.onError();
                        if (!wasReset) {
                            handleCommand(command);
                        }
                    }
                });
    }

}
