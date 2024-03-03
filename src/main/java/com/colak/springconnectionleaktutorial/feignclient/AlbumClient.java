package com.colak.springconnectionleaktutorial.feignclient;

import com.colak.springconnectionleaktutorial.dto.AlbumDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "ApiClient",
        url = "${album.client.url}"
)
public interface AlbumClient {

    @GetMapping("/album/{albumId}")
    AlbumDto getById(@PathVariable UUID albumId);
}

