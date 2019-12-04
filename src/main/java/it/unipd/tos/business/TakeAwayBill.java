////////////////////////////////////////////////////////////////////
// TOMMASO AZZALIN 1169740
////////////////////////////////////////////////////////////////////
package it.unipd.tos.business;

import java.util.List;

import it.unipd.tos.business.exception.TakeAwayBillException;
import it.unipd.tos.model.MenuItem;

public interface TakeAwayBill {
    double getOrderPrice(List<MenuItem> itemsOrdered) throws TakeAwayBillException;
}