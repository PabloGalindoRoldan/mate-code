package com.parque_industrial.persistence.jdbc;

import com.parque_industrial.persistence.dtos.ConsumosDTO;
import com.parque_industrial.services.ConsumosDAO;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConsumosJDBC implements ConsumosDAO {
    private final DataSource conecction;
    public ConsumosJDBC(DataSource conecction) {
        this.conecction = conecction;
    }
// Consumos:{id(PK)(AUTO), mes, año, luz, gas, agua, residuos, cant_empleados, cant_vehiculos, cuit_empresa(FK)}
    @Override
    public void cargarConsumosDeEmpresa(int cuitEmpresa, ConsumosDTO consumosDTO) {
        String sql = "insert into Consumos (año, mes, luz,  gas, agua, residuos, cant_empleados, cant_vehiculos, cuit_empresa) values (?, ?, ?, ?, ?, ?, ?, ?,?)";
        int ano = LocalDate.now().getYear();
        int mes = LocalDate.now().getMonthValue();
        Connection conn = null;
            conn = DataSourceUtils.getConnection(this.conecction);
            try(PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setInt(1, ano);
                ps.setInt(2, mes);
                ps.setFloat(3, consumosDTO.luz());
                ps.setFloat(4, consumosDTO.gas());
                ps.setFloat(5, consumosDTO.agua());
                ps.setInt(6, consumosDTO.residuos());
                ps.setInt(7, consumosDTO.cantEmpleados());
                ps.setInt(7, consumosDTO.cantVehiculos());
                ps.setInt(8, consumosDTO.idEmpresa());
                ps.executeUpdate();
            }catch (SQLException exception){
                throw  new IllegalArgumentException("Error al acceder a Railway:" + exception.getMessage());
            } finally {
                DataSourceUtils.releaseConnection(conn, this.conecction);
            }
        }

    @Override
    public void asignarCantEmpleados(int cuitEmoresa, int cant) {
        String sql = "update Consumos set cant_empleados = ? where cuit_empresa = ? and año = ? and mes = ?";
        int ano = LocalDate.now().getYear();
        int mes = LocalDate.now().getMonthValue();
        Connection conn = null;
        conn = DataSourceUtils.getConnection(this.conecction);
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, cant);
            ps.setInt(2, cuitEmoresa);
            ps.setInt(3, ano);
            ps.setInt(4, mes);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalArgumentException("Error al acceder a Railway:" + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
    }

    @Override
    public void asignarCantVheiculos(int cuitEmoresa, int cant) {
        String sql = "update Consumos set cant_vehiculos = ? where cuit_empresa = ? and año = ? and mes = ?";
        int ano = LocalDate.now().getYear();
        int mes = LocalDate.now().getMonthValue();
        Connection conn = null;
        conn = DataSourceUtils.getConnection(this.conecction);
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, cant);
            ps.setInt(2, cuitEmoresa);
            ps.setInt(3, ano);
            ps.setInt(4, mes);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalArgumentException("Error al acceder a Railway:" + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
    }

    @Override
    public void asignarConsumoGas(int cuitEmoresa, float gas) {
        String sql = "update Consumos set gas = ? where cuit_empresa = ? and año = ? and mes = ?";
        int ano = LocalDate.now().getYear();
        int mes = LocalDate.now().getMonthValue();
        Connection conn = null;
        conn = DataSourceUtils.getConnection(this.conecction);
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setFloat(1, gas);
            ps.setInt(2, cuitEmoresa);
            ps.setInt(3, ano);
            ps.setInt(4, mes);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalArgumentException("Error al acceder a Railway:" + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
    }

    @Override
    public void asignarConsumoLuz(int cuitEmoresa, float luz) {
        String sql = "update Consumos set luz = ? where cuit_empresa = ? and año = ? and mes = ?";
        int ano = LocalDate.now().getYear();
        int mes = LocalDate.now().getMonthValue();
        Connection conn = null;
        conn = DataSourceUtils.getConnection(this.conecction);
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setFloat(1, luz);
            ps.setInt(2, cuitEmoresa);
            ps.setInt(3, ano);
            ps.setInt(4, mes);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalArgumentException("Error al acceder a Railway:" + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
    }


    @Override
    public void asignarConsumoResiduos(int cuitEmoresa, int kilosResiduos) {
        String sql = "update Consumos set residuos = ? where cuit_empresa = ? and año = ? and mes = ?";
        int ano = LocalDate.now().getYear();
        int mes = LocalDate.now().getMonthValue();
        Connection conn = null;
        conn = DataSourceUtils.getConnection(this.conecction);
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, kilosResiduos);
            ps.setInt(2, cuitEmoresa);
            ps.setInt(3, ano);
            ps.setInt(4, mes);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalArgumentException("Error al acceder a Railway:" + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
    }
    @Override
    public List<ConsumosDTO> generarReporteConsumoTotalParque() {
        String sql = "select * from Consumos";
        List<ConsumosDTO> listaConsumos = new ArrayList<>();
        Connection conn = null;
        try{
            conn = DataSourceUtils.getConnection(this.conecction);
            try(PreparedStatement ps = conn.prepareStatement(sql)){
                ResultSet res = ps.executeQuery();
                while(res.next()){
                    listaConsumos.add(new ConsumosDTO(res.getInt("id"),
                            res.getInt("año"),
                            res.getInt("mes"),
                            res.getFloat("luz"),
                            res.getFloat("agua"),
                            res.getFloat("gas"),
                            res.getInt("residuos"),
                            res.getInt("cant_empleados"),
                            res.getInt("cant_vehiculos"),
                            res.getInt("cuit_empresa"))
                    );
                }
                return listaConsumos;
            }
        }catch (SQLException exception){
            throw  new IllegalArgumentException("Error al acceder a Railway:" + exception.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
    }

    @Override
    public List<ConsumosDTO> generarReporteConsumoTotalEmpresa(String cuitEmpresa) {
        String sql = "select * from Consumos where cuit_empresa = ?";
        List<ConsumosDTO> listaConsumos = new ArrayList<>();
        Connection conn = null;
        try{
            conn = DataSourceUtils.getConnection(this.conecction);
            try(PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setString(1, cuitEmpresa);
                ResultSet res = ps.executeQuery();
                while(res.next()){
                    listaConsumos.add(new ConsumosDTO(res.getInt("id"),
                            res.getInt("año"),
                            res.getInt("mes"),
                            res.getFloat("luz"),
                            res.getFloat("agua"),
                            res.getFloat("gas"),
                            res.getInt("residuos"),
                            res.getInt("cant_empleados"),
                            res.getInt("cant_vehiculos"),
                            res.getInt("cuit_empresa"))
                    );
                }
                return listaConsumos;
            }
        }catch (SQLException exception){
            throw  new IllegalArgumentException("Error al acceder a Railway:" + exception.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
    }

    @Override
    public ConsumosDTO generarReporteConsumoEmpresa(String cuitEmpresa) {
        int ano = LocalDate.now().getYear();
        int mes = LocalDate.now().getMonthValue();
        String sql = "select * from Consumos where cuitEmpresa = ? and año = ? and mes = ?";
        Connection conn = null;
        try{
            conn = DataSourceUtils.getConnection(this.conecction);
            try(PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setString(1, cuitEmpresa);
                ps.setInt(2, ano);
                ps.setInt(3, mes);
                ResultSet res = ps.executeQuery();
                if(res.next()){
                    return new ConsumosDTO(res.getInt("id"),
                            res.getInt("año"),
                            res.getInt("mes"),
                            res.getFloat("luz"),
                            res.getFloat("agua"),
                            res.getFloat("gas"),
                            res.getInt("residuos"),
                            res.getInt("cant_empleados"),
                            res.getInt("cant_vehiculos"),
                            res.getInt("cuit_empresa")
                    );
                }
                else {
                    throw new IllegalArgumentException("No se encontraron consumos para la empresa con CUIT: " + cuitEmpresa);
                }
            }
        }catch (SQLException exception){
            throw  new IllegalArgumentException("Error al acceder a Railway:" + exception.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(conn, this.conecction);
        }
    }
}
