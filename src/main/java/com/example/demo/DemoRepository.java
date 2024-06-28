package com.example.demo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;   

@Repository
public class DemoRepository {
    private final JdbcClient jdbcClient;

    public DemoRepository(JdbcTemplate template, JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Song> getAllSongs() {
        return jdbcClient.sql("SELECT * FROM music").query(
                (rs, rowNum) -> new Song(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getString("artist"),
                    rs.getString("album"),
                    rs.getString("genre"),
                    rs.getInt("release_year"),
                    rs.getInt("duration"))
        ).list();
    }

    public void addSong(Song newSong) {
        String sql = "INSERT INTO music (title, artist, album, genre, release_year, duration) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(sql)
                .params(newSong.getTitle(), newSong.getArtist(), newSong.getAlbum(), newSong.getGenre(), newSong.getReleaseYear(), newSong.getDuration())
                .update(keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            newSong.setId(key.longValue());
        }
    }

    public Song getSongById(Long id) {
        return jdbcClient.sql("SELECT * FROM music WHERE id = :id")
                .params(Map.of("id", id))
                .query(
                (rs, rowNum) -> new Song(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("artist"),
                        rs.getString("album"),
                        rs.getString("genre"),
                        rs.getInt("release_year"),
                        rs.getInt("duration"))
        ).single();
    }

    public void deleteSongById(Long id) {
        jdbcClient.sql("DELETE FROM music WHERE id = :id")
                .params(Map.of("id", id))
                .update();
    }
}
