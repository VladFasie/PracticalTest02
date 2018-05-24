package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Vlad on 24-May-18.
 */

public class ServerThread extends Thread {

    private int port = 0;

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    private ServerSocket serverSocket;
    private HashMap<String, String> cache = null;

    public ServerThread(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ioException) {
            Log.e(PracticalTest02MainActivity.TAG, "An exception has occurred: " + ioException.getMessage());
        }
        cache = new HashMap<>();
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Socket socket = serverSocket.accept();
                CommunicationThread communicationThread = new CommunicationThread(this, socket);
                new Thread(communicationThread).start();
            }
        } catch (Exception ex) {
            Log.e(PracticalTest02MainActivity .TAG, "[SERVER THREAD] An exception has occurred: " + ex.getMessage());
        }
    }

    public void stopThread() {
        interrupt();
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ioException) {
                Log.e(PracticalTest02MainActivity.TAG, "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());
            }
        }
    }

    public synchronized void setData(String url, String body) {
        cache.put(url, body);
    }

    public synchronized HashMap<String, String> getData() {
        return cache;
    }
}
