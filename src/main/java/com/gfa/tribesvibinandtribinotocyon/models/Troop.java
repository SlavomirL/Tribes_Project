package com.gfa.tribesvibinandtribinotocyon.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Troop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "kingdom_id")
    private Kingdom kingdom;
}