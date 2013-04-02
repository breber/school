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
 * Connect to the server given on the command line using
 * SSL, and print out the resulting certificates.
 *
 * @author Brian Reber (breber)
 */
public class SSL {

	public static void main(String[] args) throws UnknownHostException,
			IOException, NoSuchAlgorithmException, KeyManagementException {
		 if (args.length != 1) {
			 System.err.println("Usage: java SSL https://www.site.com");
			 return;
		 }

		// We will allow all certificates, even if they don't
		// have a matching URL (www.gmail.com has a certificate with URL mail.google.com)
		HostnameVerifier hv = new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Basic initialization of the SSL engine
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, null, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		HttpsURLConnection.setDefaultHostnameVerifier(hv);

		// Connect to the given URL
		SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		URL url = new URL(args[0]);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setSSLSocketFactory(sslsocketfactory);

		conn.connect();

		// Print out the certificates
		int i = 1;
		for (Certificate c : conn.getServerCertificates()) {
			System.err.println("Certificate #" + i++);
			System.out.println(c);
		}
	}
}
