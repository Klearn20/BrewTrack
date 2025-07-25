package com.Brew_Track.Cafe.Brew_Track.POJO;

import java.io.Serializable;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

@NamedQuery(
    name = "Category.getAllCategory",
    query = "SELECT c FROM Category c WHERE c.id IN (SELECT p.category.id FROM Product p WHERE p.status='true')"
)


@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "category")

public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Integer id;


    @Column(name = "name")
    private String name;

}
