package com.paraschool.editor.catalog.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.util.Version;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.paraschool.editor.catalog.dao.ResourceDao;
import com.paraschool.editor.catalog.dao.jpa.util.EntityIdJpaDao;
import com.paraschool.editor.catalog.models.Filter;
import com.paraschool.editor.catalog.models.Resource;

/**
 * Define DAO for JPA Resource entity.
 * 
 * @author blamouret
 * 
 */
@Repository("resourceDao")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ResourceJpaDao extends EntityIdJpaDao<Resource> implements
		ResourceDao {

	private static final Log logger = LogFactory.getLog(ResourceJpaDao.class);

	private static final String COUNT_BY_FILTER = "SELECT count(o) FROM "
		+ Resource.class.getName() + " o inner join o.filterValues v "
		+ "where v.pk.filter.id = :id";

	private static final String SELECT = "SELECT DISTINCT o FROM "
			+ Resource.class.getName() + " o "
			+ "left join fetch o.filterValues where o.id = :id";

	/**
	 * Return the class associated with the dao.
	 * 
	 * @return the class associated with the dao.
	 */
	@Override
	protected Class<Resource> getEntityClass() {
		return Resource.class;
	}

	/**
	 * Return a resource according to a primary key. The returned object
	 * contains filterValue and technical data values.
	 * 
	 * @param pk
	 *            the primary key to find object.
	 * @return the object associated with the primary key, with filter's values
	 *         and technical datas.
	 */
	@Override
	public Resource find(Integer id) {
		List<Resource> list = em.createQuery(SELECT, Resource.class)
				.setParameter("id", id).getResultList();
		if (list.isEmpty()) {
			return null;
		} else {
			Resource r = list.get(0);
			// Load technical data
			r.getTechnicalDatas().size();
			return r;
		}
	}

	/**
	 * Return a resource according to a searched text. The returned object
	 * contains filterValue and technical data values.
	 * 
	 * @param pk
	 *            the primary key to find object.
	 * @return the object associated with the primary key, with filter's values
	 *         and technical datas.
	 */
	@SuppressWarnings("unchecked")
	public List<Resource> search(String txt, List<Filter> filters) {
		List<Resource> list;

		FullTextEntityManager fullTextEm = super.getFullTextEntityManager();
		SearchFactory sf = fullTextEm.getSearchFactory();

		String[] fields = new String[filters.size()]; 
		int i = 0; 
		for (Filter filter : filters) { 
			fields[i++] = "filterValues" + filter.getId().toString(); 
		}

	    QueryParser parser = new MultiFieldQueryParser(
	    		Version.LUCENE_30, fields, sf.getAnalyzer(Resource.class));
		
		org.apache.lucene.search.Query luceneQuery = null;
		try {
			luceneQuery = parser.parse(txt);
		} catch (ParseException e) {
			logger.warn(e.getMessage(),e);
			return new ArrayList<Resource>();
		}

		FullTextQuery ftq = fullTextEm.createFullTextQuery(luceneQuery, Resource.class);
		Session fts = (Session) em.getDelegate();
		Criteria criteria = fts.createCriteria(Resource.class)
			.setFetchMode("filterValues", FetchMode.JOIN)
			.setFetchMode("technicalDatas", FetchMode.JOIN);
		ftq.setCriteriaQuery(criteria);
		
		list = ftq.getResultList();

		return list;
	}

	/**
	 * Return true if the filter is used on resources.
	 * 
	 * @param entity
	 *            the filter to test.
	 * @return true if the filter is used on resources.
	 */
	public boolean isUsed(Filter entity) {
		Long count = em.createQuery(COUNT_BY_FILTER, Long.class)
				.setParameter("id", entity.getId()).getSingleResult();
		return count != null && count > 0;
	}

}
