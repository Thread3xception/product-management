package example.task.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class type, Long id) {
        super(type.getSimpleName() + " with id: " + id + " not found!");
    }
}
