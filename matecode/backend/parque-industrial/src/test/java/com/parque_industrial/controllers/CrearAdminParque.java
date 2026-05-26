package com.parque_industrial.controllers;
import com.parque_industrial.config.JwtUtil;
import com.parque_industrial.dto.auth.RegisterRequest;
import com.parque_industrial.persistence.empresa.EmpresaDAO;
import com.parque_industrial.persistence.empresa.EmpresaDAOJDBC;
import com.parque_industrial.persistence.usuario.UsuarioDAO;
import com.parque_industrial.persistence.usuario.UsuarioDAOJDBC;
import com.parque_industrial.services.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.ContextLoader;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
public class CrearAdminParque {
    @Test

    public void crear() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // 1. Creamos un DataSource básico a mano para el test
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://root:YqSktvgWHwoSSPBtiOpLLscudbWsccaq@kodama.proxy.rlwy.net:12484/railway"); // <-- Cambia "tu_base_de_datos" por el nombre real
        dataSource.setUsername("root"); // <-- Tu usuario de MySQL
        dataSource.setPassword("YqSktvgWHwoSSPBtiOpLLscudbWsccaq"); // <-- Tu contraseña de MySQL

        // 2. Se lo pasamos al JdbcTemplate
        JdbcTemplate template = new JdbcTemplate(dataSource);

        // El resto de tu código queda exactamente igual, sin tocar nada más:
        EmpresaDAO empresaDAO = new EmpresaDAOJDBC(template);
        UsuarioDAO usuarioDAO = new UsuarioDAOJDBC(template);
        JwtUtil j = new JwtUtil();
        AuthService authService = new AuthService(usuarioDAO, empresaDAO, j, passwordEncoder);

        RegisterRequest r = new RegisterRequest("adminiparque",
                "Admin",
                "ParqueINDUSTRIAl@gmail.com",
                "Admin",
                "adminParque123",
                "adminParque123",
                "12-34567890-1",
                "12345678901",
                "12-34567890-1");

        authService.registerAdministradorParque(r);
    }
}