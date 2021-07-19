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
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
            PrintWriter to_server = new PrintWriter(client_socket.getOutputStream());
            Scanner from_user = new Scanner(System.in);

            String message_from_server;
            String message_to_server;
            int value;

            while (true) {
                while ((value = br.read()) != -1) {
                    char c = (char) value;
                    if (c == '§') break;
                    sb.append(c);
                }
                message_from_server = sb.toString();
                if (message_from_server.equals("Chiusura Connessione...")) return;
                sb.setLength(0);
                System.out.print(message_from_server);

                while ((message_to_server = from_user.nextLine()).equals(""))
                    ; //Per non far bloccare il programma quando si preme "invio"

                to_server.println(message_to_server);
                //to_server.flush();
                if (to_server.checkError())
                    throw new Exception();

            }
        } catch (Exception e) {
            System.out.println("Connessione chiusa dal server");
            System.exit(1);
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
