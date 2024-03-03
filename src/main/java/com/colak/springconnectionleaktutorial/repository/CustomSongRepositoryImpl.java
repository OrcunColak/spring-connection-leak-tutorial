package com.colak.springconnectionleaktutorial.repository;

import com.colak.springconnectionleaktutorial.jpa.SongEntity;
import lombok.AllArgsConstructor;
import org.apache.commons.io.input.ProxyInputStream;
import org.postgresql.copy.CopyManager;
import org.postgresql.copy.CopyOut;
import org.postgresql.copy.PGCopyInputStream;
import org.postgresql.core.BaseConnection;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.stream.Stream;

@Repository
@AllArgsConstructor
class CustomSongRepositoryImpl implements CustomSongRepository {
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Stream<SongEntity> getAllStream() {
        return jdbcTemplate.queryForStream("""
                select id as id
                     , name as name
                     , artist as artist
                     , album_id as albumId
                from songs
                """, new DataClassRowMapper<>(SongEntity.class));
    }

    @Override
    public InputStream getAllCopy() {
        try {
            Connection connection = dataSource.getConnection();
            BaseConnection baseConnection = connection.unwrap(BaseConnection.class);
            CopyManager copyManager = new CopyManager(baseConnection);
            CopyOut copyOut = copyManager.copyOut("COPY songs TO STDOUT (HEADER, DELIMITER ',');");
            return new PGCopyInputStream(copyOut);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Here I used ProxyInputStream from org.apache.commons although any delegating InputStream, including self-written, will do.
    // Now, when Spring Boot invokes close() on the InputStreamResource, the invocation will be propagated to the underlying PGCopyInputStream and PGCopyInputStream, closing the acquired connection.
    public InputStream getAllCopy2() {
        try {
            Connection connection = DataSourceUtils.getConnection(dataSource);
            BaseConnection baseConnection = connection.unwrap(BaseConnection.class);
            CopyManager copyManager = new CopyManager(baseConnection);
            CopyOut copyOut = copyManager.copyOut("COPY songs TO STDOUT (HEADER, DELIMITER ',');");
            return new ProxyInputStream(new PGCopyInputStream(copyOut)) {
                @Override
                public void close() throws IOException {
                    super.close();
                    DataSourceUtils.releaseConnection(connection, dataSource);
                }
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
