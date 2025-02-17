package ficheros;

import java.util.ArrayList;
import java.util.List;

import java.io.File;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.time.LocalDate;

import model.Alumno;
import model.Grupo;

public class FicheroXML implements FicherosInterface{
	
	private static final String RUTA_XML = "grupos.xml";

	public String generarFichero(List<Alumno> alumnos) {
        guardarComoXML(alumnos);
        return RUTA_XML;
    }

	
	public List<Alumno> leerFichero(String ruta) {
        return leerFicheroXML(ruta);
    }
	/**
     * Método para leer un archivo XML y convertirlo en una lista de grupos.
     * @param Ruta del archivo XML
     * @return Lista de grupos leída del XML
     */

	private List<Alumno> leerFicheroXML(String ruta) {
	    List<Alumno> grupos = new ArrayList<>();
	    try {
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(new File(ruta));
	        doc.getDocumentElement().normalize();

	        NodeList listaGrupos = doc.getElementsByTagName("Grupo");
	        for (int i = 0; i < listaGrupos.getLength(); i++) {
	            Node nodo = listaGrupos.item(i);
	            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
	                Element elementoGrupo = (Element) nodo;
	                int idGrupo = Integer.parseInt(elementoGrupo.getAttribute("id"));
	                String nombreGrupo = elementoGrupo.getAttribute("nombre");
	                Grupo grupo = new Grupo();
	                grupo.setId_grupo(idGrupo);
	                grupo.setNombre(nombreGrupo);

	                NodeList listaAlumnos = elementoGrupo.getElementsByTagName("Alumno");
	                for (int j = 0; j < listaAlumnos.getLength(); j++) {
	                    Element elementoAlumno = (Element) listaAlumnos.item(j);
	                    Alumno alumno = new Alumno(
	                        Integer.parseInt(elementoAlumno.getAttribute("nia")),
	                        elementoAlumno.getAttribute("nombre"),
	                        elementoAlumno.getAttribute("apellidos"),
	                        LocalDate.parse(elementoAlumno.getAttribute("fechaNacimiento")),
	                        elementoAlumno.getAttribute("genero").charAt(0),
	                        elementoAlumno.getAttribute("ciclo"),
	                        elementoAlumno.getAttribute("curso"),
	                        grupo // Se asigna el grupo correctamente
	                    );
	                    grupo.getAlumnos().add(alumno); // Agregamos el alumno al grupo
	                }
	               // grupos.add(alumno);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	    return grupos;
	}

	/**
     * Método para guardar una lista de grupos en un archivo XML .
     * @param Lista de grupos leída del XML
     * @return Ruta del archivo XML
     */
	private void guardarComoXML(List<Alumno> alumnos) {
	    try {
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document doc = builder.newDocument();

	        Element rootElement = doc.createElement("Grupos");
	        doc.appendChild(rootElement);

	        for (Alumno alumno : alumnos) {
	            Element grupoElement = doc.createElement("Grupo");
	            rootElement.appendChild(grupoElement);

//	            grupoElement.setAttribute("id", String.valueOf(alumno.getId_grupo()));
//	            grupoElement.setAttribute("nombre", alumno.getNombre());
//
//	            for (Alumno alumno : alumno.getAlumnos()) {
//	                Element alumnoElement = doc.createElement("Alumno");
//	                grupoElement.appendChild(alumnoElement);
//
//	                alumnoElement.setAttribute("nia", String.valueOf(alumno.getNia()));
//	                alumnoElement.setAttribute("nombre", alumno.getNombre());
//	                alumnoElement.setAttribute("apellidos", alumno.getApellidos());
//	                alumnoElement.setAttribute("fechaNacimiento", alumno.getFechaNacimiento().toString());
//	                alumnoElement.setAttribute("genero", String.valueOf(alumno.getGenero()));
//	                alumnoElement.setAttribute("ciclo", alumno.getCiclo());
//	                alumnoElement.setAttribute("curso", alumno.getCurso());
//	                alumnoElement.setAttribute("grupo", String.valueOf(alumno.getGrupo()));
//	            }
	        }

	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

	        DOMSource source = new DOMSource(doc);
	        StreamResult result = new StreamResult(new File(RUTA_XML));
	        transformer.transform(source, result);

	    } catch (ParserConfigurationException | TransformerException e) {
	        e.printStackTrace();
	    }
	}
}
