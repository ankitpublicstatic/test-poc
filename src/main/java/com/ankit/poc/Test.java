package com.ankit.poc;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

// Write a Java method that accepts a list of objects ”people” and a list of search values
// “searchData” as arguments. It should return all objects where any property (id or name) matches
// any of the given search values.
//
// people = Arrays.asList(
// new Person("123", "AA"),
// new Person("333", "BBBB"),
// new Person("444", "CCC"),
// new Person("555", "CCC")
// );
//
// searchData = ["444", "XYZ", "CCC"]
//
// Output: [{id: 444, name: CCC}, {id: 555, name: CCC}]

class Person {
  private String id;
  private String name;

  @Override
  public String toString() {
    return "Person [id=" + id + ", name=" + name + "]";
  }

  public Person(String id, String name) {
    super();
    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Person other = (Person) obj;
    return Objects.equals(id, other.id) && Objects.equals(name, other.name);
  }

}


public class Test {
  public static List<Person> people = List.of(new Person("123", "AA"), new Person("333", "BBBB"),
      new Person("444", "CCC"), new Person("555", "CCC"));

  public static void main(String[] args) {
    System.out.println(Test.searchPersonByAnyFields(List.of("444", "XYZ", "CCC")));
  }

  public static Set<Person> searchPersonByAnyFields(List<String> searchKeyword) {
    Set<Person> result = new HashSet();
    for (String keyword : searchKeyword) {
      if (keyword != null) {
        Set<Person> persons =
            people.stream().filter(person -> person.getId().equalsIgnoreCase(keyword)
                || person.getName().equalsIgnoreCase(keyword)).collect(Collectors.toSet());
        result.addAll(persons);
      }
    }
    return result;
  }

}
