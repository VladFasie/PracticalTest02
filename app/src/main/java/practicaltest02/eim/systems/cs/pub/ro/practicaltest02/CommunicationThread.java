package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by Vlad on 24-May-18.
 */

public class CommunicationThread implements Runnable {

    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        if (serverThread == null || socket == null)
            throw new IllegalArgumentException("parameters are null");
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {

        try {

            BufferedReader bufferedReader = Utils.getReader(socket);
            PrintWriter printWriter = Utils.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(PracticalTest02MainActivity.TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }

            Log.i(PracticalTest02MainActivity.TAG, "[COMMUNICATION THREAD] Waiting for url");
            String url = bufferedReader.readLine();
            if (url == null || url.isEmpty()) {
                Log.e(PracticalTest02MainActivity.TAG, "[COMMUNICATION THREAD] Error receiving url from client");
                return;
            }

            HashMap<String, String> data = serverThread.getData();
            String body = null;
            /*if (data.containsKey(url)) {
                Log.i(PracticalTest02MainActivity.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");
                body = data.get(url);
            } else {*/
            Log.i(PracticalTest02MainActivity.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
                /*HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String pageSourceCode = httpClient.execute(httpGet, responseHandler);
                Log.i(PracticalTest02MainActivity.TAG, pageSourceCode);
                body = pageSourceCode;
                if (body != null)
                    serverThread.setData(url, body);*/

            String[] tokens = url.split(",");
            int a = Integer.parseInt(tokens[1]);
            int b = Integer.parseInt(tokens[2]);
            Integer c;
            if (tokens[0].equals("mul")) {
                Thread.sleep(2000);
                c = a * b;

            } else {
                c = a + b;
            }

            body = c.toString();

            //}
            if (body == null) {
                Log.e(PracticalTest02MainActivity.TAG, "[COMMUNICATION THREAD] body is null!");
                return;
            }
            printWriter.println(body);
        } catch (IOException ioException) {
            Log.e(PracticalTest02MainActivity.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(PracticalTest02MainActivity.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                }
            }
        }
    }
}

