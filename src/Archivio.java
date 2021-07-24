import java.io.*;
import java.util.*;

public class Archivio implements Serializable {
    private static final long serialVersionUID = -4140436349008090061L;
    private LinkedList<Utente> listaUtenti;
    private transient LinkedList<String> sessione;

    public Archivio() {
        this.listaUtenti = new LinkedList<Utente>();
        this.sessione = new LinkedList<>();
    }

    public synchronized void creaSessione(){
        sessione = new LinkedList<>();
    }

    public synchronized LinkedList<Utente> getListaUtenti() {
        return listaUtenti;
    }

    public synchronized Utente getUtente(String username){
        Utente utente = null;

        for(Utente u : listaUtenti){
            if(u.getUsername().equals(username)) return u;
        }
        return utente;
    }

    public synchronized Utente loginCheck(String username, String password){

        for(Utente u : listaUtenti){
            if(u.getUsername().equals((username)) && u.getPassword().equals(password)) {
                sessione.add(username);
                return u;
            }
        }
        return null;
    }

    public synchronized boolean isLogged(String username){
        if(sessione.contains(username))
            return true;
        else return false;
    }

    public synchronized void logout(String username){
        sessione.remove(username);
    }

    public synchronized void addUtente(Utente utente){
        listaUtenti.add(utente);
        onFile();
    }

    public synchronized void addCorso(Docente docente, String nome){
        docente.addCorso(nome);
        onFile();
    }

    public synchronized void addLezione(Docente docente, Corso corso, Date data, int ore, String argomento){
        corso.addLezione(data, ore, argomento);
        docente.addNumLezioni();
        onFile();
    }

    public synchronized void delLezione(Docente docente, Corso corso, int id){
        corso.delLezione(id);
        docente.remNumLezioni();
        onFile();
    }

    public synchronized void delCorso(Docente docente, int id){
        docente.delCorso(id);
        onFile();
    }

    public synchronized void onFile(){
        try {
            FileOutputStream f = new FileOutputStream("archivio.ser");
            ObjectOutputStream os = new ObjectOutputStream(f);
            os.writeObject(this);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean checkDoc(String username){
         for(Utente u : listaUtenti){
             if(u instanceof Docente)
                 if(u.getUsername().equals(username)) return true;
         }
         return false;
    }

    public synchronized void delDocente(String username){

        for(Utente u : listaUtenti)
            if(u instanceof Docente)
                if(u.getUsername().equals(username)){
                    listaUtenti.remove(u);
                    onFile();
                    return;
                }
        /*Iterator<Utente> iterator = listaUtenti.iterator();
        while(iterator.hasNext()){
            Utente u = iterator.next();
            if(u.getUsername().equals(username))
                listaUtenti.remove(u);
        }
        onFile();*/
    }

    public synchronized Docente getDocente(String username){
        for(Utente u : listaUtenti){
            if(u.getUsername().equals(username)) return (Docente)u;
        }
        return null;
    }

    public synchronized LinkedList<Utente> ordinaUtenti(){
        LinkedList<Utente> listaord = new LinkedList<>(listaUtenti);
        Collections.sort(listaord);
        return listaord;
    }

    public synchronized void addAvviso(String testo, Direttore direttore){
        direttore.addAvviso(testo);
        onFile();
    }

    public synchronized String getCognomeNome(String user){
        String s = " ";
        for(Utente u : listaUtenti)
            if(u.getUsername().equals(user))
                return u.getCognome()+" "+u.getCognome();
        return s;
    }

    public synchronized void delAvviso(int id, Direttore direttore){
        direttore.delAvviso(id);
        onFile();
    }

    @Override
    public synchronized String toString() {
        return "Archivio{" +
                "listaUtenti=" + listaUtenti +
                '}';
    }

}
