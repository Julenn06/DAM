namespace ariketa09
{
    public class Employee
    {
        public required string Name;
        public int Age;
        public double Salary;

        public static List<Employee> employees = new List<Employee>
        {
            new Employee { Name = "Ana", Age = 28, Salary = 2000 },
            new Employee { Name = "Luis", Age = 35, Salary = 2500 },
            new Employee { Name = "Marta", Age = 40, Salary = 3000 }
        };
    }
}