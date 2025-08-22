package com.htsc.transaction.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {

		int q = 2;
		int w = 3;
		System.out.println(q*w);
	}

}
