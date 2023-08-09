import com.google.gson.JsonObject;
import socket_handler.SocketWrapper;

import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

class SendInvoice {
	public static void main(String[] args) throws Exception {
		SocketWrapper socket = new SocketWrapper(InetAddress.getByName("localhost"), 9998, "key.jks", "hungcom23");
		socket.write("{\"macAddress\":\"24:41:8C:06:2C:82\"}");
		JsonObject obj = new JsonObject();
		obj.addProperty("job", "ViettelEInvoice");
		obj.addProperty("subJob", "uploadInvoice");
		obj.addProperty("username", "0101954482");
		obj.addProperty("password", "123456aA@");
		socket.write(obj.toString());
		obj.addProperty("file", Base64.getEncoder().encodeToString(Files.readAllBytes(Path.of("/home/microbe-obliterator/Downloads/test.xlsx"))));
		System.out.println(socket.read());
	}
}
