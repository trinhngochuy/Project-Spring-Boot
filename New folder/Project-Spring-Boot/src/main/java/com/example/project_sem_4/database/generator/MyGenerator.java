package com.example.project_sem_4.database.generator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.stream.Stream;

public class MyGenerator implements IdentifierGenerator {
    private String prefix = "HN";
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
        String query = "SELECT e.id FROM Booking e";
        Stream<String> ids = session.createQuery(query, String.class).stream();
        Long max = ids.map(o -> o.replace(prefix, "")).mapToLong(Long::parseLong).max().orElse(0L);
        return prefix + (String.format("%d", max + 1));
    }
}
