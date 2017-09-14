/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.dealer.domain;

/**
 *
 * @author db2admin
 */
public enum DealerTypes {

    DealerShop("DealerShop"),
    ExternalDealer("ExternalDealer"),
    EDGEDealer("EDGEDealer"),
    PersonalDealer("PersonalDealer");

    private final String text;

    private DealerTypes(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
