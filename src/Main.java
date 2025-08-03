import java.util.*;
import java.io.*;   
class Expense {
    double amount;
    String category;
    String date;

    Expense(double amount, String category, String date) {
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    @Override
    public String toString() {
        return date + " | " + category + " | " + amount;
    }
    public String toCSV() {
        return amount + "," + category + "," + date;
    }

    public static Expense fromCSV(String line) {
        String[] parts = line.split(",");
        double amount = Double.parseDouble(parts[0]);
        String category = parts[1];
        String date = parts[2];
        return new Expense(amount, category, date);
    }
}

public class Main {
    static final String FILE_NAME = "expenses.txt";

    public static void loadExpenses(ArrayList<Expense> expenses) {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) return; 

            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                expenses.add(Expense.fromCSV(line));
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error loading expenses: " + e.getMessage());
        }
    }

    public static void saveExpenses(ArrayList<Expense> expenses) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME));
            for (Expense e : expenses) {
                writer.println(e.toCSV());
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error saving expenses: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ArrayList<Expense> expenses = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        loadExpenses(expenses);

        int choice;
        do {
            System.out.println("\n---- Smart Expense Tracker ----");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. Category Summary");
            System.out.println("4. Monthly Summary");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter amount: ");
                    double amount = sc.nextDouble();
                    sc.nextLine();

                    System.out.print("Enter category: ");
                    String category = sc.nextLine();

                    System.out.print("Enter date (YYYY-MM-DD): ");
                    String date = sc.nextLine();

                    Expense exp = new Expense(amount, category, date);
                    expenses.add(exp);
                    saveExpenses(expenses);

                    System.out.println("✅ Expense added!");
                    break;

                case 2:
                    System.out.println("\n--- All Expenses ---");
                    if (expenses.isEmpty()) {
                        System.out.println("No expenses recorded yet.");
                    } else {
                        for (Expense e : expenses) {
                            System.out.println(e);
                        }
                    }
                    break;

                case 3:
                    System.out.println("\n--- Category Summary ---");
                    if (expenses.isEmpty()) {
                        System.out.println("No expenses to summarize.");
                    } else {
                        HashMap<String, Double> categorySummary = new HashMap<>();
                        for (Expense e : expenses) {
                            categorySummary.put(
                                e.category,
                                categorySummary.getOrDefault(e.category, 0.0) + e.amount
                            );
                        }
                        for (String cat : categorySummary.keySet()) {
                            System.out.println(cat + " : " + categorySummary.get(cat));
                        }
                    }
                    break;

                case 4:
                    System.out.println("\n--- Monthly Summary ---");
                    if (expenses.isEmpty()) {
                        System.out.println("No expenses to summarize.");
                    } else {
                        HashMap<String, Double> monthSummary = new HashMap<>();
                        for (Expense e : expenses) {
                            String month = e.date.substring(0, 7);
                            monthSummary.put(
                                month,
                                monthSummary.getOrDefault(month, 0.0) + e.amount
                            );
                        }
                        for (String month : monthSummary.keySet()) {
                            System.out.println(month + " : " + monthSummary.get(month));
                        }
                    }
                    break;

                case 5:
                    System.out.println("👋 Exiting... Thank you for using Smart Expense Tracker!");
                    break;

                default:
                    System.out.println("❌ Invalid choice! Please try again.");
            }
        } while (choice != 5);

        sc.close();
    }
}

