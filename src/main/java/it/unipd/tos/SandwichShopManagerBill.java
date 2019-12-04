////////////////////////////////////////////////////////////////////
// TOMMASO AZZALIN 1169740
////////////////////////////////////////////////////////////////////
package it.unipd.tos;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import it.unipd.tos.business.TakeAwayBill;
import it.unipd.tos.business.exception.TakeAwayBillException;
import it.unipd.tos.model.ItemType;
import it.unipd.tos.model.MenuItem;

public class SandwichShopManagerBill implements TakeAwayBill {
    @Override
    public double getOrderPrice(List<MenuItem> itemsOrdered) throws TakeAwayBillException {
        // Calcolo del totale dell'ordine
        double totale = itemsOrdered.stream().filter((item) -> item != null).mapToDouble((item) -> item.getPrice()).sum();

        // Sconto 50% panino meno caro se acquisto più di 5 panini
        // restituisco stream di MenuItem t.c. ogni oggetto di MenuItem nello stream abbia getItemType() == ItemType.Panino
        Stream<MenuItem> panini = itemsOrdered.stream().filter((item) -> item != null && item.getItemType() == ItemType.Panino).unordered();
        if (panini.count() > 5) {
            double minPrice = panini.min(Comparator.comparing(MenuItem::getPrice)).get().getPrice();
            return totale - (minPrice * 0.5);
        }

        return totale;
    }
}