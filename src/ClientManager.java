import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.net.*;
import java.io.*;

public class ClientManager implements Runnable{
    Socket assigned_client;
    Archivio archivio;
    final char end = '§';
    private volatile boolean running = true;

    public ClientManager(Socket assigned_client, Archivio archivio) {
        this.assigned_client = assigned_client;
        this.archivio = archivio;
    }

    @Override
    public void run(){

    Scanner from_client = null;
    PrintWriter to_client = null;

        while(running){
            try {

                int message_from_client;
                Utente utente = null;

                try {
                    from_client = new Scanner(assigned_client.getInputStream());
                    to_client = new PrintWriter(assigned_client.getOutputStream());

                } catch (IOException e) {
                    e.printStackTrace();
                }

                while(true) {
                    String welcomeMessage = "***** BENVENUTO *****\n";
                    String menu = "1 Login\n2 Registrazione\n3 Esci";
                    to_client.print(welcomeMessage + menu + "\nscelta: "+end);
                    to_client.flush();

                    while(true) {
                        if(from_client.hasNextInt() == true){
                            message_from_client = from_client.nextInt();
                            break;
                        }
                        from_client.next();
                        to_client.print("Inserisci un numero: "+end);
                        to_client.flush();
                    }

                    if (message_from_client == 1) {
                        utente = login(assigned_client, from_client, to_client, archivio);
                        if (utente instanceof  Direttore) direttore(assigned_client, from_client, to_client, archivio, utente);
                        if (utente instanceof Docente) docente(assigned_client, from_client, to_client, archivio, utente);
                    }
                    if (message_from_client == 2) {
                        registrazione(assigned_client, from_client, to_client);
                    }
                    if(message_from_client == 3){
                        running = false;
                        to_client.print("Chiusura Connessione..."+end);
                        to_client.flush();
                        break;
                    }
                }
            }catch (Exception ex){
                ex.printStackTrace();
                running = false;
                to_client.print("Chiusura Connessione..."+end);
                to_client.flush();
            }
        }
        System.out.println("Connessione chiusa col Client: "+assigned_client.getRemoteSocketAddress());
    }

    private void registrazione(Socket client, Scanner from_client, PrintWriter to_client){
        int scelta = 0;
        String pass = "";
        Utente utente;
        HashMap<String, String> dati;

        while(scelta != 1 && scelta != 2) {
            to_client.print("\n-----REGISTRAZIONE-----");
            to_client.print("\n1) Registrazione Admin");
            to_client.print("\n2) Registrazione Docente");
            to_client.print("\nscelta: "+end);
            to_client.flush();
            while(true) {
                if(from_client.hasNextInt() == true){
                    scelta = from_client.nextInt();
                    break;
                }
                from_client.next();
                to_client.print("Inserisci un numero: "+end);
                to_client.flush();
            }
        }

        if (scelta == 1) {
            while(true) {
                to_client.print("\nDigita password per registrazione: " + end);
                to_client.flush();
                pass = from_client.next();
                if(pass.equals("passadmin") == false) {
                    to_client.print("\nPassword errata, ritenta");
                }else break;
            }
            dati = registrazioneUtente(client, from_client, to_client, archivio);
            Direttore direttore = new Direttore(dati.get("username"), dati.get("password"), dati.get("nome"), dati.get("cognome"));
            archivio.addUtente(direttore);
            to_client.print("\nDirettore registrato!\n\n");
        }

        if (scelta == 2) {
            while(true) {
                to_client.print("\nDigita password per registrazione: " + end);
                to_client.flush();
                pass = from_client.next();
                if(pass.equals("passdoc") == false) {
                    to_client.print("\nPassword errata, ritenta");
                }else break;
            }
            dati = registrazioneUtente(client, from_client, to_client, archivio);
            Docente docente = new Docente(dati.get("username"), dati.get("password"), dati.get("nome"), dati.get("cognome"));
            archivio.addUtente(docente);
            to_client.print("\nDocente registrato!\n\n");
        }
    }

    private HashMap<String, String> registrazioneUtente(Socket client, Scanner from_client, PrintWriter to_client, Archivio archivio){
        HashMap<String, String> dati = new HashMap<>();
        String username;
        String nome;
        String cognome;
        String pass;

        to_client.print("\n-----------------------");
        while(true) {
            to_client.print("\nInserisci username: " + end);
            to_client.flush();
            dati.put("username", from_client.next());
            Utente utente = archivio.getUtente(dati.get("username"));
            if(utente == null)
                break;
            else to_client.print("\nUsername già registrato!");
        }
        to_client.print("\nInserisci password: "+end);
        to_client.flush();
        dati.put("password", from_client.next());
        to_client.print("\nInserisci Nome: "+end);
        to_client.flush();
        dati.put("nome", from_client.next());
        to_client.print("\nInserisci Cognome: "+end);
        to_client.flush();
        dati.put("cognome", from_client.next());

        return dati;
    }

    private Utente login(Socket client, Scanner from_client, PrintWriter to_client, Archivio archivio){
        String username;
        String password;
        Utente utente = null;

        while (true) {
            to_client.print("\nInserisci username: " + end);
            to_client.flush();
            username = from_client.next();
            to_client.print("\nInserisci password: "+ end);
            to_client.flush();
            password = from_client.next();
            utente = archivio.loginCheck(username, password);

            if (utente == null){
                to_client.print("Errati\n");
            }else break;
        }
        return utente;
    }


