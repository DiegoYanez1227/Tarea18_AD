package pojos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "alumnos")
public class Alumno implements Serializable {
    
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nia")
    private Integer nia;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;
    
    @Column(name = "fecha", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "genero", nullable = false, length = 1)
    private char genero;

    @Column(name = "ciclo", nullable = false, length = 50)
    private String ciclo;

    @Column(name = "curso", nullable = false, length = 10)
    private String curso;

    @ManyToOne
    @JoinColumn(name = "grupo", referencedColumnName = "id_grupo", nullable = false)
    private Grupo grupo;

    
    public Alumno() {}

	public Alumno(String nombre, String apellidos, LocalDate fechaNacimiento,char genero, String ciclo, String curso, Grupo grupo) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.fechaNacimiento = fechaNacimiento;
		this.genero=genero;
		this.ciclo = ciclo;
		this.curso = curso;
	}
	
	public Alumno(int nia, String nombre, String apellidos, LocalDate fechaNacimiento,char genero, String ciclo, String curso, Grupo grupo) {
		this(nombre,apellidos,fechaNacimiento,genero, ciclo, curso, grupo);
		this.nia=nia;
	}

	

	public Integer getNia() {
		return nia;
	}


	public void setNia(Integer nia) {
		this.nia = nia;
	}

	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getApellidos() {
		return apellidos;
	}


	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}


	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}


	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public char getGenero() {
		return genero;
	}


	public void setGenero(char genero) {
		this.genero = genero;
	}


	public String getCiclo() {
		return ciclo;
	}


	public void setCiclo(String ciclo) {
		this.ciclo = ciclo;
	}


	public String getCurso() {
		return curso;
	}


	public void setCurso(String curso) {
		this.curso = curso;
	}

	public Grupo getGrupo() {
		return grupo;
	}


	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}



	@Override
	public String toString() {
		return "Alumno [nia=" + nia + ", nombre=" + nombre + ", apellidos=" + apellidos + ", fechaNacimiento="
				+ fechaNacimiento + ", genero=" + genero + ", ciclo=" + ciclo + ", curso=" + curso + ", grupo=" + grupo
				+ "]";
	}

	
}
