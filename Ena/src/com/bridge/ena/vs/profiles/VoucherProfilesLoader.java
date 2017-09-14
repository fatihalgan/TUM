/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.vs.profiles;

import com.bridge.ena.vs.profiles.serializer.Serializer;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author db2admin
 */
public class VoucherProfilesLoader {

    private static VoucherProfilesLoader instance = null;
    private static final Log logger = LogFactory.getLog(VoucherProfilesLoader.class);
    private Serializer serializer = null;

    private VoucherProfilesLoader() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream input = loader.getResourceAsStream("voucherprofiles.xml");
        if(input == null) {
            logger.warn("Could not located voucherprofiles.xml file in classpath.");
            return;
        }
        try {
            serializer = new Serializer(input);
            logger.info("Loaded Voucher Profiles...");
        } catch(Exception e) {
            logger.error("BadXmlFormat...Could not parse Voucher profiles information..");
            throw new RuntimeException(e);
        }
    }

    public static VoucherProfilesLoader instance() {
        if(instance == null) {
            instance = new VoucherProfilesLoader();
        }
        return instance;
    }

    public VoucherProfiles getVoucherProfiles() {
        if(serializer == null) return null;
        VoucherProfiles voucherProfiles = (VoucherProfiles)serializer.parse();
        return voucherProfiles;
    }
}
