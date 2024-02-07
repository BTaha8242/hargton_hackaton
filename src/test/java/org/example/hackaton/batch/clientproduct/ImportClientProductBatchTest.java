package org.example.hackaton.batch.clientproduct;

import jakarta.persistence.EntityManagerFactory;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.example.hackaton.entity.Client;
import org.example.hackaton.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBatchTest
@SpringJUnitConfig(classes = {ImportClientProductBatchConfig.class, ImportClientProductBatchConfigTest.class})
public class ImportClientProductBatchTest {


    private final String WRONG_INPUT = "src/test/resources/csv/wrongtest.csv";
    private final String GOOD_INPUT = "src/test/resources/csv/CLT-XXX-04111989.csv";

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    @Qualifier("importUserJob")
    Job job;

    private JobParameters parameters(String path) {
        val paramsBuilder = new JobParametersBuilder();
        paramsBuilder.addString("inputFile", path);
        return paramsBuilder.toJobParameters();
    }

    @Test
    void given_wrong_input_when_launch_job_then_get_job_instance() throws Exception {
        jobLauncherTestUtils.setJob(job);
        val jobExecution = jobLauncherTestUtils.launchJob(parameters(GOOD_INPUT));
        jobExecution.getStatus();
        // THEN
        Assertions.assertThat(ExitStatus.FAILED.getExitCode().equals(BatchStatus.FAILED));

    }

    @Test
    void given_good_input_when_launch_job_then_get_user_saved() throws Exception {
        clientRepository.deleteAll();
        jobLauncherTestUtils.setJob(job);
        val jobExecution = jobLauncherTestUtils.launchJob(parameters(GOOD_INPUT));
        jobExecution.getStatus();
        Assertions.assertThat(ExitStatus.COMPLETED.getExitCode().equals(BatchStatus.COMPLETED));
        Assertions.assertThat(jobExecution.getJobParameters().getParameter("inputFile").equals(GOOD_INPUT));
        Assertions.assertThat(jobExecution.getJobInstance().getJobName().equals("importUserJob"));
        List<Client> clients = clientRepository.findAll();
        Assertions.assertThat(clients.size() == 1);
    }


}

