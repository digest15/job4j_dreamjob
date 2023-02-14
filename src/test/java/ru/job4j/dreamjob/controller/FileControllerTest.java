package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.service.FileService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileControllerTest {

    private FileService fileService;

    private FileController fileController;

    @BeforeEach
    public void initServices() {
        fileService = mock(FileService.class);
        fileController = new FileController(fileService);
    }

    @Test
    public void whenGetFileById() {
        var content = new byte[]{1, 2, 3};
        var fileDto = new FileDto("test.txt", content);
        when(fileService.getFileById(any(Integer.class))).thenReturn(Optional.of(fileDto));

        ResponseEntity<?> response = fileController.getById(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(content);
    }


}