package com.demo.bpm.rest.client;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;


import static javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT;

public class JAXBHelper
{

    private final Class<?> jaxbClass;

    public JAXBHelper(Class<?> jaxbClass)
    {
        this.jaxbClass = jaxbClass;
    }

    public String marshallIntoXML(Object instance)
            throws Exception
    {
        StringWriter sw = new StringWriter();
        makeMarshaller(jaxbClass).marshal(instance, sw);
        return sw.toString();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public String marshallIntoXML(String name, List<?> instances)
            throws JAXBException
    {
        // make a wrapper marker for the list so I can assign a QName for it
        Wrapper wrapper = new Wrapper(instances);
        QName qName = new QName(name);
        JAXBElement<Wrapper> jaxbElement = new JAXBElement<Wrapper>(qName, Wrapper.class, wrapper);

        // now marshall as normal
        StringWriter sw = new StringWriter();
        makeMarshaller(Wrapper.class, jaxbClass).marshal(jaxbElement, sw);
        return sw.toString();
    }

    private Marshaller makeMarshaller(Class<?>... jaxbClasses)
            throws JAXBException
    {
        JAXBContext jbc = JAXBContext.newInstance(jaxbClasses);
        Marshaller marshaller = jbc.createMarshaller();
        marshaller.setProperty(JAXB_FORMATTED_OUTPUT, true);
        return marshaller;
    }

    public static class Wrapper<T>
    {

        private List<T> items;

        public Wrapper()
        {
            items = new ArrayList<T>();
        }

        public Wrapper(List<T> items)
        {
            this.items = items;
        }

        @XmlAnyElement(lax = true)
        public List<T> getItems()
        {
            return items;
        }

    }
}
