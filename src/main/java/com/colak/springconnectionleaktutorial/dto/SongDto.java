package com.colak.springconnectionleaktutorial.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class SongDto {
    private final UUID id;
    private final String name;
    private final String artist;
    private final UUID albumId;
}
