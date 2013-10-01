package com.paraschool.editor.server.thumbnail;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.simple.Graphics2DRenderer;
import org.xhtmlrenderer.simple.extend.XhtmlNamespaceHandler;
import org.xhtmlrenderer.swing.NaiveUserAgent;

import com.paraschool.editor.server.LinkRequest;

/*
 * Created at 3 sept. 2010
 * By bathily
 */
public class WebPageThumbnailBuilder implements ThumbnailBuilder {

	private static final Logger log = Logger.getLogger( WebPageThumbnailBuilder.class );
	private static final int WIDTH = 1024;
	private static final int HEIGHT = 768;
	
	private final String url;
	
	public WebPageThumbnailBuilder(String url) {
		this.url = url;
	}
	
	@Override
	public InputStream make(final InputStream mediaStream) throws Throwable {
		try {
			final File file = File.createTempFile("WEB-", ".png");
			new LinkRequest(url, new LinkRequest.Handler() {
				
				@Override
				public void onSuccess(InputStream stream) {
					Tidy tidy = new Tidy( );

					tidy.setQuiet( true );
					tidy.setXHTML( true );
					tidy.setHideComments( true );
					tidy.setInputEncoding( "UTF-8" );
					tidy.setOutputEncoding( "UTF-8" );
					tidy.setShowErrors( 0 );
					tidy.setShowWarnings( false );

					Document doc = tidy.parseDOM(stream,null );

					if ( doc != null )
					{
						log.debug(doc.getElementsByTagName("title").item(0).getFirstChild().getNodeValue());
						
						BufferedImage      buf       = new BufferedImage( WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB );
						Graphics2D         graphics  = (Graphics2D)buf.getGraphics( );
						Graphics2DRenderer renderer  = new Graphics2DRenderer( );
						SharedContext      context   = renderer.getSharedContext( );
						UserAgentCallback  userAgent = new HTMLImagerUserAgent( url ); 

						context.setUserAgentCallback( userAgent );
						context.setNamespaceHandler( new XhtmlNamespaceHandler( ) );

						renderer.setDocument( doc, url );
						renderer.layout( graphics, new Dimension( WIDTH, HEIGHT ) );
						renderer.render( graphics );
						graphics.dispose( );
						
						try {
							ImageIO.write(buf, "png", file);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					else
					{
						if ( log.isDebugEnabled( ) )
							log.debug( "Unable to image URL '" + url + 
									"'.  The HTML that was returned could not be tidied." );
					}
					
				}
			}, 5000);
			return new FileInputStream(file);
		} catch (IOException e) {}
		
		return null;
	}
	
	/**
	 * An XHTMLRenderer (Flying Saucer) UserAgent implementation used to resolve
	 * relative URL's in Tidy'ed HTML.
	 * 
	 * @author Terry Longrie
	 */
	private class HTMLImagerUserAgent extends NaiveUserAgent 
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
