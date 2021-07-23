import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Direttore extends Utente{

    private LinkedList<Avviso> listaAvvisi;
    private static final long serialVersionUID = 7518339047369784272L;

    public Direttore(String username, String password, String nome, String cognome) {
        super(username, password, nome, cognome);
        listaAvvisi = new LinkedList<>();
    }

    public void addAvviso(String testo){
        int id;
        try{
            Avviso a = listaAvvisi.getLast();
            id = a.getId()+1;
        }catch(NoSuchElementException ex){
            id = 1;
        }
        Avviso avviso = new Avviso(id, testo, super.getUsername());
        listaAvvisi.add(avviso);
    }

    public boolean checkIdAvviso(int id){
        for(Avviso a : listaAvvisi)
            if(a.getId() == id) return true;
        return false;
    }

    public void delAvviso(int id){
        for(Avviso a : listaAvvisi)
            if(a.getId()==id){
                listaAvvisi.remove(a);
                return;
            }
    }

    public LinkedList<Avviso> getListaAvvisi(){
        return listaAvvisi;
    }

    @Override
    public String toString() {
        return "\n\nDirettore{" +
                super.toString()+
                "Lista Avvisi ="+listaAvvisi+
                "}";
    }


}
