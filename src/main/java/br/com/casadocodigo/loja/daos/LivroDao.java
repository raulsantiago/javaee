package br.com.casadocodigo.loja.daos;

import java.util.List;

import javax.ejb.Stateful;
import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.hibernate.SessionFactory;
import org.hibernate.annotations.QueryHints;

import br.com.casadocodigo.loja.models.Livro;

@Stateful
public class LivroDao {
	
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager manager;
	
	public void salvar(Livro livro) {		
		manager.persist(livro);
	}
	
	// Metodo pra limpar o cacher  - só conhecimento sem uso
	public void limparCache() {
		Cache cache = manager.getEntityManagerFactory().getCache();
		// Limpa somente o primento Id do Livro
		cache.evict(Livro.class, 1l);
		// Limpa todo o Livro
		cache.evict(Livro.class);
		// Limpa todas as entidades
		cache.evictAll();
		
		SessionFactory factory = manager.getEntityManagerFactory().unwrap(SessionFactory.class);
		// Limpa todas as regiões
		factory.getCache().evictAllRegions();
		// Limpa as regiões mapeadas como HOME
		factory.getCache().evictQueryRegion("home");
	}

	 public List<Livro> listar() {
	        String jpql = "select distinct(l) from Livro l"
	            + " join fetch l.autores";        

	        return manager.createQuery(jpql, Livro.class).getResultList();
	    }

	
	// Fazendo um Hint - Manter dados do banco em memoria para acesso velox
	public List<Livro> ultimosLancamentos() {
		String jpql = "select l from Livro l order by l.dataPublicacao desc";
		return manager.createQuery(jpql, Livro.class).setMaxResults(5)
				.setHint(QueryHints.CACHEABLE, true)
				.setHint(QueryHints.CACHE_REGION, "home")
				.getResultList();
	}

	public List<Livro> demaisLivros() {
		String jpql = "select l from Livro l order by l.dataPublicacao desc";
		return manager.createQuery(jpql, Livro.class).setFirstResult(5)
				.setHint(QueryHints.CACHEABLE, true)
				.setHint(QueryHints.CACHE_REGION, "home")
				.getResultList();
	}

	// Query para fazer a busca pelos relacionamento
	public Livro buscarPorId(Integer id) {
		// Resolvendo problema de Session via @PersistenceContext(type = PersistenceContextType.EXTENDED) no manager Só funciona com EBJ @Stateful ( Fez 2 select consume mais query )
		return manager.find(Livro.class, id);
		
		
		// Resolvendo problema de Session via SQL ( Fez 1 select consume menos query ) 
//		String jqpl = "select l from Livro l join fetch l.autores "
//	            + "where l.id = :id"; 
//		return manager.createQuery(jqpl, Livro.class).setParameter("id", id).getSingleResult();
	}

}

