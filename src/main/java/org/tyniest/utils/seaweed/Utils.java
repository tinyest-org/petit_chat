package org.tyniest.utils.seaweed;

import java.util.List;
import java.util.Random;
import java.util.Set;

import lombok.experimental.UtilityClass;

@UtilityClass
class Utils {
    static <E> E getRandomSetElement(Set<E> set) {
        return set.stream().skip(new Random().nextInt(set.size())).findFirst().orElseThrow();
    }

    static <E> E getRandomListElement(List<E> list) {
        final var i = new Random().nextInt(list.size());
        return list.get(i);
    }

}
