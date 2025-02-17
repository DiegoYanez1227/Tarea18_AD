package ficheros;

import java.util.List;

import model.Alumno;

public interface FicherosInterface {
	String generarFichero(List<Alumno> alumnos);
	
	List<Alumno> leerFichero(String ruta);
	
	
}
