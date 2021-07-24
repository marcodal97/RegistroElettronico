import java.io.Serializable;
import java.util.Date;

public class Avviso implements Serializable, Comparable<Avviso>{
    private int id;
    private String testo;
    private Date data;
    private String dirUser;
    private static final long serialVersionUID = 6155605812130458885L;

    public Avviso(int id, String testo, String dirUser) {
        this.testo = testo;
        this.data = new Date();
        this.id = id;
        this.dirUser = dirUser;
    }

    public synchronized int getId() {
        return id;
    }

    public synchronized String getTesto() {
        return testo;
    }

    public synchronized Date getData() {
        return data;
    }

    public synchronized String getUser(){
        return dirUser;
    }

    @Override
    public synchronized int compareTo(Avviso o) {
        if(o.getData().before(this.data)) return -1;
        if(o.getData().after(this.data)) return 1;
        return 0;
    }

    @Override
    public synchronized String toString() {
        return "\nAvviso{" +
                "id=" + id +
                ", testo='" + testo + '\'' +
                ", data=" + data +
                '}';
    }
}
