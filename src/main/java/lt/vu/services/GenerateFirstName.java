package lt.vu.services;

import com.github.javafaker.Faker;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Specializes;

@Specializes
@ApplicationScoped
@Alternative
public class GenerateFirstName extends GenerateName {
    public String generateName() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }

        Faker faker = new Faker();
        return faker.name().firstName();
    }
}
