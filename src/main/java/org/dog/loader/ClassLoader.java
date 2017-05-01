package org.dog.loader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ClassLoader {


    public static ClassLoader getLoader() throws IllegalStateException {
        if (usingJARLoader()) {
            return new JARClassLoader();
        }
        if (usingFileLoader()) {
            return new FileClassLoader();
        }
        throw new IllegalStateException("Can't use File or JAR Class Loaders");
    }

    private static boolean usingFileLoader() {
        return null != java.lang.ClassLoader.getSystemClassLoader();
    }

    private static boolean usingJARLoader() {
        URL resource = ClassLoader.class.getClassLoader().getResource("META-INF");
        return resource.getPath().contains("orm-dog");
    }

    protected abstract List<String> getClassNames(String aPackage) throws Exception;

    public List<Class> loadClasses(String aPackage) throws Exception {
        List<String> classNames = getClassNames(aPackage);

        String packageName = "(([\\w]*)([\\\\/]))+";
        String className = "([\\w]+)(?=\\.class)";
        Pattern classPattern = Pattern.compile(
                packageName + className);

        List<Class> classes = new ArrayList<>();

        for (String s : classNames) {
            Matcher matcher = classPattern.matcher(s);
            if (matcher.find()) {
                String group = matcher.group();
                String replace = group.replaceAll("[\\\\/]", ".");
                int i = replace.indexOf(aPackage);
                if (i == -1) {
                    continue;
                }
                String substring = replace.substring(i);
                Class aClass = Class.forName(substring);
                classes.add(aClass);
            }
        }
        return classes;
    }

}
