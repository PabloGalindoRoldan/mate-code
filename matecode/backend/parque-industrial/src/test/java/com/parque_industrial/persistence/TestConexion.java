package com.parque_industrial.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertNotNull;


/*
bueno buscando como conectar la bd con springboot:
 use dataSource en vez de DriverManager porque es la mejor para manejar conexiones a bd.
 Spring Boot configura automáticamente un DataSource basado en las propiedades que se pusieron en application.properties (esta en resources)
 ademas tambien le consulte a la ia y dice q es mejor porque es un pool de conexiones en vez de abrir, usar y cerrar cada vez q se usa, etc.
 */

@SpringBootTest //levanta el contexto completo de la aplicación, tipo configuraciones, propiadades y todo lo de springboot
class TestConexion {


    // Springboot crea directamente un DataSource con las propiedades configurados por haber agregado la dependencia spring-boot-starter-jdbc

    @Autowired //esto busca un DataSource y lo asigna (va a encontrar el q creo Springboot con las propiedades de application.properties)
    private DataSource dataSource;

    @Test
    void conectarBD() throws Exception {
 //variables de entorno
        // (si creas otro entorno de desarrollo en railway y copias las variables de entorno,  en url agregarle al principio jdbc:)
        // produccion: DB_URL=jdbc:mysql://viaduct.proxy.rlwy.net:20719/railway;DB_USERNAME=root;DB_PASSWORD=zTUeEPDomRVwmjXyqYKWNqerekEQPJwl
        try (Connection con = dataSource.getConnection()) {
            assertNotNull(con);
            System.out.println("Conexión exitosa");
        }
    }
}


