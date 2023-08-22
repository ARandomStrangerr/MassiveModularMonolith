import chain.public_fianace.ChainStartModule;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import socket_handler.SocketWrapper;

import java.net.InetAddress;

public class PublicFinance {
	public static void main(String[] args) {
		JsonObject argument;
		try {
			argument = new Gson().fromJson(args[0], JsonObject.class);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("No argument is passed through");
			return;
		} catch (JsonSyntaxException e) {
			System.err.println("Argument cannot be parsed to Json");
			return;
		}
		if (!new ChainStartModule(argument).execute()) {
			System.exit(-1);
		}
	}
}
