package org.dog.loader;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

public class FileClassLoader extends ClassLoader {

    @Override
    protected List<String> getClassNames(String aPackage) throws Exception {
        String packageToPath = aPackage.replace(".", "\\");
        URLClassLoader loader = (URLClassLoader) java.lang.ClassLoader.getSystemClassLoader();
        return Collections.list(loader.getResources(packageToPath)).stream()
                .map(url -> {
                    File value = null;
                    try {
                        value = new File(URLDecoder.decode(url.getPath(), "UTF-8"));
                        return Optional.of(value);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return Optional.ofNullable(value);
                })
                .filter(Optional::isPresent)
                .filter(file -> file.get().exists())
                .flatMap(file -> Arrays.stream(file.get().listFiles()))
                .map(File::getPath)
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
