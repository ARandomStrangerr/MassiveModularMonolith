package chain.treasury_public_finance.start_module;

import chain.Chain;
import chain.Link;
import memorable.TreasuryPublicFinance;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

class LinkReadStructure extends Link {
    LinkReadStructure(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        HashMap<String, LinkedList<String>> apiStructure = new HashMap<>();
        String excelPath = chain.getProcessObject().get("excelFile").getAsString();
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(new File(excelPath));
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        Sheet sheet = workbook.getSheetAt(1);
        DataFormatter formatter = new DataFormatter();
        for (Row row:sheet){
            int i = 0;
            String formName = null;
            LinkedList<String> param = new LinkedList<>();
            for (Cell cell : row){
                switch (i) {
                    case 0:
                        formName = formatter.formatCellValue(cell);
                    default:
                        param.add(formatter.formatCellValue(cell));
                }
                i++;
            }
            apiStructure.put(formName, param);
        }
        try {
            workbook.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        TreasuryPublicFinance.getInstance().apiStructure = apiStructure;
        return true;
    }
}
