package ru.Tekknoy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import ru.Tekknoy.model.DatabaseSequence;
import ru.Tekknoy.model.Order;
import ru.Tekknoy.repositor.OrderRepository;

import java.util.List;
import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component
public class CRUD {
    @Autowired
    private OrderRepository repository;

    private MongoOperations mongoOperations;

    public CRUD() {
    }

    @Autowired
    public CRUD(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }
    //формируем ID
    public long generateSequence(String seqName) {

        DatabaseSequence counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
                new Update().inc("seq", 1), options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;

    }

    //добовляем модель в бд
    public void create(Order order) {
        long id = generateSequence(Order.SEQUENCE_NAME);
        order.setId(id);
        repository.save(order);
    }

    //выбираем все модели из бд
    public List<Order> getAll() {
        return repository.findAll();
    }

    //находим нужный модель по ID, изменяем и обновляем бд.
    public Order findByNumberOrder(String numberOrder) {
        Order order = repository.findByNumberOrder(numberOrder);

        return order;
    }

    //удалить все модели в бд
    public void deleteAll() {
        repository.deleteAll();
    }

    //удалить выбранную модель счета
    public void delete(long id) {
        Order order = repository.findById(id);
        repository.delete(order);
    }

    //сохранить модель в БД.
    public void save(Order order) {
        repository.save(order);
    }
}
