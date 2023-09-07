package my.notify.bd.dto;

import java.util.Objects;

public class User {
    private Integer id;
    private String name;
    private Integer year;
    private String month;
    private Integer day;
    private Integer age;

    public User() {
    }

    public User(Integer id, String name, Integer year, String month, Integer day, Integer age) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.month = month;
        this.day = day;
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", year=" + year +
                ", month='" + month + '\'' +
                ", day=" + day +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(year, user.year) &&
                Objects.equals(month, user.month) &&
                Objects.equals(day, user.day) &&
                Objects.equals(age, user.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, year, month, day, age);
    }
}
