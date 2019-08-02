package com.cdy.demo.framework.jpa;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="MAN")
@Data
public class Man {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private boolean disabled;

}
