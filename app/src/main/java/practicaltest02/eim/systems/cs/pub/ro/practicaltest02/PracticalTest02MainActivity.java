package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {

    public static final String TAG = "colocviu_2";

    EditText port, url, port_client;
    Button start, get;
    TextView body;

    ServerThread serverThread;
    ClientThread clientThread;

    private Button.OnClickListener startListener = new Button.OnClickListener() {

        @Override
        public void onClick(View view) {
            String serverPort = port.getText().toString();
            if (serverPort == null) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Could not create server thread!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread.start();
            Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server started !", Toast.LENGTH_SHORT).show();
        }

    };

    private Button.OnClickListener getListener = new Button.OnClickListener() {

        @Override
        public void onClick(View view) {
            String serverPort = port_client.getText().toString();
            if (serverPort == null) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String urlText = url.getText().toString();
            if (urlText == null) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            new ClientThread(Integer.parseInt(serverPort), urlText, body).start();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        port = findViewById(R.id.port);
        url = findViewById(R.id.url);

        start = findViewById(R.id.start);
        get = findViewById(R.id.get);

        body = findViewById(R.id.body);

        start.setOnClickListener(startListener);
        get.setOnClickListener(getListener);

        port_client = findViewById(R.id.port_client);
    }

    @Override
    protected void onDestroy() {
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }

}
