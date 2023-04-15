package chain.data_stream.start_module;

import chain.Chain;
import chain.Link;
import memorable.DataStream;
import system_monitor.MonitorHandler;

class LinkSetInfo extends Link {
    LinkSetInfo(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        DataStream memorableObject = DataStream.getInstance();
        memorableObject.setModuleName(chain.getProcessObject().get("moduleName").getAsString());
        MonitorHandler.addQueue("SUCCESS declare initial information");
        return true;
    }
}
