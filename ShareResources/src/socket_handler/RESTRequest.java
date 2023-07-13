package socket_handler;

import javax.net.ssl.*;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

public class RESTRequest {
    public static String untrustPost(String uri, String body, Map<String, String> header) throws IOException {
        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509ExtendedTrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }
        };
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e){
            e.printStackTrace();
        }
        HttpsURLConnection con = (HttpsURLConnection) new URL (uri).openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");if (header != null)
            for (Map.Entry<String, String> entry : header.entrySet())
                con.addRequestProperty(entry.getKey(), entry.getValue());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
        writer.write(body);
        writer.flush();
        writer.close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        return reader.readLine();
    }

	/**
	 *
	 * @param uri address to connect
	 * @param body data to send to the address
	 * @param header header of the html request
	 * @return the data responded by the post request
	 * @throws IOException when cannot read / write to the address
	 * @throws RuntimeException when the address response an error code
	 */
    public static String post(String uri, String body, Map<String, String> header) throws RuntimeException, IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(uri).openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        if (header != null)
            for (Map.Entry<String, String> entry : header.entrySet())
                con.addRequestProperty(entry.getKey(), entry.getValue());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
        writer.write(body);
        writer.flush();
		writer.close();
        try {
            return new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
        } catch (IOException e) {
			throw new RuntimeException(new BufferedReader(new InputStreamReader(con.getErrorStream())).readLine());
        } finally {
			con.disconnect();
		}
    }

    public static String get(String uri) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(uri).openConnection();
        con.setDoOutput(false);
        con.setRequestMethod("GET");
        return new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
    }
}
