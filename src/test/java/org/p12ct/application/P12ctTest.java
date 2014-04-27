package org.p12ct.application;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.Security;
import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Before;
import org.junit.Test;
import org.p12ct.beans.EntryDetails;
import org.p12ct.beans.P12InfoBean;
import org.p12ct.utils.KeyStoreUtils;

import static org.junit.Assert.assertFalse;

public class P12ctTest
{
    
    private P12InfoBean loadTestData()
    {
	P12InfoBean p12InfoBean = new P12InfoBean();
	
	p12InfoBean.setPkcs12Name("cda_proves.p12");
	p12InfoBean.setNewPkcs12Name("cda_proves2.p12");
	p12InfoBean.setPin("1234");
	p12InfoBean.setNewPin("1234");
	p12InfoBean.setNewPinValidation("1234");
	p12InfoBean.setAliasNumber(null);
	p12InfoBean.setNewAlias("");
	
	return p12InfoBean;
    }
    
    private P12InfoBean loadTestDataWithEntryDetailList() throws IOException, GeneralSecurityException
    {
	P12InfoBean p12InfoBean = loadTestData();
	KeyStore ks = KeyStoreUtils.loadKeyStore(p12InfoBean.getPkcs12Name(), p12InfoBean.getPin());
	List<EntryDetails> entryDetailsList = KeyStoreUtils.getAliasList(ks, p12InfoBean.getPin());
	p12InfoBean.setEntryDetailList(entryDetailsList);
	return p12InfoBean;
	    
    }
    
    @Before
    public void prepareTestCasesDepenencies()
    {
	Security.addProvider(new BouncyCastleProvider());
    }
    
    @Test
    public void changeAliasP12Test()
    {
	try
	{
	    P12InfoBean p12InfoBean = loadTestDataWithEntryDetailList();
	    p12InfoBean.setAliasNumber(0);
	    p12InfoBean.setNewAlias("NEW_ALIAS");
	    P12ct.modifyP12(p12InfoBean);
	}
	catch(Exception exc)
	{
	    assertFalse(exc.toString(),true);
	}
    }
    
    @Test
    public void cloneP12Test() 
    {
	try
	{
	    P12ct.modifyP12(loadTestDataWithEntryDetailList());
	}
	catch(Exception exc)
	{
	    assertFalse(exc.toString(),true);
	}
    }
    
    @Test
    public void listAliasesTest()
    {
	try
	{
	    P12ct.printAliasList(loadTestData());
	}
	catch(Exception exc)
	{
	    assertFalse(exc.toString(),true);
	}
    }

}
