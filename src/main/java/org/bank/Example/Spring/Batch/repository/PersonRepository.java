package org.bank.Example.Spring.Batch.repository;

import org.bank.Example.Spring.Batch.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person,Long> {
}
