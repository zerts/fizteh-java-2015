package ru.fizteh.fivt.students.zerts.miniorm;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Zerts on 17.12.2015.
 */

public class User {
    /*@Table(name = "TESTTABLE")
    static class Tab {
        @PrimaryKey
        @Column(name = "ID")
        Integer a;

        @Column(name = "STRING")
        String s;

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Tab) {
                return ((Objects.equals(this.a, ((Tab) obj).a)) && (Objects.equals(this.s, ((Tab) obj).s)));
            }
            return false;
        }

        Tab(Object a, Object s) {
            this.a = (Integer) a;
            this.s = (String) s;
        }

        Tab() {
            this.a = 0;
            this.s = "";
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder().append("Id = ").append(a).append(", String = ").append(s);
            return result.toString();
        }
    }*/

    public static void main(String[] argv) throws IOException, IllegalAccessException, SQLException,
            InstantiationException {
        /*MiniOrm<Tab> bd = new MiniOrm<>(Tab.class);
        try {
            bd.createTable();
            bd.insert(new Tab(1, "one"));
            List<Tab> all = bd.queryForAll();
            all.forEach(System.out::println);

            bd.insert(new Tab(2, "two"));
            bd.insert(new Tab(3, "three"));
            bd.insert(new Tab(4, "four"));

            Tab elem = bd.queryById(3);
            System.out.println(elem);

            all = bd.queryForAll();
            all.forEach(System.out::println);
        } finally {
            bd.dropTable();
        }*/
    }
}
