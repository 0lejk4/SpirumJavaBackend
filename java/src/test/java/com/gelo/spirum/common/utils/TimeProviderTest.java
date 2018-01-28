package com.gelo.spirum.common.utils;

import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.*;

public class TimeProviderTest {
    @Test
    public void now() throws Exception {
        assertThat(new TimeProvider().now()).isCloseTo(new Date(), 1000);
    }

}