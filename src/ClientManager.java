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
    Utente utente = null;

        while(running){
            try {

                int message_from_client;

                try {
                    from_client = new Scanner(assigned_client.getInputStream());
                    to_client = new PrintWriter(assigned_client.getOutputStream());

                } catch (IOException e) {
                    e.printStackTrace();
                }

                while(true) {
                    String welcomeMessage = "\n\n\n\n***** BENVENUTO *****\n";
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
                        utente = login(from_client, to_client, archivio);
                        if (utente instanceof  Direttore) direttore(from_client, to_client, archivio, (Direttore)utente);
                        if (utente instanceof Docente) docente(from_client, to_client, archivio, (Docente)utente);
                    }
                    if (message_from_client == 2) {
                        registrazione(from_client, to_client);
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
                if(utente != null)
                    logout(utente.getUsername());
                to_client.print("Chiusura Connessione..."+end);
                to_client.flush();
            }
        }
        System.out.println("Connessione chiusa col Client: "+assigned_client.getRemoteSocketAddress());
    }

//////////////////////////////////////////////// DIRETTORE ////////////////////////////////////////////////////////////

    private void direttore(Scanner from_client, PrintWriter to_client, Archivio archivio, Direttore direttore){
        int message_from_client;
        boolean run = true;

        System.out.println(archivio.toString());

        to_client.print("\n\nLogin Direttore: "+direttore.getUsername());
        while(run) {
            to_client.print("\n\n------------------------");
            to_client.print("\nMENU PRINCIPALE");
            to_client.print("\n\n1)Menu Docenti");
            to_client.print("\n2)Utility");
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
                    menuDocDirettore(from_client, to_client, direttore);
                    break;

                case 2:
                    utilityDirettore(from_client,to_client,direttore,archivio);
                    break;
        
                case 3:
                    run = false;
                    logout(direttore.getUsername());
                    break;
            }
        }
    }

    private void menuDocDirettore(Scanner from_client, PrintWriter to_client, Direttore direttore){
        boolean run = true;
        int message_from_client;
        int num;
        String user;

        while(run) {
            to_client.print("\n\n------------------------");
            to_client.print("\nMenu Docenti");
            to_client.print("\n\n1)Lista Docenti");
            to_client.print("\n2)Elimina Docente");
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
                    LinkedList<Utente> lista = archivio.ordinaUtenti();
                    to_client.print("\n\n------------------------");
                    to_client.print("\nLISTA DOCENTI\n");
                    to_client.print("\nCOGNOME | NOME | USERNAME");
                    to_client.flush();
                    for(Utente u : lista){
                        if(u instanceof  Docente)
                            to_client.print("\n"+u.getCognome()+" - "+u.getNome()+" - "+u.getUsername());
                        to_client.flush();
                    }
                    to_client.print("\n\nInserisci l'username del Docente (digita 0 per tornare indietro): "+end);
                    to_client.flush();

                    while (true) {
                        user = from_client.next();
                        try{
                            num = Integer.parseInt(user);
                        }catch(NumberFormatException ex){
                            num = -1;
                        }
                        if(archivio.checkDoc(user) || num == 0){
                            break;
                        }
                        to_client.print("Docente non trovato, riprova: " + end);
                        to_client.flush();
                    }

                    if(num == 0) break;

                    visualDocente(from_client, to_client, archivio, user);

                    break;


                case 2:
                    to_client.print("\n\nNOME | COGNOME | USERNAME");
                    for(Utente u : archivio.getListaUtenti()){
                        if(u instanceof Docente)
                            to_client.print("\n"+u.getNome()+" - "+u.getCognome()+" - "+u.getUsername());
                    }
                    to_client.print("\n\nInserisci l'username del Docente da eliminare (digita 0 per tornare indietro): "+end);
                    to_client.flush();

                    while (true) {
                            user = from_client.next();
                            try{
                                num = Integer.parseInt(user);
                            }catch(NumberFormatException ex){
                                num = -1;
                            }
                            if(archivio.checkDoc(user) || num == 0){
                                break;
                            }
                        to_client.print("Inserisci un username valido: " + end);
                        to_client.flush();
                    }
                    if (num == 0) break;
                    archivio.delDocente(user);
                    to_client.print("\nDocente Eliminato!");
                    to_client.flush();
                    back(from_client,to_client);
                    break;


                case 3:
                    run = false;
                    break;

            }
        }
    }

    private void utilityDirettore(Scanner from_client, PrintWriter to_client, Direttore direttore, Archivio archivio){
        boolean run = true;
        int message_from_client;
        Date date1 = null;
        Date date2;

        while(run) {
            to_client.print("\n\n------------------------");
            to_client.print("\nUTILITY");
            to_client.print("\n\n1)Genera Compensi");
            to_client.print("\n2)Cerca lezioni per data");
            to_client.print("\n3)Avvisi");
            to_client.print("\n4)Indietro\n" + end);
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
                    to_client.print("\n\n------------------------");
                    to_client.print("\nCOMPENSI");
                    to_client.print("\nInserisci la paga oraria (digita 0 per tornare indietro): "+end);
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
                    if(message_from_client == 0) break;
                    int ore = 0;


                    while(true) {
                        to_client.print("Inserisci Data inizio: " + end);
                        to_client.flush();
                        String data = from_client.next();
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            date1 = df.parse(data);
                            break;
                        } catch (ParseException e) {
                            to_client.print("\nInserire data valida \"gg/mm/aaaa\"\n");
                        }
                    }

                    while(true) {
                        to_client.print("Inserisci Data fine: " + end);
                        to_client.flush();
                        String data = from_client.next();
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            date2 = df.parse(data);
                            break;
                        } catch (ParseException e) {
                            to_client.print("\nInserire data valida \"gg/mm/aaaa\"\n");
                        }
                    }



                    LinkedList<Utente> lista = archivio.ordinaUtenti();
                    to_client.print("\n\nCOGNOME | NOME | COMPENSO");
                    for(Utente u : lista){
                        if(u instanceof Docente) {
                            for (Corso c : ((Docente) u).getListaCorsi())
                                for(Lezione l : c.getListaLezioni())
                                    if(l.getData().before(date2)&& l.getData().after(date1)){
                                        ore = ore + l.getOre();
                                    }
                            to_client.print("\n"+u.getCognome()+" - "+u.getNome()+" - €"+ore*message_from_client);
                            to_client.flush();
                            ore = 0;
                        }
                    }
                    back(from_client, to_client);
                    break;

                case 2:
                    boolean back = false;

                    while(true) {
                        to_client.print("Inserisci Data (digita 0 per tornare indietro): " + end);
                        to_client.flush();
                        String data = from_client.next();
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            date1 = df.parse(data);
                            break;
                        } catch (ParseException e) {
                            try{
                                int num = Integer.parseInt(data);
                                if(num == 0)
                                    back = true;
                            }catch(NumberFormatException ex) { }
                            if(back == true)
                                break;
                            to_client.print("\nInserire data valida \"gg/mm/aaaa\"\n");
                        }
                    }

                    if(back == true)
                        break;

                    LinkedList<Utente> list = archivio.getListaUtenti();
                    to_client.print("\n\n------------------------");
                    to_client.print("\nLEZIONI IN DATA: " +String.format("%td %tb %tY",date1, date1, date1));
                    to_client.print("\n\nCOGNOME | NOME | CORSO | ORE | ARGOMENTO");
                    for(Utente u : list)
                        if(u instanceof Docente)
                            for(Corso c : ((Docente)u).getListaCorsi())
                                for(Lezione l : c.getListaLezioni())
                                    if(l.getData().equals(date1)){
                                        to_client.print("\n"+u.getCognome()+" - "+u.getNome()+" - "+c.getNomeCorso()+" - "+l.getOre()+" - "+l.getArgomento());
                                        to_client.flush();
                                    }

                    back(from_client, to_client);
                    break;

                case 3:
                    menuAvvisi(from_client, to_client, archivio, direttore);
                    break;

                case 4:
                    run = false;
                    break;
            }
        }
    }

    private void visualDocente(Scanner from_client, PrintWriter to_client, Archivio archivio, String username){
        Docente docente = archivio.getDocente(username);
        to_client.print("\n***********************\n");
        to_client.print("\nNOME: "+docente.getNome()+" - COGNOME: "+docente.getCognome()+" - N° CORSI: "+docente.getTotCorsi() + " - N° LEZIONI: "+docente.getTotLezioni());
        int scelta;

        while(true) {
            scelta = sceltaCorso(from_client, to_client, archivio, docente);
            if (scelta == 0) return;
            Corso corso;
            if (docente.checkCorso(scelta)) {
                corso = docente.getCorso(scelta);
                visualLezioniPerData(to_client, corso);
                back(from_client,to_client);
            }
        }
    }

    private void menuAvvisi(Scanner from_client, PrintWriter to_client, Archivio archivio, Direttore direttore){
        int message_from_client;
        boolean run = true;

        while(run) {
            to_client.print("\n\n------------------------");
            to_client.print("\nMENU AVVISI");
            to_client.print("\n\n1)Crea Avviso");
            to_client.print("\n2)Elimina Avviso");
            to_client.print("\n3)Lista Avvisi");
            to_client.print("\n4)Indietro\n" + end);
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
                    to_client.print("\nInserisci testo: "+end);
                    to_client.flush();
                    from_client.nextLine();
                    String testo = from_client.nextLine();
                    archivio.addAvviso(testo, direttore);
                    to_client.print("\nAvviso Inviato!");
                    back(from_client, to_client);
                    break;

                case 2:
                    int id;
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    to_client.print("\n\nID|    DATA    | TESTO");
                    for(Avviso a : direttore.getListaAvvisi()){
                        to_client.print("\n"+a.getId()+" - "+formatter.format(a.getData())+" - "+a.getTesto());
                    }
                    to_client.print("\n\nInserisci l'ID dell'avviso da eliminare (digita 0 per tornare indietro): "+end);
                    to_client.flush();

                    boolean num = false;
                    while (true) {
                        if (from_client.hasNextInt() == true) {
                            id = from_client.nextInt();
                            num = true;
                            if(direttore.checkIdAvviso(id) || id == 0){
                                break;
                            }
                        }
                        if(num == false)
                            from_client.next();
                        to_client.print("Inserisci un ID valido: " + end);
                        to_client.flush();
                    }
                    if (id == 0) break;
                    archivio.delAvviso(id, direttore);
                    to_client.print("\nAvviso Eliminato!");
                    to_client.flush();
                    back(from_client,to_client);
                    break;

                case 3:
                    visualAvvisi(to_client,archivio);
                    back(from_client,to_client);
                    break;

                case 4:
                    run = false;
                    break;
            }
        }

    }

    /////////////////////////////////////////////  DOCENTE ////////////////////////////////////////////////////////////

    private void docente(Scanner from_client, PrintWriter to_client, Archivio archivio, Docente docente){

        int message_from_client;
        boolean run = true;

        System.out.println(archivio.toString());

        to_client.print("\n\nLoginDocente: "+docente.getUsername());
        while(run) {
            to_client.print("\n\n------------------------");
            to_client.print("\nMENU PRINCIPALE");
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
                    menuCorsi(from_client, to_client, archivio, docente);
                    break;

                case 2:
                    visualAvvisi(to_client, archivio);
                    back(from_client,to_client);
                    break;

                case 3:
                    run = false;
                    logout(docente.getUsername());
                    break;
            }
        }
    }

    private void menuCorsi(Scanner from_client, PrintWriter to_client, Archivio archivio, Docente docente){
        boolean run = true;
        int message_from_client;

        while(run) {
            to_client.print("\n\n------------------------");
            to_client.print("\nMENU CORSI");
            to_client.print("\n\n1)Lista Corsi");
            to_client.print("\n2)Aggiungi Corso");
            to_client.print("\n3)Elimina Corso");
            to_client.print("\n4)Indietro\n" + end);
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
                    utilityCorsi(from_client, to_client, archivio, docente);
                    break;

                case 2:
                    to_client.print("\nNome Corso: " + end);
                    to_client.flush();
                    from_client.nextLine();
                    String nomeCorso = from_client.nextLine();
                    archivio.addCorso(docente, nomeCorso);
                    to_client.println("\nCorso Creato!");
                    back(from_client,to_client);
                    break;


                case 3:
                    int id;
                    to_client.print("\n\nID | NOME");
                    for(Corso c : docente.getListaCorsi()){
                        to_client.print("\n"+c.getIdCorso()+" - "+c.getNomeCorso());
                    }
                    to_client.print("\n\nInserisci l'ID del corso da eliminare (digita 0 per tornare indietro): "+end);
                    to_client.flush();

                    boolean num = false;
                    while (true) {
                        if (from_client.hasNextInt() == true) {
                            id = from_client.nextInt();
                            num = true;
                            if(docente.checkIdCorso(id) || id == 0){
                                break;
                            }
                        }
                        if(num == false)
                            from_client.next();
                        to_client.print("Inserisci un ID valido: " + end);
                        to_client.flush();
                    }
                    if (id == 0) break;
                    archivio.delCorso(docente, id);
                    to_client.print("\nCorso Eliminato!");
                    to_client.flush();
                    back(from_client,to_client);
                    break;

                case 4:
                    run = false;
                    break;

            }
        }
    }

    private void utilityCorsi(Scanner from_client, PrintWriter to_client, Archivio archivio, Docente docente){

        int scelta;

        while(true) {
            scelta = sceltaCorso(from_client, to_client, archivio, docente);
            if (scelta == 0) return;

            Corso corso;
            if (docente.checkCorso(scelta)) {
                corso = docente.getCorso(scelta);
                utilityLezioni(from_client, to_client, archivio, docente, corso);
            }
        }
    }

    private void utilityLezioni(Scanner from_client, PrintWriter to_client, Archivio archivio, Docente docente, Corso corso){

        int scelta;
        boolean run = true;

        while(run) {
            to_client.print("\n\n------------------------");
            to_client.print("\nCORSO: " + corso.getNomeCorso()+"\n");
            to_client.print("\n1)Lista lezioni");
            to_client.print("\n2)Aggiungi lezione");
            to_client.print("\n3)Elimina lezione");
            to_client.print("\n4)Indietro\n" + end);
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
                    visualLezioniPerData(to_client, corso);
                    back(from_client,to_client);
                    break;

                case 2:

                    Date date;
                    int ore;
                    while(true) {
                        to_client.print("Data: " + end);
                        to_client.flush();
                        String data = from_client.next();
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            date = df.parse(data);
                            break;
                        } catch (ParseException e) {
                            to_client.print("\nInserire data valida \"gg/mm/aaaa\"\n");
                        }
                    }

                    while (true) {
                        to_client.print("Ore: "+end);
                        to_client.flush();
                        if (from_client.hasNextInt() == true) {
                            ore = from_client.nextInt();
                            break;
                        }
                        to_client.print("Inserisci un numero!\n");
                        from_client.next();
                    }
                    from_client.nextLine(); //non mettendolo, il prossimo nextLine() mi legge \n del next() precedente
                    to_client.print("Argomento: "+end);
                    to_client.flush();
                    String argomento = from_client.nextLine();

                    archivio.addLezione(docente, corso, date, ore, argomento);

                    to_client.print("\nLezione Inserita!");
                    back(from_client,to_client);

                   break;

                case 3:

                    int id;
                    to_client.print("\n\nID|    DATA    | ORE | ARGOMENTO");
                    for(Lezione l : corso.getListaLezioni()){
                        to_client.print("\n"+String.format(l.getIdLezione()+" - %td %tb %tY", l.getData(),l.getData(), l.getData()) +" -"+l.getOre()+"   -  "+l.getArgomento());
                    }
                    to_client.print("\n\nInserisci l'ID della lezione da eliminare (digita 0 per tornare indietro): "+end);
                    to_client.flush();

                    boolean num = false;
                    while (true) {
                        if (from_client.hasNextInt() == true) {
                            id = from_client.nextInt();
                            num = true;
                            if(corso.checkId(id) || id == 0){
                            break;
                            }
                        }
                        if(num == false)
                            from_client.next();
                        to_client.print("Inserisci un ID valido: " + end);
                        to_client.flush();
                    }
                    if (id == 0) break;
                    archivio.delLezione(docente, corso, id);
                    to_client.print("\nLezione Eliminata!");
                    to_client.flush();
                    back(from_client,to_client);
                    break;



                case 4:
                    return;
            }
        }
    }

    private void avvisi(Scanner from_client, PrintWriter to_client, Archivio archivio, Docente docente){
        to_client.print("\nAvvisi"+end);
        to_client.flush();
        return;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void registrazione(Scanner from_client, PrintWriter to_client){
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
        dati = registrazioneUtente(from_client, to_client, archivio);
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
        dati = registrazioneUtente(from_client, to_client, archivio);
        Docente docente = new Docente(dati.get("username"), dati.get("password"), dati.get("nome"), dati.get("cognome"));
        archivio.addUtente(docente);
        to_client.print("\nDocente registrato!\n\n");
    }
}

    private HashMap<String, String> registrazioneUtente(Scanner from_client, PrintWriter to_client, Archivio archivio){
        HashMap<String, String> dati = new HashMap<>();

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
        from_client.nextLine();
        dati.put("nome", from_client.nextLine());
        to_client.print("\nInserisci Cognome: "+end);
        to_client.flush();
        dati.put("cognome", from_client.nextLine());

        return dati;
    }

    private Utente login(Scanner from_client, PrintWriter to_client, Archivio archivio){
        String username;
        String password;
        Utente utente = null;

        while (true) {
            to_client.print("\nInserisci username: " + end);
            to_client.flush();
            username = from_client.next();

            if(archivio.isLogged(username)) {
                to_client.print("\nLogin già effettuato\n");
                continue;
            }
            to_client.print("Inserisci password: "+ end);
            to_client.flush();
            password = from_client.next();
            utente = archivio.loginCheck(username, password);
            if (utente == null){
                to_client.print("\nDati Errati\n");
            }else break;
        }
        return utente;
    }

    private int sceltaCorso(Scanner from_client, PrintWriter to_client, Archivio archivio, Docente docente){

        LinkedList<Corso> lista = docente.getListaCorsi();

        int scelta;

                to_client.print("\n\n------------------------");
                to_client.print("\nLISTA CORSI\n");
                to_client.print("\nID | NOME | N°LEZIONI");
                for(Corso c:lista){
                    to_client.print("\n"+c.getIdCorso()+" - "+c.getNomeCorso()+ " - " +c.getTotLezioni());
                }

                while(true){
                    to_client.print("\n\nScegli ID corso (digita 0 per tornare indietro): "+end);
                    to_client.flush();
                    if(from_client.hasNextInt()==true){
                        scelta=from_client.nextInt();
                        break;
                    }
                    from_client.next();
                }
            return scelta;
    }

    private void visualLezioniPerData(PrintWriter to_client, Corso corso){
        to_client.print("\n------------------------");
        to_client.print("\nCORSO: "+corso.getNomeCorso()+ " - Tot ore: " + corso.getTotOre());
        LinkedList<Lezione> lezOrd = corso.ordinaLezioni();
        to_client.print("\n\n|    DATA    | ORE | ARGOMENTO");
        for(Lezione l : lezOrd){
            to_client.print(String.format("\n %td %tb %tY", l.getData(),l.getData(), l.getData()) +" -  "+l.getOre()+"  -  "+l.getArgomento());
        }
        to_client.print("\n");
        to_client.flush();
    }

    private void back(Scanner from_client, PrintWriter to_client){
        int message_from_client;
        boolean num = false;
            while (true) {
                to_client.print("\n\ndigita 0 per tornare indietro: "+end);
                to_client.flush();
                if (from_client.hasNextInt() == true) {
                    message_from_client = from_client.nextInt();
                    num = true;
                    if(message_from_client == 0)
                       return;
                }
                if(num == false)
                    from_client.next();
            }
    }

    private void visualAvvisi(PrintWriter to_client, Archivio archivio){

        TreeSet<Avviso> set = new TreeSet<>();
        for(Utente u : archivio.getListaUtenti())
            if(u instanceof Direttore)
                set.addAll(((Direttore) u).getListaAvvisi());

        to_client.print("\n\n------------------------");
        to_client.print("\nLISTA AVVISI\n");
        to_client.print("\n        DATA        |   MITTENTE   | TESTO");
        to_client.flush();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        for(Avviso a : set) {
            to_client.print("\n"+formatter.format(a.getData())+" - "+archivio.getCognomeNome(a.getUser())+" - "+a.getTesto());
            to_client.flush();
        }
    }

    private void logout(String username){
        archivio.logout(username);
    }

}

