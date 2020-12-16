package project1.visitors;

import project1.actors.Animal;
import project1.actors.Bush;

public interface WorldActorVisitor {

    void visitBush(Bush bush);

    void visitAnimal(Animal animal);

}
