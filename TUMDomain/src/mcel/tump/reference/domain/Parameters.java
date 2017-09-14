package mcel.tump.reference.domain;

import java.io.Serializable;
import mcel.tump.util.value.TresholdValues;
import mcel.tump.util.view.beans.SelectableDataItem;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public class Parameters implements Serializable, SelectableDataItem {

    /**
     *
     */
    private static final long serialVersionUID = 3191331064036931432L;
    private Long id;
    private TresholdValues tresholdValues = new TresholdValues();

    public Parameters() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TresholdValues getTresholdValues() {
        return tresholdValues;
    }

    public void setTresholdValues(TresholdValues tresholdValues) {
        this.tresholdValues = tresholdValues;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Parameters)) {
            return false;
        }
        Parameters castOther = (Parameters) other;
        return new EqualsBuilder().append(id, castOther.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(-1672282123, 961238861).append(id).toHashCode();
    }
}
