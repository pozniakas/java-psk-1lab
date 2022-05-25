package lt.vu.services;

import lt.vu.services.interfaces.IGenerateName;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

@Decorator
public abstract class GenerateNameDecorator implements IGenerateName {
    @Inject
    @Delegate
    @Any
    IGenerateName generateName;

    public String generateName() {
        String name = generateName.generateName();
        name = name + "_LTU";

        return name;
    }
}