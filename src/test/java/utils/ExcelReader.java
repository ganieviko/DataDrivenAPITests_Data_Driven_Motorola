package utils;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelReader {
    private final Workbook workbook;
    private final Sheet sheet;
    private final DataFormatter dataFormatter;

    public ExcelReader(String pathname) throws IOException {
        File f = new File(pathname);
        workbook = WorkbookFactory.create(f);
        sheet = workbook.getSheetAt(0);
        dataFormatter = new DataFormatter();
    }

    public Map<String, String> getMap() throws IOException {
        int numberOfRows = sheet.getPhysicalNumberOfRows();
        Map<String, String> map = new HashMap<>();

        for (int i = 0; i < numberOfRows; i++) {
            Row row = sheet.getRow(i);
            String key = row.getCell(0).toString();
            String value = row.getCell(1).toString();
            map.put(key, value);
        }
        return map;
    }

    public List<List<String>> getLists() throws IOException {
        List<List<String>> listOfLists = new ArrayList<>();
        int numberOfRows = sheet.getPhysicalNumberOfRows();

        for (int i = 0; i < numberOfRows; i++) {
            List<String> list = new ArrayList<>();

            Row row = sheet.getRow(i);
            int numberOfCells = row.getPhysicalNumberOfCells();
            for (int j = 0; j < numberOfCells; j++) {
                Cell cell = row.getCell(j);
                list.add(cell.getStringCellValue());
            }

            listOfLists.add(list);
        }

        return listOfLists;
    }

    public List<Map<String, String>> getListOfMaps() throws IOException {
        List<String> titles = new ArrayList<String>();
        Row titleRow = sheet.getRow(0); // reading the first row
        for (int i = 0; i < titleRow.getPhysicalNumberOfCells(); i++) {
            Cell titleCell = titleRow.getCell(i);
            titles.add(titleCell.toString());
        }

        List<Map<String, String>> listOfMaps = new ArrayList<>();
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) { // staring from second row
            Map<String, String> map = new HashMap<>();

            Row row = sheet.getRow(i);
            for (int j = 0; j < titles.size(); j++) {
                String key = titles.get(j);
                String value = null;
                if(row.getCell(j) != null) {
                    value = dataFormatter.formatCellValue(row.getCell(j));
                    if(value.equals("\"\"")) {
                        value = "";
                    }
                }
                map.put(key, value);
            }

            // save the map inside a list
            listOfMaps.add(map);
        }

        return listOfMaps;
    }
}
