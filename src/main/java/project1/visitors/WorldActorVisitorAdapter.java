package project1.visitors;

import project1.actors.Animal;
import project1.actors.Bush;

public abstract class WorldActorVisitorAdapter implements WorldActorVisitor{
    @Override
    public void visitBush(Bush bush) {
    }

    @Override
    public void visitAnimal(Animal animal) {
    }
}