    private void direttore(Socket client, Scanner from_client, PrintWriter to_client, Archivio archivio, Utente utente){
        to_client.print("\nLogin Direttore: "+utente.getUsername()+end);
        to_client.flush();
    }

    private void docente(Socket client, Scanner from_client, PrintWriter to_client, Archivio archivio, Utente utente){

        int message_from_client;
        boolean run = true;

        System.out.println(archivio.toString());

        to_client.print("\n\nLoginDocente: "+utente.getUsername());
        while(run) {
            to_client.print("\n\n1)Menù corsi");
            to_client.print("\n2)Avvisi");
            to_client.print("\n3)Logout\n" + end);
            to_client.flush();

            while (true) {
                if (from_client.hasNextInt() == true) {
                    message_from_client = from_client.nextInt();
                    break;
                }
                from_client.next();
                to_client.print("Inserisci un numero: " + end);
                to_client.flush();
            }
            switch (message_from_client) {
                case 1:
                    menucorsi(client, from_client, to_client, archivio, utente);
                    break;

                case 2:
                    avvisi(client, from_client, to_client, archivio, utente);
                    break;

                case 3:
                    run = false;
                    break;
            }
        }
    }

    private void menucorsi(Socket client, Scanner from_client, PrintWriter to_client, Archivio archivio, Utente utente){
        boolean run = true;
        int message_from_client;
        to_client.print("\nMenù Corsi");
        while(run) {
            to_client.print("\n\n1)Lista Corsi");
            to_client.print("\n2)Aggiungi Corso");
            to_client.print("\n3)Indietro\n" + end);
            to_client.flush();

            while (true) {
                if (from_client.hasNextInt() == true) {
                    message_from_client = from_client.nextInt();
                    break;
                }
                from_client.next();
                to_client.print("Inserisci un numero: " + end);
                to_client.flush();
            }
            switch (message_from_client) {
                case 1:
                    utilityCorsi(client, from_client, to_client, archivio, utente);
                    break;

                case 2:
                    to_client.print("\nNome Corso: " + end);
                    to_client.flush();
                    String nomeCorso = from_client.next();
                    archivio.addCorso((Docente)utente, nomeCorso);
                    to_client.println("\nCorso Inserito!");
                    break;

                case 3:
                    run = false;
                    break;

            }
        }
    }

    private void utilityCorsi(Socket client, Scanner from_client, PrintWriter to_client, Archivio archivio, Utente utente){
        Docente docente = (Docente)utente;
        LinkedList<Corso> lista = docente.getListaCorsi();
        int scelta;

        while(true) {

            for (Corso c : lista) {
                to_client.print("\n" + c.getIdCorso() + ")" + c.getNomeCorso());
            }

            while (true) {
                to_client.print("\nScegli il corso (digita 0 per tornare indietro): " + end);
                to_client.flush();
                if (from_client.hasNextInt() == true) {
                    scelta = from_client.nextInt();
                    break;
                }
            }
            if (scelta == 0) return;

        Corso corso;

            if((corso = containCorso(lista, scelta)) != null){
                utilityLezioni(client, from_client, to_client, archivio, utente, corso);
            }
        }
    }

    private Corso containCorso(LinkedList<Corso> lista, int id){
        Corso corso = null;
        for(Corso c : lista){
            if(c.getIdCorso()==id) return c;
        }
        return corso;
    }

    private void utilityLezioni(Socket client, Scanner from_client, PrintWriter to_client, Archivio archivio, Utente utente, Corso corso){

        int scelta;
        boolean run = true;

        while(run) {
            to_client.print("\n\nCorso: " + corso.getNomeCorso());
            to_client.print("\n1)Lista lezioni");
            to_client.print("\n2)Aggiungi lezione");
            to_client.print("\n3)Indietro\n" + end);
            to_client.flush();

            while (true) {
                if (from_client.hasNextInt() == true) {
                    scelta = from_client.nextInt();
                    break;
                }
                from_client.next();
                to_client.print("Inserisci un numero: " + end);
                to_client.flush();
            }

            switch (scelta){
                case 1:
                    to_client.print(corso.getListaLezioni());
                    to_client.flush();
                    break;

                case 2:

                    Date date;
                    int ore;
                    while(true) {
                        to_client.print("Data: " + end);
                        to_client.flush();
                        String data = from_client.next();
                        DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
                        try {
                            date = df.parse(data);
                            break;
                        } catch (ParseException e) {
                            to_client.print("\nInserire data valida \"dd/mm/yyyy\"");
                        }
                    }

                    while (true) {
                        to_client.print("Ore: ");
                        to_client.print("Inserisci un numero: " + end);
                        to_client.flush();
                        if (from_client.hasNextInt() == true) {
                            ore = from_client.nextInt();
                            break;
                        }
                        from_client.next();
                    }

                    to_client.print("Argomento: "+end);
                    to_client.flush();
                    String argomento = from_client.next();

                    archivio.addLezione((Docente)utente, corso, date, ore, argomento);

                    to_client.print("\nLezione Inserita!");

                   break;

                case 3:
                    return;
            }
        }
    }
    private void avvisi(Socket client, Scanner from_client, PrintWriter to_client, Archivio archivio, Utente utente){
        to_client.print("\nAvvisi"+end);
        to_client.flush();
        return;
    }

}
