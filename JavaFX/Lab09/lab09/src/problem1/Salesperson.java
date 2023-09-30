/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem1;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 *
 * @author echrk
 */
@FunctionalInterface
interface Adder<T extends Salesperson>{
    T add (T op1, T op2);
    default String printNumSales (T obj){
        return "Adder " + obj.getNumSales() ;
    }
    static void printSales(Salesperson s)
    {
        System.out.println(s.getNumSales());
    }
}

public class Salesperson implements Adder<Salesperson>
{
   private String name;
   private double salary;
   private int numsales;

  public   Salesperson(String name, double salary, int numsales)
   {
      this.name = name;
      this.salary = salary;
      this.numsales = numsales;
   }

  public   void addBonus(double amount)
   {
      salary += amount;
   }

  public   int getNumSales()
   {
      return numsales;
   }
  public   double getSalary()
   {
      return salary;
   }
  public  String printNumSales (Salesperson obj){
       
        return String.format("%s Sales: %d",Adder.super.printNumSales(obj),
                   obj.getNumSales());
    }
  
    public static Set<Salesperson> distinct (List<Salesperson> list) {
      if (list == null)
          return null;

        return new HashSet<>(list);
    }
   @Override
   public String toString()
   {
      return String.format("name: %s, salary: %.2f numsales: %d ", 
                              name, salary, numsales);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Salesperson that = (Salesperson) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public Salesperson add(Salesperson op1, Salesperson op2) {
        return new Salesperson(op1.name, op1.salary, op1.numsales+ op2.numsales); //To change body of generated methods, choose Tools | Templates.
    }
}

 class LambdaDemo
{
   public static void main(String[] args)
   {
      Predicate<Salesperson> predicate1 = sp -> sp.getNumSales() > 1200; // да се инициализира
      Predicate<Salesperson> predicate2 = sp -> sp.getNumSales() <= 900; // да се инициализира
      Predicate<Salesperson> predicate = predicate1.or(predicate2); // да се инициализира

      Consumer<Salesperson> consumer1 = sp -> {
          sp.addBonus(0.05 * sp.getSalary());
          System.out.println(sp);
      }; // да се инициализира
      Consumer<Salesperson> consumer2 = sp -> {
          if (predicate1.test(sp)) {
              sp.addBonus(sp.getSalary() * 0.02);
          } else {
              sp.addBonus(sp.getSalary() * -0.02);
          }

          System.out.println(sp);
      }; // да се инициализира

      Comparator<Salesperson> comparator1 = (sp1, sp2) -> Double.compare(sp2.getSalary(), sp1.getSalary()); // да се инициализира
      Comparator<Salesperson> comparator2 = (sp1, sp2) -> {
          int comp = Double.compare(sp2.getSalary(), sp1.getSalary());

          if (comp == 0) {
              return sp1.getNumSales() - sp2.getNumSales();
          } else {
              return comp;
          }
      }; // да се инициализира

       Random random = new Random();
      Salesperson[] salespersons =
      {
         new Salesperson("John Doe", 2000, 949),
         new Salesperson("Jane Doe", 3900, 1500),
         new Salesperson("Jack Doe", random.nextInt(1000, 10_001), random.nextInt(1000, 4000)),
         new Salesperson("Will Doe", random.nextInt(1000, 10_001), random.nextInt(1000, 4000)),
         new Salesperson("Kane Doe", random.nextInt(1000, 10_001), random.nextInt(1000, 4000)),
         new Salesperson("Bron Doe", random.nextInt(1000, 10_001), random.nextInt(1000, 4000)),
         new Salesperson("Jill Doe", random.nextInt(1000, 10_001), random.nextInt(1000, 4000)),
         new Salesperson("Jade Doe", random.nextInt(1000, 10_001), random.nextInt(1000, 4000)),
         new Salesperson("Jean Doe", random.nextInt(1000, 10_001), random.nextInt(1000, 4000)),
         new Salesperson("Beck Boe", random.nextInt(1000, 10_001), random.nextInt(1000, 4000))
      // да се добавят  още 10 обекти от тип Salesperson
      // или да се инициализират с Random стойности

      };

       System.out.println(salespersons[0].printNumSales(salespersons[1]));
       System.out.println("pred1 " + predicate1.test(salespersons[0]));
       System.out.println("pred2 " + predicate1.test(salespersons[1]));
       consumer1.accept(salespersons[0]);
       consumer2.accept(salespersons[0]);
       consumer2.accept(salespersons[1]);
       System.out.println("Test comparator1: " + comparator1.compare(salespersons[0], salespersons[1]));

       Salesperson added;
       added = salespersons[0].add(salespersons[0], salespersons[1]);
       System.out.println("Using added: " + added);

       System.out.println("\nApply bonus: ");

       List<Salesperson> listOfSalespersons = new ArrayList<>();

       listOfSalespersons.addAll(Arrays.asList(salespersons));
      for (Salesperson salesperson: salespersons)
      {
         applyBonus(salesperson, predicate1,   consumer1);
         System.out.println(salesperson);
         salesperson.printNumSales(salesperson);

      }

       System.out.println("\nApply bonus 2: ");

       for (Salesperson salesperson: salespersons)
      {
         applyBonus(salesperson, predicate2,  consumer2);
         System.out.println(salesperson);
      }

       System.out.println("\nSort by comparator1: ");
       sort(listOfSalespersons, comparator1);
       System.out.println(listOfSalespersons);

       System.out.println("\nSort by comparator2: ");
       sort(listOfSalespersons, comparator2);
       System.out.println(listOfSalespersons);

   }
     
   public static void applyBonus(Salesperson salesperson,
                                 Predicate<Salesperson> predicate,
                                 Consumer<Salesperson> consumer)
   {
      // Напишете if  команда, където използвайте predicate 
      //    за да определите дали да изпълните consumer
      // Изпълнете consumer, когато условието на if  командата е изпълнено
       if (salesperson == null || predicate == null || consumer == null)
           return;

       if (predicate.test(salesperson))
           consumer.accept(salesperson);
   }
 
   public static void applyBonus(List<Salesperson> salespersons,
                                 Predicate<Salesperson> predicate,
                                 Consumer<Salesperson> consumer)
   {
      // Напишете if  команда, където използвайте predicate 
      //    за да определите дали да изпълните consumer
      // Изпълнете consumer, когато условието на if  командата е изпълнено

       if (salespersons == null || predicate == null || consumer == null)
           return;

       for (var sp : salespersons) {
           if (sp != null && predicate.test(sp))
               consumer.accept(sp);
       }
   }

   public static void sort(List<Salesperson> salespersons,
                                 Comparator<Salesperson> comparator)
   {
      if (salespersons == null || comparator == null)
          return;

      salespersons.sort(comparator);
   }

}

