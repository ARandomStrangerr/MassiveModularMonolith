package socket_handler;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

public class RESTRequest {
    public static String untrustPost(String uri, String body, HashMap<String, String> header) throws IOException {
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
    public static String post(String uri, String body, HashMap<String, String> header) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(uri).openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        if (header != null)
            for (Map.Entry<String, String> entry : header.entrySet())
                con.addRequestProperty(entry.getKey(), entry.getValue());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
        writer.write(body);
        writer.flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String returnString =  reader.readLine();
        writer.close();
        reader.close();
        con.disconnect();
        return returnString;
    }

    public static String get(String uri) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(uri).openConnection();
        con.setDoOutput(false);
        con.setRequestMethod("GET");
        return new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
    }
}
