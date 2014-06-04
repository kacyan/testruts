package jp.co.ksi.incubator;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;


/**
 * RSSリーダーの習作
 * rome-1.0.jar, jdom.jar(v1.1.1) を使用する
 * @author kac
 * @since 2012/05/10
 * @version 2012/05/10
 */
public class RSSTest
{
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		String	url= "http://headlines.yahoo.co.jp/rss/all-dom.xml";
		url= "http://rss.rssad.jp/rss/itmtop/2.0/itmedia_all.xml";
		url= "http://ardbeg.dev.ksi.co.jp/bbs/usr/sha-ml/rss.do";
		String proxyHost = "172.16.10.3";
		int proxyPort = 3128;

		try
		{
			Proxy	proxy= new Proxy( Proxy.Type.HTTP, new InetSocketAddress(proxyHost,proxyPort) );
			URL	u= new URL( url );
			
			URLConnection con = u.openConnection( proxy );
			
			XmlReader	reader= new XmlReader( con );
			
			SyndFeedInput	in= new SyndFeedInput();
			SyndFeed	feed= in.build( reader );
			System.out.println( "author="+ feed.getAuthor() );
			System.out.println( "copyright="+ feed.getCopyright() );
			System.out.println( "description="+ feed.getDescription() );
			System.out.println( "encoding="+ feed.getEncoding() );
			System.out.println( "feedType="+ feed.getFeedType() );
			System.out.println( "foreignMarkup="+ feed.getForeignMarkup() );
			System.out.println( "language="+ feed.getLanguage() );
			System.out.println( "link="+ feed.getLink() );
			System.out.println( "title="+ feed.getTitle() );
			System.out.println( "uri="+ feed.getUri() );
			System.out.println( "publishedDate="+ feed.getPublishedDate() );
			System.out.println( "----+----+----+----+----+----+----+----+----+----+----+----+" );
			
			debugList( feed.getAuthors(), "authors" );
			debugList( feed.getCategories(), "categories" );
			debugList( feed.getContributors(), "contributors" );
			debugList( feed.getEntries(), "entries" );
			debugList( feed.getLinks(), "links" );
			debugList( feed.getModules(), "modules" );
			debugList( feed.getSupportedFeedTypes(), "supportedFeedTypes" );
			
			List	entries= feed.getEntries();
			for( int i= 0; i < 1; i++ )
//			for( int i= 0; i < entries.size(); i++ )
			{
				SyndEntry	entry= (SyndEntry)entries.get( i );
				debugList( entry.getAuthors(), "authors" );
				debugList( entry.getCategories(), "categories" );
				debugList( entry.getContents(), "contents" );
				debugList( entry.getContributors(), "contributors" );
				debugList( entry.getEnclosures(), "enclosures" );
				debugList( entry.getLinks(), "links" );
				debugList( entry.getModules(), "modules" );
				System.out.println( entry.getTitle() );
				SyndContent	content= entry.getDescription();
				System.out.println( "description.mode="+ content.getMode() );
				System.out.println( "description.type="+ content.getType() );
				System.out.println( "description.value="+ content.getValue() );
				System.out.println( "link="+ entry.getLink() );
				System.out.println( "uri="+ entry.getUri() );
				System.out.println( "publishedDate="+ entry.getPublishedDate() );
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	private static void debugList( List list, String label )
	{
		System.out.println( label+"="+ list.size() );
		for( int i= 0; i < list.size(); i++ )
		{
			Object	obj= list.get( i );
			System.out.println( label+": "+ obj +" - "+ obj.getClass().getName() );
		}
	}
	
}
