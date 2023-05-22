package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;

public class LinkReadFromExcel extends Link {
    public LinkReadFromExcel(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        JsonArray sendArray = new JsonArray();
        DataFormatter formatter = new DataFormatter();
        try (Workbook workbook = WorkbookFactory.create(new File(chain.getProcessObject().get("body").getAsJsonObject().get("fileName").getAsString()))) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                JsonObject sendObject = new JsonObject();
                // general invoice information section
                JsonObject generalInvoiceInfo = new JsonObject();
                generalInvoiceInfo.addProperty("invoiceType", formatter.formatCellValue(row.getCell(0)));
                generalInvoiceInfo.addProperty("templateCode", formatter.formatCellValue(row.getCell(1)));
                generalInvoiceInfo.addProperty("invoiceSeries", formatter.formatCellValue(row.getCell(2)));
                generalInvoiceInfo.addProperty("currencyCode", "VND");
                generalInvoiceInfo.addProperty("paymentStatus", true);
                // seller information
                JsonObject sellerInfo = new JsonObject();
                sellerInfo.addProperty("sellerLegalName", formatter.formatCellValue(row.getCell(3)));
                sellerInfo.addProperty("sellerTaxCode", chain.getProcessObject().get("body").getAsJsonObject().get("username").getAsString());
                sellerInfo.addProperty("sellerAddressLine", formatter.formatCellValue(row.getCell(4)));
                sellerInfo.addProperty("merchantCode", formatter.formatCellValue(row.getCell(5)));
                sellerInfo.addProperty("merchantName", formatter.formatCellValue(row.getCell(6)));
                sellerInfo.addProperty("merchantCity", formatter.formatCellValue(row.getCell(7)));
                // buyer information
                JsonObject buyerInfo = new JsonObject();
                buyerInfo.addProperty("buyerName", formatter.formatCellValue(row.getCell(8)));
                buyerInfo.addProperty("buyerCode", formatter.formatCellValue(row.getCell(9)));
                buyerInfo.addProperty("buyerAddressLine", formatter.formatCellValue(row.getCell(10)));
                // payment method
                JsonArray paymentMethod = new JsonArray();
                JsonObject paymentMethodObj = new JsonObject();
                paymentMethodObj.addProperty("paymentMethodName", formatter.formatCellValue(row.getCell(11)));
                // item information
                JsonArray itemInfo = new JsonArray();
                JsonObject item = new JsonObject();
                for (int index = 12; index < row.getLastCellNum(); index += 5, item = new JsonObject()) {
                    item.addProperty("itemName", formatter.formatCellValue(row.getCell(index)));
                    item.addProperty("unitName", formatter.formatCellValue(row.getCell(index + 1)));
                    item.addProperty("unitPrice", formatter.formatCellValue(row.getCell(index + 2)));
                    item.addProperty("quantity", formatter.formatCellValue(row.getCell(index + 3)));
                    item.addProperty("unitPrice", formatter.formatCellValue(row.getCell(index + 4)));
                }
            }
        } catch (IOException e) {

        }
        chain.getProcessObject().get("body").getAsJsonObject().add("sendData", sendArray);
        return true;
    }
}
