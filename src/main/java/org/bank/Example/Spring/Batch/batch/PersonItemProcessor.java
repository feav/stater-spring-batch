package org.bank.Example.Spring.Batch.batch;

import lombok.NoArgsConstructor;
import org.bank.Example.Spring.Batch.entity.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

@NoArgsConstructor
public class PersonItemProcessor implements ItemProcessor<Person,Person> {
    private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);
    @Override
    public Person process(Person person) throws Exception {
        final String firstName = person.getFirstName().toUpperCase();
        final String lastName = person.getLastName().toUpperCase();
        final Person transformPerson = Person.builder()
                .firstName(firstName)
                .lastName(lastName)
                .age(person.getAge())
                .sexe(person.getSexe())
                .build();
        log.info("Converting ( " + person + " ) into ( " +transformPerson + " )");
        return transformPerson;
    }
}
