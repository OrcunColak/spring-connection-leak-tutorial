package com.colak.springconnectionleaktutorial.dto;

import java.util.UUID;


public record SongWithAlbumDto(UUID id, String name, String artist, AlbumDto albumDto) {
}
