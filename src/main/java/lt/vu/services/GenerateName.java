package lt.vu.services;

import lt.vu.services.interfaces.IGenerateName;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GenerateName implements IGenerateName {
    public String generateName() {
        return "Default Name";
    }
}
