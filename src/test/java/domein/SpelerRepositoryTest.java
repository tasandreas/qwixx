package domein;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SpelerRepositoryTest {

    @Test
    void constructor_MaaktRepository() {
        SpelerRepository repository = assertDoesNotThrow(SpelerRepository::new);

        assertNotNull(repository);
    }
}
