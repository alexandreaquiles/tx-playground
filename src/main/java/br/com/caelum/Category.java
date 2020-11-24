package br.com.caelum;

import javax.persistence.*;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Deprecated
    protected Category() { }

    public Category(String name) {
        this.name = name;
    }
}
