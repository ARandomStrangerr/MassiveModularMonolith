package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;

public class LinkExtendChain extends Link {
	public LinkExtendChain(Chain chain) {
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
			case "createInvoice":
				chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Chức năng này hiện tại chưa được hỗ trợ");
				return false;
			case "getInvoice":
				chain.addLink(new LinkGetInvoice(chain));
				break;
			default:
				chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Trường thông tin subJob không chính xác");
				return false;
		}
		return true;
	}
}
