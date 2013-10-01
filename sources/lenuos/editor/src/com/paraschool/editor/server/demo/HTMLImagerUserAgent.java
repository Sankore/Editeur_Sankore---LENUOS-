package com.paraschool.editor.server.demo;
import java.net.URI;
import java.net.URISyntaxException;

import org.xhtmlrenderer.swing.NaiveUserAgent;

/**
 * An XHTMLRenderer (Flying Saucer) UserAgent implementation used to resolve
 * relative URL's in Tidy'ed HTML.
 * 
 * @author Terry Longrie
 */
public class HTMLImagerUserAgent extends NaiveUserAgent 
{
	private String base;    

	/**
	 * Check for and resolve a possible relative URI to a full URL.
	 * 
	 * @param uri The uri that may need to be resolved.
	 * 
	 * @returns If the provided uri is relative returns a full URL to the 
	 *          relative resource.  If the provided uri is not relative then
	 *          returns it unmodified.
	 */
	public String resolveURI( String uri )
	{
		String resolved = null;
		String trimmed  = uri.trim( );

		if ( !trimmed.startsWith( "http://" ) )
		{
			StringBuilder buf = new StringBuilder( 1024 );

			buf.append( base );

			if ( !trimmed.startsWith( "/" ) )
			{
				buf.append( "/" );
			}

			buf.append( trimmed );

			resolved = buf.toString( );
		}
		else
		{
			resolved = trimmed;
		}

		return resolved;
	}

	/**
	 * Construct a new HTMLImagerUserAgent.
	 * 
	 * @param url The URL of the HTML page that is being retrieved.
	 */
	protected HTMLImagerUserAgent( String url )
	{
		StringBuilder buf = new StringBuilder( );        

		try
		{
			URI uri = new URI( url );                

			buf.append( "http://" );
			buf.append( uri.getHost( ) );

			int port = uri.getPort( );

			if ( port >= 0 )
			{
				buf.append( ":" );
				buf.append( port );
			}
		}

		catch ( URISyntaxException use )
		{
			buf.append( "" );
		}

		this.base = buf.toString( );
	}    
}