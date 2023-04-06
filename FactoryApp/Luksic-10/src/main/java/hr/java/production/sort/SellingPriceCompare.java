package hr.java.production.sort;

import hr.java.production.model.Item;

import java.util.Comparator;

public class SellingPriceCompare implements Comparator<Item> {

    @Override
    public int compare(Item i1, Item i2) {
        int compare = 0;
        compare = ((i2.getSellingPrice().compareTo((i1.getSellingPrice()))));
        return compare;
    }
}
