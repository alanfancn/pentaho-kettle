package org.pentaho.di.trans.steps.ldapinput;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;

import org.junit.Before;
import org.junit.Test;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.trans.steps.ldapinput.store.CustomSocketFactory;

public class LdapSslProtocolTest {
  private LogChannelInterface mockLogChannelInterface;

  private VariableSpace mockVariableSpace;

  private LdapMeta mockLdapMeta;

  private class TestableLdapProtocol extends LdapSslProtocol {
    public Hashtable<String, String> contextEnv = null;

    public boolean trustAllCertificates = false;

    public String trustStorePath = null;

    public String trustStorePassword = null;

    public TestableLdapProtocol(LogChannelInterface log, VariableSpace variableSpace, LdapMeta meta,
        Collection<String> binaryAttributes) {
      super(log, variableSpace, meta, binaryAttributes);
    }

    @Override
    protected InitialLdapContext createLdapContext(Hashtable<String, String> env) throws NamingException {
      contextEnv = env;
      return null;
    }

    @Override
    protected void configureSocketFactory(boolean trustAllCertificates, String trustStorePath, String trustStorePassword)
        throws KettleException {
      this.trustAllCertificates = trustAllCertificates;
      this.trustStorePath = trustStorePath;
      this.trustStorePassword = trustStorePassword;
    }
  }

  @Before
  public void setup() {
    mockLogChannelInterface = mock(LogChannelInterface.class);
    mockVariableSpace = mock(VariableSpace.class);
    mockLdapMeta = mock(LdapMeta.class);
  }

  @Test
  public void testLdapProtocolAddsLdapPrefixIfNecessary() throws KettleException {
    String hostConcrete = "host_concrete";
    String portConcrete = "12345";
    when(mockLdapMeta.getHost()).thenReturn(hostConcrete);
    when(mockLdapMeta.getPort()).thenReturn(portConcrete);
    when(mockLdapMeta.getDerefAliases()).thenReturn("always");
    when(mockLdapMeta.getReferrals()).thenReturn("follow");

    when(mockVariableSpace.environmentSubstitute(eq(hostConcrete))).thenReturn(hostConcrete);
    when(mockVariableSpace.environmentSubstitute(eq(portConcrete))).thenReturn(portConcrete);

    TestableLdapProtocol testableLdapProtocol = new TestableLdapProtocol(mockLogChannelInterface, mockVariableSpace,
        mockLdapMeta, null);
    testableLdapProtocol.connect(null, null);

    assertEquals(testableLdapProtocol.getConnectionPrefix() + hostConcrete + ":" + portConcrete,
        testableLdapProtocol.contextEnv.get(Context.PROVIDER_URL));
  }

  @Test
  public void testLdapProtocolSkipsAddingLdapPrefixIfNecessary() throws KettleException {
    String hostnameConcrete = "host_concrete";
    String hostConcrete = "ldaps://" + hostnameConcrete;
    String portConcrete = "12345";
    when(mockLdapMeta.getHost()).thenReturn(hostConcrete);
    when(mockLdapMeta.getPort()).thenReturn(portConcrete);
    when(mockLdapMeta.getDerefAliases()).thenReturn("always");
    when(mockLdapMeta.getReferrals()).thenReturn("follow");

    when(mockVariableSpace.environmentSubstitute(eq(hostConcrete))).thenReturn(hostConcrete);
    when(mockVariableSpace.environmentSubstitute(eq(portConcrete))).thenReturn(portConcrete);

    TestableLdapProtocol testableLdapProtocol = new TestableLdapProtocol(mockLogChannelInterface, mockVariableSpace,
        mockLdapMeta, null);
    testableLdapProtocol.connect(null, null);

    assertEquals(testableLdapProtocol.getConnectionPrefix() + hostnameConcrete + ":" + portConcrete,
        testableLdapProtocol.contextEnv.get(Context.PROVIDER_URL));
  }
  
  @Test
  public void testLdapProtocolSetsSsl() throws KettleException {
    String hostConcrete = "host_concrete";
    String portConcrete = "12345";
    when(mockLdapMeta.getHost()).thenReturn(hostConcrete);
    when(mockLdapMeta.getPort()).thenReturn(portConcrete);
    when(mockLdapMeta.getDerefAliases()).thenReturn("always");
    when(mockLdapMeta.getReferrals()).thenReturn("follow");

    when(mockVariableSpace.environmentSubstitute(eq(hostConcrete))).thenReturn(hostConcrete);
    when(mockVariableSpace.environmentSubstitute(eq(portConcrete))).thenReturn(portConcrete);

    TestableLdapProtocol testableLdapProtocol = new TestableLdapProtocol(mockLogChannelInterface, mockVariableSpace,
        mockLdapMeta, null);
    testableLdapProtocol.connect(null, null);

    assertEquals("ssl", testableLdapProtocol.contextEnv.get(Context.SECURITY_PROTOCOL));
  }
  
