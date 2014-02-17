package com.demo.bpm.jee.util;

import java.io.File;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


import static com.demo.bpm.shared.DemoBPMConstants.DEMO_BPM_LOG_DIR_PROP_NAME;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;

@ApplicationScoped
public class DemoJBPMConfiguration
        implements Serializable
{
    public static final String DEMO_JBPM_PROPERTIES = "demo-jbpm.properties";
    public static final String USER_HOME = "user.home";
    public static final String DEFAULT_DATA_DIR = new File(System.getProperty(USER_HOME), "data/jbpm").toString();
    private File globalLogDir;

    @Inject
    private Logger log;

    @PostConstruct
    public void doPostConstruct()
    {
        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new SystemConfiguration());
        try
        {
            config.addConfiguration(new PropertiesConfiguration(DEMO_JBPM_PROPERTIES));
        } catch (ConfigurationException ignored)
        {

        }

        this.globalLogDir = new File(config.getString(DEMO_BPM_LOG_DIR_PROP_NAME, DEFAULT_DATA_DIR));
        log.info("Global Logging Directory Home: " + this.globalLogDir);
        this.globalLogDir.mkdirs();
    }

    public File getGlobalLogDir()
    {
        return globalLogDir;
    }

    public void setGlobalLogDir(File globalLogDir)
    {
        this.globalLogDir = globalLogDir;
    }

}
