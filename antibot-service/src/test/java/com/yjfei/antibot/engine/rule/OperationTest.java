package com.yjfei.antibot.engine.rule;

import com.yjfei.antibot.engine.Context;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class OperationTest {

    @Test
    public void of() {
        Optional<Operation> op = Operation.of("test");
        assertFalse(op.isPresent());
    }

    @Test
    public void greaterThan() {
        Optional<Operation> op = Operation.of(">");
        assertTrue(op.isPresent());

        Context context = mock(Context.class);
        assertFalse(op.get().apply(c -> 10, "10").apply(context));
        assertFalse(op.get().apply(c -> 10, "10.0").apply(context));
        assertFalse(op.get().apply(c -> 9, "10").apply(context));
        assertTrue(op.get().apply(c -> 11, "10").apply(context));
    }

    @Test
    public void greaterThanEquals() {
        Optional<Operation> op = Operation.of(">=");
        assertTrue(op.isPresent());

        Context context = mock(Context.class);
        assertTrue(op.get().apply(c -> 10, "10").apply(context));
        assertTrue(op.get().apply(c -> 10, "10.0").apply(context));
        assertFalse(op.get().apply(c -> 9, "10").apply(context));
        assertTrue(op.get().apply(c -> 11, "10").apply(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void lessThanNonNumber() {
        Optional<Operation> op = Operation.of("<");
        assertTrue(op.isPresent());

        op.get().apply(c -> 10, "abc");
    }

    @Test
    public void lessThan() {
        Optional<Operation> op = Operation.of("<");
        assertTrue(op.isPresent());

        Context context = mock(Context.class);
        assertFalse(op.get().apply(c -> 10, "10").apply(context));
        assertFalse(op.get().apply(c -> 10, "10.0").apply(context));
        assertTrue(op.get().apply(c -> 9, "10").apply(context));
        assertFalse(op.get().apply(c -> 11, "10").apply(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void lessThanEqualsNonNumber() {
        Optional<Operation> op = Operation.of("<=");
        assertTrue(op.isPresent());

        op.get().apply(c -> 10, "abc");
    }

    @Test
    public void lessThanEquals() {
        Optional<Operation> op = Operation.of("<=");
        assertTrue(op.isPresent());

        Context context = mock(Context.class);
        assertTrue(op.get().apply(c -> 10, "10").apply(context));
        assertTrue(op.get().apply(c -> 10, "10.0").apply(context));
        assertTrue(op.get().apply(c -> 9, "10").apply(context));
        assertFalse(op.get().apply(c -> 11, "10").apply(context));
    }

    @Test
    public void isTrue() {
        Optional<Operation> op = Operation.of("T");
        assertTrue(op.isPresent());

        Context context = mock(Context.class);
        assertFalse(op.get().apply(c -> false, null).apply(context));
        assertTrue(op.get().apply(c -> true, null).apply(context));
    }

    @Test
    public void isFalse() {
        Optional<Operation> op = Operation.of("F");
        assertTrue(op.isPresent());

        Context context = mock(Context.class);
        assertTrue(op.get().apply(c -> false, null).apply(context));
        assertFalse(op.get().apply(c -> true, null).apply(context));
    }

    @Test
    public void in() {
        Optional<Operation> op = Operation.of("IN");
        assertTrue(op.isPresent());

        Context context = mock(Context.class);
        assertFalse(op.get().apply(c -> 1, "92,99,102,104,100,101,103,108,105,109").apply(context));
        assertTrue(op.get().apply(c -> 92, "92,99,102,104,100,101,103,108,105,109").apply(context));
        assertTrue(op.get().apply(c -> 109, "92,99,102,104,100,101,103,108,105,109").apply(context));
    }
}
