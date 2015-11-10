package ru.fizteh.fivt.students.zerts.collectionquery;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static ru.fizteh.fivt.students.zerts.collectionquery.Aggregates.count;
import static ru.fizteh.fivt.students.zerts.collectionquery.CollectionQuery.Student.student;
import static ru.fizteh.fivt.students.zerts.collectionquery.OrderByConditions.asc;
import static ru.fizteh.fivt.students.zerts.collectionquery.Sources.list;
import static ru.fizteh.fivt.students.zerts.collectionquery.impl.FromStmt.from;

/**
 * @author akormushin
 */
public class CollectionQuery {

    /**
     * Make this code work!
     *
     * @param args
     */
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        /*Iterable<Statistics> statistics =
                from(list(
                        student("iglina", LocalDate.parse("1986-08-06"), "494"),
                        student("kargaltsev", LocalDate.parse("1986-08-06"), "495"),
                        student("zertsalov", LocalDate.parse("1986-08-06"), "495"),
                        student("ivanov", LocalDate.parse("1986-08-06"), "494")))
                        .select(Statistics.class, Student::getGroup, count(Student::getGroup), avg(Student::age))
                        //.where(rlike(Student::getName, ".*ov").and(s -> s.age() > 20))
                        .groupBy(Student::getName)
                        //.having(s -> s.getCount() > 0)
                        //.orderBy(asc(Student::getGroup), desc(count(Student::getGroup)))
                        /*.limit(100)
                        .union()
                        .from(list(student("ivanov", LocalDate.parse("1985-08-06"), "494")))
                        .selectDistinct(Statistics.class, s -> "all", count(s -> 1), avg(Student::age))*/
                        /*.execute();*/

        /*List<Student> ex = new ArrayList<>();
        ex.add(student("iglina", LocalDate.parse("1986-08-06"), "494"));
        ex.add(student("kargaltsev", LocalDate.parse("1986-08-06"), "495"));
        ex.add(student("zertsalov", LocalDate.parse("1986-08-06"), "495"));
        ex.add(student("ivanov", LocalDate.parse("1986-08-06"), "494"));
        ex.sort(asc(Student::getGroup));
        System.out.println(ex);*/

        Iterable<Statistics> statistics =
                from(list(
                        student("iglina", LocalDate.parse("1986-08-06"), "494"),
                        student("zvereva", LocalDate.parse("1986-08-06"), "494"),
                        student("zuev", LocalDate.parse("1986-08-06"), "494"),
                        student("kargaltsev", LocalDate.parse("1986-08-06"), "495"),
                        student("zertsalov", LocalDate.parse("1986-08-06"), "495"),
                        student("kargaltsev", LocalDate.parse("1986-08-06"), "495"),
                        student("garkavyy", LocalDate.parse("1986-08-06"), "495"),
                        student("ivanov", LocalDate.parse("1986-08-06"), "494")))
                        .select(Statistics.class, Student::getGroup, count(Student::getName))
                        .groupBy(Student::getGroup)
                        .orderBy(asc(Statistics::getCount))
                        .execute();
        System.out.println(statistics);
    }


    public static class Student {
        private final String name;

        private final LocalDate dateOfBith;

        private final String group;

        public String getName() {
            return name;
        }

        public Student(String group) {
            this.name = null;
            this.dateOfBith = null;
            this.group = group;
        }

        public Student(String name, LocalDate dateOfBith, String group) {
            this.name = name;
            this.dateOfBith = dateOfBith;
            this.group = group;
        }

        public Student(String name, String group) {
            this.name = name;
            this.dateOfBith = null;
            this.group = group;
        }

        public LocalDate getDateOfBith() {
            return dateOfBith;
        }

        public String getGroup() {
            return group;
        }

        public long age() {
            return ChronoUnit.YEARS.between(getDateOfBith(), LocalDateTime.now());
        }

        public static Student student(String name, LocalDate dateOfBith, String group) {
            return new Student(name, dateOfBith, group);
        }

        @Override
        public String toString() {
            return "Statistics{"
                    + "group='" + group + '\''
                    + ", name=" + name
                    + ", dateOfBith=" + dateOfBith
                    + '}';
        }
    }


    public static class Statistics {

        private final String group;
        private final Integer count;
        private final Integer age;

        public String getGroup() {
            return group;
        }

        public Integer getCount() {
            return count;
        }

        public Integer getAge() {
            return age;
        }

        public Statistics(String group, Integer count) {
            this.group = group;
            this.count = count;
            this.age = null;
        }

        public Statistics(String group, Integer count, Integer age) {
            this.group = group;
            this.count = count;
            this.age = age;
        }

        public Statistics(String group) {
            this.group = group;
            this.count = (Integer) 0;
            this.age = (Integer) 0;
        }

        @Override
        public String toString() {
            return "Statistics{"
                    + "group='" + group + '\''
                    + ", count=" + count
                    + ", age=" + age
                    + '}';
        }
    }

}
