package ru.fizteh.fivt.students.zerts.miniorm;

import com.google.common.base.CaseFormat;
import org.h2.jdbcx.JdbcConnectionPool;
import ru.fizteh.fivt.students.zerts.miniorm.annotations.Column;
import ru.fizteh.fivt.students.zerts.miniorm.annotations.PrimaryKey;
import ru.fizteh.fivt.students.zerts.miniorm.annotations.Table;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * Created by Zerts on 17.12.2015.
 */

public class MiniOrm<T> implements Closeable {
    private String connection;
    private String username;
    private String password;

    private String tableName;
    private Class<T> tableClass;

    private int primaryKeyFieldNumber = -1;

    private Field[] fields;
    private String[] namesOfColumns;
    private JdbcConnectionPool connectionTool;


    private void init(Class<T> currClass, String propertiesPath) throws IOException {
        Properties properties = new Properties();
        try (InputStream input = this.getClass().getResourceAsStream(propertiesPath)) {
            properties.load(input);
        }
        connection = properties.getProperty("connection_name");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
        tableClass = currClass;

        if (!tableClass.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("Class is not a Table");
        }
        if (Objects.equals(tableClass.getAnnotation(Table.class).name(), "")) {
            tableName = tableClass.getSimpleName();
        } else {
            tableName = tableClass.getAnnotation(Table.class).name();
        }

        List<String> columnNames = new ArrayList<>();
        List<Field> columns = new ArrayList<>();
        for (Field currColumn : tableClass.getDeclaredFields()) {
            if (!currColumn.isAnnotationPresent(Column.class)) {
                throw new IllegalArgumentException("Not all fields are columns");
            }
            String currColumnName = currColumn.getAnnotation(Column.class).name();
            if (Objects.equals(currColumnName, "")) {
                currColumnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, currClass.getName());
            }

            if (currColumn.isAnnotationPresent(PrimaryKey.class)) {
                if (primaryKeyFieldNumber != -1) {
                    throw new IllegalArgumentException("Not one primary Key");
                } else {
                    primaryKeyFieldNumber = columns.size();
                }
            }

            columnNames.add(currColumnName);
            columns.add(currColumn);
        }

        fields = new Field[columns.size()];
        fields = columns.toArray(fields);

        namesOfColumns = new String[columnNames.size()];
        namesOfColumns = columnNames.toArray(namesOfColumns);

        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("No H2 driver found");
        }

        connectionTool = JdbcConnectionPool.create(connection, username, password);
    }

    public MiniOrm(Class<T> currClass) throws IOException {
        this.init(currClass, "/db.properties");
    }

    public <K> T queryById(K key) throws SQLException, IllegalAccessException, InstantiationException {
        StringBuilder query = new StringBuilder().append("SELECT * FROM ").append(tableName).append(" WHERE ")
                .append(namesOfColumns[primaryKeyFieldNumber]).append("=").append(key.toString());
        System.out.println(query.toString());
        Connection connect = connectionTool.getConnection();
        ResultSet baseResult = connect.createStatement().executeQuery(query.toString());
        if (baseResult.next()) {
            T element = tableClass.newInstance();
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getClass().isAssignableFrom(Number.class)) {
                    Long currValue = baseResult.getLong(namesOfColumns[i]);
                    fields[i].set(element, currValue);
                } else if (fields[i].getType() == String.class) {
                    String currValue = baseResult.getString(namesOfColumns[i]);
                    fields[i].set(element, currValue);
                } else {
                    Object currValue = baseResult.getObject(namesOfColumns[i]);
                    fields[i].set(element, currValue);
                }
            }
            return element;
        }
        return null;
    }

    public List<T> queryForAll() throws SQLException, IllegalAccessException, InstantiationException {
        List<T> result = new ArrayList<>();
        StringBuilder query = new StringBuilder().append("SELECT * FROM ").append(tableName);
        Connection connect = connectionTool.getConnection();
        ResultSet baseResult = connect.createStatement().executeQuery(query.toString());
        while (baseResult.next()) {
            T element = tableClass.newInstance();
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getClass().isAssignableFrom(Number.class)) {
                    Long currValue = baseResult.getLong(namesOfColumns[i]);
                    fields[i].set(element, currValue);
                } else if (fields[i].getType() == String.class) {
                    String currValue = baseResult.getString(namesOfColumns[i]);
                    fields[i].set(element, currValue);
                } else {
                    Object currValue = baseResult.getObject(namesOfColumns[i]);
                    fields[i].set(element, currValue);
                }
            }
            result.add(element);
        }
        return result;
    }

    public void insert(T element) throws IllegalAccessException, SQLException {
        StringBuilder query = new StringBuilder().append("INSERT INTO ").append(tableName).append(" VALUES(");
        for (int i = 0; i < fields.length; i++) {
            if (i != 0) {
                query.append(", ");
            }
            query.append("?");
        }
        query.append(")");
        try (Connection connect = connectionTool.getConnection()) {
            PreparedStatement statement = connect.prepareStatement(query.toString());
            for (int i = 0; i < fields.length; i++) {
                statement.setObject(i + 1, fields[i].get(element));
            }
            statement.execute();
        }
    }

    public void update(T element) throws IllegalAccessException, SQLException {
        for (int i = 0; i < namesOfColumns.length; i++) {
            StringBuilder query = new StringBuilder().append("UPDATE ").append(tableName).append(" SET ")
                    .append(namesOfColumns[i]).append("=").append("?").append(" WHERE ")
                    .append(namesOfColumns[primaryKeyFieldNumber]).append("=").append("?");
            try (Connection connect = connectionTool.getConnection()) {
                PreparedStatement statement = connect.prepareStatement(query.toString());
                statement.setObject(1, fields[i].get(element));
                statement.setObject(2, fields[primaryKeyFieldNumber].get(element));
                statement.execute();
            }
        }
    }

    public void delete(T element) throws IllegalAccessException, SQLException {
        StringBuilder query = new StringBuilder().append("DELETE FROM ").append(tableName).append(" WHERE ")
                .append(namesOfColumns[primaryKeyFieldNumber]).append("=")
                .append("?");
        try (Connection connect = connectionTool.getConnection()) {
            PreparedStatement statement = connect.prepareStatement(query.toString());
            statement.setObject(1, fields[primaryKeyFieldNumber].get(element));
            statement.execute();
        }
    }

    public void createTable() {
        StringBuilder query = new StringBuilder().append("CREATE TABLE IF NOT EXISTS ").append(tableName).append("(");
        for (int i = 0; i < fields.length; ++i) {
            query.append(namesOfColumns[i]).append(" ")
                    .append(ClassConverter.convert(fields[i].getType()));
            if (i == primaryKeyFieldNumber) {
                query.append(" PRIMARY KEY");
            }
            if (i != fields.length - 1) {
                query.append(", ");
            }
        }
        query.append(")");
        execute(query);
    }

    public void dropTable() {
        StringBuilder query = new StringBuilder().append("DROP TABLE IF EXISTS ").append(tableName);
        execute(query);
    }

    private void execute(StringBuilder query) {
        try (Connection conn = connectionTool.getConnection()) {
            conn.createStatement().execute(query.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        if (connectionTool != null) {
            connectionTool.dispose();
        }
    }

}
