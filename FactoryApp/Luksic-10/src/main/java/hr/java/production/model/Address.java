package hr.java.production.model;

import hr.java.production.enumeracija.AvailableCities;

import java.io.Serializable;
import java.util.Objects;

/**
 * sluzi za stvaranje nove adrese
 */

public class Address implements Serializable {
    /**
     * oblikovni obrazac Builder
     */
    public static class Builder {
        private Long id;
        /**
         * sprema naziv ulice
         */
        private String street;
        /**
         *sprema naziv grada i postanski broj
         */
        private AvailableCities city;
        /**
         * sprema kucni broj
         */
        private String houseNumber;

        /**
         * default konstruktor objekta klase Builder
         */
        public Builder() {

        }
        public Builder withId(Long id) {
            this.id = id;
            return this;
        }
        /**
         * sluzi za unos naziva grada i postanskog broja
         * @param city unos naziva grada i postanskog broja
         * @return vraca objekt klase Builder sa dodanim informacijama o nazivu grada i postanskom broju
         */
        public Builder atCityWithPostalCode(AvailableCities city) {
            this.city = city;
            return this;
        }

        /**
         * sluzi za unos ulice
         * @param street unos naziva ulice
         * @return vraca objekt klase Builder sa dodanom informacijom o nazivu ulice
         */
        public Builder atStreet(String street) {
            this.street = street;
            return this;
        }

        /**
         * sluzi za unos kucnog broja
         * @param houseNumber unos kucnog broja
         * @return vraca objekt klase Builder sa dodanom informacijom o kucnom broju
         */
        public Builder atHouseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
            return this;
        }

        /**
         * sluzi za prijenos informacija o adresi unesenoj pomocu oblikovnog obrasca Builder
         * privatnom konstruktoru objekta tipa Address
         * @return vraca novi objekt tipa Address
         */
        public hr.java.production.model.Address build() {

            hr.java.production.model.Address address = new hr.java.production.model.Address();
            address.id = this.id;
            address.city = this.city;
            address.street = this.street;
            address.houseNumber = this.houseNumber;

            return address;
        }
    }
    private Long id;

    /**
     * sprema naziv ulice
     */
    private String street;
    /**
     * sprema kucni broj
     */
    private String houseNumber;
    /**
     * spremna naziv grada i postanski broj
     */
    private AvailableCities city;

    private String cityy;
    private String postalCode;

    /**
     * privatni default konstruktor objekta klase tipa Address koji prima informacije iz oblikovnog obrasca Builder
     * za stvaranje nove adrese
     */
    private Address() {

    }

    public Address(Long id, String street, String houseNumber, String cityy, String postalCode) {
        this.id = id;
        this.street = street;
        this.houseNumber = houseNumber;
        this.cityy = cityy;
        this.postalCode = postalCode;
    }
    public Address(String street, String houseNumber, String cityy, String postalCode) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.cityy = cityy;
        this.postalCode = postalCode;
    }

    public Address(Long id) {
        this.id = id;
    }

    public String getCityy() {
        return cityy;
    }

    public void setCityy(String cityy) {
        this.cityy = cityy;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public AvailableCities getCity() {
        return city;
    }

    public void setCity(AvailableCities city) {
        this.city = city;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        hr.java.production.model.Address address = (hr.java.production.model.Address) o;
//        return Objects.equals(id, address.id) && Objects.equals(street, address.street) && Objects.equals(houseNumber, address.houseNumber) && city == address.city;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, street, houseNumber, city);
//    }
//
//    @Override
//    public String toString() {
//        return "Adresa\nID: " + id + "\tUlica: " + street + "\tKucni broj: " + houseNumber + "\tGrad: " + city + "\tPostanski broj: " +
//                this.city.getPostalCode();
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(getId(), address.getId()) && Objects.equals(getStreet(), address.getStreet()) && Objects.equals(getHouseNumber(), address.getHouseNumber()) && Objects.equals(getCityy(), address.getCityy()) && Objects.equals(getPostalCode(), address.getPostalCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getStreet(), getHouseNumber(), getCityy(), getPostalCode());
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", cityy='" + cityy + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
