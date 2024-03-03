package com.colak.springconnectionleaktutorial.mapper;

import com.colak.springconnectionleaktutorial.dto.AlbumDto;
import com.colak.springconnectionleaktutorial.dto.SongDto;
import com.colak.springconnectionleaktutorial.dto.SongWithAlbumDto;
import com.colak.springconnectionleaktutorial.jpa.SongEntity;
import org.springframework.stereotype.Component;

@Component
public class SongMapper {

    public SongDto toDto(SongEntity entity) {
        return new SongDto(
                entity.getId(),
                entity.getName(),
                entity.getArtist(),
                entity.getAlbumId()
        );
    }

    public SongWithAlbumDto toDtoWithAlbum(SongEntity entity, AlbumDto albumDto) {
        return new SongWithAlbumDto(
                entity.getId(),
                entity.getName(),
                entity.getArtist(),
                albumDto
        );
    }
}
