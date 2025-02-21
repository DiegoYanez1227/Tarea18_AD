package ficheros;

import java.util.List;

import pojos.Alumno;

public interface FicherosInterface {
	String generarFichero(List<Alumno> alumnos);
	
	List<Alumno> leerFichero(String ruta);
	
	
}
