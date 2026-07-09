package com.izak.synapse_backend;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.izak.synapse_backend.entities.Users;

@SpringBootTest
class SynapseBackendApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void entityCanBeInstantiatedWithoutArguments() {
		Users user = new Users();
		user.setUsername("demo");

		assertNotNull(user);
	}

}
