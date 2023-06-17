package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.poi.ss.usermodel.*;

import java.io.File;

public class LinkReadFromExcel extends Link {
	public LinkReadFromExcel(Chain chain) {
		super(chain);
	}

	@Override
	public boolean execute() {
		JsonArray sendArray = new JsonArray();
		int rowIndex = 0;
		DataFormatter dataFormatter = new DataFormatter();
		JsonArray adjustInvoiceLocation = new JsonArray();
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
				// trạng thái điều chỉnh hóa đơn
				int adjustmentType;
				try {
					adjustmentType = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(5)));
				} catch (Exception e) {
					adjustmentType = 1;
				}
				if (adjustmentType == 5) adjustInvoiceLocation.add(rowIndex);
				generalInvoiceInfo.addProperty("adjustmentType", adjustmentType);
				generalInvoiceInfo.addProperty("adjustmentInvoiceType", dataFormatter.formatCellValue(row.getCell(6))); // loại hóa đơn điều chỉnh
				generalInvoiceInfo.addProperty("originalInvoiceId", dataFormatter.formatCellValue(row.getCell(7))); // Số hóa đơn gốc
				generalInvoiceInfo.addProperty("originalInvoiceIssueDate", dataFormatter.formatCellValue(row.getCell(8))); // thời gian lập hóa đơn gốc
				generalInvoiceInfo.addProperty("paymentStatus", dataFormatter.formatCellValue(row.getCell(9))); //Trạng thái thanh toán
				// seller information
				JsonObject sellerInfo = new JsonObject();
				sellerInfo.addProperty("sellerLegalName", dataFormatter.formatCellValue(row.getCell(5))); // tên người bán
				sellerInfo.addProperty("sellerTaxCode", chain.getProcessObject().get("body").getAsJsonObject().get("username").getAsString()); // mã số thuế bên bán
				sellerInfo.addProperty("sellerAddressLine", dataFormatter.formatCellValue(row.getCell(6))); // địa chỉ bên bán
				// buyer information
				JsonObject buyerInfo = new JsonObject();
				buyerInfo.addProperty("buyerName", dataFormatter.formatCellValue(row.getCell(7))); // tên người mua
				buyerInfo.addProperty("buyerCode", dataFormatter.formatCellValue(row.getCell(8))); // mã khách hàng
				buyerInfo.addProperty("buyerAddressLine", dataFormatter.formatCellValue(row.getCell(9))); // địa chỉ xuất hóa đơn
				// payment method
				JsonArray paymentMethod = new JsonArray();
				JsonObject paymentMethodObj = new JsonObject();
				paymentMethodObj.addProperty("paymentMethodName", dataFormatter.formatCellValue(row.getCell(10))); // hình thức thanh toán
				paymentMethod.add(paymentMethodObj);
				// item information
				JsonArray itemInfo = new JsonArray();
				JsonObject item = new JsonObject();
				for (int index = 11; index < row.getLastCellNum(); index += 5, item = new JsonObject()) {
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
		if (adjustInvoiceLocation.size() != 0)
			chain.getProcessObject().get("body").getAsJsonObject().add("adjustInvoiceLocation", adjustInvoiceLocation);
		chain.getProcessObject().get("body").getAsJsonObject().add("sendData", sendArray);
		return true;
	}
}
