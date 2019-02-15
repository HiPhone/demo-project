package org.hiphone.auto.publisher.constants;

import java.io.File;

/**
 * @author HiPhone
 */
public class Constant {

    public static final String SWAGGER_BASE_PACKAGE = "org.hiphone.auto.publisher";

    public static final String SWAGGER_TITLE = "Auto-publisher RESTful API 文档";

    public static final String SWAGGER_DESCRIPTION = "auto-publisher";

    public static final String SWAGGER_SERVICE_URL = "http://localhost:8080";

    public static final String SWAGGER_CONTACT_NAME = "HiPhone";

    public static final String SWAGGER_CONTACT_URL = "https://github.com/HiPhone";

    public static final String SWAGGER_CONTACT_EMAIL = "zhyzyhf@gmail.com";

    public static final String SWAGGER_VERSION = "1.0.0";

    public static final String SHELL_SCRIPT_HOME = new File("").getAbsolutePath() + "/bin/";

    public static final String AUTH_CONFIG = SHELL_SCRIPT_HOME + "config/auth/";

    public static final String AUTH_SCRIPT = SHELL_SCRIPT_HOME + "bin/authorized.sh";

    public static final String DEPLOY_SCRIPT = SHELL_SCRIPT_HOME + "bin/deploy.sh";

    public static final String IPS_SCRIPT = SHELL_SCRIPT_HOME + "bin/function/loadhost.sh";

    public static final String EXTENDED_ACTION = "-e";

    public static final String AUTH_HOSTS = "hosts";

    public static final String EXTENDED_NAME = "extended";

}
