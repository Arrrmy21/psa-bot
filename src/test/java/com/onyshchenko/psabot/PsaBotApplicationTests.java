package com.onyshchenko.psabot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.telegram.telegrambots.meta.TelegramBotsApi;

@SpringBootTest
@TestPropertySource("classpath:application.properties")
class PsaBotApplicationTests {

	@MockBean
	TelegramBotsApi telegramBotsApi;

	@Test
	void contextLoads() {
	}

}
