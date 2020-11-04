package com.yjfei.antibot.engine.rule;

import com.yjfei.antibot.engine.Context;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class RelationTest {


    @Test
    public void and() {
        Optional<Relation> relation = Relation.of("&");
        assertTrue(relation.isPresent());

        Context context = mock(Context.class);
        assertFalse(relation.get().apply(c -> false, c -> false).apply(context));
        assertFalse(relation.get().apply(c -> false, c -> true).apply(context));
        assertFalse(relation.get().apply(c -> true, c -> false).apply(context));
        assertTrue(relation.get().apply(c -> true, c -> true).apply(context));
    }
}
