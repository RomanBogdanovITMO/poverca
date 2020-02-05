package ru.Tekknoy.controllers;


import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import ru.Tekknoy.config.ApplicationConfiguration;
import ru.Tekknoy.model.Order;
import ru.Tekknoy.service.ServiceOrder;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;


@Controller
public class OrderControllers {
    private final static Logger logger = Logger.getLogger(OrderControllers.class.getName());
    @Autowired
    private ServiceOrder service;
    @Autowired
    private Order order;
    @Autowired
    private ApplicationConfiguration configuration;

    //создаем счет
    @GetMapping("/order/create")
    public String orderCreate(Model model) {
        model.addAttribute("order", new Order());
        return "OrderCreate";
    }

    //добавляем счет в бд
    @PostMapping("order/save")
    public RedirectView orderSave(@ModelAttribute Order order) {
        service.create(order);
        return new RedirectView("/order/list", true);
    }

    //удаляем весь список счетов
    @PostMapping("/order/deleteAll")
    public RedirectView orderDeleteAll() {
        service.deleteAll();
        return new RedirectView("/order/list", true);
    }

    //показывает весь список счетов
    //обнавляем счета проверяя уловия сроков поверки
    @GetMapping({"/", "/order/list", "/order/update"})
    public String orderList(Model model) throws ParseException {
        //обнавляем счета проверяя условия сроков поверки
        List<Order> orders = service.getAll();
        for (Order order : orders) {
            if (!(order.getStartPoverc().isEmpty())) {
                int days = (int) (service.getMilliseconds(order.getStartPoverc()) / (24 * 60 * 60 * 1000));
                if (days < configuration.getValueDaysForPoverc()) { //5 days
                    order.setStatusPoverce("в работе " + days + "д.");
                } else if (days > configuration.getValueDaysForPoverc()) { //5 days
                    int day = days - configuration.getValueDaysForPoverc(); //5 days
                    order.setStatusPoverce("просрочен на " + day + " д.");
                    order.setColorPoverce("#FF0000");
                } else if (days == configuration.getValueDaysForPoverc()) { // 5 days
                    int day = days - configuration.getValueDaysForPoverc(); //5 days
                    order.setStatusPoverce("срок поверки завершен " + day + " д.");
                    order.setColorPoverce("#FFFF00");
                }
            }
            service.save(order);
        }
        //обновляем счета проверяя сколько осталось дней для отправки груза грузополучателю
        for (Order order : orders) {
            String modificValueResult = order.getDataOfArrival() + 1;


            if (!(modificValueResult.equals("null1")) && !(order.getDataOfArrival().isEmpty())) {
                int days = (int) (service.getMilliseconds(order.getDataOfArrival()) / (24 * 60 * 60 * 1000));

                int days1 = service.getCountDaysShipment(order);
                int result = days1 - days;

                if (result > 1) {
                    order.setDaysForShipment(result + " д.");
                } else if (result == 1) {
                    order.setDaysForShipment(result + " д.");
                    order.setColorShipment("#FFFF00");

                } else if (result == 0 & service.getCountDaysShipment(order) != 0) {
                    order.setDaysForShipment(result + " д.");
                    order.setColorShipment("#FFFF00");
                } else if (result == 0 & service.getCountDaysShipment(order) == 0) {
                    order.setDaysForShipment(configuration.getNotCountDays());// неопределено

                } else if (!modificValueResult.isEmpty() & service.getCountDaysShipment(order) == 0) {
                    order.setDaysForShipment(configuration.getNotCountDays());// неопределено

                } else {
                    order.setDaysForShipment(configuration.getOrderExpired());// просрочено
                    order.setColorShipment("#FF0000");

                }
            } else {
                service.getCountDaysShipment(order);
            }
            service.save(order);
        }
        model.addAttribute("orders", orders);
        return "OrderList";
    }

    //редкатируем счет
    @GetMapping("/edit/{numberOrder}")
    public String orderEdit(@PathVariable("numberOrder") String numberOrder, Model model) {
        Order order = service.findByNumberOrder(numberOrder);
        model.addAttribute("order", order);

        return "OrderEdit";
    }

    //после изменения добавляем счет в бд.
    @PostMapping("/edit/save")
    public RedirectView orderEditSave(@ModelAttribute Order order) {
        if (order.getDataShipment().equals(configuration.getOrderShipment())) { // отгружен
            int idOrder = (int) order.getId();
            service.delete(idOrder);
        } else {
            service.save(order);
        }
        return new RedirectView("/order/list", true);
    }

    //счет отгружен и удаляется из бд.
    @PostMapping("/edit/{id}")
    public RedirectView deletOrder(@PathVariable("id") long id) {
        service.delete(id);
        return new RedirectView("/order/list", true);
    }

    //загружаем файл через web страницу
    @PostMapping("/uploadFile")
    public RedirectView uploadFile(@RequestParam("file") MultipartFile file) throws IOException, InvalidFormatException {
        //service.uploadFile(file);
        File uploadFileResult = service.uploadFile(file);
        service.parsingAndCreateOrder(order, uploadFileResult);

        return new RedirectView("/order/list", true);
    }

    //сохраняем таблицу в виде файла XLS в директорию
    @PostMapping("/order/saveFile")
    public RedirectView saveFileInXLS() throws IOException {
        List<Order> orderList = service.getAll();
        service.saveFileInXLS(orderList);
        return new RedirectView("/order/list", true);
    }
}
