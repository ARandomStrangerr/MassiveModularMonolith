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
		int line = 0;
        try (Workbook workbook = WorkbookFactory.create(new File(chain.getProcessObject().get("body").getAsJsonObject().get("fileName").getAsString()))) {
            Sheet sheet = workbook.getSheetAt(0);
			sheet.removeRow(sheet.getRow(0)); // remove the first row, since the customer request so
            for (Row row : sheet) {
				line++;
                JsonObject sendObject = new JsonObject();
                // general invoice information section
                JsonObject generalInvoiceInfo = new JsonObject();
                generalInvoiceInfo.addProperty("invoiceType", formatter.formatCellValue(row.getCell(1))); // mã hóa đơn
                generalInvoiceInfo.addProperty("templateCode", formatter.formatCellValue(row.getCell(2))); // ký hiệu mẫu hóa đơn
                generalInvoiceInfo.addProperty("invoiceSeries", formatter.formatCellValue(row.getCell(3))); // ký hiệu hóa đơn
                generalInvoiceInfo.addProperty("currencyCode", formatter.formatCellValue(row.getCell(4))); // mã tiền tệ
                generalInvoiceInfo.addProperty("paymentStatus", true);
                // seller information
                JsonObject sellerInfo = new JsonObject();
                sellerInfo.addProperty("sellerLegalName", formatter.formatCellValue(row.getCell(5))); // tên người bán
                sellerInfo.addProperty("sellerTaxCode", chain.getProcessObject().get("body").getAsJsonObject().get("username").getAsString()); // mã số thuế bên bán
                sellerInfo.addProperty("sellerAddressLine", formatter.formatCellValue(row.getCell(6))); // địa chỉ bên bán
                // buyer information
                JsonObject buyerInfo = new JsonObject();
                buyerInfo.addProperty("buyerName", formatter.formatCellValue(row.getCell(7))); // tên người mua
                buyerInfo.addProperty("buyerCode", formatter.formatCellValue(row.getCell(8))); // mã khách hàng
                buyerInfo.addProperty("buyerAddressLine", formatter.formatCellValue(row.getCell(9))); // địa chỉ xuất hóa đơn
                // payment method
                JsonArray paymentMethod = new JsonArray();
                JsonObject paymentMethodObj = new JsonObject();
                paymentMethodObj.addProperty("paymentMethodName", formatter.formatCellValue(row.getCell(10))); // hình thức thanh toán
                // item information
                JsonArray itemInfo = new JsonArray();
				JsonObject item = new JsonObject();
                for (int index = 11; index < row.getLastCellNum(); index += 5, item = new JsonObject()) {
                    item.addProperty("itemName", formatter.formatCellValue(row.getCell(index))); // tên hàng hóa, dịch vụ
                    item.addProperty("unitName", formatter.formatCellValue(row.getCell(index + 1))); // tên đơn vị tính
                    item.addProperty("unitPrice", formatter.formatCellValue(row.getCell(index + 2))); // đơn giá
                    item.addProperty("quantity", formatter.formatCellValue(row.getCell(index + 3))); // số lượng
                    item.addProperty("unitPrice", formatter.formatCellValue(row.getCell(index + 4))); // thuế xuất
					itemInfo.add(item);
                }
            }
        } catch (IOException e) {

        }
        chain.getProcessObject().get("body").getAsJsonObject().add("sendData", sendArray);
        return true;
    }
}
