package org.dsc.deseametry.test;

import java.io.File;

import org.dsc.deseametry.DbContext;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("file:src/main/resources/spring/testContext.xml") })

public class Neo4jTest {

	@Autowired
	DbContext dbContext;

	private static void recursiveDelete(File file) {
		// to end the recursive loop
		if (!file.exists())
			return;

		// if directory, go inside and call recursively
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				// call recursively
				recursiveDelete(f);
			}
		}
		// call delete to delete files and empty directory
		file.delete();
	}

	private static void deleteDatabase() {
		File file = new File("target/neo4j-db-test");

		recursiveDelete(file);
	}

	public void setUp() {
		deleteDatabase();
	}

	public void tearDown() {
		deleteDatabase();
	}
}
