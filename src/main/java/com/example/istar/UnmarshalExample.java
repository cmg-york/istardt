package com.example.istar;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;

public class UnmarshalExample {
    public static void main(String[] args) {
        try {
            // Create a JAXBContext for the Actor class and its dependencies
            JAXBContext context = JAXBContext.newInstance(Actor.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // Provide the path to your XML file (e.g., in src/main/resources)
            File xmlFile = new File("src/main/resources/xml/figure1a_fixed.xml");
            Actor actor = (Actor) unmarshaller.unmarshal(xmlFile);

            System.out.println("Actor Name: " + actor.getName());
            // You can further inspect the object, for example:
            if (actor.getPredicates() != null) {
                actor.getPredicates().getPredicateList().forEach(p ->
                        System.out.println("Predicate: " + p.getValue() + " (" + p.getDescription() + ")")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
