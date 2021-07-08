import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

public class Corso {
    private int idCorso;
    private String nomeCorso;
    private LinkedList<Lezione> listaLezioni;
    private int totOre;
    private int totLezioni;

    public Corso(int idCorso, String nomeCorso, Set<Lezione> listaLezioni) {
        this.idCorso = idCorso;
        this.nomeCorso = nomeCorso;
        this.listaLezioni = new LinkedList<>();
        this.totOre = 0;
        this.totLezioni = 0;
    }
}