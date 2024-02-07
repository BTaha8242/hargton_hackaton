package org.example.hackaton.batch.clientproduct.write;

import org.example.hackaton.entity.Client;
import org.example.hackaton.entity.Product;
import org.example.hackaton.repository.ClientRepository;
import org.example.hackaton.repository.ProductRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientItemWriter implements ItemWriter<Client> {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProductRepository productRepository;


    @Override
    public void write(Chunk<? extends Client> chunk) {
        for (Client client : chunk) {
            client.getProducts().forEach(this::saveProduct);
            clientRepository.saveAndFlush(client);
        }
    }

    private Product saveProduct(final Product product){
        return productRepository.saveAndFlush(product);
    }
}