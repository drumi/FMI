package problem6;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class EmployeeTest {
    List<Employee> employees;

    {
        setup();
    }

    public void setup() {
        employees = new ArrayList<>();
        employees.add(new Employee(123, "Jack", "Johnson", LocalDate.of(1988, Month.APRIL, 12)));
        employees.add(new Employee(345, "Cindy", "Bower", LocalDate.of(2011, Month.DECEMBER, 15)));
        employees.add(new Employee(567, "Perry", "Node", LocalDate.of(2005, Month.JUNE, 07)));
        employees.add(new Employee(467, "Pam", "Krauss", LocalDate.of(2005, Month.JUNE, 07)));
        employees.add(new Employee(435, "Fred", "Shak", LocalDate.of(1988, Month.APRIL, 17)));
        employees.add(new Employee(678, "Ann", "Lee", LocalDate.of(2007, Month.APRIL, 12)));
    }

    public static void main(String[] args) {
        var test = new EmployeeTest();

        System.out.println("\nAscending employee number");
        test.ascending();

        System.out.println("\nDescending hireDate");
        test.descending();

        System.out.println("\nLongest worked employee");
        test.longest();

        System.out.println("\nAscending first name descending last name");
        test.ascendingDescending();
    }

    private void ascending() {
        employees.stream()
                 .sorted(Comparator.comparingInt(Employee::getEmployeeNumber))
                 .forEach(System.out::println);
    }

    private void descending() {
        employees.stream()
                 .sorted(Comparator.comparing(Employee::getHireDate).reversed())
                 .forEach(System.out::println);
    }

    private Optional<Employee> longest() {
        var longest = employees.stream()
                               .max(Comparator.comparing(Employee::getHireDate));

        longest.ifPresent(System.out::println);

        return longest;
    }

    private void ascendingDescending() {
        employees.stream()
                 .sorted(Comparator.comparing(Employee::getEmployeeFirstName)
                                   .thenComparing(
                                       Comparator.comparing(Employee::getEmployeeLastName).reversed())
                                    )
                 .forEach(System.out::println);
    }


}
