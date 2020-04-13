package com.gazellig.amqpservice.amqp.audit.handledevents;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HandledEventRepository extends CrudRepository<HandledEvent, String> {
}
