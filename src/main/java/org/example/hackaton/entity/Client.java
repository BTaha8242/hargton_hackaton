package org.example.hackaton.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "REF_CLIENT")
    private String refClient;
    @Column(name = "CLIENT_NAME")
    private String clientName;
    @Column(name = "MAIL")
    private String mail;

    @OneToMany(fetch = FetchType.EAGER)
    @JsonManagedReference
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Product> products;


}
