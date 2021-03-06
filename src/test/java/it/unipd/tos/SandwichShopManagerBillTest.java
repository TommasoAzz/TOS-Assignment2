package it.unipd.tos;

import it.unipd.tos.business.exception.TakeAwayBillException;
import it.unipd.tos.model.ItemType;
import it.unipd.tos.model.MenuItem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SandwichShopManagerBillTest extends Assert {
    private final SandwichShopManagerBill ssmb = new SandwichShopManagerBill();
    private final List<MenuItem> itemsOrdered = new ArrayList<>();

    // istanzie di esempio da usare per i test
    // -- panini
    private final MenuItem contadino = new MenuItem(ItemType.Panino, "Panino Contadino", 1.5); // panino meno costoso
    private final MenuItem primavera = new MenuItem(ItemType.Panino, "Panino Primavera", 5.0);
    private final MenuItem vegetariano = new MenuItem(ItemType.Panino, "Panino Vegetariano", 6.0);
    private final MenuItem giorno = new MenuItem(ItemType.Panino, "Panino del giorno", 7.0); // panino più costoso
    // -- bevande
    private final MenuItem cola = new MenuItem(ItemType.Bevanda, "Cola", 2.5);
    private final MenuItem acqua = new MenuItem(ItemType.Bevanda, "Acqua", 1.0);
    // -- fritti
    private final MenuItem arancini = new MenuItem(ItemType.Fritto, "Arancini", 1.5);
    private final MenuItem oliveascolane = new MenuItem(ItemType.Fritto, "Olive Ascolane", 3.5);

    @Before
    public void emptyList() {
        itemsOrdered.clear();
    }

    @Test
    public void getOrderPrice_totalPrice_calculated() throws TakeAwayBillException {
        itemsOrdered.addAll(
            Arrays.asList(
                primavera, // 5
                vegetariano, // 6
                cola, // 2.5
                arancini, // 1.5
                oliveascolane, // 3.5
                giorno, // 7.0
                acqua, // 1.0
                contadino // 1.5
            )
        ); // 3 panini, 2 bevande, 2 fritti, totale = 28
        
        assertEquals(ssmb.getOrderPrice(itemsOrdered), 28.0, 0.0);
    }

    @Test
    public void getOrderPrice_emptyList_totalEquals0() throws TakeAwayBillException {
        // itemsOrdered is cleared by method emptyList
        assertEquals(ssmb.getOrderPrice(itemsOrdered), 0.0, 0.0);
    }

    @Test
    public void getOrderPrice_NaNPriceInAMenuItem_totalEqualsNaN() throws TakeAwayBillException {
        itemsOrdered.addAll(
            Arrays.asList(
                vegetariano, // 6.0
                new MenuItem(ItemType.Panino, "NaN Test", Double.NaN)
            )
        ); // 2 panini, totale NaN

        assertEquals(ssmb.getOrderPrice(itemsOrdered), Double.NaN, 0.0);
    }

    @Test
    public void getOrderPrice_nullItemsOnOrderNotConsideredForTotalPrice_calculated() throws TakeAwayBillException {
        itemsOrdered.addAll(
            Arrays.asList(
                vegetariano, // 6.0
                acqua, // 1.0
                cola, // 2.5
                null, // null
                giorno // 7.0
            )
        ); // 2 panini, 2 bevande, totale 16.5

        assertEquals(ssmb.getOrderPrice(itemsOrdered), 16.5, 0.0);
    }

    @Test(expected = TakeAwayBillException.class)
    public void getOrderPrice_nullList_exceptionThrown() throws TakeAwayBillException {
        assertEquals(ssmb.getOrderPrice(null), 0.0, 0.0);
    }

    @Test
    public void getOrderPrice_totalPriceMinus50PercentDiscountOnTheCheapestSandwich_calculated() throws TakeAwayBillException {
        itemsOrdered.addAll(
            Arrays.asList(
                primavera, // 5
                vegetariano, // 6
                cola, // 2.5
                arancini, // 1.5
                oliveascolane, // 3.5
                giorno, // 7.0
                acqua, // 1.0
                contadino, // 1.5
                vegetariano, // 6
                primavera // 5
            )
        ); // 5 panini, 2 bevande, 2 fritti, totale = 39 - (0.5 * 1.5) = 38.25

        assertEquals(ssmb.getOrderPrice(itemsOrdered), 38.25, 0.0);
    }

    @Test
    public void getOrderPrice_totalPriceMinus50PercentDiscountOnThe1stCheapestSandwich_calculated() throws TakeAwayBillException {
        itemsOrdered.addAll(
            Arrays.asList(
                primavera, // 5
                vegetariano, // 6
                cola, // 2.5
                arancini, // 1.5
                oliveascolane, // 3.5
                giorno, // 7.0
                acqua, // 1.0
                contadino, // 1.5
                vegetariano, // 6
                primavera, // 5
                contadino // 1.5
            )
        ); // 6 panini, 2 bevande, 2 fritti, totale = 40.5 - (0.5 * 1.5) = 39.75

        assertEquals(ssmb.getOrderPrice(itemsOrdered), 39.75, 0.0);
    }

    @Test
    public void getOrderPrice_10PercentDiscountOnMoreThan50EurosOnSandiwichesAndFrieds_calculated() throws TakeAwayBillException {
        itemsOrdered.addAll(
            Arrays.asList(
                giorno, // 7.0
                giorno, // 7.0
                giorno, // 7.0
                giorno, // 7.0
                arancini, // 1.5
                arancini, // 1.5
                arancini, // 1.5
                arancini, // 1.5
                arancini, // 1.5
                oliveascolane, // 3.5
                oliveascolane, // 3.5
                oliveascolane, // 3.5
                oliveascolane, // 3.5
                oliveascolane, // 3.5
                acqua, // 1.0
                acqua, // 1.0
                acqua, // 1.0
                cola // 2.5
            )
        ); // 4 panini, 4 bevande, 9 fritti, totale = 58.5 - (58.5 * 0.1) = 52.65

        assertEquals(ssmb.getOrderPrice(itemsOrdered), 52.65, 0.0);
    }

    @Test
    public void getOrderPrice_OnMoreThan50EurosOnSandiwichesAndFriedsGet10PercentDiscountOnTotalAnd50PercentDiscountOnCheapestSandwich_calculated()
            throws TakeAwayBillException {
        itemsOrdered.addAll(
            Arrays.asList(
                primavera, // 5
                vegetariano, // 6
                cola, // 2.5
                arancini, // 1.5
                oliveascolane, // 3.5
                giorno, // 7.0
                acqua, // 1.0
                contadino, // 1.5
                vegetariano, // 6
                primavera, // 5
                contadino, // 1.5
                oliveascolane, // 3.5
                arancini, // 1.5
                primavera, // 5
                giorno // 7.0
            )
        ); // 9 panini, 2 bevande, 4 fritti, totale = 57.5 - (57.5 * 0.1) - (0.5 * 1.5) = 51

        assertEquals(ssmb.getOrderPrice(itemsOrdered), 51.0, 0.0);
    }

    @Test(expected = TakeAwayBillException.class)
    public void getOrderPrice_OrderWithMoreThan30Items_exceptionThrown() throws TakeAwayBillException {
        for(int i = 0; i < 31; ++i) {
            itemsOrdered.add(acqua);
        } // 31 bevande, totale = 31 (ma lanciata eccezione)

        assertEquals(ssmb.getOrderPrice(itemsOrdered), 31.0, 0.0);
    }

    @Test
    public void getOrderPrice_50centsCommissionOnTotalOrderSmallerThan10Euros_calculated() throws TakeAwayBillException {
        for(int i = 0; i < 5; ++i) {
            itemsOrdered.add(acqua);
        } // 5 bevande, totale = 5 + 0.5 = 5.5

        assertEquals(ssmb.getOrderPrice(itemsOrdered), 5.5, 0.0);
    }
}