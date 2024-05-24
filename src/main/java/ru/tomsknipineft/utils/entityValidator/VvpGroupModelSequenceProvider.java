package ru.tomsknipineft.utils.entityValidator;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import ru.tomsknipineft.entities.areaObjects.Vvp;

import java.util.ArrayList;
import java.util.List;

public class VvpGroupModelSequenceProvider implements DefaultGroupSequenceProvider<Vvp> {
    @Override
    public List<Class<?>> getValidationGroups(Vvp vvp) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(Vvp.class);
        groups.add(Vvp.class);
        if (vvp != null) {
            if (vvp.isActive() && vvp.getHelicopterModel()==null && vvp.getSquare()==null) {
                groups.add(VvpModelCheck.class);
            }
        }
        return groups;
    }
}
