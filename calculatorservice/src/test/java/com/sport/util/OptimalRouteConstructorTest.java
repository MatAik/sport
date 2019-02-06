package com.sport.util;

import com.sport.data.SubRouteJson;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class OptimalRouteConstructorTest {

    @Test
    public void findEasiestAndFastestOption() {

        OptimalRouteConstructor constructor = new OptimalRouteConstructor();
        List<SubRouteJson> subRoutes = new ArrayList<>();
        subRoutes.add(new SubRouteJson("Barcelona", "Madrid", LocalTime.of(2, 5)));
        subRoutes.add(new SubRouteJson("Madrid", "Sevilla", LocalTime.of(6, 30)));
        subRoutes.add(new SubRouteJson("Sevilla", "Zaragoza", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("Barcelona", "Oviedo", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("Oviedo", "Zaragoza", LocalTime.of(16, 30)));

        final List<OptimalRouteConstructor.CalculatedSubRoutes> possibleRoutes = constructor.findPossibleRoutes(subRoutes, "Barcelona", "Zaragoza");

        Assert.assertEquals(2, possibleRoutes.size());
        Assert.assertEquals(2, possibleRoutes.get(0).getSubroutes().size());
        Assert.assertEquals("Barcelona", possibleRoutes.get(0).getSubroutes().get(0).getStartCity());
        Assert.assertEquals("Oviedo", possibleRoutes.get(0).getSubroutes().get(0).getDestinationCity());
        Assert.assertEquals("Oviedo", possibleRoutes.get(0).getSubroutes().get(1).getStartCity());
        Assert.assertEquals("Zaragoza", possibleRoutes.get(0).getSubroutes().get(1).getDestinationCity());

        Assert.assertEquals(3, possibleRoutes.get(1).getSubroutes().size());
        Assert.assertEquals("Barcelona", possibleRoutes.get(1).getSubroutes().get(0).getStartCity());
        Assert.assertEquals("Madrid", possibleRoutes.get(1).getSubroutes().get(0).getDestinationCity());
        Assert.assertEquals("Madrid", possibleRoutes.get(1).getSubroutes().get(1).getStartCity());
        Assert.assertEquals("Sevilla", possibleRoutes.get(1).getSubroutes().get(1).getDestinationCity());
        Assert.assertEquals("Sevilla", possibleRoutes.get(1).getSubroutes().get(2).getStartCity());
        Assert.assertEquals("Zaragoza", possibleRoutes.get(1).getSubroutes().get(2).getDestinationCity());

        final List<OptimalRouteConstructor.CalculatedSubRoutes> easiestRoutes = constructor.findEasiestRoutes(possibleRoutes);

        Assert.assertEquals(1, easiestRoutes.size());
        Assert.assertEquals(easiestRoutes.get(0), possibleRoutes.get(0));
        Assert.assertEquals("04:35", easiestRoutes.get(0).getTime().toString());
        Assert.assertEquals(1l, easiestRoutes.get(0).getDays());

        final List<OptimalRouteConstructor.CalculatedSubRoutes> fastestRoutes = constructor.findFastestRoutes(possibleRoutes);

        Assert.assertEquals(1, fastestRoutes.size());
        Assert.assertEquals(fastestRoutes.get(0), possibleRoutes.get(1));
        Assert.assertEquals("20:40", fastestRoutes.get(0).getTime().toString());
        Assert.assertEquals(0l, fastestRoutes.get(0).getDays());

    }

    @Test
    public void findPossibleRouteMultipleOptions() {

        OptimalRouteConstructor constructor = new OptimalRouteConstructor();
        List<SubRouteJson> subRoutes = new ArrayList<>();
        subRoutes.add(new SubRouteJson("Barcelona", "Madrid", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("Madrid", "Zaragoza", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("Barcelona", "Sevilla", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("Sevilla", "Zaragoza", LocalTime.of(12, 5)));

        final List<OptimalRouteConstructor.CalculatedSubRoutes> possibleRoutes = constructor.findPossibleRoutes(subRoutes, "Barcelona", "Zaragoza");

        Assert.assertEquals(2, possibleRoutes.size());

        Assert.assertEquals(2, possibleRoutes.get(0).getSubroutes().size());
        Assert.assertEquals(2, possibleRoutes.get(1).getSubroutes().size());

        Assert.assertEquals("Barcelona", possibleRoutes.get(0).getSubroutes().get(0).getStartCity());
        Assert.assertEquals("Madrid", possibleRoutes.get(0).getSubroutes().get(0).getDestinationCity());
        Assert.assertEquals("Madrid", possibleRoutes.get(0).getSubroutes().get(1).getStartCity());
        Assert.assertEquals("Zaragoza", possibleRoutes.get(0).getSubroutes().get(1).getDestinationCity());

        Assert.assertEquals("Barcelona", possibleRoutes.get(1).getSubroutes().get(0).getStartCity());
        Assert.assertEquals("Sevilla", possibleRoutes.get(1).getSubroutes().get(0).getDestinationCity());
        Assert.assertEquals("Sevilla", possibleRoutes.get(1).getSubroutes().get(1).getStartCity());
        Assert.assertEquals("Zaragoza", possibleRoutes.get(1).getSubroutes().get(1).getDestinationCity());
    }

    @Test
    public void findOnlyPossibleRoute() {

        OptimalRouteConstructor constructor = new OptimalRouteConstructor();
        List<SubRouteJson> subRoutes = new ArrayList<>();
        subRoutes.add(new SubRouteJson("Barcelona", "Zaragoza", LocalTime.of(12, 5)));

        final List<OptimalRouteConstructor.CalculatedSubRoutes> possibleRoutes = constructor.findPossibleRoutes(subRoutes, "Barcelona", "Zaragoza");

        Assert.assertEquals(1, possibleRoutes.size());

        OptimalRouteConstructor.CalculatedSubRoutes calculatedSubRoutes = possibleRoutes.get(0);
        Assert.assertEquals(1, calculatedSubRoutes.getSubroutes().size());

        SubRouteJson subRouteJson = calculatedSubRoutes.getSubroutes().get(0);
        Assert.assertEquals("Barcelona", subRouteJson.getStartCity());
        Assert.assertEquals("Zaragoza", subRouteJson.getDestinationCity());

        Assert.assertEquals("12:05", subRouteJson.getTime().toString());

        Assert.assertEquals("12:05", calculatedSubRoutes.getTime().toString());
        Assert.assertEquals(0l, calculatedSubRoutes.getDays());
    }


    @Test
    public void routeNotFound() {

        OptimalRouteConstructor constructor = new OptimalRouteConstructor();
        List<SubRouteJson> subRoutes = new ArrayList<>();
        subRoutes.add(new SubRouteJson("Barcelona", "Zaragoza", LocalTime.of(12, 5)));

        final List<OptimalRouteConstructor.CalculatedSubRoutes> possibleRoutes = constructor.findPossibleRoutes(subRoutes, "Barcelona", "Azogaraz");

        Assert.assertEquals(0, possibleRoutes.size());
    }

    @Test
    public void findTwoSubRoutes() {

        OptimalRouteConstructor constructor = new OptimalRouteConstructor();
        List<SubRouteJson> subRoutes = new ArrayList<>();
        subRoutes.add(new SubRouteJson("Barcelona", "Madrid", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("Madrid", "Zaragoza", LocalTime.of(12, 5)));

        final List<OptimalRouteConstructor.CalculatedSubRoutes> possibleRoutes = constructor.findPossibleRoutes(subRoutes, "Barcelona", "Zaragoza");

        Assert.assertEquals(1, possibleRoutes.size());

        OptimalRouteConstructor.CalculatedSubRoutes calculatedSubRoutes = possibleRoutes.get(0);

        SubRouteJson subRouteJson = calculatedSubRoutes.getSubroutes().get(0);
        Assert.assertEquals("Barcelona", subRouteJson.getStartCity());
        Assert.assertEquals("Madrid", subRouteJson.getDestinationCity());

        Assert.assertEquals("12:05", subRouteJson.getTime().toString());

        subRouteJson = calculatedSubRoutes.getSubroutes().get(1);
        Assert.assertEquals("Madrid", subRouteJson.getStartCity());
        Assert.assertEquals("Zaragoza", subRouteJson.getDestinationCity());

        Assert.assertEquals("12:05", subRouteJson.getTime().toString());

        Assert.assertEquals("00:10", calculatedSubRoutes.getTime().toString());
        Assert.assertEquals(1l, calculatedSubRoutes.getDays());
    }

    @Test
    public void findTwoSubRoutesAndNonWorkingRoutes() {

        OptimalRouteConstructor constructor = new OptimalRouteConstructor();
        List<SubRouteJson> subRoutes = new ArrayList<>();
        subRoutes.add(new SubRouteJson("Barcelona", "Madrid", LocalTime.of(12, 15)));
        subRoutes.add(new SubRouteJson("Madrid", "Milan", LocalTime.of(12, 9)));
        subRoutes.add(new SubRouteJson("Madrid", "Sevilla", LocalTime.of(12, 8)));
        subRoutes.add(new SubRouteJson("Barcelona", "Sevilla", LocalTime.of(12, 7)));
        subRoutes.add(new SubRouteJson("Oviedo", "Zaragoza", LocalTime.of(12, 6)));
        subRoutes.add(new SubRouteJson("Milan", "Zaragoza", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("Milan", "Torino", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("Zaragoza", "Milan", LocalTime.of(12, 4)));

        final List<OptimalRouteConstructor.CalculatedSubRoutes> possibleRoutes = constructor.findPossibleRoutes(subRoutes, "Barcelona", "Zaragoza");

        Assert.assertEquals(1, possibleRoutes.size());

        OptimalRouteConstructor.CalculatedSubRoutes calculatedSubRoutes = possibleRoutes.get(0);

        Assert.assertEquals(3, calculatedSubRoutes.getSubroutes().size());

        SubRouteJson subRouteJson = calculatedSubRoutes.getSubroutes().get(0);
        Assert.assertEquals("Barcelona", subRouteJson.getStartCity());
        Assert.assertEquals("Madrid", subRouteJson.getDestinationCity());

        Assert.assertEquals("12:15", subRouteJson.getTime().toString());

        subRouteJson = calculatedSubRoutes.getSubroutes().get(1);
        Assert.assertEquals("Madrid", subRouteJson.getStartCity());
        Assert.assertEquals("Milan", subRouteJson.getDestinationCity());

        Assert.assertEquals("12:09", subRouteJson.getTime().toString());

        subRouteJson = calculatedSubRoutes.getSubroutes().get(2);
        Assert.assertEquals("Milan", subRouteJson.getStartCity());
        Assert.assertEquals("Zaragoza", subRouteJson.getDestinationCity());

        Assert.assertEquals("12:05", subRouteJson.getTime().toString());

        Assert.assertEquals("12:29", calculatedSubRoutes.getTime().toString());
        Assert.assertEquals(1l, calculatedSubRoutes.getDays());
    }

    @Test
    public void findEasiestRouteMultipleOptions() {

        OptimalRouteConstructor constructor = new OptimalRouteConstructor();
        List<SubRouteJson> subRoutes = new ArrayList<>();
        subRoutes.add(new SubRouteJson("Barcelona", "Madrid", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("Madrid", "Zaragoza", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("Barcelona", "Sevilla", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("Sevilla", "Zaragoza", LocalTime.of(12, 5)));

        final List<OptimalRouteConstructor.CalculatedSubRoutes> possibleRoutes = constructor.findPossibleRoutes(subRoutes, "Barcelona", "Zaragoza");

        Assert.assertEquals(2, possibleRoutes.size());

        Assert.assertEquals(2, possibleRoutes.get(0).getSubroutes().size());
        Assert.assertEquals(2, possibleRoutes.get(1).getSubroutes().size());

        Assert.assertEquals("Barcelona", possibleRoutes.get(0).getSubroutes().get(0).getStartCity());
        Assert.assertEquals("Madrid", possibleRoutes.get(0).getSubroutes().get(0).getDestinationCity());
        Assert.assertEquals("Madrid", possibleRoutes.get(0).getSubroutes().get(1).getStartCity());
        Assert.assertEquals("Zaragoza", possibleRoutes.get(0).getSubroutes().get(1).getDestinationCity());

        Assert.assertEquals("Barcelona", possibleRoutes.get(1).getSubroutes().get(0).getStartCity());
        Assert.assertEquals("Sevilla", possibleRoutes.get(1).getSubroutes().get(0).getDestinationCity());
        Assert.assertEquals("Sevilla", possibleRoutes.get(1).getSubroutes().get(1).getStartCity());
        Assert.assertEquals("Zaragoza", possibleRoutes.get(1).getSubroutes().get(1).getDestinationCity());
    }

    @Test
    public void findRouteHugeRoute() {

        OptimalRouteConstructor constructor = new OptimalRouteConstructor();
        List<SubRouteJson> subRoutes = new ArrayList<>();
        subRoutes.add(new SubRouteJson("Q", "W", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("W", "E", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("E", "R", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("R", "T", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("T", "Y", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("Y", "U", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("U", "I", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("I", "O", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("O", "P", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("P", "A", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("A", "S", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("S", "D", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("D", "F", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("F", "G", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("G", "H", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("H", "J", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("J", "K", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("K", "L", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("L", "Z", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("Z", "X", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("X", "C", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("C", "V", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("V", "B", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("B", "N", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("N", "M", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("M", "Q1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("Q1", "W1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("W1", "E1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("E1", "R1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("R1", "T1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("T1", "Y1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("Y1", "U1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("U1", "I1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("I1", "O1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("O1", "P1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("P1", "A1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("A1", "S1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("S1", "D1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("D1", "F1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("F1", "G1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("G1", "H1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("H1", "J1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("J1", "K1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("K1", "L1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("L1", "Z1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("Z1", "X1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("X1", "C1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("C1", "V1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("V1", "B1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("B1", "N1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("N1", "M1", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("M1", "Q2", LocalTime.of(12, 5)));

        final List<OptimalRouteConstructor.CalculatedSubRoutes> possibleRoutes = constructor.findPossibleRoutes(subRoutes, "Q", "Q2");

        Assert.assertEquals(52, possibleRoutes.get(0).getSubroutes().size());

    }

    @Test
    public void findManyEasiestOptions() {

        OptimalRouteConstructor constructor = new OptimalRouteConstructor();
        List<SubRouteJson> subRoutes = new ArrayList<>();
        subRoutes.add(new SubRouteJson("Barcelona", "Madrid", LocalTime.of(2, 5)));
        subRoutes.add(new SubRouteJson("Madrid", "Zaragoza", LocalTime.of(6, 30)));

        subRoutes.add(new SubRouteJson("Barcelona", "Oviedo", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("Oviedo", "Zaragoza", LocalTime.of(16, 30)));

        final List<OptimalRouteConstructor.CalculatedSubRoutes> possibleRoutes = constructor.findPossibleRoutes(subRoutes, "Barcelona", "Zaragoza");

        final List<OptimalRouteConstructor.CalculatedSubRoutes> easiestRoutes = constructor.findEasiestRoutes(possibleRoutes);

        Assert.assertEquals(2, easiestRoutes.size());

        Assert.assertEquals("Barcelona", easiestRoutes.get(0).getSubroutes().get(0).getStartCity());
        Assert.assertEquals("Madrid", easiestRoutes.get(0).getSubroutes().get(0).getDestinationCity());
        Assert.assertEquals("Madrid", easiestRoutes.get(0).getSubroutes().get(1).getStartCity());
        Assert.assertEquals("Zaragoza", easiestRoutes.get(0).getSubroutes().get(1).getDestinationCity());

        Assert.assertEquals("Barcelona", easiestRoutes.get(1).getSubroutes().get(0).getStartCity());
        Assert.assertEquals("Oviedo", easiestRoutes.get(1).getSubroutes().get(0).getDestinationCity());
        Assert.assertEquals("Oviedo", easiestRoutes.get(1).getSubroutes().get(1).getStartCity());
        Assert.assertEquals("Zaragoza", easiestRoutes.get(1).getSubroutes().get(1).getDestinationCity());

        final List<OptimalRouteConstructor.CalculatedSubRoutes> fastestRoutes = constructor.findFastestRoutes(possibleRoutes);

        Assert.assertEquals(1, fastestRoutes.size());
    }

    @Test
    public void findManyFastestOptions() {

        OptimalRouteConstructor constructor = new OptimalRouteConstructor();
        List<SubRouteJson> subRoutes = new ArrayList<>();
        subRoutes.add(new SubRouteJson("Barcelona", "Madrid", LocalTime.of(2, 5)));
        subRoutes.add(new SubRouteJson("Madrid", "Sevilla", LocalTime.of(6, 30)));
        subRoutes.add(new SubRouteJson("Sevilla", "Zaragoza", LocalTime.of(6, 30)));

        subRoutes.add(new SubRouteJson("Barcelona", "Oviedo", LocalTime.of(12, 5)));
        subRoutes.add(new SubRouteJson("Oviedo", "Zaragoza", LocalTime.of(3, 0)));

        subRoutes.add(new SubRouteJson("Barcelona", "Torino", LocalTime.of(2, 5)));
        subRoutes.add(new SubRouteJson("Torino", "Milan", LocalTime.of(6, 30)));
        subRoutes.add(new SubRouteJson("Milan", "Zaragoza", LocalTime.of(6, 31)));

        final List<OptimalRouteConstructor.CalculatedSubRoutes> possibleRoutes = constructor.findPossibleRoutes(subRoutes, "Barcelona", "Zaragoza");

        final List<OptimalRouteConstructor.CalculatedSubRoutes> easiestRoutes = constructor.findEasiestRoutes(possibleRoutes);

        Assert.assertEquals(1, easiestRoutes.size());

        final List<OptimalRouteConstructor.CalculatedSubRoutes> fastestRoutes = constructor.findFastestRoutes(possibleRoutes);

        Assert.assertEquals(2, fastestRoutes.size());

        Assert.assertEquals("Barcelona", fastestRoutes.get(0).getSubroutes().get(0).getStartCity());
        Assert.assertEquals("Oviedo", fastestRoutes.get(0).getSubroutes().get(0).getDestinationCity());
        Assert.assertEquals("Oviedo", fastestRoutes.get(0).getSubroutes().get(1).getStartCity());
        Assert.assertEquals("Zaragoza", fastestRoutes.get(0).getSubroutes().get(1).getDestinationCity());

        Assert.assertEquals("Barcelona", fastestRoutes.get(1).getSubroutes().get(0).getStartCity());
        Assert.assertEquals("Madrid", fastestRoutes.get(1).getSubroutes().get(0).getDestinationCity());
        Assert.assertEquals("Madrid", fastestRoutes.get(1).getSubroutes().get(1).getStartCity());
        Assert.assertEquals("Sevilla", fastestRoutes.get(1).getSubroutes().get(1).getDestinationCity());
        Assert.assertEquals("Sevilla", fastestRoutes.get(1).getSubroutes().get(2).getStartCity());
        Assert.assertEquals("Zaragoza", fastestRoutes.get(1).getSubroutes().get(2).getDestinationCity());

    }
}