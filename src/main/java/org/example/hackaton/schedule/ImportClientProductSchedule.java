package org.example.hackaton.schedule;

import lombok.RequiredArgsConstructor;
import org.example.hackaton.service.FileReaderService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Calendar;


@Component
@RequiredArgsConstructor
public class ImportClientProductSchedule {

    private final FileReaderService fileReaderService;
    private final JobLauncher jobLauncher;
    private final Job importUserJob;

    @Scheduled(fixedRate = 2000)
    public void importClientProduct() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        String directoryPath = "src/main/resources/csv/";
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        if (files != null && files.length > 0) {
            File file = files[0];
            if (fileReaderService.isValidFormatName(file.getName())) {
                fileReaderService.verifyFileNameExist(file.getName());
                JobParameters jobParameters = new JobParametersBuilder()
                        .addDate("timestamp", Calendar.getInstance().getTime())
                        .addString("inputFile", file.getPath())
                        .toJobParameters();
                jobLauncher.run(importUserJob, jobParameters);
                fileReaderService.saveFileNameHistory(file.getName());
            }
            file.delete();
        }
    }
}
