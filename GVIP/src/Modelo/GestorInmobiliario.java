package Modelo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
public interface GestorInmobiliario {
    public List<Lote> LotesDisponibles();
    public void asignarLote(Empresa empresa, Lote lote);
    public void crearLote(Lote lote);
    public void venderLote(Lote lote);
    public void reservarLote(Lote lote);
}