  @Test
  public void testLdapProtocolSetsSocketFactory() throws KettleException {
    String hostConcrete = "host_concrete";
    String portConcrete = "12345";
    when(mockLdapMeta.getHost()).thenReturn(hostConcrete);
    when(mockLdapMeta.getPort()).thenReturn(portConcrete);
    when(mockLdapMeta.getDerefAliases()).thenReturn("always");
    when(mockLdapMeta.getReferrals()).thenReturn("follow");

    when(mockVariableSpace.environmentSubstitute(eq(hostConcrete))).thenReturn(hostConcrete);
    when(mockVariableSpace.environmentSubstitute(eq(portConcrete))).thenReturn(portConcrete);

    TestableLdapProtocol testableLdapProtocol = new TestableLdapProtocol(mockLogChannelInterface, mockVariableSpace,
        mockLdapMeta, null);
    testableLdapProtocol.connect(null, null);

    assertEquals(CustomSocketFactory.class.getCanonicalName(), testableLdapProtocol.contextEnv.get("java.naming.ldap.factory.socket"));
  }
  
  @Test
  public void testLdapProtocolSkipsConfiguresSocketFactoryIfNecessary() throws KettleException {
    String hostConcrete = "host_concrete";
    String portConcrete = "12345";
    String trustStorePath = "TEST_PATH";
    String trustStorePassword = "TEST_PASSWORD";
    
    when(mockLdapMeta.getHost()).thenReturn(hostConcrete);
    when(mockLdapMeta.getPort()).thenReturn(portConcrete);
    when(mockLdapMeta.getDerefAliases()).thenReturn("always");
    when(mockLdapMeta.getReferrals()).thenReturn("follow");
    when(mockLdapMeta.isUseCertificate()).thenReturn(false);
    when(mockLdapMeta.isTrustAllCertificates()).thenReturn(true);
    when(mockLdapMeta.getTrustStorePath()).thenReturn(trustStorePath);
    when(mockLdapMeta.getTrustStorePassword()).thenReturn(trustStorePassword);

    when(mockVariableSpace.environmentSubstitute(eq(hostConcrete))).thenReturn(hostConcrete);
    when(mockVariableSpace.environmentSubstitute(eq(portConcrete))).thenReturn(portConcrete);

    TestableLdapProtocol testableLdapProtocol = new TestableLdapProtocol(mockLogChannelInterface, mockVariableSpace,
        mockLdapMeta, null);
    testableLdapProtocol.connect(null, null);

    assertEquals(false, testableLdapProtocol.trustAllCertificates);
    assertEquals(null, testableLdapProtocol.trustStorePath);
    assertEquals(null, testableLdapProtocol.trustStorePassword);
  }
  
  @Test
  public void testLdapProtocolConfiguresSocketFactoryIfNecessary() throws KettleException {
    String hostConcrete = "host_concrete";
    String portConcrete = "12345";
    String trustStorePath = "TEST_PATH";
    String trustStorePassword = "TEST_PASSWORD";
    
    when(mockLdapMeta.getHost()).thenReturn(hostConcrete);
    when(mockLdapMeta.getPort()).thenReturn(portConcrete);
    when(mockLdapMeta.getDerefAliases()).thenReturn("always");
    when(mockLdapMeta.getReferrals()).thenReturn("follow");
    when(mockLdapMeta.isUseCertificate()).thenReturn(true);
    when(mockLdapMeta.isTrustAllCertificates()).thenReturn(true);
    when(mockLdapMeta.getTrustStorePath()).thenReturn(trustStorePath);
    when(mockLdapMeta.getTrustStorePassword()).thenReturn(trustStorePassword);

    when(mockVariableSpace.environmentSubstitute(eq(hostConcrete))).thenReturn(hostConcrete);
    when(mockVariableSpace.environmentSubstitute(eq(portConcrete))).thenReturn(portConcrete);

    TestableLdapProtocol testableLdapProtocol = new TestableLdapProtocol(mockLogChannelInterface, mockVariableSpace,
        mockLdapMeta, null);
    testableLdapProtocol.connect(null, null);

    assertEquals(true, testableLdapProtocol.trustAllCertificates);
    assertEquals(trustStorePath, testableLdapProtocol.trustStorePath);
    assertEquals(trustStorePassword, testableLdapProtocol.trustStorePassword);
  }
}
