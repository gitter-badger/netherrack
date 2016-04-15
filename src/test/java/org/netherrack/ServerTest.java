package org.netherrack;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ServerTest extends TestCase{

    public ServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(ServerTest.class);
    }

    public void testServer() {
        assertTrue( true );
    }
}
