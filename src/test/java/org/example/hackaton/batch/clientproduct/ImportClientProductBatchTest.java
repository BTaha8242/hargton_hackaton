package org.example.hackaton.batch.clientproduct;

import lombok.val;
import org.assertj.core.api.Assertions;
import org.example.hackaton.entity.Client;
import org.example.hackaton.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBatchTest
@SpringJUnitConfig(classes = {ImportClientProductBatchTest.class, ImportClientProductBatchConfigTest.class})
public class ImportClientProductBatchTest {


    private final String WRONG_INPUT = "src/test/resources/csv/wrongtest.csv";
    private final String GOOD_INPUT = "src/test/resources/csv/CLT-XXX-04111989.csv";

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private ClientRepository clientRepository;

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
        val jobExecution = jobLauncherTestUtils.launchJob(parameters(WRONG_INPUT));
        Assertions.assertThat(ExitStatus.COMPLETED.getExitCode().equals(jobExecution.getStatus().COMPLETED));
        Assertions.assertThat(jobExecution.getJobParameters().getParameter("inputFile").equals(WRONG_INPUT));
        Assertions.assertThat(jobExecution.getJobInstance().getJobName().equals("importUserJob"));
    }

    @Test
    void given_good_input_when_launch_job_then_get_user_saved() throws Exception {
        jobLauncherTestUtils.setJob(job);
        val jobExecution = jobLauncherTestUtils.launchJob(parameters(GOOD_INPUT));
        Assertions.assertThat(ExitStatus.COMPLETED.getExitCode().equals(jobExecution.getStatus().COMPLETED));
        Assertions.assertThat(jobExecution.getJobParameters().getParameter("inputFile").equals(GOOD_INPUT));
        Assertions.assertThat(jobExecution.getJobInstance().getJobName().equals("importUserJob"));
        List<Client> clients = clientRepository.findAll();
        Assertions.assertThat(clients.size() == 1);
        Assertions.assertThat(clients.get(0).getClientName() == "Jane");
    }


}

