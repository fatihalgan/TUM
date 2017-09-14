/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.dealer.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author db2admin
 */
public class EDGEDealer extends ExternalDealer {

    public EDGEDealer() {
        super();
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof EDGEDealer)) {
            return false;
        }
        EDGEDealer castOther = (EDGEDealer) other;
        return new EqualsBuilder().append(dealerCode, castOther.dealerCode).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(-1300409025, 1507648711).append(dealerCode).toHashCode();
    }

    @Override
    public DealerTypes getDealerType() {
        return DealerTypes.EDGEDealer;
    }

}
