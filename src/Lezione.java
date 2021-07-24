import java.io.Serializable;
import java.util.Date;

public class Lezione implements Serializable, Comparable<Lezione>{
    private static final long serialVersionUID = 5134225067835947374L;
    private  int idLezione;
    private Date data;
    private int ore;
    private String argomento;

    public Lezione(int idLezione, Date data, int ore, String argomento) {
        this.idLezione = idLezione;
        this.data = data;
        this.ore = ore;
        this.argomento = argomento;
    }

    public synchronized int getIdLezione() {
        return idLezione;
    }

    public synchronized Date getData() {
        return data;
    }

    public synchronized int getOre() {
        return ore;
    }

    public synchronized String getArgomento() {
        return argomento;
    }

    @Override
    public synchronized String toString() {

        return "Lezione{" +
                "idLezione=" + idLezione +
                ", data=" + String.format("%td %tb %tY", data,data, data)+
                ", ore=" + ore +
                ", argomento='" + argomento + '\'' +
                '}';
    }

    @Override
    public synchronized int compareTo(Lezione lezione) {
        if(lezione.getData().before(this.data)) return -1;
        if(lezione.getData().after(this.data)) return 1;
        return 0;
    }
}