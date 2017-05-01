package org.dog;

import org.dog.db.mapper.TableMapper;
import org.dog.loader.ClassLoader;
import org.dog.util.Closer;

import java.sql.Connection;
import java.sql.DriverManager;

public class DogORM {

    private Connection connection;

    private DogORM() {
    }

    public Transaction transaction() {
        return new Transaction(connection);
    }

    public void close() {
        Closer.close(connection);
    }

    public static Builder builder() {
        return new DogORM().new Builder();
    }

    public class Builder {
        private String url;
        private String user;
        private String password;
        private String aPackage;

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setUser(String user) {
            this.user = user;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder loadClasses(String aPackage) {
            this.aPackage = aPackage;
            return this;
        }

        public DogORM build() throws Exception {
            connection = DriverManager.getConnection(url, user, password);
            /* generate sql for classes */
            for (Class clazz : ClassLoader.getLoader().loadClasses(aPackage)) {
                TableMapper.link(clazz);
            }
            return DogORM.this;
        }
    }

}
