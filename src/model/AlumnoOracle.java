package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



import org.apache.logging.log4j.LogManager;

import pool.MyDataSource;
import pool.OracleDataSource;

public class AlumnoOracle implements AlumnoDAO{

	static org.apache.logging.log4j.Logger Logger = LogManager.getLogger(AlumnoMySQL.class);
	
	 @Override
	    public int aniadirAlumno(Alumno alumno) {
	        String sql = "INSERT INTO alumno (nombre, apellidos, fecha_nacimiento, genero, ciclo, curso, grupo) " +
	                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
	        
	        int result = 0;

	        try (Connection conexion = OracleDataSource.getConnection();
	             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

	            sentencia.setString(1, alumno.getNombre());
	            sentencia.setString(2, alumno.getApellidos());
	            sentencia.setDate(3, Date.valueOf(alumno.getFechaNacimiento())); 
	            sentencia.setString(4, String.valueOf(alumno.getGenero()));
	            sentencia.setString(5, alumno.getCiclo());
	            sentencia.setString(6, alumno.getCurso());
	            sentencia.setInt(7, alumno.getGrupo().getId_grupo()); 
	            
	            result = sentencia.executeUpdate();
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	            Logger.error("Error al introducir el alumno dentro de la base de datos");
	        }
	        return result;
	    }

	@Override
	public int aniadirAlumnos(List<Alumno> alumnos) {
	    String sql = "INSERT INTO alumno (nombre, apellidos, fecha_nacimiento, genero, ciclo, curso, grupo) " +
	                 "VALUES (?, ?, ?, ?, ?, ?, ?)";
	    
	    int result = 0;
	    
	    try (Connection conexion = OracleDataSource.getConnection();
	         PreparedStatement sentencia = conexion.prepareStatement(sql)) {

	        // Desactivamos el autocommit para mejorar el rendimiento
	        conexion.setAutoCommit(false);
	        
	        for (Alumno alumno : alumnos) {
	            sentencia.setString(1, alumno.getNombre());
	            sentencia.setString(2, alumno.getApellidos());
	            sentencia.setDate(3, Date.valueOf(alumno.getFechaNacimiento()));
	            sentencia.setString(4, String.valueOf(alumno.getGenero()));
	            sentencia.setString(5, alumno.getCiclo());
	            sentencia.setString(6, alumno.getCurso());
	            sentencia.setInt(7, alumno.getGrupo().getId_grupo());
	            
	            // Añadimos la inserción al batch
	            sentencia.addBatch();
	        }
	        
	        // Ejecutamos el batch
	        int[] resultados = sentencia.executeBatch();
	        
	        // Confirmamos la transacción
	        conexion.commit();
	        
	        // Contamos el número de filas afectadas
	        result = resultados.length;
	        
	        Logger.info("Se ha completado con éxito el añadido de todos los alumnos");
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	        Logger.error("Error al añadir los alumnos a la base de datos");

	    }
	    
	    return result;
	}

	@Override
	public int aniadirGrupos(List<Grupo> grupos) {
	    String sql = "INSERT INTO grupo (nombre) VALUES (?)";
	    
	    int result = 0;
	    
	    try (Connection conexion = OracleDataSource.getConnection();
	         PreparedStatement sentencia = conexion.prepareStatement(sql)) {

	        // Desactivamos el autocommit para mejorar el rendimiento
	        conexion.setAutoCommit(false);

	        for (Grupo grupo : grupos) {
	            sentencia.setString(1, grupo.getNombre());
	            
	            // Añadimos la inserción al batch
	            sentencia.addBatch();
	        }
	        
	        // Ejecutamos el batch
	        int[] resultados = sentencia.executeBatch();
	        
	        // Confirmamos la transacción
	        conexion.commit();
	        
	        // Contamos el número de filas afectadas
	        result = resultados.length;
	        
	        Logger.info("Se ha completado con éxito el añadido de todos los grupos");
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	        Logger.error("Error al añadir los grupos a la base de datos");
	    }
	    
	    return result;
	}

	@Override
	public int aniadirGrupo(Grupo grupo) {
	    String sql = "INSERT INTO grupo (nombre) VALUES (?)";
	    
	    int result = 0;

	    try (Connection conexion = OracleDataSource.getConnection();
	         PreparedStatement sentencia = conexion.prepareStatement(sql)) {

	        sentencia.setString(1, grupo.getNombre());

	        result = sentencia.executeUpdate();

	    } catch (SQLException e) {
	        e.printStackTrace();
	        Logger.error("Error al introducir el grupo dentro de la base de datos");
	    }
	    
	    return result;
	}

