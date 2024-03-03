package com.colak.springconnectionleaktutorial.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "album")

@Getter
@AllArgsConstructor
@NoArgsConstructor
class AlbumEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private Integer year;
}
