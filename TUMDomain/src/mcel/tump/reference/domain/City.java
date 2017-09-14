package mcel.tump.reference.domain;

import java.io.Serializable;
import mcel.tump.util.view.beans.SelectableDataItem;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public class City implements Serializable, Comparable<City>, SelectableDataItem {

    /**
     *
     */
    private static final long serialVersionUID = -1044556523025612558L;
    private Long id;
    private String name;


    public City() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof City)) {
            return false;
        }
        City castOther = (City) other;
        return new EqualsBuilder().append(name, castOther.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(-1716066125, -1614776211).append(name).toHashCode();
    }

    @Override
    public int compareTo(City o) {
        return this.getName().compareTo(o.getName());
    }
    @Override
    public String toString() {
        return getName();
    }
}
