package org.dsc.diseametry.test;

import java.io.File;

import org.dsc.diseametry.DbContext;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("file:src/main/resources/spring/testContext.xml") })

public abstract class  Neo4jTest extends TestCase {

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

	protected static void deleteDatabase() {
		File file = new File("target/neo4j-db-test");

		recursiveDelete(file);
	}

	@Override
	public void setUp() throws Exception {
		deleteDatabase();
	}

	@Override
	public void tearDown() throws Exception {
		deleteDatabase();
	}
}
