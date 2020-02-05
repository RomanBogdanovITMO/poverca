package ru.Tekknoy.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.Tekknoy.config.ApplicationConfiguration;
import ru.Tekknoy.model.Order;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

@Component
public class OperationOrder {
    private final static Logger logger = Logger.getLogger(ServiceOrder.class.getName());

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private ApplicationConfiguration configuration;

    @Autowired
    private CRUD crud;

    public OperationOrder() {

    }

    //создаем директорию и загружаем в эту директорию файл
    public File uploadFile(MultipartFile file) throws IOException {
        File file1 = null;
        if (file != null) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFileName));

            file1 = new File(uploadPath + "/" + resultFileName);

        }
        return file1;
    }

    //определяем какой вид файла и на основе информации внутри файл создаем новый счет
    public void parsingAndCreateOrder(Order order, File file) {
        String parserFileName = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        if (parserFileName.equals(configuration.getResultParserNameXLSX())) { // xlsx
            try (OPCPackage pkg = OPCPackage.open(file)) {
                XSSFWorkbook wb1 = new XSSFWorkbook(pkg);
                parsingResultWithXLSX(order, wb1);
            } catch (Exception e) {
                logger.info("info1: " + e);
            }
        } else if (parserFileName.equals(configuration.getResultParserNameXLS())) {  //xls
            try (POIFSFileSystem fs = new POIFSFileSystem(file)) {
                Workbook wb = new HSSFWorkbook(fs.getRoot(), true);
                parsingResultWithXLS(order, wb);
            } catch (Exception e) {
                logger.info("info2: " + e);
            }
        } else {
            logger.info("Ошибка - не верный формат файла");
        }
    }

    //обработка файла .xlsx
    public void parsingResultWithXLSX(Order order, XSSFWorkbook workbook) throws ParseException {

        DataFormatter formatter = new DataFormatter();
        Map<String, String> mapForCreateNewOrder = new HashMap<>();
        int sizeRow = 0;
        int sizeCell = 0;

        for (Row row : workbook.getSheetAt(0)) {
            sizeRow = row.getRowNum();
        }


        for (Cell cell : workbook.getSheetAt(0).getRow(0)) {
            sizeCell = cell.getColumnIndex();
        }


        int numberRow = 1;
        for (int x = 0; x <= sizeRow; x++) {
            if (numberRow <= sizeRow) {
                for (int y = 0; y <= sizeCell; y++) {
                    String valueCell = formatter.formatCellValue(workbook.getSheetAt(0).getRow(0).getCell(y));
                    String valueCell1 = formatter.formatCellValue(workbook.getSheetAt(0).getRow(numberRow).getCell(y));
                    mapForCreateNewOrder.put(valueCell, valueCell1);


                }
                logger.info("mapForCreateNewOrder: " + mapForCreateNewOrder.toString());
                addValueInOrder(order, mapForCreateNewOrder);
            }
            numberRow++;
            logger.info("_________________________________");
        }

    }

    //обработка файла .xls
    public void parsingResultWithXLS(Order order, Workbook workbook) throws ParseException {
        DataFormatter formatter = new DataFormatter();
        Map<String, String> mapForCreateNewOrder = new HashMap<>();
        int sizeRow = 0;
        int sizeCell = 0;


        for (Row row : workbook.getSheetAt(0)) {
            sizeRow = row.getRowNum();
        }


        for (Cell cell : workbook.getSheetAt(0).getRow(0)) {
            sizeCell = cell.getColumnIndex();
        }


        int numberRow = 1;
        for (int x = 0; x <= sizeRow; x++) {
            if (numberRow <= sizeRow) {
                for (int y = 0; y <= sizeCell; y++) {
                    String valueCellKey = formatter.formatCellValue(workbook.getSheetAt(0).getRow(0).getCell(y));
                    String valueCell = formatter.formatCellValue(workbook.getSheetAt(0).getRow(numberRow).getCell(y));
                    mapForCreateNewOrder.put(valueCellKey, valueCell);

                }
                logger.info("mapForCreateNewOrder: " + mapForCreateNewOrder.toString());
                addValueInOrder(order, mapForCreateNewOrder);

            }
            numberRow++;
            logger.info("_________________________________");
        }

    }

    //добовляем в счет информацию из мапы
    public void addValueInOrder(Order order, Map<String, String> mapValuesFromFile) throws ParseException {
        for (Map.Entry<String, String> entry : mapValuesFromFile.entrySet()) {
            getValueInOrder(entry.getKey(), entry.getValue(), order);

        }

        if ((order.getDateOfDelivery().isEmpty()) | (order.getDataOfArrival().isEmpty())) {
            order.setDaysForShipment(configuration.getNotCountDays()); //неопределено
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(configuration.getFormatDateResult()); //dd.MM.yyyy
            Date date = sdf.parse(order.getDataOfArrival());
            Date date1 = sdf.parse(order.getDateOfDelivery());
            long milliseconds = date1.getTime() - date.getTime();
            int days = (int) (milliseconds / (24 * 60 * 60 * 1000));
            order.setDaysForShipment(days + "");
        }
        List<Order> orderListBefore = crud.getAll();
        boolean flag = true;
        if (orderListBefore.isEmpty()) {
            crud.create(order);
        }
        List<Order> orderListAfter = crud.getAll();
        for (Order order1 : orderListAfter) {
            if (order1.getNumberOrder().equals(order.getNumberOrder())) {
                flag = false;
            }
        }
        if (flag == true) {
            crud.create(order);
        }
    }

    //получаем значение для счета
    public void getValueInOrder(String key, String keyValue, Order order) throws ParseException {
        if (key.equals(configuration.getInfoByOrder())) { //По счету
            byte[] bytesValue = keyValue.getBytes();
            if (bytesValue.length > 5) {
                char[] charsKeyValue = keyValue.toCharArray();
                String resultValue = keyValue.substring(keyValue.indexOf(""), keyValue.indexOf(charsKeyValue[5]));
                order.setNumberOrder(resultValue);
            } else {
                order.setNumberOrder(keyValue);
            }

        } else if (key.equals(configuration.getInfoByPoverca())) { //Сдан в поверку
            if (keyValue.isEmpty()) {
                order.setStartPoverc(keyValue);
            } else {

                order.setStartPoverc(getDate(keyValue));
            }

        } else if (key.equals(configuration.getInfoByDataShipment())) { //Дата отгрузки
            if (keyValue.isEmpty()) {
                order.setDataShipment(keyValue);
            } else {

                order.setDataShipment(getDate(keyValue));
            }
        } else if (key.equals(configuration.getInfoByAcknoledg())) { // Дата поступления
            if (keyValue.isEmpty()) {
                order.setDataOfArrival(keyValue);
            } else {

                order.setDataOfArrival(getDate(keyValue));
            }
        } else if (key.equals(configuration.getInfoByDelivery())) { // Дата поставки
            if (keyValue.isEmpty()) {
                order.setDateOfDelivery(keyValue);
            } else {
                order.setDateOfDelivery(getDate(keyValue));
            }
        } else {
            logger.info("данный стлобец не используется в приложении: " + key);

        }
    }

    // получаем дату в формате dd.MM.yyyy
    public String getDate(String fomatDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(configuration.getFormatDateBefore()); // dd/MM/yyyy
        Date date = format.parse(fomatDate);
        SimpleDateFormat formatResult = new SimpleDateFormat();
        formatResult.applyPattern(configuration.getFormatDateResult()); // dd.MM.yyyy
        String ResultDateFormat = formatResult.format(date);
        return ResultDateFormat;
    }

    public int getCountDaysShipment(Order order) throws ParseException {
        int days = 0;
        if ((order.getDateOfDelivery().isEmpty()) | (order.getDataOfArrival().isEmpty())) {
            order.setDaysForShipment(configuration.getNotCountDays()); // неопределено
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(configuration.getFormatDateResult()); // dd.MM.yyyy
            Date date = sdf.parse(order.getDataOfArrival());
            Date date1 = sdf.parse(order.getDateOfDelivery());
            long milliseconds = date1.getTime() - date.getTime();
            days = (int) (milliseconds / (24 * 60 * 60 * 1000));
        }

        return days;
    }

    public long getMilliseconds(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(configuration.getFormatDateResult()); //dd.MM.yyyy
        Date firstDate = sdf.parse(date);
        Date lastDate = sdf.parse(sdf.format(Calendar.getInstance().getTime()));
        long milliseconds = lastDate.getTime() - firstDate.getTime();


        return milliseconds;
    }

    //сохраняем таблицу в виде файла XLS в директорию
    public void saveFileInXLS(List<Order> order) throws IOException {

        Document html = Jsoup.parse(new File(configuration.getFileHTMLForParser()), configuration.getCharsetName()); //fileHTML, ISO-8859-1
        String[] strings = html.getElementsByTag(configuration.getTagName()).html().split("\n");
        //собираем в массив названия колонок их html
        List<String> htmlValue = Arrays.asList(strings);

        Row row;
        Cell cell = null;
        boolean flag = true;

        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("info");
        //Формируем документ из списка счетов
        for (int x = 0; x < order.size(); x++) {
            if (flag == true) {
                row = sheet.createRow(x);
                //заполняем название каждой колонки из массива с названиями html
                for (int x1 = 0; x1 < htmlValue.size(); x1++) {
                    if (htmlValue.get(x1).equals(configuration.getNameEdit())) {  //edit
                        flag = false;
                        break;
                    }
                    cell = row.createCell(x1);
                    cell.setCellValue(htmlValue.get(x1));
                }
                //смещаем курсор на строку ниже и заполняем строку данными из первого счета
                int v = x + 1;
                row = sheet.createRow(v);
                result(order.get(x), cell, row);

            } else if (flag == false) {
                //смещаем курсор на строку ниже и заполняем все остальные счета
                int value = x + 1;
                row = sheet.createRow(value);
                result(order.get(x), cell, row);
            }

        }

        String resultUuidFileName = UUID.randomUUID().toString();
        FileOutputStream fos = new FileOutputStream(uploadPath + "/" + resultUuidFileName + ".xls");
        wb.write(fos);
        fos.close();

    }

    //результат готового XLS файла
    public void result(Order order, Cell cell, Row row) {

        String[] strr = order.toString().trim().split(",");
        List<String> list = Arrays.asList(strr);

        for (int i = 0; i < list.size(); i++) {
            cell = row.createCell(i);
            cell.setCellValue(list.get(i));
        }

    }

}
