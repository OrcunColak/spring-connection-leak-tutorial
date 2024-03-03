package com.colak.springconnectionleaktutorial.repository;

import com.colak.springconnectionleaktutorial.jpa.SongEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SongRepository extends CustomSongRepository, JpaRepository<SongEntity, UUID> {
}
