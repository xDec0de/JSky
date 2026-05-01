package net.codersky.jsky.test.cli;

import net.codersky.jsky.cli.CLICommand;
import net.codersky.jsky.cli.CLICommandPool;
import net.codersky.jsky.cli.JCLI;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestJCLI {

    /*
     - Registration
     */

    @Test
    public void testRegisterConsumer() {
        final JCLI cli = new JCLI();
        assertTrue(cli.registerConsumer("hi", args -> {
        }));
        assertNotNull(cli.getCommand("hi"));
    }

    @Test
    public void testRegisterPredicateReturnsTrue() {
        final JCLI cli = new JCLI();
        assertTrue(cli.registerPredicate("p", args -> true));
    }

    @Test
    public void testUnregisterExistingCommand() {
        final JCLI cli = new JCLI();
        cli.registerConsumer("hi", args -> {
        });
        assertTrue(cli.unregisterCommand("hi"));
        assertNull(cli.getCommand("hi"));
    }

    @Test
    public void testUnregisterMissingCommandReturnsFalse() {
        assertFalse(new JCLI().unregisterCommand("missing"));
    }

    /*
     - getCommand: case-insensitive lookup, alias matching
     */

    @Test
    public void testGetCommandIsCaseInsensitive() {
        final JCLI cli = new JCLI();
        cli.registerConsumer("hello", args -> {
        });
        assertNotNull(cli.getCommand("HELLO"));
        assertNotNull(cli.getCommand("Hello"));
    }

    @Test
    public void testGetCommandMatchesAlias() {
        final JCLI cli = new JCLI();
        cli.registerCommand(new CLICommand("hello", "hi", "hey") {
            @Override
            public boolean onCommand(@NotNull String @NotNull [] args) {
                return true;
            }
        });
        assertNotNull(cli.getCommand("hi"));
        assertNotNull(cli.getCommand("hey"));
    }

    @Test
    public void testGetCommandReturnsNullForMissing() {
        assertNull(new JCLI().getCommand("missing"));
    }

    /*
     - process: dispatch to the right handler
     */

    @Test
    public void testProcessDispatchesAndReturnsTrue() {
        final JCLI cli = new JCLI();
        final AtomicReference<String[]> received = new AtomicReference<>();
        cli.registerConsumer("hello", received::set);

        assertTrue(cli.process("hello world !!"));
        assertArrayEquals(new String[]{"world", "!!"}, received.get());
    }

    @Test
    public void testProcessWithNoArgsReceivesEmptyArray() {
        final JCLI cli = new JCLI();
        final AtomicReference<String[]> received = new AtomicReference<>();
        cli.registerConsumer("ping", received::set);

        assertTrue(cli.process("ping"));
        assertArrayEquals(new String[0], received.get());
    }

    /*
     - process: blank or empty input
     */

    @Test
    public void testProcessBlankInputReturnsFalse() {
        final JCLI cli = new JCLI();
        assertFalse(cli.process(""));
        assertFalse(cli.process("   "));
        assertFalse(cli.process("\t"));
    }

    /*
     - process: unknown command callback
     */

    @Test
    public void testProcessUnknownTriggersCallback() {
        final JCLI cli = new JCLI();
        final AtomicReference<String> unknown = new AtomicReference<>();
        cli.setOnUnknownCommand(unknown::set);

        assertFalse(cli.process("nope arg"));
        assertEquals("nope", unknown.get());
    }

    @Test
    public void testProcessUnknownWithoutCallbackReturnsFalse() {
        final JCLI cli = new JCLI();
        cli.setOnUnknownCommand(null);
        assertFalse(cli.process("nope"));
    }

    /*
     - process: argument parsing
     */

    @Test
    public void testProcessQuotedArgIsKeptAsOne() {
        final JCLI cli = new JCLI();
        final AtomicReference<String[]> received = new AtomicReference<>();
        cli.registerConsumer("cmd", received::set);

        cli.process("cmd \"hello world\" tail");
        assertArrayEquals(new String[]{"hello world", "tail"}, received.get());
    }

    @Test
    public void testProcessEscapedQuoteInsideArg() {
        final JCLI cli = new JCLI();
        final AtomicReference<String[]> received = new AtomicReference<>();
        cli.registerConsumer("cmd", received::set);

        cli.process("cmd say\\\"hi\\\"");
        assertArrayEquals(new String[]{"say\"hi\""}, received.get());
    }

    @Test
    public void testProcessCollapsesConsecutiveSpaces() {
        final JCLI cli = new JCLI();
        final AtomicReference<String[]> received = new AtomicReference<>();
        cli.registerConsumer("cmd", received::set);

        cli.process("cmd    a    b");
        assertArrayEquals(new String[]{"a", "b"}, received.get());
    }

    @Test
    public void testProcessAllowBlankArgsKeepsEmptyTokens() {
        final JCLI cli = new JCLI();
        cli.setAllowBlankArgs(true);
        final AtomicReference<String[]> received = new AtomicReference<>();
        cli.registerConsumer("cmd", received::set);

        cli.process("cmd \"\" tail");
        assertArrayEquals(new String[]{"", "tail"}, received.get());
    }

    /*
     - process: pool routing
     */

    @Test
    public void testProcessRoutesToPoolWhenPresent() {
        final CLICommandPool pool = new CLICommandPool();
        final JCLI cli = new JCLI(pool);
        final AtomicReference<String[]> received = new AtomicReference<>();
        cli.registerConsumer("cmd", received::set);

        cli.process("cmd one two");
        // The pool defers execution: the consumer must not have run yet.
        assertNull(received.get());
        pool.runPending();
        assertArrayEquals(new String[]{"one", "two"}, received.get());
    }

    /*
     - Lifecycle: start, stop, isRunning, stream consumption
     */

    @Test
    public void testIsRunningTransitions() {
        final JCLI cli = new JCLI();
        assertFalse(cli.isRunning());
        cli.start(new ByteArrayInputStream(new byte[0]));
        assertTrue(cli.isRunning());
        cli.stop();
        assertFalse(cli.isRunning());
    }

    @Test
    public void testStartConsumesAllLinesAndExitsOnEof() throws InterruptedException {
        final JCLI cli = new JCLI();
        final CountDownLatch latch = new CountDownLatch(3);
        cli.registerConsumer("bump", args -> latch.countDown());

        final ByteArrayInputStream stream = new ByteArrayInputStream(
                "bump\nbump\nbump\n".getBytes(StandardCharsets.UTF_8));
        cli.start(stream);

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        cli.stop();
    }

    @Test
    public void testStartSkipsUnknownCommandsInStream() throws InterruptedException {
        final JCLI cli = new JCLI();
        final CountDownLatch known = new CountDownLatch(1);
        final CountDownLatch unknown = new CountDownLatch(1);
        cli.registerConsumer("ok", args -> known.countDown());
        cli.setOnUnknownCommand(name -> unknown.countDown());

        final ByteArrayInputStream stream = new ByteArrayInputStream(
                "garbage\nok\n".getBytes(StandardCharsets.UTF_8));
        cli.start(stream);

        assertTrue(unknown.await(2, TimeUnit.SECONDS));
        assertTrue(known.await(2, TimeUnit.SECONDS));
        cli.stop();
    }

    /*
     - Pool accessor
     */

    @Test
    public void testGetPoolReturnsConstructorArgument() {
        final CLICommandPool pool = new CLICommandPool();
        assertEquals(pool, new JCLI(pool).getPool());
    }

    @Test
    public void testGetPoolNullByDefault() {
        assertNull(new JCLI().getPool());
    }
}