	@Override
	public List<Alumno> obtenerTodosLosAlumnos() {
	    String sql = "SELECT nia, nombre, apellidos, fecha_nacimiento, genero, ciclo, curso, grupo FROM alumno";

	    List<Alumno> alumnos = new ArrayList<>();

	    try (Connection conexion = OracleDataSource.getConnection();
	         PreparedStatement sentencia = conexion.prepareStatement(sql);
	         ResultSet rs = sentencia.executeQuery()) {

	        Alumno alumno;

	        while (rs.next()) {
	            alumno = new Alumno();
	            generarAlumno(rs, alumno);  // Asegúrate de que este método esté correcto para Oracle

	            alumnos.add(alumno);
	        }

	    } catch (SQLException e) {
	        Logger.error("Error al obtener todos los alumnos desde la base de datos");
	        e.printStackTrace();
	        return null;
	    }

	    return alumnos;
	}

	@Override
	public List<Grupo> obtenerTodosLosGrupos() {
	    String sql = "SELECT id_grupo, nombre FROM grupos";

	    List<Grupo> grupos = new ArrayList<>(); // Inicializamos la lista antes de usarla

	    try (Connection conexion = OracleDataSource.getConnection();
	         PreparedStatement sentencia = conexion.prepareStatement(sql);
	         ResultSet rs = sentencia.executeQuery()) {

	        while (rs.next()) {
	            Grupo grupo = new Grupo();
	            generarGrupo(rs, grupo);  // Llamada para mapear los resultados al objeto Grupo
	            grupos.add(grupo);  // Añadimos el grupo a la lista
	        }

	    } catch (SQLException e) {
	        Logger.error("Error al obtener todos los grupos");
	        e.printStackTrace();
	        return null;
	    }

	    return grupos;
	}


	@Override
	public Alumno obtenerAlumnoPorNIA(int nia) {
	    String sql = "SELECT nia, nombre, apellidos, fecha_nacimiento, genero, ciclo, curso, grupo FROM alumno WHERE nia= ?";

	    Alumno alumno = null;

	    try (Connection conexion = OracleDataSource.getConnection();
	         PreparedStatement sentencia = conexion.prepareStatement(sql)) {

	        sentencia.setInt(1, nia);
	        
	        try (ResultSet rs = sentencia.executeQuery()) {
	            if (rs.next()) {
	                alumno = new Alumno();
	                generarAlumno(rs, alumno);
	            }
	        }
	    } catch (SQLException e) {
	        Logger.error("Error al obtener el alumno con nia " + nia);
	        e.printStackTrace();
	    }

	    return alumno;
	}

	@Override
	public List<Alumno> obtenerAlumnosPorGrupo(Grupo grupo) {
	    String sql = "SELECT nia, nombre, apellidos, fecha_nacimiento, genero, ciclo, curso, grupo FROM alumno WHERE grupo= ?";

	    List<Alumno> alumnos = new ArrayList<>(); // Inicializamos la lista antes de usarla

	    try (Connection conexion = OracleDataSource.getConnection();
	         PreparedStatement sentencia = conexion.prepareStatement(sql)) {

	        sentencia.setInt(1, grupo.getId_grupo());
	        
	        try (ResultSet rs = sentencia.executeQuery()) {
	            while (rs.next()) {
	                Alumno alumno = new Alumno();
	                generarAlumno(rs, alumno);
	                alumnos.add(alumno);  // Añadimos el alumno a la lista
	            }
	        }

	    } catch (SQLException e) {
	        Logger.error("Error al obtener los alumnos de un grupo");
	        e.printStackTrace();
	        return null;
	    }

	    return alumnos;
	}


	@Override
	public int modificarNombrePorNia(int nia, String nombre) {
	    // Primero obtenemos el alumno (esto puede no ser necesario en Oracle si no necesitas los datos)
	    Alumno alumno = obtenerAlumnoPorNIA(nia);
	    
	    // Actualizar los datos en la base de datos
	    String sql = "UPDATE alumno SET nombre = ? WHERE nia = ?";

	    int filasActualizadas = 0;
	    try (Connection conexion = OracleDataSource.getConnection();
	         PreparedStatement sentencia = conexion.prepareStatement(sql)) {

	        sentencia.setString(1, nombre);
	        sentencia.setInt(2, nia);

	        // Ejecutamos la actualización y obtenemos el número de filas afectadas
	        filasActualizadas = sentencia.executeUpdate();

	    } catch (SQLException e) {
	        Logger.error("Error en la modificación del nombre del alumno con nia " + nia);
	        e.printStackTrace();
	    }

	    return filasActualizadas;
	}


