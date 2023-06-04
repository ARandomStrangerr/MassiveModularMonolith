package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class LinkMaterializeExcelFile extends Link {
	public LinkMaterializeExcelFile(Chain chain) {
		super(chain);
	}

	@Override
	public boolean execute() {
		String fileName = String.format("%s %s.xlsx",
			LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
			chain.getProcessObject().get("body").getAsJsonObject().get("username").getAsString());
		byte[] decodedContent = null;
		try {
			decodedContent = Base64.getDecoder().decode(chain.getProcessObject().get("body").getAsJsonObject().remove("file").getAsString().getBytes());
		} catch (NullPointerException ignore) {
		}
		try (FileOutputStream fos = new FileOutputStream(fileName)) {
			assert decodedContent != null;
			fos.write(decodedContent);
		} catch (IOException e) {
			chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Không ghi chép lại được tệp tin Excel truyền lên");
			return false;
		}
		chain.getProcessObject().get("body").getAsJsonObject().addProperty("fileName", fileName);
		return true;
	}
}
