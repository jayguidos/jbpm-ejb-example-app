package com.demo.bpm.jee.test;

import java.util.logging.Logger;

import javax.inject.Inject;


import com.demo.bpm.facts.model.WorkDay;
import com.demo.bpm.jee.data.WorkDayProducer;
import com.demo.bpm.jee.util.Resources;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class WorkDayRegistrationTest
{
    @Inject
    WorkDayProducer memberRegistration;
    @Inject
    Logger log;

    @Deployment
    public static Archive<?> createTestArchive()
    {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(WorkDay.class, WorkDayProducer.class, Resources.class)
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void testRegister()
            throws Exception
    {
    }

}
