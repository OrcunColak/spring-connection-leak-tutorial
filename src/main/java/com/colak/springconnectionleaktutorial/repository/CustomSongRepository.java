package com.colak.springconnectionleaktutorial.repository;

import com.colak.springconnectionleaktutorial.jpa.SongEntity;

import java.io.InputStream;
import java.util.stream.Stream;

public interface CustomSongRepository {

    Stream<SongEntity> getAllStream();

    InputStream getAllCopy();
}
