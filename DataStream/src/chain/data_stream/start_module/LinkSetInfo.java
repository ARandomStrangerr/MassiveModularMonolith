package chain.data_stream.start_module;

import chain.Chain;
import chain.Link;
import memorable.DataStream;

class LinkSetInfo extends Link {
    LinkSetInfo(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        DataStream memorableObject = DataStream.getInstance();
        memorableObject.setModuleName(chain.getProcessObject().get("moduleName").getAsString());
        return true;
    }
}
