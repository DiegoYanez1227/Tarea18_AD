package ejecutadores;

import controlador.Controlador;
import model.AlumnoDAO;
import model.AlumnoHibernate;
import vista.IVista;
import vista.VistaConsola;

public class EjecutadorHibernate {
	public static void main(String[] args) {
		AlumnoDAO modelo = new AlumnoHibernate();
		IVista vista = new VistaConsola();
		new Controlador().ejecutar(modelo, vista);

	}
}
