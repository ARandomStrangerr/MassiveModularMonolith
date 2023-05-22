package chain.viettel_einvoice;

public enum Url {
    Authenticate("auth/login"),
    UploadInvoice("InvoiceAPI/InvoiceWS/createInvoice"),
	DownloadInvoice("InvoiceAPI/InvoiceUtilsWS/getInvoiceRepresentationFile");

    public String path;

    Url(String path) {
        this.path = String.format("%s/%s", "https://api-vinvoice.viettel.vn/services/einvoiceapplication/api", path);
    }
}
