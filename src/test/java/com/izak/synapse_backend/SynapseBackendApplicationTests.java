package com.izak.synapse_backend;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import com.izak.synapse_backend.entities.Users;

class SynapseBackendApplicationTests {

	@Test
	void entityCanBeInstantiatedWithoutArguments() {
		Users user = new Users();
		user.setUsername("demo");

		assertNotNull(user);
	}

}
