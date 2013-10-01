package com.paraschool.editor.catalog.models;

import java.util.Set;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

/**
 * Define bridge for composed primary key of a filter value.
 * @author blamouret
 *
 */
public class FilterValueBridge implements FieldBridge  {

	public void set(String name, Object value, Document document, 
            LuceneOptions luceneOptions) {
		
		@SuppressWarnings("unchecked")
		Set<FilterValue> filterValues = (Set<FilterValue>) value;
		
		for (FilterValue one : filterValues) {
			Object val = one.getValue();
			if (val != null) {
		        luceneOptions.addFieldToDocument(
		                name + one.getFilter().getId(),
		                val.toString(),
		                document );
			} else {
		        luceneOptions.addFieldToDocument(
		                name + one.getFilter().getId(),
		                null,
		                document );
			}
		}
		
    }
	
}
