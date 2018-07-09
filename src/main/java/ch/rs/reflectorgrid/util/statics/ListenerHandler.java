package ch.rs.reflectorgrid.util.statics;

import ch.rs.reflectorgrid.util.annotations.TransferGridChangeListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ListenerHandler {

    private static Map<Class<?>, List<Method>> listeners = new LinkedHashMap<Class<?>, List<Method>>(1,0.75f, true) {
        public int MAX_SIZE = 15;

        @Override
        protected boolean removeEldestEntry(Map.Entry<Class<?>, List<Method>> eldest) {
            return size() > MAX_SIZE;
        }
    };

    public static void invokeListeners(Object object, Field field) throws ReflectiveOperationException {
        if (!listeners.containsKey(object.getClass())) {
            List<Method> changeListenerMethods = new ArrayList<>();
            for (Method method : object.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(TransferGridChangeListener.class)) {
                    method.setAccessible(true);
                    changeListenerMethods.add(method);
                }
            }
            listeners.put(object.getClass(), changeListenerMethods);
            System.out.println("added " + object + " to list");
        }

        for (Method method : listeners.get(object.getClass())) {
            System.out.println("invoking");
            method.invoke(object, field);
        }
    }



}