	@Override
	public int modificarGrupoDeAlumno(int nia, Grupo grupo) {
	    // Obtener el alumno, aunque en este caso parece que no es necesario para la actualización
	    Alumno alumno = obtenerAlumnoPorNIA(nia);

	    // Actualizar el grupo del alumno en la base de datos
	    String sql = "UPDATE alumno SET grupo = ? WHERE nia = ?";

	    int filasActualizadas = 0;
	    try (Connection conexion = OracleDataSource.getConnection();
	         PreparedStatement sentencia = conexion.prepareStatement(sql)) {

	        sentencia.setInt(1, grupo.getId_grupo());
	        sentencia.setInt(2, nia);

	        // Ejecutar la actualización
	        filasActualizadas = sentencia.executeUpdate();

	    } catch (SQLException e) {
	        Logger.error("Error en la modificación del grupo del alumno con nia " + nia);
	        e.printStackTrace();
	    }

	    return filasActualizadas;
	}


	@Override
	public void eliminarPorNia(int nia) {
	    String sql = "DELETE FROM alumno WHERE nia = ?";

	    try (Connection conexion = OracleDataSource.getConnection();
	         PreparedStatement sentencia = conexion.prepareStatement(sql)) {

	        sentencia.setInt(1, nia);

	        // Ejecutar el DELETE
	        sentencia.executeUpdate();

	    } catch (SQLException e) {
	        e.printStackTrace();
	        Logger.error("Error al eliminar el alumno con nia " + nia);
	    }
	}


	@Override
	public void eliminarPorCurso(String curso) {
	    // Llamamos al método obtenerCursos si realmente se necesita, pero no se está utilizando aquí.
	    obtenerCursos();

	    String sql = "DELETE FROM alumno WHERE curso = ?";

	    try (Connection conexion = OracleDataSource.getConnection();
	         PreparedStatement sentencia = conexion.prepareStatement(sql)) {

	        sentencia.setString(1, curso);

	        // Ejecutamos la eliminación de los alumnos que pertenecen al curso especificado
	        sentencia.executeUpdate();

	    } catch (SQLException e) {
	        Logger.error("Error al eliminar los alumnos que pertenecen al curso: " + curso);
	        e.printStackTrace();
	    }
	}


	@Override
	public List<String> obtenerCursos() {
	    String sql = "SELECT DISTINCT curso FROM alumno";
	    List<String> cursos = new ArrayList<>();

	    try (Connection conexion = OracleDataSource.getConnection();
	         PreparedStatement sentencia = conexion.prepareStatement(sql);
	         ResultSet rs = sentencia.executeQuery()) {

	        while (rs.next()) {
	            String curso = rs.getString("curso");
	            cursos.add(curso); 
	        }

	    } catch (SQLException e) {
	        Logger.error("Error al obtener los cursos");
	        e.printStackTrace();
	        return null;
	    }

	    return cursos;
	}


	@Override
	public void cerrarSession() {
		// TODO Auto-generated method stub
		
	}
	
	private void generarAlumno(ResultSet rs, Alumno alumno) throws SQLException {
		alumno.setNia(rs.getInt("nia"));
		alumno.setNombre(rs.getString("nombre"));
		alumno.setApellidos(rs.getString("apellidos"));
		alumno.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
		String genero = rs.getString("genero");
		alumno.setGenero(genero.charAt(0));
		alumno.setCiclo(rs.getString("ciclo"));
		alumno.setCurso(rs.getString("curso"));
		int idGrupo = rs.getInt("id_grupo");
		Grupo grupo = new Grupo();
	    grupo.setId_grupo(idGrupo);
	    alumno.setGrupo(grupo);
	}
	
	private void generarGrupo(ResultSet rs, Grupo grupo) throws SQLException {
		grupo.setId_grupo(rs.getInt("nia"));
		grupo.setNombre(rs.getString("nombre"));
		
	}
	
}
