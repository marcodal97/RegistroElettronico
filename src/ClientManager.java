import java.util.*;
import java.net.*;
import java.io.*;

public class ClientManager implements Runnable {
    Socket assigned_client;

    public ClientManager(Socket assigned_client) {
        this.assigned_client = assigned_client;
    }

    @Override
    public void run() {
        Scanner from_client = null;
        PrintWriter to_client = null;

        try {
            from_client = new Scanner(assigned_client.getInputStream());
            to_client = new PrintWriter(assigned_client.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        String message_from_client;
        to_client.println("Benvenuto");
        to_client.flush();

        while(true){
            message_from_client = from_client.nextLine();
            System.out.println("Received Message from Client: "+ message_from_client);

            to_client.println("Salve");
            to_client.flush();

        }




    }
}
