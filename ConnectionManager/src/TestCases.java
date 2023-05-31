import com.google.gson.Gson;
import com.google.gson.JsonObject;
import socket_handler.SocketWrapper;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;

class TestCases1 { // testing the connection manager
	public static void main(String[] args) throws Exception {
		SocketWrapper socket = new SocketWrapper(InetAddress.getByName("118.70.127.55"), 9999, "key.jks", "hungcom23");
		socket.write("{macAddress: \"24:41:8C:06:2C:82\"}");
		JsonObject writeObject = new JsonObject();
		writeObject.addProperty("job", "SMTPMail");
		socket.write(writeObject.toString());
		System.out.println(socket.read());
	}
}

class TestAuthentication {
	public static void main(String[] args) throws Exception {
		SocketWrapper socket = new SocketWrapper(InetAddress.getByName("localhost"), 9998, "key.jks", "hungcom23");
		socket.write("{macAddress: \"24:41:8C:06:2C:82\"}");
		JsonObject writeObject = new JsonObject();
		writeObject.addProperty("job", "a");
		for (int i = 0; i < 10; i++) {
			socket.write(writeObject.toString());
		}
		for (int i = 0; i < 50; i++) {
			System.out.println(socket.read());
		}
	}
}

class Test4 {
	public static void main(String[] args) throws Exception{
		File f = new File("'30-05-2023 24-41-8C-06-2C-82.xlsx'");
		FileOutputStream fos = new FileOutputStream(f);
		fos.write(Files.readAllBytes(Paths.get("/home/microbe-obliterator/Downloads/Guithanhmoi.xlsx")));
	}
}

class TestViettelEInvoice {
	public static void main(String[] args) throws Exception {
		SocketWrapper soc = new SocketWrapper(InetAddress.getByName("localhost"), 9998, "key.jks", "hungcom23");
		JsonObject json = new JsonObject();
		json.addProperty("macAddress", "24:41:8C:06:2C:82");
		soc.write(json.toString());
		json = new JsonObject();
		json.addProperty("job", "ViettelEInvoice");
		json.addProperty("subJob", "uploadDraftInvoice");
		json.addProperty("username", "0101954482");
		json.addProperty("password", "123456aA@");
		json.addProperty("file", Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get("/home/microbe-obliterator/Downloads/Guithanhmoi.xlsx"))));
		soc.write(json.toString());
		System.out.println(soc.read());
	}
}

class Test3 {
	public static void main(String[] args) throws Exception {
//		https://api-vinvoice.viettel.vn/services/einvoiceapplication/api/
		HttpsURLConnection con = (HttpsURLConnection) new URL("https://api-vinvoice.viettel.vn/auth/login").openConnection();
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
		writer.write("{\n" +
			"\"username\":\"0101954482\",\n" +
			"\"password\":\"123456aA@\"\n" +
			"}");
		writer.newLine();
		writer.flush();
		BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		JsonObject returnObj = new Gson().fromJson(reader, JsonObject.class);
		con = (HttpsURLConnection) new URL("https://api-vinvoice.viettel.vn/services/einvoiceapplication/api/InvoiceAPI/InvoiceUtilsWS/getInvoiceRepresentationFile").openConnection();
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Cookie", "access_token=" + returnObj.get("access_token").getAsString());
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		JsonObject sendObj = new JsonObject();
		sendObj.addProperty("supplierTaxCode", "0101954482");
		sendObj.addProperty("invoiceNo", "QD/2022/E0001133");
		sendObj.addProperty("templateCode", "CTT560/001");
		sendObj.addProperty("fileType", "PDF");
		writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
		writer.write(sendObj.toString());
		writer.newLine();
		writer.flush();
		reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
	}
}
