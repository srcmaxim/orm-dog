package org.dog.loader;


import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JARClassLoader extends ClassLoader {

    @Override
    protected List<String> getClassNames(String aPackage) throws Exception {
        URL url = getClass().getClassLoader().getResource("META-INF");
        List<String> files = new ArrayList<>();
        JarURLConnection jarURL = (JarURLConnection) (url.openConnection());
        try (JarFile jar = jarURL.getJarFile()) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                String file = entries.nextElement().getName();
                files.add(file);
            }
        }
        return files;
    }
}
