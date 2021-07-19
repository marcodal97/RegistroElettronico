import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Set;

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

    public int getIdCorso() {
        return idCorso;
    }

    public String getNomeCorso() {
        return nomeCorso;
    }

    public LinkedList<Lezione> getListaLezioni() {
        return listaLezioni;
    }

    public int getTotOre() {
        return totOre;
    }

    public int getTotLezioni() {
        return totLezioni;
    }

    public void addLezione(Date data, int ore, String argomento){
        int id;
        Lezione lez = listaLezioni.getFirst();
        if(lez == null) id = 0;
        else id = lez.getIdLezione()+1;

        Lezione l = new Lezione(id, data, ore, argomento);
        listaLezioni.add(l);
        addOre(ore);
        addNumLezione();
    }

    public void addOre(int ore){
        this.totOre = this.totOre+ore;
    }

    public void addNumLezione(){
        this.totLezioni = this.totLezioni + 1;
    }

    @Override
    public String toString() {
        return "Corso{" +
                "idCorso=" + idCorso +
                ", nomeCorso='" + nomeCorso + '\'' +
                ", listaLezioni=" + listaLezioni +
                ", totOre=" + totOre +
                ", totLezioni=" + totLezioni +
                '}';
    }
}
