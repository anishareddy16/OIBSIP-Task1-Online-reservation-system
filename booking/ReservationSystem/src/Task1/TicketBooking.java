package Task1;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class TicketBooking {
    static Scanner sc = new Scanner(System.in);
    static final String FILE_PATH = "tickets.dat";
    static List<Ticket> tickets = loadTickets();
    static final String USERNAME = "user";
    static final String PASSWORD = "pass";

    static class Train {
        String number, name, depTime;
        int fareAC, fareSleeper, fareGeneral;
        int seatsAC = 20, seatsSleeper = 30, seatsGeneral = 50;

        Train(String number, String name, String depTime, int ac, int sl, int gn) {
            this.number = number;
            this.name = name;
            this.depTime = depTime;
            this.fareAC = ac;
            this.fareSleeper = sl;
            this.fareGeneral = gn;
        }
    }

    static class Ticket implements Serializable {
        String pnr, name, gender, trainNo, trainName, classType, journeyDate, from, to, bookedAt, depTime;
        int age, fare;

        public Ticket(String pnr, String name, int age, String gender, String trainNo, String trainName,
                      String classType, int fare, String journeyDate, String from, String to, String bookedAt, String depTime) {
            this.pnr = pnr;
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.trainNo = trainNo;
            this.trainName = trainName;
            this.classType = classType;
            this.fare = fare;
            this.journeyDate = journeyDate;
            this.from = from;
            this.to = to;
            this.bookedAt = bookedAt;
            this.depTime = depTime;
        }

        void display() {
            System.out.println("=========================================");
            System.out.println("🎟️ PNR         : " + pnr);
            System.out.println("👤 Name        : " + name + " (" + gender + ", Age " + age + ")");
            System.out.println("🚆 Train       : " + trainNo + " - " + trainName);
            System.out.println("📍 From / To   : " + from + " → " + to);
            System.out.println("🏷️ Class       : " + classType);
            System.out.println("🕒 Departure   : " + depTime);
            System.out.println("💵 Ticket Fee  : ₹" + fare);
            System.out.println("📅 Journey     : " + journeyDate);
            System.out.println("📌 Booked At   : " + bookedAt);
            System.out.println("=========================================");
        }
    }

    static Map<String, Train> trainMap = Map.ofEntries(
            Map.entry("12627", new Train("12627", "Karnataka Express", "07:30 AM", 850, 650, 450)),
            Map.entry("12191", new Train("12191", "Shanthi Express", "10:00 AM", 800, 600, 400)),
            Map.entry("12009", new Train("12009", "Shatabdi Express", "12:30 PM", 950, 700, 500)),
            Map.entry("12801", new Train("12801", "Purushottam Express", "01:45 PM", 900, 650, 450)),
            Map.entry("12301", new Train("12301", "Rajdhani (Howrah) Express", "03:00 PM", 1100, 800, 600)),
            Map.entry("12430", new Train("12430", "New Delhi Express", "07:30 PM", 900, 700, 500)),
            Map.entry("12245", new Train("12245", "Duronto Express", "06:00 PM", 1000, 750, 550)),
            Map.entry("12623", new Train("12623", "Trivandrum Mail", "04:15 PM", 750, 600, 400)),
            Map.entry("12701", new Train("12701", "Hussain Sagar Express", "11:15 AM", 700, 500, 300)),
            Map.entry("12951", new Train("12951", "Rajdhani Express", "08:45 AM", 1200, 900, 700))
    );

    static void saveTickets() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            out.writeObject(tickets);
        } catch (IOException e) {
            System.out.println("⚠️ Could not save tickets.");
        }
    }

    static List<Ticket> loadTickets() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Ticket>) in.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    static String generatePNR(String name, String date) {
        String initials = name.length() >= 2 ? name.substring(0, 2).toUpperCase() : "XX";
        int random = new Random().nextInt(9000) + 1000;
        return initials + "-" + date.replace("-", "") + "-" + random;
    }

    static String getCurrentTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    static boolean login() {
        while (true) {
            System.out.println("👤 Login");
            System.out.print("Enter username: ");
            String user = sc.nextLine();
            System.out.print("Enter password: ");
            String pass = sc.nextLine();
            if (user.equals(USERNAME) && pass.equals(PASSWORD)) return true;
            System.out.println("❌ Invalid login. Try again.\n");
        }
    }

    static void showTrains() {
        System.out.println("\n🚄 Available Trains:");
        for (Train train : trainMap.values()) {
            System.out.printf("Train No: %s | %s | Dep: %s | ₹%d (AC)\n",
                    train.number, train.name, train.depTime, train.fareAC);
        }
    }

    static void reserveTicket() {
        System.out.println("\n🚆 New Ticket Reservation");
        System.out.println("--------------------------------------------------");
        System.out.print("How many tickets to book? ");
        int numTickets = Integer.parseInt(sc.nextLine());

        for (int i = 1; i <= numTickets; i++) {
            System.out.println("\nPassenger " + i + " Details:");
            System.out.print("Enter passenger name: ");
            String name = sc.nextLine();
            System.out.print("Enter age: ");
            int age = Integer.parseInt(sc.nextLine());
            System.out.print("Enter gender (M/F): ");
            String gender = sc.nextLine();

            showTrains();
            System.out.print("Enter train number from the list: ");
            String trainNo = sc.nextLine();

            Train train = trainMap.get(trainNo);
            if (train == null) {
                System.out.println("❌ Invalid train number.");
                return;
            }

            System.out.println("\n🎫 Class & Fare Information:");
            System.out.printf("AC: ₹%d (%d seats)\n", train.fareAC, train.seatsAC);
            System.out.printf("Sleeper: ₹%d (%d seats)\n", train.fareSleeper, train.seatsSleeper);
            System.out.printf("General: ₹%d (%d seats)\n", train.fareGeneral, train.seatsGeneral);

            System.out.print("Enter class type (AC/Sleeper/General): ");
            String classType = sc.nextLine();

            int fare;
            switch (classType.toLowerCase()) {
                case "ac":
                    fare = train.fareAC;
                    break;
                case "sleeper":
                    fare = train.fareSleeper;
                    break;
                case "general":
                    fare = train.fareGeneral;
                    break;
                default:
                    System.out.println("⚠️ Invalid class type.");
                    return;
            }


            System.out.print("Enter journey date (YYYY-MM-DD): ");
            String journeyDate = sc.nextLine();

            System.out.print("From: ");
            String from = sc.nextLine();
            System.out.print("To: ");
            String to = sc.nextLine();

            System.out.println("\n📋 Booking Summary:");
            System.out.printf("Name     : %s (%s, Age %d)\n", name, gender, age);
            System.out.printf("Train    : %s - %s\n", trainNo, train.name);
            System.out.printf("Class    : %s\n", classType);
            System.out.printf("Fare     : ₹%d\n", fare);
            System.out.printf("Journey  : %s\n", journeyDate);
            System.out.printf("Departs  : %s\n", train.depTime);

            System.out.print("\n✅ Confirm booking? (yes/no): ");
            if (!sc.nextLine().equalsIgnoreCase("yes")) {
                System.out.println("❎ Booking cancelled.");
                return;
            }

            System.out.print("💳 Select payment mode (Card/UPI/Cash): ");
            String mode = sc.nextLine();

            double gst = fare * 0.05;
            double fee = 20;
            double insurance = 0.5;
            double total = fare + gst + fee + insurance;

            String pnr = generatePNR(name, journeyDate);
            String bookedAt = getCurrentTimestamp();

            Ticket ticket = new Ticket(pnr, name, age, gender, trainNo, train.name, classType, fare,
                    journeyDate, from, to, bookedAt, train.depTime);
            tickets.add(ticket);
            saveTickets();

            System.out.println("\n✅ Ticket booked successfully!");
            ticket.display();
            System.out.println("💳 Payment Details:");
            System.out.printf("Ticket Fare             : ₹%.2f\n", (double) fare);
            System.out.printf("GST (5%%)                : ₹%.2f\n", gst);
            System.out.printf("IRCTC Convenience Fee   : ₹%.2f\n", fee);
            System.out.printf("Travel Insurance        : ₹%.2f\n", insurance);
            System.out.printf("Total Fare              : ₹%.2f\n", total);
        }
    }

    static void cancelTicket() {
        System.out.print("\n❌ Enter PNR to cancel: ");
        String pnr = sc.nextLine();
        for (Ticket t : tickets) {
            if (t.pnr.equalsIgnoreCase(pnr)) {
                t.display();
                System.out.print("🛑 Confirm cancellation? (yes/no): ");
                if (sc.nextLine().equalsIgnoreCase("yes")) {
                    try {
                        Date bookedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(t.bookedAt);
                        long diffMs = new Date().getTime() - bookedDate.getTime();
                        long hours = diffMs / (1000 * 60 * 60);
                        double refund = hours <= 24 ? t.fare : t.fare * 0.5;
                        tickets.remove(t);
                        saveTickets();
                        System.out.printf("✅ Ticket cancelled. Refund: ₹%.2f\n", refund);
                    } catch (Exception e) {
                        System.out.println("❌ Error processing refund.");
                    }
                }
                return;
            }
        }
        System.out.println("⚠️ PNR not found.");
    }

    static void showAllTickets() {
        if (tickets.isEmpty()) {
            System.out.println("📭 No reservations found.");
        } else {
            System.out.println("\n📄 All Reservations:");
            for (Ticket t : tickets) t.display();
        }
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("       🌐 ONLINE RAILWAY RESERVATION SYSTEM       ");
        System.out.println("==================================================");
        System.out.println("📅 Today: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
                + "   ⏰ Time: " + new SimpleDateFormat("hh:mm a").format(new Date()));
        System.out.println("--------------------------------------------------");

        if (!login()) return;

        System.out.println("\n✅ Login Successful!");
        while (true) {
            System.out.println("\n=========== 🎫 MAIN MENU ===========");
            System.out.println("1. 🚆 Reserve Ticket");
            System.out.println("2. ❌ Cancel Ticket");
            System.out.println("3. 📄 Show All Tickets");
            System.out.println("4. 🔚 Exit");
            System.out.print("Enter your choice: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> reserveTicket();
                case "2" -> cancelTicket();
                case "3" -> showAllTickets();
                case "4" -> {
                    System.out.println("👋 Logged out. Thank you!");
                    return;
                }
                default -> System.out.println("⚠️ Invalid choice.");
            }
        }
    }
}
