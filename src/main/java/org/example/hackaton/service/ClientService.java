package org.example.hackaton.service;

import lombok.RequiredArgsConstructor;
import org.example.hackaton.entity.Client;
import org.example.hackaton.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    public List<Client> findAll(){
        return clientRepository.findAll();
    }

}
