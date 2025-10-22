package bablr.chat.infrastructure.persistence.sqlite;

import bablr.chat.common.Discoverable;
import bablr.chat.common.Initializable;
import bablr.chat.domain.repository.ParticipantRepository;

import java.sql.SQLException;

@Discoverable
public class ParticipantRepositorySqliteImpl extends BaseRepositorySqliteImpl implements Initializable,
                                                                                         ParticipantRepository {
    @Override
    public void initialize() {
        try (var connection = connection(); var statement = connection.createStatement()) {
            statement.execute("""
                                       CREATE TABLE IF NOT EXISTS participant (
                                           id UUID PRIMARY KEY,
                                           chat_id UUID NOT NULL,
                                           created TIMESTAMP NOT NULL,
                                           updated TIMESTAMP NOT NULL,
                                           FOREIGN KEY (chat_id) REFERENCES chat(id) ON DELETE CASCADE
                                      )
                                      """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
