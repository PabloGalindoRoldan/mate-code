package com.parque_industrial.persistence;

import com.parque_industrial.persistence.jdbc.LoteJDBC;
import org.junit.jupiter.api.BeforeEach;

import javax.sql.DataSource;
import java.sql.SQLException;

public class LoteJDBCTest {
    private LoteJDBC repo;
    private MySqlDataSource testDataSource; // Implementación concreta para el test

    @BeforeEach
    void setUp() throws SQLException {
        // 1. Configuramos el DataSource apuntando a la BD de pruebas (Sandbox) [9]
        testDataSource = new MySqlDataSource();
        testDataSource.setUrl("jdbc:mysql://localhost:3306/parque_industrial_test");
        testDataSource.setUser("test_user");
        testDataSource.setPassword("test_pass");

        // 2. Inyectamos el DataSource en el repositorio (Constructor Injection) [1, 2]
        repo = new LoteRepositoryJDBC(testDataSource);
    }
}
