package org.openml.tools.dataset;

import java.io.File;

import com.thoughtworks.xstream.XStream;

import org.openml.apiconnector.algorithms.Conversion;
import org.openml.apiconnector.io.ApiConnector;
import org.openml.apiconnector.io.ApiSessionHash;
import org.openml.apiconnector.xml.DataSetDescription;
import org.openml.apiconnector.xstream.XstreamXmlMapping;

public class TransferDatasets {

	private static final String TO = "http://openml.liacs.nl/";
	private static final String FROM = "http://www.openml.org/";
	
	public static void main( String[] args ) throws Exception {
		new TransferDatasets();
	}
	
	public TransferDatasets() throws Exception {
		ApiConnector.API_URL = TO;
		XStream xstream = XstreamXmlMapping.getInstance();
		ApiSessionHash ash = new ApiSessionHash();
		ash.set("janvanrijn@gmail.com", "Feyenoord2002");
		
		for( int i = 1; i <= 62; ++i ) {
			try {
				ApiConnector.API_URL = FROM;
				DataSetDescription dsd = ApiConnector.openmlDataDescription( i );
				ApiConnector.API_URL = TO;
				dsd.unsetUrl();
				String descriptionXML = xstream.toXML(dsd);
				
				File description = Conversion.stringToTempFile( descriptionXML, dsd.getName() + "_description", "xml" );
				
				ApiConnector.openmlDataUpload( description, dsd.getDataset(), ash.getSessionHash() );
				System.out.println("Succes at #" + i);
			} catch( Exception e ) {
				e.printStackTrace();
				System.out.println( "Error at #" + i + ": " + e.getMessage() );
			}
		}
	}
}
