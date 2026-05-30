package com.example.bookcatalog.controller;

import com.example.bookcatalog.domain.Publisher;
import com.example.bookcatalog.repository.PublisherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublisherControllerTest {

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private PublisherController publisherController;

    @Test
    void getAllPublishers_shouldReturnList() {
        Publisher p = new Publisher();
        p.setName("Penguin");
        when(publisherRepository.findAll()).thenReturn(List.of(p));

        ResponseEntity<List<Publisher>> response = publisherController.getAllPublishers();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Penguin", response.getBody().get(0).getName());
    }

    @Test
    void getAllPublishers_whenEmpty_shouldReturnEmptyList() {
        when(publisherRepository.findAll()).thenReturn(List.of());

        ResponseEntity<List<Publisher>> response = publisherController.getAllPublishers();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void getPublisherById_whenExists_shouldReturnPublisher() {
        Publisher p = new Publisher();
        p.setId(1L);
        p.setName("Penguin");
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(p));

        ResponseEntity<Publisher> response = publisherController.getPublisherById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Penguin", response.getBody().getName());
    }

    @Test
    void getPublisherById_whenNotExists_shouldReturn404() {
        when(publisherRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Publisher> response = publisherController.getPublisherById(99L);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void createPublisher_shouldReturnSavedPublisher() {
        Publisher p = new Publisher();
        p.setName("Oxford Press");
        p.setAddress("Oxford, UK");
        when(publisherRepository.save(any(Publisher.class))).thenReturn(p);

        ResponseEntity<Publisher> response = publisherController.createPublisher(p);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Oxford Press", response.getBody().getName());
        verify(publisherRepository, times(1)).save(any(Publisher.class));
    }

    @Test
    void updatePublisher_whenExists_shouldReturnUpdated() {
        Publisher existing = new Publisher();
        existing.setId(1L);
        existing.setName("Old Name");
        existing.setAddress("Old Address");

        Publisher details = new Publisher();
        details.setName("New Name");
        details.setAddress("New Address");

        when(publisherRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(publisherRepository.save(existing)).thenReturn(existing);

        ResponseEntity<Publisher> response = publisherController.updatePublisher(1L, details);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("New Name", response.getBody().getName());
        assertEquals("New Address", response.getBody().getAddress());
    }

    @Test
    void updatePublisher_whenNotExists_shouldThrowException() {
        when(publisherRepository.findById(99L)).thenReturn(Optional.empty());

        Publisher details = new Publisher();
        details.setName("Some");

        assertThrows(RuntimeException.class,
                () -> publisherController.updatePublisher(99L, details));
    }

    @Test
    void deletePublisher_shouldReturn204() {
        doNothing().when(publisherRepository).deleteById(1L);

        ResponseEntity<Void> response = publisherController.deletePublisher(1L);

        assertEquals(204, response.getStatusCode().value());
        verify(publisherRepository, times(1)).deleteById(1L);
    }
}
