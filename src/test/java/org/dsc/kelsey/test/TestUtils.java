package org.dsc.kelsey.test;

import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class TestUtils {

    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(TestUtils.class);

    public static byte[] readResourceAsBytes(String name) {
        InputStream inputStream = TestUtils.class.getClassLoader().getResourceAsStream(name);
        byte[] bytes = new byte[0];

        try {
            bytes = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            Logger.error(e.toString());
            throw new RuntimeException(e);
        }

        return bytes;
    }
}
