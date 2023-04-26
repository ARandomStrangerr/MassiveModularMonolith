package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class LinkMaterializeExcelFile extends Link {
    public LinkMaterializeExcelFile(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        byte[] decodedByte = Base64.getDecoder().decode(chain.getProcessObject().get("body").getAsJsonObject().get("file").getAsString().getBytes(StandardCharsets.UTF_8));
        try {
            FileOutputStream fos = new FileOutputStream(Thread.currentThread().getName());
            fos.write(decodedByte);
            fos.close();
        } catch (IOException e){
            chain.getProcessObject().addProperty("error", " Không đọc được tệp tin excel gửi lên máy chủ");
            return false;
        }
        return true;
    }
}
