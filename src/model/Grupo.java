package model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

@Embeddable
@Entity
@Table(name = "grupos")
public class Grupo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grupo")
    private Integer id_grupo;
    
    @Column(nullable = false, length = 50)
    private String nombre;
    
    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Alumno> alumnos;
    
    public Grupo() {}

	public Grupo(String nombre) {
		this.nombre = nombre;
	}
	
	public Grupo(int id_grupo, String nombre) {
		this.id_grupo = id_grupo;
		this.nombre = nombre;
	}
	
	

	public Integer getId_grupo() {
		return id_grupo;
	}

	public void setId_grupo(Integer id_grupo) {
		this.id_grupo = id_grupo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public List<Alumno> getAlumnos() {  // Agregar este método
        return alumnos;
    }

    public void setAlumnos(List<Alumno> alumnos) {  // Agregar este método
        this.alumnos = alumnos;
    }

	@Override
	public int hashCode() {
		return Objects.hash(id_grupo, nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Grupo other = (Grupo) obj;
		return id_grupo == other.id_grupo && Objects.equals(nombre, other.nombre);
	}

	@Override
	public String toString() {
		return "Grupo [id_grupo=" + id_grupo + ", nombre=" + nombre + "]";
	}
}
