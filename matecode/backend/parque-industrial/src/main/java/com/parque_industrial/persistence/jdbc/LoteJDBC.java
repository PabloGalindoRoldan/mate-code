package com.parque_industrial.persistence.jdbc;
import com.parque_industrial.entities.Lote;
import com.parque_industrial.persistence.dtos.LoteDTO;
import com.parque_industrial.services.DAOInmobiliario;
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
public class LoteJDBC implements DAOInmobiliario {
    // columnas de lote
    private final String ID = "id";
    private final String SUPERFICIE = "superficie";
    private final String ESTADO = "estado";
    private final String FECHA = "fechaVenta";
    private final String MONTO = "montoVenta";
    // estados de lote
    private final String DISPONIBLE = "disponible";
    private final String RESERVADO = "reservado";
    private final String VENDIDO= "vendido";

    private final DataSource conecction;
    public LoteJDBC(DataSource conecction) {
        this.conecction = conecction;
    }

    @Override
    public List<LoteDTO> LotesDisponibles() throws Exception{
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
                    lotes.add( LoteDTO.dto(new Lote(res.getInt(ID),
                            res.getDouble(SUPERFICIE),
                            res.getString(ESTADO),
                            fechaVenta,
                            res.getDouble(MONTO)))
                    );
                }
            }
        }catch (SQLException ex) {
            throw new Exception("Error al acceder a Railway:" + ex.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction); // Liberar conexión transaccional
        }
        return lotes;
    }

    @Override
    public void crearLote(Lote lote) throws Exception {
        String insert = "INSERT INTO Lote ("+ID+","+ SUPERFICIE +","+ESTADO+","+FECHA+","+MONTO+") VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        try{
            conn = DataSourceUtils.getConnection(this.conecction);
            try(PreparedStatement ps = conn.prepareStatement(insert)){
                ps.setInt(1, lote.getIdentificacion());
                ps.setDouble(2, lote.getSuperficie());
                ps.setString(3, lote.getEstado());
                ps.setDate(4, lote.FechaVentaSQL());
                ps.setDouble(5, lote.getMontoVenta());
                ps.executeUpdate();
            }
        }catch (SQLException exception){
            throw  new Exception("Error al acceder a Railway:" + exception.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
    }
    @Override
    public void venderLote(Lote lote) throws Exception {
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
            throw  new Exception("Error al acceder a Railway:" + exception.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
    }

    @Override
    public void reservarLote(Lote lote) throws Exception {
        String sql = "UPDATE Lote SET " + ESTADO + " = '" + RESERVADO+"' WHERE " + ID + " = ? ";
        Connection conn = null;
        try{
            conn = DataSourceUtils.getConnection(this.conecction);
            try(PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setInt(1, lote.getIdentificacion());
                ps.executeUpdate();
            }
        }catch (SQLException exception){
            throw  new Exception("Error al acceder a Railway:" + exception.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
    }

    @Override
    public LoteDTO buscarLotePorID(int identificacion) throws Exception {
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
                            res.getDouble(MONTO));
                }else {
                    return null;
                }
            }
        } catch (SQLException exception){
            throw  new Exception("Error al acceder a Railway:" + exception.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
    }

    @Override
    public List<LoteDTO> LotesVendidos() throws Exception {
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
                    lotes.add( LoteDTO.dto(new Lote(res.getInt(ID),
                            res.getDouble(SUPERFICIE),
                            res.getString(ESTADO),
                            fechaVenta,
                            res.getDouble(MONTO)))
                    );
                }
            }
        }catch (SQLException ex) {
            throw new Exception("Error al acceder a Railway:" + ex.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
        return lotes;
    }

    @Override
    public List<LoteDTO> LotesReservados() throws Exception {
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
                    lotes.add( LoteDTO.dto(new Lote(res.getInt(ID),
                            res.getDouble(SUPERFICIE),
                            res.getString(ESTADO),
                            fechaVenta,
                            res.getDouble(MONTO)))
                    );
                }
            }
        }catch (SQLException ex) {
            throw new Exception("Error al acceder a Railway:" + ex.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
        return lotes;
    }

    @Override
    public void cancelarReserva(Lote lote) throws Exception {
        String sql = "UPDATE Lote SET " + ESTADO + " = '" + DISPONIBLE + "' WHERE " + ID + " = ? ";
        Connection conn = null;
        try{
            conn = DataSourceUtils.getConnection(this.conecction);
            try(PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setInt(1, lote.getIdentificacion());
                ps.executeUpdate();
            }
        }catch (SQLException exception){
            throw  new Exception("Error al acceder a Railway:" + exception.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
    }
}