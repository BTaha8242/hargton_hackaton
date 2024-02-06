package org.example.hackaton.service;

import org.example.hackaton.entity.FileHistory;
import org.example.hackaton.repository.FileHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileReaderServiceTest {

    @InjectMocks
    private FileReaderService fileReaderService;

    @Mock
    private FileHistoryRepository fileHistoryRepository;

    @Test
    void whenverifyFileNameExist_thenFailWithException_givenFileName() {
        String fileName = "testFile.csv";
        FileHistory existingFile = new FileHistory(); // Remplacez par le type réel de votre entité
        when(fileHistoryRepository.findByFileName(fileName)).thenReturn(Optional.of(existingFile));

        assertThrows(RuntimeException.class, () -> fileReaderService.verifyFileNameExist(fileName));

    }

    @Test
    void whenVerifyFileName_thenReturnFalse_givenFileName() {
        String fileName = "CLT-XXX-0411198.csv";
        assertEquals(false, fileReaderService.isValidFormatName(fileName));
    }

    @Test
    void whenVerifyFileName_thenReturnTrue_givenFileName() {
        String fileName = "CLT-XXX-04111989.csv";
        assertEquals(true, fileReaderService.isValidFormatName(fileName));
    }
}