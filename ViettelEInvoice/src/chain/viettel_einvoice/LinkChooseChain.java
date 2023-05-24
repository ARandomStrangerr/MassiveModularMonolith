package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;

public class LinkChooseChain extends Link {
	public LinkChooseChain(Chain chain) {
		super(chain);
	}

	@Override
	public boolean execute() {
		String subJob;
		try {
			subJob = chain.getProcessObject().get("body").getAsJsonObject().get("subJob").getAsString();
		} catch (NullPointerException e) {
			chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Thiếu trường thông tin subJob");
			return false;
		}
		switch (subJob) {
			case "uploadDraftInvoice" -> {
				return new ChainUploadDraftInvoice(chain.getProcessObject()).execute(); // pass by ref
			}
			case "uploadInvoice" -> {
				return new ChainUploadInvoice(chain.getProcessObject()).execute(); // pass by ref
			}
			case "downloadInvoice" -> {
				return new ChainDownloadInvoice(chain.getProcessObject()).execute(); // pass by ref
			}
			default -> {
				chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Trường thông tin subJob không chính xác");
				return false;
			}
		}
	}
}
