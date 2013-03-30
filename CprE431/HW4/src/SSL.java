import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author breber
 */
public class SSL {

	public static void main(String[] args) throws UnknownHostException,
			IOException, NoSuchAlgorithmException, KeyManagementException {
		// if (args.length != 2) {
		// System.err.println("Usage: " + args[0] + " www.site.com");
		// }

		HostnameVerifier hv = new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, null, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		HttpsURLConnection.setDefaultHostnameVerifier(hv);

		SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		URL url = new URL("https://webct.its.iastate.edu");
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setSSLSocketFactory(sslsocketfactory);

		conn.connect();

		for (Certificate c : conn.getServerCertificates()) {
			System.out.println(c);
		}
	}

}
