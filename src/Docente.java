import java.io.Serializable;
import java.util.LinkedList;

public class Docente extends Utente implements Serializable {

    private LinkedList<Corso> listaCorsi;
    private int totCorsi;
    private int totLezioni;
    private static final long serialVersionUID = 437850178713925464L;

    public Docente(String username, String password, String nome, String cognome) {
        super(username, password, nome, cognome);
        listaCorsi = new LinkedList<>();
        this.totCorsi = 0;
        this.totLezioni = 0;
    }

    public LinkedList<Corso> getListaCorsi() {
        return listaCorsi;
    }

    public int getTotCorsi() {
        return totCorsi;
    }

    public int getTotLezioni() {
        return totLezioni;
    }

    public void addCorso(String nome){
        int id;
        Corso c = listaCorsi.getFirst();
        if(c == null) id = 0;
        else id = c.getIdCorso()+1;
        Corso corso = new Corso(id, nome);
        totCorsi = totCorsi+1;
        listaCorsi.add(corso);
    }

    public void addNumLezioni(){
        totLezioni = totLezioni+1;
    }

    @Override
    public String toString() {
        return "Docente{" +
                super.toString()+
                "listaCorsi=" + listaCorsi +
                ", totCorsi=" + totCorsi +
                ", totLezioni=" + totLezioni +
                '}';
    }
}
