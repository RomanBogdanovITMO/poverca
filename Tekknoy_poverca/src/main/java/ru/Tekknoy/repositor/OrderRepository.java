package ru.Tekknoy.repositor;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.Tekknoy.model.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order, Integer> {
    public Order findByNumberOrder(String numberOrder);

    public Order findById(long id);


}

