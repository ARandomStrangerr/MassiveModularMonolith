package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.util.Objects;

public class LinkReadFromExcel extends Link {
	public LinkReadFromExcel(Chain chain) {
		super(chain);
	}

	@Override
	public boolean execute() {
		JsonArray sendArray = new JsonArray();
		int rowIndex = 0;
		DataFormatter dataFormatter = new DataFormatter();
		try (Workbook workbook = WorkbookFactory.create(new File(chain.getProcessObject().get("body").getAsJsonObject().get("fileName").getAsString()))) {
			Sheet sheet = workbook.getSheetAt(0);
//            sheet.removeRow(sheet.getRow(0)); // remove the first row, since the customer request so
			for (Row row : sheet) {
				// general invoice information section
				JsonObject generalInvoiceInfo = new JsonObject();
				generalInvoiceInfo.addProperty("invoiceType", dataFormatter.formatCellValue(row.getCell(1))); // mã hóa đơn
				generalInvoiceInfo.addProperty("templateCode", dataFormatter.formatCellValue(row.getCell(2))); // ký hiệu mẫu hóa đơn
				generalInvoiceInfo.addProperty("invoiceSeries", dataFormatter.formatCellValue(row.getCell(3))); // ký hiệu hóa đơn
				generalInvoiceInfo.addProperty("currencyCode", dataFormatter.formatCellValue(row.getCell(4))); // mã tiền tệ
				int adjustmentType;
				try {
					adjustmentType = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(5)));
				} catch (Exception e){
					adjustmentType = 1;
				}
				generalInvoiceInfo.addProperty("adjustmentType", adjustmentType); // Trạng thái điều chỉnh hóa đơn
				generalInvoiceInfo.addProperty("adjustmentInvoiceType", dataFormatter.formatCellValue(row.getCell(6))); // Loại điều chỉnh
				generalInvoiceInfo.addProperty("originalInvoiceId", dataFormatter.formatCellValue(row.getCell(7))); // số hóa đơn gốc
				// Thời gian lập hóa đơn gốc going to be in the next step
				generalInvoiceInfo.addProperty("paymentStatus", dataFormatter.formatCellValue(row.getCell(9))); // trạng thái thanh toán
				generalInvoiceInfo.addProperty("originalInvoiceType",dataFormatter.formatCellValue(row.getCell(10))); // loại hóa đơn gốc
				// seller information
				JsonObject sellerInfo = new JsonObject();
				sellerInfo.addProperty("sellerLegalName", dataFormatter.formatCellValue(row.getCell(11))); // tên người bán
				sellerInfo.addProperty("sellerTaxCode", chain.getProcessObject().get("body").getAsJsonObject().get("username").getAsString()); // mã số thuế bên bán
				sellerInfo.addProperty("sellerAddressLine", dataFormatter.formatCellValue(row.getCell(12))); // địa chỉ bên bán
				// buyer information
				JsonObject buyerInfo = new JsonObject();
				buyerInfo.addProperty("buyerName", dataFormatter.formatCellValue(row.getCell(13))); // tên người mua
				buyerInfo.addProperty("buyerCode", dataFormatter.formatCellValue(row.getCell(14))); // mã khách hàng
				buyerInfo.addProperty("buyerAddressLine", dataFormatter.formatCellValue(row.getCell(15))); // địa chỉ xuất hóa đơn
				// payment method
				JsonArray paymentMethod = new JsonArray();
				JsonObject paymentMethodObj = new JsonObject();
				paymentMethodObj.addProperty("paymentMethodName", dataFormatter.formatCellValue(row.getCell(16))); // hình thức thanh toán
				paymentMethod.add(paymentMethodObj);
				// item information
				JsonArray itemInfo = new JsonArray();
				JsonObject item = new JsonObject();
				for (int index = 17; !Objects.equals(dataFormatter.formatCellValue(row.getCell(index)), ""); index += 5, item = new JsonObject()) {
					item.addProperty("itemName", dataFormatter.formatCellValue(row.getCell(index))); // tên hàng hóa, dịch vụ
					item.addProperty("unitName", dataFormatter.formatCellValue(row.getCell(index + 1))); // tên đơn vị tính
					item.addProperty("unitPrice", Long.parseLong(dataFormatter.formatCellValue(row.getCell(index + 2)))); // đơn giá
					item.addProperty("quantity", Integer.parseInt(dataFormatter.formatCellValue(row.getCell(index + 3)))); // số lượng
					item.addProperty("taxPercentage", (int) row.getCell(index + 4).getNumericCellValue()); // thuế xuất
					item.addProperty("itemTotalAmountWithoutTax", row.getCell(index + 2).getNumericCellValue() * row.getCell(index + 3).getNumericCellValue()); //thành tiền
					itemInfo.add(item);
				}
				JsonObject sendObject = new JsonObject();
				sendObject.add("generalInvoiceInfo", generalInvoiceInfo);
				sendObject.add("sellerInfo", sellerInfo);
				sendObject.add("buyerInfo", buyerInfo);
				sendObject.add("payments", paymentMethod);
				sendObject.add("itemInfo", itemInfo);
				sendArray.add(sendObject);
				rowIndex++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", String.format("Tiệp tin excel bị gặp vấn đề tại dòng số %d với lỗi %s", rowIndex + 1, e.getMessage()));
			return false;
		}
		chain.getProcessObject().get("body").getAsJsonObject().add("sendData", sendArray);
		return true;
	}
}
