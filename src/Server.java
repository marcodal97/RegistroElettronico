import java.util.*;
import java.net.*;
import java.io.*;

public class Server {

    ServerSocket server_socket;
    Socket client_socket;
    private int port;

    public Server(int port){
        this.port = port;
        System.out.println("Creazione server sulla porta " + port);
    }

    public void start(){

        int n_connection = 0;
        Archivio archivio;

        try {
            FileInputStream f = new FileInputStream("archivio.ser");
            ObjectInputStream os = new ObjectInputStream(f);
             archivio = (Archivio) (os.readObject());
        } catch (IOException e) {
             archivio = new Archivio();
            System.out.println("Creazione nuovo archivio");
        } catch (ClassNotFoundException e) {
             archivio = new Archivio();
            System.out.println("Creazione nuovo archivio");
        }


        try {
            server_socket = new ServerSocket(port);

            while(true){
                client_socket = server_socket.accept();
                n_connection++;
                System.out.println("Connessione accettata dal client: " + client_socket.getRemoteSocketAddress());

                ClientManager cm = new ClientManager(client_socket, archivio);
                Thread t = new Thread(cm);
                t.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exit(){
        try {
            server_socket.close();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
	Server server = new Server(5000);
	server.start();
    }
}
