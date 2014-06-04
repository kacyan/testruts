package jp.co.ksi.incubator;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;

/**
 * XMLファイルをXPathで処理する習作
 * @author kac
 * @since 2011/07/22
 * @version 2011/07/22
 */
public class XPathTest
{
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		XPathTest	test= new XPathTest();
		test.test();
	}

	/**
	 * XMLファイルを読込domを生成します。
	 * XPathで該当ノードを取得します。
	 * 取得したノードのみを階層構造を維持したままXML出力します。
	 */
	public void test()
	{
		String	file= "./misc/xml/Export-out-20110721.xml";
		
		FileInputStream	in= null;
		try
		{
			in= new FileInputStream( file );

			DocumentBuilder	builder= DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document	dom= builder.parse( in );
			
			String	expression= "/request/portal/web-app/portlet-app/portlet[@uniquename='wps.p.Feedspace']";
			expression= "//*/portlet[@uniquename='wps.p.Feedspace']";
			XPath	xpath= XPathFactory.newInstance().newXPath();
			Node	node= (Node)xpath.evaluate( expression, dom, XPathConstants.NODE );
			System.out.println( getNodePath( node ) );
			
			/**
			 * 2011/07/22 Kac
			 * 発見したnodeを親の階層構造ごと保存する場合、親nodeの余計な子nodeを除去する為に親nodeを作り直すのが良い
			 */
			ArrayList<Node>	nodeList= new ArrayList<Node>();
			Node	parent= node.getParentNode();
			while( parent != null )
			{
				if( parent instanceof Document )
				{//	ドキュメントに到達した->終了
					break;
				}
				//	親ノードを作り直す
				Element	newParent= dom.createElement( parent.getNodeName() );
				NamedNodeMap	attrs= parent.getAttributes();
				for( int i= 0; i < attrs.getLength(); i++ )
				{//	属性を全てコピーする
					Node	attr= attrs.item( i );
					newParent.setAttribute( attr.getNodeName(), attr.getNodeValue() );
				}
				//	ノードを親ノードの子ノードとして追加する
				newParent.appendChild( node );
//				debugNode( newParent, System.out );
				nodeList.add( newParent );
				
				//	親ノードを手繰るーぷ
				node= newParent;
				parent= parent.getParentNode();
			}
			
			debugNode( node, System.out );
			

		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{//	後始末
			if( in != null )
			{
				try
				{
					in.close();
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * 指定されたノードの階層をXPath表現で返します。
	 * @param node
	 * @return
	 */
	public String getNodePath( Node node )
	{
		StringBuffer	buf= new StringBuffer();
		
		ArrayList<Node>	nodeList= new ArrayList<Node>();
		nodeList.add( node );
		Node	parent= node.getParentNode();
		while( parent != null )
		{
			if( parent instanceof Document )
			{
				break;
			}
			nodeList.add( parent );
			parent= parent.getParentNode();
		}

		for( int i= nodeList.size()-1; i >= 0; i-- )
		{
			Node	nodeTmp= nodeList.get( i );
			buf.append( "/"+ nodeTmp.getNodeName() );
		}
		return buf.toString();
	}
	
	/**
	 * 指定されたノードをXML形式で出力します
	 * @param node	ノード
	 * @param out	出力ストリーム
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 */
	public void debugNode( Node node, OutputStream out ) throws TransformerException, ParserConfigurationException
	{
		DOMSource	xmlSource= new DOMSource( node );
		StreamResult	outputTarget= new StreamResult( out );
		Transformer	transformer= TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
		transformer.setOutputProperty( OutputPropertiesFactory.S_KEY_INDENT_AMOUNT, "1" );
		transformer.transform( xmlSource, outputTarget );
	}
}
