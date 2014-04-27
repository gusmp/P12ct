package org.p12ct.beans;

import org.p12ct.enums.EntryTypeEnum;

public class EntryDetails
{

    private String alias;
    private EntryTypeEnum entryType;

    public EntryDetails(String alias, EntryTypeEnum entryType)
    {
	this.alias = alias;
	this.entryType = entryType;
    }

    public String getAlias()
    {
	return alias;
    }

    public void setAlias(String alias)
    {
	this.alias = alias;
    }

    public EntryTypeEnum getEntryType()
    {
	return entryType;
    }

    public void setEntryType(EntryTypeEnum entryType)
    {
	this.entryType = entryType;
    }

}
