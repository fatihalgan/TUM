package mcel.tump.security.service;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
import org.jasypt.hibernate.encryptor.HibernatePBEEncryptorRegistry;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.log.Log;

@Name("initializer")
public class Initializer {

	@Logger
	Log log;
	
	@Observer("org.jboss.seam.postInitialization")
	public void initializeJasypt() {
		log.info("Initializing Jasypt String Encryptor");
		StandardPBEStringEncryptor strongEncryptor = new StandardPBEStringEncryptor();
		EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        config.setPassword("jasypt");
        config.setAlgorithm("PBEWithMD5AndTripleDES");
        strongEncryptor.setConfig(config);
        HibernatePBEEncryptorRegistry registry = HibernatePBEEncryptorRegistry.getInstance();
        registry.registerPBEStringEncryptor("strongHibernateStringEncryptor", strongEncryptor);
	}
	
}
