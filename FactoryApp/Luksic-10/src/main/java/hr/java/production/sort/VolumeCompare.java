package hr.java.production.sort;

import hr.java.production.model.Item;

import java.util.Comparator;

/**
 * sluzi za poblize opisivanje i usmjeravanje sortiranja putem implementacije Comparator-a
 */
public class VolumeCompare implements Comparator<Item> {
    /**
     * definira uvjete usporedbe
     * @param i1 artikl kojeg usporedujemo
     * @param i2 artikl s kojim usporedujemo
     * @return vraca vrijednost koja odgovara rezultatu usporedbe
     */
    @Override
    public int compare(Item i1, Item i2) {
        int compare = 0;
        compare = ((i1.getHeight()).multiply(i1.getLength()).multiply(i1.getWidth())).compareTo((i2.getHeight()).
                multiply(i2.getLength()).multiply(i2.getWidth()));
        return compare;
    }
}

