package pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;

public class OracleDataSource {
	// Instancia del pool de conexiones
    private static final BasicDataSource ds = new BasicDataSource();

    static {
        try {
            // Configuración de la conexión a la base de datos Oracle
            ds.setUrl("jdbc:oracle:thin:@localhost:1521:ex"); // Cambia según tu configuración
            ds.setUsername("system");
            ds.setPassword("admin");
            ds.setDriverClassName("oracle.jdbc.OracleDriver");

         
            ds.setMinIdle(5); 
            ds.setMaxIdle(10); 
            ds.setMaxOpenPreparedStatements(10);
            ds.setInitialSize(5); 
            ds.setMaxActive(50);

           
            ds.setTestOnBorrow(true); 
            ds.setValidationQuery("SELECT 1 FROM dual"); 
            ds.setValidationQueryTimeout(5); 
            ds.setTestWhileIdle(true); 
            ds.setMinEvictableIdleTimeMillis(30000);
            ds.setTimeBetweenEvictionRunsMillis(60000); 
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al configurar el pool de conexiones", e);
        }
    }

    // Método para obtener una conexión desde el pool
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    // Constructor privado para evitar la instanciación de esta clase
    private OracleDataSource() {}
}
