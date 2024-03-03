package com.colak.springconnectionleaktutorial.controller;

import com.colak.springconnectionleaktutorial.dto.SongDto;
import com.colak.springconnectionleaktutorial.dto.SongWithAlbumDto;
import com.colak.springconnectionleaktutorial.service.SongService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("song")

@AllArgsConstructor
class ConnectionLeakExampleController {
    private final SongService service;

    // http://localhost:8080/song/list?trouble=none
    @GetMapping(value = "list", params = "trouble=none")
    List<SongDto> getAllOk() {
        return service.getAllOk();
    }

    // http://localhost:8080/song/list?trouble=stream

    @GetMapping(value = "list", params = "trouble=stream")
    List<SongDto> getAllStream() {
        return service.getAllStream();
    }


    // http://localhost:8080/song/list?trouble=rawConnection
    // The problem with COPY is that this command is not a part of the SQL standard.
    // That is why you have to use a raw connection of type org.postgresql.core.BaseConnection instead of jdbcTemplate or java.sql.Connection to get the described capabilities.
    // There is no @Transactional annotation wrapping the service method as it would close the database connection too early, before the response is sent to the client.
    // The connection can’t simply be closed in this method as it is used while reading the returned PGCopyInputStream and the reading is happening outside the method.
    @GetMapping(value = "list", params = "trouble=rawConnection")
    ResponseEntity<InputStreamResource> getAllRawConnection() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=songs.csv");
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(service.getAllRawConnection()));
    }


    // http://localhost:8080/song/list?trouble=externalInvocation
    // This code works fine as far as the load is not high and the external service works fast.
    // All opened connections return to the pool and get reused properly.
    // But what will happen if the external service starts to respond slower and the load increases?
    // The balance between connections acquiring and releasing will be shifted.
    // As the whole method is marked by the @Transactional annotation, the acquired connection won’t be released until all network invocations are completed.
    // This dependency is quite dangerous, don’t you think so?
    // If one of the network invocations gets stuck due to some infrastructure issue, the connection will be absent for all this time.
    // As a result, the connection pool can run out of idle connections for the new requests.
    // New requests won’t be processed even if they don’t use the external service.
    // This is essentially the same mechanic that destroys the application in case of a real connection leak.
    @GetMapping(value = "list", params = "trouble=externalInvocation")
    List<SongWithAlbumDto> getAllExternalInvocation() {
        return service.getAllExternalInvocation();
    }
}

