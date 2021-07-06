import java.util.*;
import java.net.*;
import java.io.*;

public class Client {

    String address_ip;
    int port;
    Socket client_socket;

    public Client(String ip, int port) {
        this.address_ip = ip;
        this.port = port;
    }

    public void start(){

        try {
            client_socket = new Socket(address_ip, port);
            Scanner from_server = new Scanner(client_socket.getInputStream());
            var to_server = new PrintWriter(client_socket.getOutputStream());

            Scanner from_user = new Scanner(System.in);

            String message_from_server;
            String message_to_server;

            while(true){
                message_from_server = from_server.next();
                System.out.println(message_from_server);

                System.out.println("Insert message to send: ");
                message_to_server = from_user.nextLine();
                to_server.println(message_to_server);
                to_server.flush();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        if(args.length!=2){
            System.out.println("Errore");
        }

        String ip = args[0];
        int port = Integer.parseInt(args[1]);

        System.out.println("Starting connection to:" + ip + "   port:" + port);
        Client client = new Client(ip, port);
        client.start();
    }
}
