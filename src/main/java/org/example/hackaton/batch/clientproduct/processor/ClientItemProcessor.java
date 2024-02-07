package org.example.hackaton.batch.clientproduct.processor;

import lombok.extern.slf4j.Slf4j;
import org.example.hackaton.dto.ClientFileDto;
import org.example.hackaton.entity.Client;
import org.example.hackaton.entity.Product;
import org.example.hackaton.repository.ClientRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class ClientItemProcessor implements ItemProcessor<ClientFileDto, Client> {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Client process(ClientFileDto item) throws Exception {
        Optional<Client> clientOptional = clientRepository.findByRef(item.getRefClient());
        Client client;
        List<Product> products = new ArrayList<>();
        if (clientOptional.isPresent()) {
            products = clientOptional.get().getProducts();
            client = clientOptional.get();
        } else {
            client = Client.builder().refClient(item.getRefClient()).clientName(item.getClientName()).mail(item.getMail()).build();
        }
        Product productItem;
        try {
            productItem = Product.builder().productName(item.getProductName()).productQuantity(Integer.valueOf(item.getProductQuantity())).productPrice(Float.valueOf(item.getProductPrice())).build();
            products.add(productItem);
            client.setProducts(products);
            return client;
        } catch (Exception exception) {
            throw new Exception("error with creation productItem");
        }

    }
}
