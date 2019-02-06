package com.sport.util;

import com.sport.data.SubRouteJson;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SubRouteJsonMapperTest {

    @Test
    public void convertToTransactions() throws IOException {
        final SubRouteJsonMapper mapper = new SubRouteJsonMapper();
        final InputStream in = ClassLoader.getSystemResourceAsStream("json/testroutes.json");
        final List<SubRouteJson> subRoutes = mapper.convertToSubRoutes(in);

        int testsDone = 0;

        for (SubRouteJson subRoute : subRoutes) {
            if (subRoute.getStartCity().equals("Barcelona") && subRoute.getDestinationCity().equals("Zaragoza")) {
                // Test to see that time is calculated correctly over change of day
                Assert.assertEquals("22:55", subRoute.getTime().toString());
                testsDone++;
            } else if (subRoute.getStartCity().equals("Madrid") && subRoute.getDestinationCity().equals("Oviedo")) {
                // Test to see that time is calculated correctly when day does not change
                Assert.assertEquals("04:15", subRoute.getTime().toString());
                testsDone++;
            }
        }

        // See that all tests were hit
        Assert.assertEquals(2, testsDone);

        // See that everything was parsed
        Assert.assertEquals(24, subRoutes.size());
    }
}