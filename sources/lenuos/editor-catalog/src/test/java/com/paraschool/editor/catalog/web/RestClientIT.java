package com.paraschool.editor.catalog.web;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.web.client.RestTemplate;

/**
 * Define REST client.
 * @author blamouret
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
	/*"classpath:com/paraschool/editor/catalog/*.xml",*/
	"classpath:com/paraschool/editor/catalog/web/*.xml"	
	})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public abstract class RestClientIT<R> {

	/**
	 * Default http headers with application/xml value.
	 */
	private static final HttpHeaders requestHeaders = new HttpHeaders() {
			{this.set("Accept", "application/xml");}
	};
	
	@Autowired
	RestTemplate restTemplate;

	/**
	 * Return current class name to be RESTed.
	 * @return
	 */
	protected abstract Class<R> getRClass();
	
	/**
	 * Return REST url.
	 * @return
	 */
	protected abstract String getUrl();
	
	/**
	 * Return an new request object
	 * @return
	 */
	private HttpEntity<R> getRequest() {
		return new HttpEntity<R>(requestHeaders);
	}
	
	/**
	 * Return an new request object with specific element.
	 * @param restElement
	 * @return
	 */
	private HttpEntity<R> getRequest(R restElement) {
		return new HttpEntity<R>(restElement, requestHeaders);
	}
	
	/**
	 * Return the response of the REST call.
	 * @param response
	 * @return
	 */
	private R getResponse(ResponseEntity<R> response) {
		assertNotNull("No response", response.getBody());
		return response.getBody();
	}
	
	/**
	 * Execute a REST get call.
	 * @return
	 * @throws Exception
	 */
	protected R doGet() throws Exception{
		try {
			ResponseEntity<R> response = restTemplate.exchange(
					this.getUrl(), 
					HttpMethod.GET, this.getRequest(), this.getRClass());
			return this.getResponse(response);
		} catch (Exception e) {
			assertNull("Exception "  + e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Execute a REST get call with an id
	 * @return
	 * @throws Exception
	 */
	protected R doGet(Integer id) throws Exception{
		try {
			ResponseEntity<R> response = restTemplate.exchange(
					this.getUrl() + id.toString(), 
					HttpMethod.GET, this.getRequest(), this.getRClass());
			return this.getResponse(response);
		} catch (Exception e) {
			assertNull("Exception "  + e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Execute a REST get call with parameters
	 * @return
	 * @throws Exception
	 */
	protected R doGet(Map<String,String> parameters) throws Exception{
		try {
			StringBuilder url = new StringBuilder(this.getUrl());
			if (parameters!= null && !parameters.isEmpty()) {
				boolean first = true;
				for (String key : parameters.keySet()) {
					url.append(first?"?":"&");
					first = false;
					url.append(key);
					url.append("=");
					url.append(parameters.get(key));
				}
			}
			ResponseEntity<R> response = restTemplate.exchange(
					url.toString(), 
					HttpMethod.GET, this.getRequest(), this.getRClass());
			return this.getResponse(response);
		} catch (Exception e) {
			assertNull("Exception "  + e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Execute a REST put call.
	 * @return
	 * @throws Exception
	 */
	protected R doPut(R restElement) throws Exception{
		try {
			ResponseEntity<R> response = restTemplate.exchange(
					this.getUrl(), 
					HttpMethod.PUT, this.getRequest(restElement), this.getRClass());
			return this.getResponse(response);
		} catch (Exception e) {
			assertNull("Exception "  + e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Execute a REST post call.
	 * @return
	 * @throws Exception
	 */
	protected R doPost(R restElement) throws Exception{
		try {
			ResponseEntity<R> response = restTemplate.exchange(
					this.getUrl(), 
					HttpMethod.POST, this.getRequest(restElement), this.getRClass());
			return this.getResponse(response);
		} catch (Exception e) {
			assertNull("Exception "  + e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Execute a REST delete call.
	 * @return
	 * @throws Exception
	 */
	protected R doDelete(Integer id) throws Exception{
		try {
			ResponseEntity<R> response = restTemplate.exchange(
					this.getUrl() + id.toString(), 
					HttpMethod.DELETE, this.getRequest(), this.getRClass());
			return this.getResponse(response);
		} catch (Exception e) {
			assertNull("Exception "  + e.getMessage(), e);
			throw e;
		}
	}
}
