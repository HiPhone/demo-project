package org.hiphone.auto.publisher.constants;

import java.util.LinkedList;
import java.util.List;

/**
 * @author HiPhone
 */
public class ParamKeys {

    public static final List<String> DEPLOY_REQUIRED_KEY = new LinkedList<>() {
        {
            add("product");
            add("project");
            add("bld_num");
            add("pkg_type");
        }
    };

    public static final List<String> DEPLOY_OPTIONAL_KEY = new LinkedList<>() {
        {
            add("depl_model");
        }
    };

    public static final List<String> DEPLOY_EXTEND_KEY = new LinkedList<>() {
        {
            add("ENV");
            add("CD_URL");
            add("CONF_FILE");
            add("PRI_TOKEN");
            add("PKG_HOME");
            add("PKG_CONF_HOME");
            add("PKG_RLSE_HOME");
            add("LOG_HOME");
            add("LOG_NAME");
            add("INV_HOME");
            add("INV_NAME");
        }
    };

    public static final List<String> AUTH_REQUIRED_KEY = new LinkedList<>() {
        {
            add("project");
        }
    };

    public static final List<String> AUTH_OPTIONAL_KEY = new LinkedList<>() {
        {
            add("auth_file");
        }
    };

    public static final List<String> AUTH_EXTENDED_KEY = new LinkedList<>() {
        {
            add("LOG_HOME");
            add("LOG_NAME");
            add("INV_HOME");
            add("INV_NAME");
            add("CONF_FILE");
            add("ENV");
        }
    };

    public static final List<String> IPS_REQUIRED_KEY = new LinkedList<>() {
        {
            add("project");
        }
    };

    public static final List<String> IPS_OPTIONAL_KEY = new LinkedList<>() {};

    public static final List<String> IPS_EXTENDED_KEY = new LinkedList<>() {
        {
            add("INV_HOME");
            add("ENV");
        }
    };


}
