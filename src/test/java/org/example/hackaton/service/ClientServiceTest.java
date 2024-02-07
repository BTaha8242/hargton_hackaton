package org.example.hackaton.service;

import org.example.hackaton.entity.Client;
import org.example.hackaton.entity.Product;
import org.example.hackaton.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    @Test
    void whenFindAll_thenRetrieveAllClients() {
        Product product = new Product();
        List<Product> products = Arrays.asList(product);
        Client client = new Client(1L,"redtClient", "firtname1", "lastname1",products);
        List<Client> clients = Arrays.asList(client);
        when(clientRepository.findAll()).thenReturn(clients);
        List<Client> result = clientService.findAll();
        assertAll("Grouped Assertions of findAll Acc",
                () -> assertNotNull(result),
                () -> assertEquals(clients.size(), result.size()),
                () -> assertEquals(clients.get(0).getClientName(), result.get(0).getClientName()),
                () -> assertEquals(clients.get(0).getRefClient(), result.get(0).getRefClient()),
                () -> assertEquals(clients.get(0).getMail(), result.get(0).getMail()));
    }
}