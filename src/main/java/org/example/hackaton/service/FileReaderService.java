package org.example.hackaton.service;

import lombok.RequiredArgsConstructor;
import org.example.hackaton.entity.FileHistory;
import org.example.hackaton.repository.FileHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class FileReaderService {


    private final FileHistoryRepository fileHistoryRepository;

    public void verifyFileNameExist(String fileName) {
        Optional<FileHistory> existingFile = fileHistoryRepository.findByFileName(fileName);

        if (existingFile.isPresent()) {
            throw new RuntimeException("File with the same name already exists");
        }
    }

    public boolean isValidFormatName(String fileName) {
        String regex = "^CLT-[A-Za-z0-9]{3}-(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])\\d{4}\\.csv$";
        return validateString(fileName, regex);
    }

    private boolean validateString(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public void saveFileNameHistory(String fileName) {
        FileHistory fileHistory = FileHistory.builder()
                .createdAt(new Date())
                .fileName(fileName)
                .build();
        fileHistoryRepository.save(fileHistory);
    }

}
