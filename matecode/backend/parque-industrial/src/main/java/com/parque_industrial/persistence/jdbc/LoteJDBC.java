package com.parque_industrial.persistence.jdbc;
import com.parque_industrial.entities.Lote;
import com.parque_industrial.persistence.dtos.LoteDTO;
import com.parque_industrial.services.DAOInmobiliario;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoteJDBC implements DAOInmobiliario {
    // columnas de lote
    private final String ID = "identificacion";
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
        try(Connection con = this.conecction.getConnection();
        PreparedStatement ps = con.prepareStatement(query)){
            ResultSet res = ps.executeQuery();
            while (res.next()){
                lotes.add( LoteDTO.dto(new Lote(res.getInt(ID),
                        res.getDouble(SUPERFICIE),
                        res.getString(ESTADO),
                        res.getDate(FECHA).toLocalDate(),
                        res.getDouble(MONTO)))
                );
            }
        }catch (SQLException ex) {
            throw new Exception("Error al acceder a Railway:" + ex.getMessage());
        }
        return lotes;
    }

    @Override
    public void crearLote(Lote lote) throws Exception {
        String insert = "INSERT INTO Lote ("+ID+","+ SUPERFICIE +","+ESTADO+","+FECHA+","+MONTO+") VALUES (?, ?, ?, ?, ?, ?)";
        try(Connection conn = this.conecction.getConnection();
            PreparedStatement ps = conn.prepareStatement(insert)){
            ps.setInt(1, lote.getIdentificacion());
            ps.setDouble(2, lote.getSuperficie());
            ps.setString(3, lote.getEstado());
            ps.setDate(4, Date.valueOf(lote.getFechaVenta()));
            ps.setDouble(5, lote.getMontoVenta());
            ps.executeUpdate();
        }catch (SQLException exception){
            throw  new Exception("Error al acceder a Railway:" + exception.getMessage());
        }
    }
    @Override
    public void venderLote(Lote lote) throws Exception {
        String sql = "UPDATE Lote SET " + ESTADO + " = '" + VENDIDO + "', FECHA +  = ?, " + MONTO + " = ? WHERE " + ID + " = ?";
        try(Connection conn = this.conecction.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setDate(1, Date.valueOf(lote.getFechaVenta()));
            ps.setDouble(2, lote.getMontoVenta());
            ps.setInt(3, lote.getIdentificacion());
            ps.executeUpdate();
        }catch (SQLException exception){
            throw  new Exception("Error al acceder a Railway:" + exception.getMessage());
        }
    }

    @Override
    public void reservarLote(Lote lote) throws Exception {
        String sql = "UPDATE Lote SET " + ESTADO + " = '" + RESERVADO+" WHERE " + ID + " = ?";
        try(Connection conn = this.conecction.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, lote.getIdentificacion());
            ps.executeUpdate();
        }catch (SQLException exception){
            throw  new Exception("Error al acceder a Railway:" + exception.getMessage());
        }
    }

    @Override
    public LoteDTO buscarLotePorID(int identificacion) throws Exception {
       String query = "SELECT * FROM LOTE WHERE " + ID + " = ?";
       try(Connection conn = this.conecction.getConnection();
           PreparedStatement ps = conn.prepareStatement(query)){
           ps.setInt(1, identificacion);
           ResultSet res = ps.executeQuery();
           if(res.next()){
               return LoteDTO.dto(new Lote(res.getInt(ID),
                       res.getDouble(SUPERFICIE),
                       res.getString(ESTADO),
                       res.getDate(FECHA).toLocalDate(),
                       res.getDouble(MONTO)));
           }else {
               return null;// esto retornaria null o un optinal? creo q nunca retornaria null siempre buscamos un lote de nuestro mapa
           }
       } catch (SQLException exception){
                throw  new Exception("Error al acceder a Railway:" + exception.getMessage());
       }
    }

    @Override
    public List<LoteDTO> LotesVendidos() throws Exception {
        String query = "SELECT * FROM Lote WHERE " + ESTADO + " = '" + VENDIDO + "'";
        List<LoteDTO> lotes = new ArrayList<>();
        try(Connection con = this.conecction.getConnection();
            PreparedStatement ps = con.prepareStatement(query)){
            ResultSet res = ps.executeQuery();
            while (res.next()){
                lotes.add( LoteDTO.dto(new Lote(res.getInt(ID),
                        res.getDouble(SUPERFICIE),
                        res.getString(ESTADO),
                        res.getDate(FECHA).toLocalDate(),
                        res.getDouble(MONTO)))
                );
            }
        }catch (SQLException ex) {
            throw new Exception("Error al acceder a Railway:" + ex.getMessage());
        }
        return lotes;
    }

    @Override
    public List<LoteDTO> LotesReservados() throws Exception {
        String query = "SELECT * FROM Lote WHERE " + ESTADO + " = '" + RESERVADO + "'";
        List<LoteDTO> lotes = new ArrayList<>();
        try(Connection con = this.conecction.getConnection();
            PreparedStatement ps = con.prepareStatement(query)){
            ResultSet res = ps.executeQuery();
            while (res.next()){
                lotes.add( LoteDTO.dto(new Lote(res.getInt(ID),
                        res.getDouble(SUPERFICIE),
                        res.getString(ESTADO),
                        res.getDate(FECHA).toLocalDate(),
                        res.getDouble(MONTO)))
                );
            }
        }catch (SQLException ex) {
            throw new Exception("Error al acceder a Railway:" + ex.getMessage());
        }
        return lotes;
    }

    @Override
    public void cancelarReserva(Lote lote) throws Exception {
        String sql = "UPDATE Lote SET " + ESTADO + " = '" + DISPONIBLE + " WHERE " + ID + " = ?";
        try(Connection conn = this.conecction.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, lote.getIdentificacion());
            ps.executeUpdate();
        }catch (SQLException exception){
            throw  new Exception("Error al acceder a Railway:" + exception.getMessage());
        }
    }

}
