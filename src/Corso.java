import java.io.Serializable;
import java.util.*;

public class Corso implements Serializable {

    private static final long serialVersionUID = -169089265239444737L;
    private int idCorso;
    private String nomeCorso;
    private LinkedList<Lezione> listaLezioni;
    private int totOre;
    private int totLezioni;

    public Corso(int idCorso, String nomeCorso) {
        this.idCorso = idCorso;
        this.nomeCorso = nomeCorso;
        this.listaLezioni = new LinkedList<Lezione>();
        this.totOre = 0;
        this.totLezioni = 0;
    }

    public synchronized int getIdCorso() {
        return idCorso;
    }

    public synchronized String getNomeCorso() {
        return nomeCorso;
    }

    public synchronized LinkedList<Lezione> getListaLezioni() {
        return listaLezioni;
    }

    public synchronized int getTotOre() {
        return totOre;
    }

    public synchronized int getTotLezioni() {
        return totLezioni;
    }

    public synchronized void addLezione(Date data, int ore, String argomento){
        int id;

        try{
            Lezione lez = listaLezioni.getLast();
            id = lez.getIdLezione()+1;
        }catch(NoSuchElementException ex){
            id = 1;
        }

        Lezione l = new Lezione(id, data, ore, argomento);
        listaLezioni.add(l);
        addOre(ore);
        addNumLezione();
    }

    public synchronized void addOre(int ore){
        this.totOre = this.totOre+ore;
    }

    public synchronized void addNumLezione(){
        this.totLezioni = this.totLezioni + 1;
    }

    public synchronized boolean checkId(int id){
        for(Lezione l : listaLezioni){
            if(l.getIdLezione() == id) return true;
        }
        return false;
    }

    public synchronized void delLezione(int id){
        for(Lezione l : listaLezioni){
            if(l.getIdLezione()==id){
                totOre = totOre - l.getOre();
                totLezioni = totLezioni - 1;
                listaLezioni.remove(l);
                return;
            }
        }

    }

    public synchronized LinkedList<Lezione> ordinaLezioni(){
        LinkedList<Lezione> lez = new LinkedList<>(listaLezioni);
        Collections.sort(lez);
        return lez;
    }

    @Override
    public synchronized String toString() {
        return "Corso{" +
                "\nidCorso=" + idCorso +
                ", nomeCorso='" + nomeCorso + '\'' +
                ", listaLezioni=" + listaLezioni +
                ", totOre=" + totOre +
                ", totLezioni=" + totLezioni +
                '}';
    }
}
