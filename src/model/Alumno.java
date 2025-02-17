package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "alumnos")
public class Alumno {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer nia;
    
    @Column(nullable = false, length = 50)
    private String nombre;
    
    @Column(nullable = false, length = 50)
    private String apellidos;
    
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;
    
    @Column(nullable = false, length = 1)
    private char genero;
    
    @Column(nullable = false, length = 50)
    private String ciclo;
    
    @Column(nullable = false, length = 10)
    private String curso;
    
    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "id_grupo", nullable = false)
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
