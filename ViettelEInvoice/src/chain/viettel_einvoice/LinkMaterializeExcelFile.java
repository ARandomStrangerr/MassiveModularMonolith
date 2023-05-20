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
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
            chain.getProcessObject().get("header").getAsJsonObject().get("clientId").getAsString());
        chain.getProcessObject().get("body").getAsJsonObject().addProperty("fileName", fileName);
        byte[] decodedContent = Base64.getDecoder().decode(chain.getProcessObject().get("body").getAsJsonObject().remove("file").getAsString().getBytes());
        try (FileOutputStream fos = new FileOutputStream(fileName)){
            fos.write(decodedContent);
        } catch (IOException e){
            chain.getProcessObject().addProperty("error", "Không ghi chép lại được tệp tin Excel truyền lên");
            return false;
        }
        return true;
    }
}
