package com.dhabits.ss.demo.domain.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResourceObjectEntity {

    @Id
    private Integer id;
    private String value;
    private String path;


}
