package com.paraschool.editor.catalog.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.lang.reflect.ParameterizedType;

import com.paraschool.editor.catalog.web.models.rest.RestElement;

/**
 * Define abstract Catalog REST client
 * @author blamouret
 *
 * @param <R> RestElement
 * @param <E> Entity
 */
public abstract class RestCatalogIT<R extends RestElement<E>, E> extends RestClientIT<R> {

	protected static final String BASE_URL = "http://localhost:8080/catalog";
	
	protected abstract void assertElement(E one, E two);

	/**
	 * Return RestElement class name
	 */
	@SuppressWarnings("unchecked")
	protected Class<R> getRClass() {
		ParameterizedType parameterizedType = (ParameterizedType) getClass()
				.getGenericSuperclass();
		return (Class<R>) parameterizedType.getActualTypeArguments()[0];
	}

	/**
	 * Assert rest elements.
	 * @param rest
	 * @param size
	 * @param error
	 */
	private void assertRest(R rest, Integer size, String error) {
		if (rest == null) {
			return;
		}
		assertNotNull("Empty Rest response", rest.getList());
		if (error == null) {
			assertNull("Error code should be null", rest.getError());
		} else {
			assertEquals("Not same error", error, rest.getError().getCode());
		}
		if (rest.getList() != null) {
			if (size != null) {
				assertEquals("Return not " + size + " elements", size.intValue(), rest.getList().size());
			}
		}
	}

	/**
	 * Create a new entity with REST service.
	 * @param element
	 * @return
	 * @throws Exception
	 */
	protected E create(E element) throws Exception {
		R request = this.getRClass().newInstance();
		request.getList().add(element);
		request = this.doPost(request);

		this.assertRest(request, 1, null);
		if (request != null) {
			if (request.getList() != null && request.getList().size() == 1) {
				E created = request.getList().get(0);
				this.assertElement(element, created);
				return created;
			}
		}
		return null;
	}

	/**
	 * Return an entity from REST service
	 * @param saved
	 * @param id
	 * @return
	 * @throws Exception
	 */
	protected E read(E saved, Integer id) throws Exception {
		if (saved == null || id == null) {
			return null;
		}
		R request = this.doGet(id);

		this.assertRest(request, 1, null);
		if (request != null) {
			if (request.getList() != null && request.getList().size() == 1) {
				E readed = request.getList().get(0);
				this.assertElement(readed, saved);
				return saved;
			}
		}
		return null;
	}

	/**
	 * Return entities from REST service.
	 * @return
	 * @throws Exception
	 */
	protected R list() throws Exception {
		R request = this.doGet();
		this.assertRest(request, null, null);
		return request;
	}

	/**
	 * Return entities from REST service. List should contained existed entity id.
	 * @param existedId
	 * @throws Exception
	 */
	protected void list(Integer existedId) throws Exception {
		R request = this.list();
		if (request != null) {
			if (request.getList() != null) {
				assertNotSame("List all should return more than one elements",
						request.getList().isEmpty(), true);
			}
		}
	}

	/**
	 * Update entity with REST service.
	 * @param element
	 * @return
	 * @throws Exception
	 */
	protected E update(E element) throws Exception {
		R request = this.getRClass().newInstance();
		request.getList().add(element);
		request = this.doPut(request);

		this.assertRest(request, 1, null);
		if (request != null) {
			if (request.getList() != null && request.getList().size() == 1) {
				E created = request.getList().get(0);
				this.assertElement(element, created);
				return created;
			}
		}
		return null;
	}

	/**
	 * Delete entity with REST service.
	 * @param id
	 * @param errorCode
	 * @throws Exception
	 */
	protected void delete(Integer id, String errorCode) throws Exception {
		R request = this.doDelete(id);

		this.assertRest(request, 0, null);
		request = this.doGet(id);
		this.assertRest(request, 0, errorCode);
	}

}
