package ru.fizteh.fivt.students.zerts.collectionquery;

import ru.fizteh.fivt.students.zerts.collectionquery.impl.Tuple;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.fizteh.fivt.students.zerts.collectionquery.Aggregates.avg;
import static ru.fizteh.fivt.students.zerts.collectionquery.Aggregates.count;
import static ru.fizteh.fivt.students.zerts.collectionquery.CollectionQuery.Student.student;
import static ru.fizteh.fivt.students.zerts.collectionquery.Conditions.rlike;
import static ru.fizteh.fivt.students.zerts.collectionquery.OrderByConditions.asc;
import static ru.fizteh.fivt.students.zerts.collectionquery.OrderByConditions.desc;
import static ru.fizteh.fivt.students.zerts.collectionquery.Sources.list;
import static ru.fizteh.fivt.students.zerts.collectionquery.impl.FromStmt.from;

public class CollectionQuery {

    /**
     * Make this code work!
     *
     * @param args
     */
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        Iterable<Statistics> statistics =
                from(list(
                        student("iglina", LocalDate.parse("1986-08-06"), "494"),
                        student("kargaltsev", LocalDate.parse("1986-08-06"), "495"),
                        student("zertsalov", LocalDate.parse("1986-08-06"), "495"),
                        student("ivanov", LocalDate.parse("1986-08-06"), "494")))
                        .select(Statistics.class, Student::getGroup, count(Student::getGroup), avg(Student::age))
                        .where(rlike(Student::getName, ".*ov").and(s -> s.age() > 20))
                        .groupBy(Student::getName)
                        .having(s -> s.getCount() > 0)
                        .orderBy(asc(Statistics::getGroup), desc(count(Statistics::getGroup)))
                        .limit(100)
                        .union()
                        .from(list(student("ivanov", LocalDate.parse("1985-08-06"), "494"),
                                student("ivanova", LocalDate.parse("1975-08-06"), "494")))
                        .selectDistinct(Statistics.class, s -> "all", count(Student::getDateOfBith), avg(Student::age))
                        .groupBy(Student::getGroup)
                        .execute();
        statistics.forEach(System.out::print);

        /*List<Student> ex = new ArrayList<>();
        ex.add(student("iglina", LocalDate.parse("1986-08-06"), "494"));
        ex.add(student("kargaltsev", LocalDate.parse("1986-08-06"), "495"));
        ex.add(student("zertsalov", LocalDate.parse("1986-08-06"), "495"));
        ex.add(student("ivanov", LocalDate.parse("1986-08-06"), "494"));
        ex.sort(asc(Student::getGroup));
        System.out.println(ex);*/

        /*Iterable<Student> statistics =
                from(list(
                        student("iglina", LocalDate.parse("1986-08-06"), "494"),
                        student("zvereva", LocalDate.parse("1986-08-06"), "494"),
                        student("zuev", LocalDate.parse("1976-08-06"), "494"),
                        student("kargaltsev", LocalDate.parse("1986-08-06"), "495"),
                        student("zertsalov", LocalDate.parse("1986-08-06"), "495"),
                        student("kargaltsev", LocalDate.parse("1986-08-06"), "495"),
                        student("garkavyy", LocalDate.parse("1986-08-06"), "495"),
                        student("ivanov", LocalDate.parse("1989-08-06"), "494")))
                        .select(Student.class, Student::getName, Student::getGroup)
                        .union()
                        .from(list(student("iglina", LocalDate.parse("1986-08-06"), "494"),
                                student("zvereva", LocalDate.parse("1986-08-06"), "494"),
                                student("iglina", LocalDate.parse("1986-08-06"), "494")))
                                .select(Student.class, Student::getName, Student::getGroup)
                                .execute();*/
        //Tuple a = new Tuple("1", "2");
        //System.out.println(a.getFirst().getClass());
        //System.out.println(a.getClass());

        Iterable<Tuple<String, String>> mentorsByStudent =
                from(list(student("ivanov", LocalDate.parse("1985-08-06"), "494"),
                            student("iglina", LocalDate.parse("1986-08-06"), "494"),
                            student("kargaltsev", LocalDate.parse("1986-08-06"), "495"),
                            student("zertsalov", LocalDate.parse("1986-08-06"), "495")))
                        .join(list(Group.group("494", "mr.sidorov")))
                        .on((s, g) -> Objects.equals(s.getGroup(), g.getGroup()))
                        .select(sg -> sg.getFirst().getName(), sg -> sg.getSecond().getMentor())
                        .where(s -> Objects.equals(s.getFirst().getName(), "iglina"))
                        .union()
                        .from(list(student("ivanov", LocalDate.parse("1985-08-06"), "494"),
                                student("iglina", LocalDate.parse("1986-08-06"), "494"),
                                student("kargaltsev", LocalDate.parse("1986-08-06"), "495"),
                                student("zertsalov", LocalDate.parse("1986-08-06"), "495")))
                        .join(list(Group.group("495", "mr.kormushin")))
                        .on(s -> s.getGroup(), f -> f.getGroup())
                        .select(sg -> sg.getFirst().getName(), sg -> sg.getSecond().getMentor())
                        .execute();
        mentorsByStudent.forEach(System.out::print);
    }

    public static class Student {
        private final String name;

        private final LocalDate dateOfBith;

        private final String group;

        public String getName() {
            return name;
        }

        /*public Student(String group) {
            this.name = null;
            this.dateOfBith = null;
            this.group = group;
        }*/

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

        public Double age() {
            return (double) ChronoUnit.YEARS.between(getDateOfBith(), LocalDateTime.now());
        }

        public static Student student(String name, LocalDate dateOfBith, String group) {
            return new Student(name, dateOfBith, group);
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder().append("Student{");
            if (group != null) {
                result.append("group='").append(group).append('\'');
            }
            if (name != null) {
                result.append(", name=").append(name);
            }
            if (dateOfBith != null) {
                result.append(", age=").append(dateOfBith);
            }
            result.append("}\n");
            return result.toString();
        }
    }

    public static class Group {
        private final String group;
        private final String mentor;

        public Group(String group, String mentor) {
            this.group = group;
            this.mentor = mentor;
        }

        public String getGroup() {
            return group;
        }

        public String getMentor() {
            return mentor;
        }

        public static Group group(String ggroup, String mmentor) {
            return new Group(ggroup, mmentor);
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder().append("Student{");
            if (group != null) {
                result.append("group='").append(group).append('\'');
            }
            if (mentor != null) {
                result.append(", name=").append(mentor);
            }
            result.append("}\n");
            return result.toString();
        }
    }


    public static class Statistics {

        private final String group;
        private final Integer count;
        private final Double age;

        public String getGroup() {
            return group;
        }

        public Integer getCount() {
            return count;
        }

        public Double getAge() {
            return age;
        }

        public Statistics(String group, Integer count) {
            this.group = group;
            this.count = count;
            this.age = null;
        }

        public Statistics(String group, Integer count, Double age) {
            this.group = group;
            this.count = count;
            this.age = age;
        }

        public Statistics(String group) {
            this.group = group;
            this.count = null;
            this.age = null;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder().append("Statistics{");
            if (group != null) {
                result.append("group='").append(group).append('\'');
            }
            if (count != null) {
                result.append(", count=").append(count);
            }
            if (age != null) {
                result.append(", age=").append(age);
            }
            result.append("}\n");
            return result.toString();
        }
    }

}
