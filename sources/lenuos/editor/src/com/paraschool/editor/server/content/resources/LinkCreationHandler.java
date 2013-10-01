package com.paraschool.editor.server.content.resources;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.swing.NaiveUserAgent;

import com.paraschool.commons.share.LinkResource;
import com.paraschool.editor.server.LinkRequest;
import com.paraschool.editor.server.ProjectManager;

/*
 * Created at 3 sept. 2010
 * By bathily
 */
public class LinkCreationHandler implements
		ResourceCreationHandler<LinkResource> {

	static final int WIDTH = 1024;
	static final int HEIGHT = 768;
	
	
	private void getInfo(final LinkResource resource, final ProjectManager projectManager) {
		new LinkRequest(resource.getUrl(), new LinkRequest.Handler() {
			
			@Override
			public void onSuccess(InputStream stream) throws Exception {
				Tidy tidy = new Tidy( );

				tidy.setQuiet( true );
				//tidy.setXHTML( true );
				tidy.setHideComments( true );
				tidy.setInputEncoding( "UTF-8" );
				tidy.setOutputEncoding( "UTF-8" );
				tidy.setShowErrors( 0 );
				tidy.setShowWarnings( false );

				Document doc = tidy.parseDOM(stream,null );

				if ( doc != null )
				{
					Node node = doc.getElementsByTagName("title").item(0).getFirstChild();
					if(node != null)
						resource.setName(node.getNodeValue());
					
				}
			}
		}, 5000);
		
	}
	
	@Override
	public void onCreate(LinkResource resource, ProjectManager projectManager) {
		resource.setId(UUID.randomUUID().toString());
		getInfo(resource, projectManager);
	}
	
	
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
		HTMLImagerUserAgent( String url )
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
	

}
