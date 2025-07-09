package com.fyp.aps;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fyp.aps.service.MailService;

@SpringBootTest
@MockBean(MailService.class)
class ApsApplicationTests {

	@Test
	void contextLoads() {
	}

}
