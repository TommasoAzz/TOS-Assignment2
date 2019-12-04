////////////////////////////////////////////////////////////////////
// TOMMASO AZZALIN 1169740
////////////////////////////////////////////////////////////////////
package it.unipd.tos;

import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import it.unipd.tos.business.TakeAwayBill;
import it.unipd.tos.business.exception.TakeAwayBillException;
import it.unipd.tos.model.ItemType;
import it.unipd.tos.model.MenuItem;

public class SandwichShopManagerBill implements TakeAwayBill {
    @Override
    public double getOrderPrice(List<MenuItem> itemsOrdered) throws TakeAwayBillException {
        /// Usare i Supplier perché gli Stream possono essere usati una volta sola

        /// Supplier di Stream di MenuItem non nulli
        Supplier<Stream<MenuItem>> menuItems = () -> itemsOrdered.stream().filter((item) -> item != null);

        // Calcolo del totale dell'ordine
        double totale = menuItems.get().mapToDouble((item) -> item.getPrice()).sum();

        // Supplier di Stream di (MenuItem t.c. getItemType() == ItemType.Panino)
        Supplier<Stream<MenuItem>> panini = () -> menuItems.get().filter((item) -> item.getItemType() == ItemType.Panino);
        
        // Sconto 50% panino meno caro se acquisto più di 5 panini
        if (panini.get().count() > 5) {
            double minPrice = panini.get().min(Comparator.comparing(MenuItem::getPrice)).get().getPrice();
            return totale - (minPrice * 0.5);
        }

        return totale;
    }
}