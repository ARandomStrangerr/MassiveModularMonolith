package chain.treasury_public_finance.handle_request;

enum Route {
    LOGIN("Login", "POST"),
    LOGOUT("Logout", "POST"),
    CHECK_DOCUMENT_STATUS("CheckDocumentStatus", "POST"),
    SEND_DOCUMENT("SendDocument", "POST"),
    DOWNLOAD("Download", "GET"),
    UPLOAD("Upload", "POST");
    private String route, method;

    Route(String route, String method) {
        this.route = "https://kbnngateway.vst.mof.gov.vn/dvc/" + route;
        this.method = method;
    }

    public String route(){
        return this.route;
    }

    public String method(){
        return this.method;
    }
}
