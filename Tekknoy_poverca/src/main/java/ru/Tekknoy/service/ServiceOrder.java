package ru.Tekknoy.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.Tekknoy.model.Order;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;


@Service
public class ServiceOrder {

    @Autowired
    private CRUD crud;

    @Autowired
    private OperationOrder operationOrder;


    //добовляем модель в бд
    public void create(Order order) {
        crud.create(order);
    }

    //выбираем все модели из бд
    public List<Order> getAll() {
        return crud.getAll();
    }

    //находим нужный модель по ID, изменяем и обновляем бд.
    public Order findByNumberOrder(String numberOrder) {

        return crud.findByNumberOrder(numberOrder);
    }

    //удалить все модели в бд
    public void deleteAll() {
        crud.deleteAll();
    }

    //удалить выбранную модель счета
    public void delete(long id) {
        crud.delete(id);
    }

    //сохранить модель в БД
    public void save(Order order) {
        crud.save(order);
    }


    //создаем директорию и загружаем в эту директорию файл
    public File uploadFile(MultipartFile file) throws IOException {
        return operationOrder.uploadFile(file);
    }

    //определяем какой вид файла и на основе информации внутри файл создаем новый счет
    public void parsingAndCreateOrder(Order order, File file) {
        operationOrder.parsingAndCreateOrder(order, file);
    }


    public int getCountDaysShipment(Order order) throws ParseException {
        return operationOrder.getCountDaysShipment(order);
    }

    public long getMilliseconds(String date) throws ParseException {
        return operationOrder.getMilliseconds(date);
    }

    //сохраняем таблицу в виде файла XLS в директорию
    public void saveFileInXLS(List<Order> order) throws IOException {
        operationOrder.saveFileInXLS(order);
    }
}
