import java.io.Serializable;
import java.util.Date;

public class Lezione implements Serializable {
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

    public int getIdLezione() {
        return idLezione;
    }

    public Date getData() {
        return data;
    }

    public int getOre() {
        return ore;
    }

    public String getArgomento() {
        return argomento;
    }

    @Override
    public String toString() {
        return "Lezione{" +
                "idLezione=" + idLezione +
                ", data=" + data +
                ", ore=" + ore +
                ", argomento='" + argomento + '\'' +
                '}';
    }
}
