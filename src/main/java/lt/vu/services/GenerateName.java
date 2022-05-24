package lt.vu.services;

import com.github.javafaker.Faker;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GenerateName {
    public String generateName() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }

        Faker faker = new Faker();
        return faker.name().firstName();
    }
}
