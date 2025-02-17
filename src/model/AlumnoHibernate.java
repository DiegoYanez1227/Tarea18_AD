package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.hibernate.query.SelectionQuery;

import pool.HibernateUtil;

public class AlumnoHibernate implements AlumnoDAO{

	StandardServiceRegistry sr= new StandardServiceRegistryBuilder().configure().build();

	SessionFactory sf= new MetadataSources(sr).buildMetadata().buildSessionFactory();

	Session session= sf.openSession();

	@Override
	public int aniadirAlumno(Alumno alumno) {
		if(alumno!=null) {
			Transaction transaction = session.beginTransaction();  
			try {
				session.persist(alumno);
				transaction.commit(); 
				return 1;
			} catch (Exception e) {
				transaction.rollback();  
				e.printStackTrace();
			}
		}
		return 0;
	}

	@Override
	public int aniadirAlumnos(List<Alumno> alumnos) {
		int contador=0;
		if(alumnos!=null) {
			Transaction transaction = session.beginTransaction();  
			try {
				for (Alumno alumno : alumnos) {
					session.persist(alumno);	
					contador++;
				}
				transaction.commit();
			} catch (Exception e) {
				transaction.rollback();  
				e.printStackTrace();
			}
			if (contador % 50 == 0) {  
				session.flush();  
				session.clear();
			}
		}
		return contador;
	}

	@Override
	public int aniadirGrupo(Grupo grupo) {
		if(grupo!=null) {
			Transaction transaction = session.beginTransaction();  
			try {
				session.persist(grupo);
				transaction.commit(); 
				return 1;
			} catch (Exception e) {
				transaction.rollback();  
				e.printStackTrace();
			}
		}
		return 0;
	}

	@Override
	public int aniadirGrupos(List<Grupo> grupos) {
		int contador=0;
		if(grupos!=null) {
			Transaction transaction = session.beginTransaction();  
			try {
				for (Grupo grupo : grupos) {
					session.persist(grupo);	
					contador++;
				}
				transaction.commit();
			} catch (Exception e) {
				transaction.rollback();  
				e.printStackTrace();
			}
			if (contador % 50 == 0) {  
				session.flush();  
				session.clear();
			}
		}
		return contador;
	}

	@Override
	public List<Alumno> obtenerTodosLosAlumnos() {
		List<Alumno> listaAlumnos=null;

		try {
			listaAlumnos= new ArrayList<>();
			listaAlumnos=session.createQuery("FROM alumno",Alumno.class).list();
		}catch(Exception e) {
			e.printStackTrace();
			//TODO Logger
			return null;
		}
		return listaAlumnos;
	}

	@Override
	public List<Grupo> obtenerTodosLosGrupos() {
		List<Grupo> listaGrupos=null;

		try {
			listaGrupos= new ArrayList<Grupo>();
			listaGrupos=session.createQuery("FROM grupo",Grupo.class).list();
		}catch(Exception e) {
			e.printStackTrace();
			//TODO Logger
			return null;
		}
		return listaGrupos;
	}

	@Override
	public Alumno obtenerAlumnoPorNIA(int nia) {
		Alumno alumno;
		try {
			alumno = (Alumno)session.getReference(Alumno.class, (int)nia);
			return alumno;
		}catch(ObjectNotFoundException e) {
			e.printStackTrace();
			return null;
		}

	}

	 public List<Alumno> obtenerAlumnosPorGrupo(Grupo grupo) {
	        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	            SelectionQuery<Alumno> query = session.createSelectionQuery("FROM Alumno WHERE grupo = :grupo", Alumno.class);
	            query.setParameter("grupo", grupo);
	            return query.getResultList();
	        }
	    }

	    public int modificarNombrePorNia(int nia, String nombre) {
	        Transaction transaction = null;
	        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	            transaction = session.beginTransaction();
	            MutationQuery query = session.createMutationQuery("UPDATE Alumno SET nombre = :nombre WHERE nia = :nia");
	            query.setParameter("nombre", nombre);
	            query.setParameter("nia", nia);
	            int result = query.executeUpdate();
	            transaction.commit();
	            return result;
	        } catch (Exception e) {
	            if (transaction != null) transaction.rollback();
	            e.printStackTrace();
	            return 0;
	        }
	    }

	    public int modificarGrupoDeAlumno(int nia, Grupo grupo) {
	        Transaction transaction = null;
	        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	            transaction = session.beginTransaction();
	            MutationQuery query = session.createMutationQuery("UPDATE Alumno SET grupo = :grupo WHERE nia = :nia");
	            query.setParameter("grupo", grupo);
	            query.setParameter("nia", nia);
	            int result = query.executeUpdate();
	            transaction.commit();
	            return result;
	        } catch (Exception e) {
	            if (transaction != null) transaction.rollback();
	            e.printStackTrace();
	            return 0;
	        }
	    }

	    public void eliminarPorNia(int nia) {
	        Transaction transaction = null;
	        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	            transaction = session.beginTransaction();
	            Alumno alumno = session.get(Alumno.class, nia);
	            if (alumno != null) {
	                session.remove(alumno);
	            }
	            transaction.commit();
	        } catch (Exception e) {
	            if (transaction != null) transaction.rollback();
	            e.printStackTrace();
	        }
	    }

	    public void eliminarPorCurso(String curso) {
	        Transaction transaction = null;
	        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	            transaction = session.beginTransaction();
	            MutationQuery query = session.createMutationQuery("DELETE FROM Alumno WHERE curso = :curso");
	            query.setParameter("curso", curso);
	            query.executeUpdate();
	            transaction.commit();
	        } catch (Exception e) {
	            if (transaction != null) transaction.rollback();
	            e.printStackTrace();
	        }
	    }

	    public List<String> obtenerCursos() {
	        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	            SelectionQuery<String> query = session.createSelectionQuery("SELECT DISTINCT curso FROM Alumno", String.class);
	            return query.getResultList();
	        }
	    }
	
	@Override
	public void cerrarSession() {
		session.close();
		sf.close();
	}

}
