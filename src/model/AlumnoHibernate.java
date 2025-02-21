package model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import pojos.Alumno;
import pojos.Grupo;
import pool.HibernateUtil;


public class AlumnoHibernate implements AlumnoDAO{



	private SessionFactory sessionFactory;

	public AlumnoHibernate() {
        this.sessionFactory = HibernateUtil.getSessionFactory(); 

	}
	
	@Override
	public int aniadirAlumno(Alumno alumno) {
		Session session = sessionFactory.openSession();
		try {
			Transaction tx = session.beginTransaction();
			session.persist(alumno);
			tx.commit();
			return 1;
		} finally {
			session.close();
		}
	}

	@Override
	public int aniadirAlumnos(List<Alumno> alumnos) {
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			for (Alumno alumno : alumnos) {
				session.persist(alumno);
			}
			tx.commit();
			return alumnos.size();
		}
	}

	@Override
	public int aniadirGrupo(Grupo grupo) {
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			session.persist(grupo);
			tx.commit();
			return 1;
		}
	}

	@Override
	public int aniadirGrupos(List<Grupo> grupos) {
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			for (Grupo grupo : grupos) {
				session.persist(grupo);
			}
			tx.commit();
			return grupos.size();
		}
	}

	@Override
	public List<Alumno> obtenerTodosLosAlumnos() {
		Session session = sessionFactory.openSession();
		List<Alumno> a = new ArrayList<>();
		try {
			a = session.createQuery("FROM Alumno", Alumno.class).list();
			return a;
		} finally {
			session.close();
		}
	}

	@Override
	public List<Grupo> obtenerTodosLosGrupos() {
		try (Session session = sessionFactory.openSession()) {
			return session.createQuery("FROM Grupo", Grupo.class).list();
		}
	}

	@Override
	public Alumno obtenerAlumnoPorNIA(int nia) {
		try (Session session = sessionFactory.openSession()) {
			return session.get(Alumno.class, nia);
		}
	}

	@Override
	public List<Alumno> obtenerAlumnosPorGrupo(Grupo grupo) {
		try (Session session = sessionFactory.openSession()) {
			return session.createQuery("FROM Alumno WHERE grupo = :grupo", Alumno.class).setParameter("grupo", grupo)
					.list();
		}
	}

	@Override
	public int modificarNombrePorNia(int nia, String nombre) {
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			Alumno alumno = session.get(Alumno.class, nia);
			if (alumno != null) {
				alumno.setNombre(nombre);
				session.merge(alumno);
				tx.commit();
				return 1;
			}
			return 0;
		}
	}

	@Override
	public int modificarGrupoDeAlumno(int nia, Grupo grupo) {
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			Alumno alumno = session.get(Alumno.class, nia);
			if (alumno != null) {
				alumno.setGrupo(grupo);
				session.merge(alumno);
				tx.commit();
				return 1;
			}
			return 0;
		}
	}

	@Override
	public void eliminarPorNia(int nia) {
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			Alumno alumno = session.get(Alumno.class, nia);
			if (alumno != null) {
				session.remove(alumno);
			}
			tx.commit();
		}
	}

	@Override
	public void eliminarPorCurso(String curso) {
	    try (Session session = sessionFactory.openSession()) {
	        Transaction tx = session.beginTransaction();
	        session.createMutationQuery("DELETE FROM Alumno WHERE curso = :curso").setParameter("curso", curso).executeUpdate();
	        tx.commit();
	    }
	}


	@Override
	public List<String> obtenerCursos() {
		try (Session session = sessionFactory.openSession()) {
			return session.createQuery("SELECT DISTINCT curso FROM Alumno", String.class).list();
		}
	}
	
	public Grupo obtenerGrupo(int id_grupo) {
	    try (Session session = sessionFactory.openSession()) {
	        return session.createSelectionQuery("FROM Grupo WHERE id = :id_grupo", Grupo.class)
	                      .setParameter("id_grupo", id_grupo)
	                      .uniqueResult();
	    }
	}

}
