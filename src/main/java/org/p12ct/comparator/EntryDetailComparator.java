package org.p12ct.comparator;

import java.util.Comparator;
import org.p12ct.beans.EntryDetails;


public class EntryDetailComparator implements Comparator<EntryDetails>
{

    public int compare(EntryDetails value1, EntryDetails value2)
    {
	return value1.getAlias().compareTo(value2.getAlias());
    }
}
