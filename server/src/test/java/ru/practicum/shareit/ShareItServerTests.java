package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest()
@ActiveProfiles(profiles = {"test"})
class ShareItServerTests {

	@Test
	void contextLoads() {
	}

}
