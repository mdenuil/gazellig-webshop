package com.gazellig.amqpservice.amqp.jackson;

import java.util.Arrays;
import java.util.Set;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gazellig.amqpservice.amqp.events.AuditableEvent;
import com.google.common.collect.Sets;

/**
 * EventSubtypeModule provides a template for registering Subtypes of {@link AuditableEvent} classes to be
 * deserialized.
 *
 * @param <T> subtype of {@link AuditableEvent} where all to register subtypes should be based off.
 */
public class EventSubtypeModule<T extends AuditableEvent> extends SimpleModule {
    private final Set<Class<?>> registeredSubtypes;
    private final Class<T> typeId;

    public EventSubtypeModule(Class<T> typeId) {
        this.typeId = typeId;
        registeredSubtypes = Sets.newHashSet();
    }

    public Set<Class<?>> getRegisteredSubtypes() {
        return registeredSubtypes;
    }

    @Override
    public Object getTypeId() {
        return typeId;
    }

    @Override
    public SimpleModule registerSubtypes(NamedType... subtypes) {
        Arrays.stream(subtypes).forEach(namedType -> {
            if (namedType.getType().isAssignableFrom(typeId)) {
                throw new IllegalStateException("NamedType types have to be a supertype of base type");
            }
            registeredSubtypes.add(namedType.getType());
        });
        return super.registerSubtypes(subtypes);
    }

}
