package org.bank.Example.Spring.Batch.batch;

import org.bank.Example.Spring.Batch.entity.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
    ClassPathResource inputCsv = new ClassPathResource("sample-data.csv");

    /**
     * Ce Beans permet de changer le contenu qui se trouve dans un fichier nommé sample-data.csv
     * @return
     */
     @Bean
     FlatFileItemReader<Person> reader(){
      return new FlatFileItemReaderBuilder<Person>()
              .name("personItemReader")
              .resource(this.inputCsv)
              .linesToSkip(1) // Commencer à lire à partir de la 2ème ligne
              .delimited()
              .names("firstname","lastname","sexe","age")
              .targetType(Person.class)
              .build();
     }

    /**
     * Permet d'éffectuer un traitement sur les données qui sont lues par le reader
     * @return
     */
    @Bean(name = "batchUpperCasePersonName")
     public PersonItemProcessor processor(){
       return new PersonItemProcessor();
     }

    /**
     *
     * @param dataSource
     * Ce Bean permet d'insérer le contenu qui a été traitées par le process dans la base de données
     * @return
     */
     @Bean(name = "batchUpdatePersonDB")
     public ItemWriter<Person> writer(DataSource dataSource){
        return new PersonItemWriter();
     }

    /**
     *
     * @param jobRepository
     * @param transactionManager
     * @param processor
     * @param writer
     * @param reader
     *
     * Step représente la premiere étape de traitement qui va traiter les données
     * Ici le traitement consite à mettre en majuscule le nom et prénom des champs
     * Et a chaque fois on ne traite que 10 elements
     * @return
     */
    @Bean
    protected Step step1(JobRepository jobRepository,PlatformTransactionManager transactionManager,
                         @Qualifier("batchUpperCasePersonName") ItemProcessor<Person,Person> processor,
                         @Qualifier("batchUpdatePersonDB") ItemWriter<Person> writer,
                         FlatFileItemReader<Person> reader) {
      return new StepBuilder("step1", jobRepository)
              .<Person, Person> chunk(2, transactionManager)
             .reader(reader)
             .processor(processor)
             .writer(writer)
             .build();
    }

    /**
     * Permet de crééer un Job qui va éxécuter un nombre prédéfini d'étapes
     * @param jobRepository
     * @param step1
     * @return
     */
    @Bean(name = "importUserJob")
    public Job importUserJob(JobRepository jobRepository, Step step1) {
       return new JobBuilder("importUserJob", jobRepository)
       .start(step1)
       .build();
    }
    @Bean(name = "transactionManager")
    public PlatformTransactionManager getTransactionManager() {
     return new ResourcelessTransactionManager();
    }

    /**
     * permet de persister les différentes informations sur le batch
     * qui permettent de gérer de maniere séraine les importations
     * @param dataSource
     * @param platformTransactionManager
     * @return
     * @throws Exception
     */
    @Bean(name = "jobRepository")
    public JobRepository getJobRepository(DataSource dataSource, PlatformTransactionManager platformTransactionManager) throws Exception {
     JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
     factory.setDataSource(dataSource);
     factory.setTransactionManager(platformTransactionManager);
     factory.afterPropertiesSet();
     return factory.getObject();
    }


}
