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
        // lista nulla => non so come comportarmi => lancio eccezione
        if (itemsOrdered == null) {
            throw new TakeAwayBillException();
        }
        /// Usare i Supplier perché gli Stream possono essere usati una volta sola

        /// Supplier di Stream di MenuItem non nulli
        Supplier<Stream<MenuItem>> menuItems = () -> itemsOrdered.stream().filter((item) -> item != null);
        
        // Se ordine ha più di 30 elementi, lanciare un'eccezione e prevedere messaggio di errore
        if(menuItems.get().count() > 30) {
            System.err.println("[SandwichShopManagerBill] ERRORE: Non è possibile ordinare più di 30 elementi.");
            throw new TakeAwayBillException();
        }

        // Calcolo del totale dell'ordine
        double totale = menuItems.get().mapToDouble((item) -> item.getPrice()).sum();
        
        // Stream di (MenuItem t.c. getItemType() == ItemType.Panino || getItemType() == ItemType.Fritto)
        Stream<MenuItem> panini_e_fritti = menuItems.get()
        .filter((item) -> (item.getItemType() == ItemType.Panino || item.getItemType() == ItemType.Fritto));
        
        // Sconto 10% sul totale se spesa in panini e fritti maggiore di 50 euro
        if(panini_e_fritti.mapToDouble((item) -> item.getPrice()).sum() > 50.0) {
            totale -= totale * 0.1;
        }
        
        // Supplier di Stream di (MenuItem t.c. getItemType() == ItemType.Panino)
        Supplier<Stream<MenuItem>> panini = () -> menuItems.get()
                .filter((item) -> item.getItemType() == ItemType.Panino);
        
        // Sconto 50% panino meno caro se acquisto più di 5 panini
        if (panini.get().count() > 5) {
            double minPrice = panini.get().min(Comparator.comparing(MenuItem::getPrice)).get().getPrice();
            totale -= minPrice * 0.5;
        }

        /// il totale deve essere maggiore di 0 per scelta progettuale.
        /// se vengono inseriti elementi nulli o la lista è vuota il totale è 0.0 euro
        // se l'importo totale è minore di 10 euro, e ci sono MenuItem nell'ordine, aggiungere una commissione di 0,50 euro
        if(totale > 0.0 && totale < 10.0) {
            totale += 0.50;
        }

        return totale;
    }
}