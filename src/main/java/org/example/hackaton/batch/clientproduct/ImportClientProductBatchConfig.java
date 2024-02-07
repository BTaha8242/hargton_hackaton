package org.example.hackaton.batch.clientproduct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.hackaton.batch.clientproduct.processor.ClientItemProcessor;
import org.example.hackaton.dto.ClientFileDto;
import org.example.hackaton.entity.Client;
import org.example.hackaton.repository.ClientRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
public class ImportClientProductBatchConfig {

    private final JobRepository jobRepository;
    private final ClientRepository clientRepository;
    private final PlatformTransactionManager transactionManager;


    @Bean
    public JobLauncher jobLauncher() throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<ClientFileDto> clientReader(@Value("#{jobParameters['inputFile']}") final String filePath) {
        FlatFileItemReader<ClientFileDto> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource(filePath));
        itemReader.setName("csv-reader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<ClientFileDto> lineMapper() {

        DefaultLineMapper<ClientFileDto> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("refClient", "clientName", "mail", "productName", "productPrice", "productQuantity");
        BeanWrapperFieldSetMapper<ClientFileDto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(ClientFileDto.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    @Bean
    public ClientItemProcessor clientProcessor() {
        return new ClientItemProcessor();
    }

    @Bean
    public RepositoryItemWriter<Client> clientWriter() {
        RepositoryItemWriter<Client> writer = new RepositoryItemWriter<>();
        writer.setRepository(clientRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step step() {
        return new StepBuilder("step with reader writer processor", jobRepository)
                .<ClientFileDto, Client>chunk(5, transactionManager)
                .reader(clientReader(null))
                .processor(clientProcessor())
                .writer(clientWriter())
                .build();
    }

    @Bean("importUserJob")
    public Job importUserJob() throws Exception {
            return new JobBuilder("importUserJob", jobRepository)
                    .flow(step()).on("FAILED").fail()
                    .end()
                    .build();

    }
}