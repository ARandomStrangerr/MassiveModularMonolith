package aux.viettel_einvoice;

public enum Url {
    Authenticate("https://api-vinvoice.viettel.vn/auth/login/"),
    UploadInvoice("https://api-vinvoice.viettel.vn/services/einvoiceapplication/api/InvoiceAPI/InvoiceWS/createInvoice/"),
    UploadDraftInvoice("https://api-vinvoice.viettel.vn/services/einvoiceapplication/api/InvoiceAPI/InvoiceWS/createOrUpdateInvoiceDraft/"),
    DownloadInvoice("https://api-vinvoice.viettel.vn/services/einvoiceapplication/api/InvoiceAPI/InvoiceUtilsWS/getInvoiceRepresentationFile/");
    public final String path;

    Url(String path) {
        this.path = path;
    }
}
