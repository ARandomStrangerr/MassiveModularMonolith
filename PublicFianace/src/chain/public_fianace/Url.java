package chain.public_fianace;

public enum Url {
	LOGIN("/Login");
	public final String path;
	Url(String path){
		this.path = "https://kbnngateway.vst.mof.gov.vn/dvc/api" + path;
	}
}
