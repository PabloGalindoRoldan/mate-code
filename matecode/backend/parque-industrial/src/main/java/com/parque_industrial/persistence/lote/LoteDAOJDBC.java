package com.parque_industrial.persistence.lote;
import com.parque_industrial.entities.Lote;
import com.parque_industrial.dto.lote.LoteDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.datasource.DataSourceUtils; // Importar DataSourceUtils

@Repository
@Transactional
public class LoteDAOJDBC implements LoteDAO {
    // columnas de lote
    private final String ID = "id";
    private final String SUPERFICIE = "superficie";
    private final String ESTADO = "estado";
    private final String FECHA = "fechaVenta";
    private final String MONTO = "montoVenta";
    private final String NC = "nc";
    private final String TIPO = "tipo";
    private final String PARQUE = "parque";
    // valores de parque
    private final String PARQUE_VIEJO = "viejo";
    private final String PARQUE_NUEVO = "nuevo";
    // estados de lote
    private final String DISPONIBLE = "disponible";
    private final String RESERVADO = "reservado";
    private final String VENDIDO= "vendido";

    private final DataSource conecction;
    public LoteDAOJDBC(DataSource conecction) {
        this.conecction = conecction;
    }

    @Override
    public void crearLote(Lote lote)  {
        String insert = "INSERT INTO Lote ("+ID+","+ SUPERFICIE +","+ESTADO+","+FECHA+","+MONTO+","+
                NC+","+TIPO+","+PARQUE+") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try{
            conn = DataSourceUtils.getConnection(this.conecction);
            try(PreparedStatement ps = conn.prepareStatement(insert)){
                ps.setInt(1, lote.getIdentificacion());
                ps.setDouble(2, lote.getSuperficie());
                ps.setString(3, lote.getEstado());
                ps.setDate(4, lote.FechaVentaSQL());
                ps.setDouble(5, lote.getMontoVenta());
                ps.setString(6, lote.getNc());
                ps.setString(7, lote.getTipo());
                ps.setString(8, lote.getParque());
                ps.executeUpdate();
            }
        }catch (SQLException exception){
            throw  new IllegalArgumentException("Error al acceder a Railway:" + exception.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
    }

    @Override
    public void venderLote(Lote lote)   {
        String sql = "UPDATE Lote SET " + ESTADO + " = '" + VENDIDO + "', " + FECHA + " = ?, " + MONTO + " = ? WHERE " + ID + " = ? ";
        Connection conn = null;
        try{
            conn = DataSourceUtils.getConnection(this.conecction);
            try(PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setDate(1, lote.FechaVentaSQL());
                ps.setDouble(2, lote.getMontoVenta());
                ps.setInt(3, lote.getIdentificacion());
                ps.executeUpdate();
            }
        }catch (SQLException exception){
            throw  new IllegalArgumentException("Error al acceder a Railway:" + exception.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
    }
    @Override
    public void reservarLote(Lote lote)   {
        String sql = "UPDATE Lote SET " + ESTADO + " = '" + RESERVADO+"' WHERE " + ID + " = ? ";
        Connection conn = null;
        try{
            conn = DataSourceUtils.getConnection(this.conecction);
            try(PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setInt(1, lote.getIdentificacion());
                ps.executeUpdate();
            }
        }catch (SQLException exception){
            throw  new IllegalArgumentException("Error al acceder a Railway:" + exception.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
    }

    @Override
    public void cambiarEstadoLote(Lote lote) {
        String sql = "UPDATE Lote SET " + ESTADO + " = '" + DISPONIBLE + "' WHERE " + ID + " = ? ";
        Connection conn = null;
        try{
            conn = DataSourceUtils.getConnection(this.conecction);
            try(PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setInt(1, lote.getIdentificacion());
                ps.executeUpdate();
            }
        }catch (SQLException exception){
            throw  new IllegalArgumentException("Error al acceder a Railway:" + exception.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
    }

    @Override
    public void cancelarReserva(Lote lote)   {
        String sql = "UPDATE Lote SET " + ESTADO + " = '" + DISPONIBLE + "' WHERE " + ID + " = ? ";
        Connection conn = null;
        try{
            conn = DataSourceUtils.getConnection(this.conecction);
            try(PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setInt(1, lote.getIdentificacion());
                ps.executeUpdate();
            }
        }catch (SQLException exception){
            throw  new IllegalArgumentException("Error al acceder a Railway:" + exception.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
    }

    @Override
    public LoteDTO buscarLotePorID(int identificacion)  {
        String query = "SELECT * FROM Lote WHERE " + ID + " = ?";
        Connection conn = null;
        try{
            conn = DataSourceUtils.getConnection(this.conecction);
            try(PreparedStatement ps = conn.prepareStatement(query)){
                ps.setInt(1, identificacion);
                ResultSet res = ps.executeQuery();
                if(res.next()){
                    Date fechaVentaSql = res.getDate(FECHA);
                    LocalDate fechaVenta = (fechaVentaSql != null) ? fechaVentaSql.toLocalDate() : null;
                    return new LoteDTO(res.getInt(ID),
                            res.getDouble(SUPERFICIE),
                            res.getString(ESTADO),
                            fechaVenta,
                            res.getDouble(MONTO),
                            res.getString(NC),
                            res.getString(TIPO),
                            res.getString(PARQUE)
                    );
                }else {
                    return null;
                }
            }
        } catch (SQLException exception){
            throw  new IllegalArgumentException("Error al acceder a Railway:" + exception.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
    }

    @Override
    public List<LoteDTO> LotesDisponibles() {
        String query = "SELECT * FROM Lote WHERE " + ESTADO + " = '" + DISPONIBLE + "'";
        List<LoteDTO> lotes = new ArrayList<>();
        Connection conn = null; // Declarar fuera del try-with-resources para DataSourceUtils
        try{
            conn = DataSourceUtils.getConnection(this.conecction); // Obtener conexión transaccional
            try(PreparedStatement ps = conn.prepareStatement(query)){
                ResultSet res = ps.executeQuery();
                while (res.next()){
                    Date fechaVentaSql = res.getDate(FECHA);
                    LocalDate fechaVenta = (fechaVentaSql != null) ? fechaVentaSql.toLocalDate() : null;
                    lotes.add( new LoteDTO(res.getInt(ID),
                                    res.getDouble(SUPERFICIE),
                                    res.getString(ESTADO),
                                    fechaVenta,
                                    res.getDouble(MONTO),
                                    res.getString(NC),
                                    res.getString(TIPO),
                                    res.getString(PARQUE)
                            )
                    );
                }
            }
        }catch (SQLException ex) {
            throw new IllegalArgumentException("Error al acceder a Railway:" + ex.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction); // Liberar conexión transaccional
        }
        return lotes;
    }

    @Override
    public List<LoteDTO> LotesVendidos()  {
        String query = "SELECT * FROM Lote WHERE " + ESTADO + " = '" + VENDIDO + "'";
        List<LoteDTO> lotes = new ArrayList<>();
        Connection conn = null;
        try{
            conn = DataSourceUtils.getConnection(this.conecction);
            try(PreparedStatement ps = conn.prepareStatement(query)){
                ResultSet res = ps.executeQuery();
                while (res.next()){
                    Date fechaVentaSql = res.getDate(FECHA);
                    LocalDate fechaVenta = (fechaVentaSql != null) ? fechaVentaSql.toLocalDate() : null;
                    lotes.add( new LoteDTO(res.getInt(ID),
                            res.getDouble(SUPERFICIE),
                            res.getString(ESTADO),
                            fechaVenta,
                            res.getDouble(MONTO),
                            res.getString(NC),
                            res.getString(TIPO),
                            res.getString(PARQUE))
                    );
                }
            }
        }catch (SQLException ex) {
            throw new IllegalArgumentException("Error al acceder a Railway:" + ex.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
        return lotes;
    }

    @Override
    public List<LoteDTO> LotesReservados()  {
        String query = "SELECT * FROM Lote WHERE " + ESTADO + " = '" + RESERVADO + "'";
        List<LoteDTO> lotes = new ArrayList<>();
        Connection conn = null;
        try{
            conn = DataSourceUtils.getConnection(this.conecction);
            try(PreparedStatement ps = conn.prepareStatement(query)){
                ResultSet res = ps.executeQuery();
                while (res.next()){
                    Date fechaVentaSql = res.getDate(FECHA);
                    LocalDate fechaVenta = (fechaVentaSql != null) ? fechaVentaSql.toLocalDate() : null;
                    lotes.add( new LoteDTO(res.getInt(ID),
                            res.getDouble(SUPERFICIE),
                            res.getString(ESTADO),
                            fechaVenta,
                            res.getDouble(MONTO),
                            res.getString(NC),
                            res.getString(TIPO),
                            res.getString(PARQUE))
                    );
                }
            }
        }catch (SQLException ex) {
            throw new IllegalArgumentException("Error al acceder a Railway:" + ex.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
        return lotes;
    }

    @Override
    public List<LoteDTO> LotesNuevos()  {
        String query = "SELECT * FROM Lote WHERE " + PARQUE + " = '" + PARQUE_NUEVO + "'";
        List<LoteDTO> lotes = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DataSourceUtils.getConnection(this.conecction);
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ResultSet res = ps.executeQuery();
                while (res.next()) {
                    Date fechaVentaSql = res.getDate(FECHA);
                    LocalDate fechaVenta = (fechaVentaSql != null) ? fechaVentaSql.toLocalDate() : null;
                    lotes.add(new LoteDTO(res.getInt(ID),
                            res.getDouble(SUPERFICIE),
                            res.getString(ESTADO),
                            fechaVenta,
                            res.getDouble(MONTO),
                            res.getString(NC),
                            res.getString(TIPO),
                            res.getString(PARQUE))
                    );
                }
            }
        } catch (SQLException ex) {
            throw new IllegalArgumentException("Error al acceder a Railway:" + ex.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
        return lotes;
    }

    @Override
    public List<LoteDTO> LotesViejos()  {
        String query = "SELECT * FROM Lote WHERE " + PARQUE + " = '" + PARQUE_VIEJO + "'";
        List<LoteDTO> lotes = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DataSourceUtils.getConnection(this.conecction);
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ResultSet res = ps.executeQuery();
                while (res.next()) {
                    Date fechaVentaSql = res.getDate(FECHA);
                    LocalDate fechaVenta = (fechaVentaSql != null) ? fechaVentaSql.toLocalDate() : null;
                    lotes.add(new LoteDTO(res.getInt(ID),
                            res.getDouble(SUPERFICIE),
                            res.getString(ESTADO),
                            fechaVenta,
                            res.getDouble(MONTO),
                            res.getString(NC),
                            res.getString(TIPO),
                            res.getString(PARQUE))
                    );
                }
            }
        } catch (SQLException ex) {
            throw new IllegalArgumentException("Error al acceder a Railway:" + ex.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
        return lotes;
    }
}