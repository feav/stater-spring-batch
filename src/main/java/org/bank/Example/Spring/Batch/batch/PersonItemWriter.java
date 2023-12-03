package org.bank.Example.Spring.Batch.batch;
import org.bank.Example.Spring.Batch.entity.Person;
import org.bank.Example.Spring.Batch.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

public class PersonItemWriter implements ItemWriter<Person> {

    private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);
    @Autowired
    private PersonRepository personRepository;

    @Override
    public void write(Chunk<? extends Person> chunk) throws Exception {
        chunk.forEach( person -> {
            Person personSave = personRepository.save(person);
            log.info("Adding Person { " + personSave + " } ");
        } );
    }
}