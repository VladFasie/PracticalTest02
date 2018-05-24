package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Vlad on 24-May-18.
 */

public class ClientThread extends Thread {

    private int port;
    private String url;
    private TextView body_textview;

    private Socket socket;

    public ClientThread(int port, String url, TextView body_textview) {
        this.port = port;
        this.url = url;
        this.body_textview = body_textview;
    }

    @Override
    public void run() {
        try {
            socket = new Socket("localhost", port);
            if (socket == null) {
                Log.e(PracticalTest02MainActivity.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }

            BufferedReader bufferedReader = Utils.getReader(socket);
            PrintWriter printWriter = Utils.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(PracticalTest02MainActivity.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }

            printWriter.println(this.url);
            String body = "";
            String line;
            while ((line = bufferedReader.readLine()) != null)
                body += line;
            final String fb = body;
            this.body_textview.post(new Runnable() {
                @Override
                public void run() {
                    body_textview.setText(fb);
                }
            });
        } catch (IOException ioException) {
            Log.e(PracticalTest02MainActivity.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(PracticalTest02MainActivity.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                }
            }
        }
    }

}
