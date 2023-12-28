import com.sun.source.tree.Tree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

// enum of Person's Gender
enum Gender {
    Male, Female
}

// Person class is being used as a node.
class Person {
    private String name;
    private Gender gender;
    private Person spouse, father, mother;

//    private Person[] Parents;
    private ArrayList<Person> relations_list = new ArrayList<Person>(); //TODO MAYBE IT WILL GET REMOVED


    /* Overload constructor method to instantiate empty node to
     * return in Node.getParents() in case of null Parents node.
     * */
    public Person() {}
    // Main Constructor to use
    public Person(String name, String gender_string) {
        this.name = name;
        this.spouse = new Person();
        this.father = new Person();
        this.mother = new Person();
        this.gender = construct_gender(gender_string);
        System.out.println(this.name + ", " + this.gender);
    }

    // Logic Functions
    public void addRelation(Person person, String relation) {
        relation = relation.toLowerCase();
        switch(relation) {
            case "mother":
                this.mother = person;
                this.relations_list.add(person);
                break;
            case "father":
                this.father = person;
                this.relations_list.add(person);
                break;
            case "husband":
                this.spouse = person;
                break;
            default:
                System.out.println("Relation has not been found, fix your input.csv file.");
                break;
        }
    }

    private Gender construct_gender(String gender_string) {
        gender_string = gender_string.trim().toLowerCase();
        if (gender_string.equals("male") || gender_string.equals("man")) {
            return Gender.Male;
        } else if (gender_string.equals("female") || gender_string.equals("woman")) {
            return Gender.Female;
        } else {
            System.out.println(this.name + " has an incorrect gender (" + gender_string + "), you will need to fix your input file, only (Male/Female) exist.");
            System.out.println("Setting the Gender to Male as default until you fix the input file.");
            return Gender.Male;
        }
    }

    // Getters for data
    public String getName() {
        if(name != null && !name.isEmpty()) {
            return this.name;
        }
        return "Null name";
    }
    public Gender getGender() {
        return this.gender;
    }

    public Person getSpouse() {
        return spouse;
    }

    public Person getFather() {
        return father;
    }

    public Person getMother() {
        return mother;
    }
    //NOT USED METHOD YET
//    public Person getParents(Gender gender) {
//        if(gender == Gender.Male && this.father != null) {
//            return this.father;
//        } else if(gender == Gender.Female && this.mother != null) {
//            return this.mother;
//        } else return new Person(); //father or mother does not exist
//    }

}

class App {
    static Map<String, List<Tree>> family_tree_map = new HashMap<>();
    static Map<String, Person> persons_map = new HashMap<>();
    static Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
        main_menu();
    }
    /* This is the Main function that will run first when the program starts
     *  it shows the user all his available choices to use the program and
     *  runs the required functions according to the user choice.
     */
    public static void main_menu() {
        boolean isActive = true;
        while (isActive) {
            System.out.println("\n------------------------------------");
            System.out.println("-    Family Tree - Main Menu      -");
            System.out.println("------------------------------------");
            System.out.println("1. Parse the csv file and print the result.");
            System.out.println("2. Find the relationship between two people.");
            System.out.println("3. Exit the program.");
            System.out.print("\nSelect an option by typing it's number and then press enter:");

            input = new Scanner(System.in);
            String selected_option = input.nextLine().trim();

            switch(selected_option) {
                case "1":
                    System.out.println("You have selected the option 1: Parse the csv file and print the result.");
                    String file_path_name = input.nextLine();
                    parse_and_print_file(file_path_name);
                    break;
                case "2":
                    System.out.println("You have selected the option 2: Find the relationship between two people.");
                    System.out.println("Enter the name of the first person.");
                    String input_first_person_name = input.nextLine();
                    System.out.println("Enter the name of the second person.");
                    String input_second_person_name = input.nextLine();
                    find_relation(input_first_person_name,input_second_person_name);
                    break;
                case "3":
                    System.out.println("You have selected the option 3: Exit the program.");
                    isActive = false;
                    break;
                default:
                    System.out.println("Invalid option.");
                    System.out.println("Choose from an option between 1 and 3.");
                    continue;
            }
        }
    }
    /*
        Asks the user if he wants to continue using the program
     */
    public static void ask_to_continue() {
        System.out.print("\n Continue? (Y/N): ");
        String user_input = input.nextLine();
        /* Makes the string to lower case and replaces
         * any parenthesis or if the user answered yes
         * or no, it will turn it to y or n, so it can pass
         * the case check.
         */
        user_input = user_input.toLowerCase().
                replace("(", "").
                replace(")", "").
                replace("es", "").
                replace("o", "");

        switch (user_input) {
            case "y":
                main_menu();
                break;
            case "n":
                System.out.println("Family Tree program will terminate, good bye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option, answer with a (Y) or (N)");
                break;
        }
    }

    public static void parse_and_print_file(String filePathName) {
        sort_content_relations(Objects.requireNonNull(read_the_file(filePathName)));
        ask_to_continue();
    }
    /* This function reads the content of the CSV file line by line,
     * trims the spaces and returns a String variable with the content of the file.
     */
    public static String read_the_file(String filePathName) {
        String content = "";
        try (BufferedReader br = new BufferedReader((new FileReader(filePathName)))) {
            String current_line;

            while ((current_line = br.readLine()) != null) {
                if (current_line.isEmpty()) continue;
                current_line = current_line.trim();
                content += current_line + "\n";
            }
            return content;
        } catch (IOException e) {
            System.out.println("There was an error while reading the file: " + e.getMessage());
            return null;
        }
    }
    /* This function receives the string file content as an input from the
     * function read_the_file and adds to the persons_list each person name and their gender.
     */
    public static void sort_content_relations(String file_content) {
        if(!persons_map.isEmpty()) persons_map.clear();
        String[] content_lines = file_content.split("\n");
//        ArrayList<Person> personsArrayList = new ArrayList<>();
        Person person_to_be_related;
        Person person_to_store_his_relative;


        for (String line : content_lines) {
            String[] parts = line.split(", ");

            if (parts.length == 2) {
                String person_name = parts[0].trim();
                String person_gender = parts[1].trim();
                Person new_person = new Person(person_name, person_gender);
                persons_map.put(person_name, new_person);
            } else if (parts.length == 3) {
                String person_to_be_relative_str = parts[0].trim();
                String relation = parts[1].trim().toLowerCase();
                String person_to_store_his_relative_str = parts[2].trim();

                if(persons_map.containsKey(person_to_be_relative_str) && persons_map.containsKey(person_to_store_his_relative_str)) {
                    person_to_be_related = persons_map.get(person_to_be_relative_str);
                    person_to_store_his_relative = persons_map.get(person_to_store_his_relative_str);

                    switch(relation) {
                        case "husband":
                            person_to_store_his_relative.addRelation(person_to_be_related, relation);
                            person_to_be_related.addRelation(person_to_store_his_relative, relation);
                            break;
                        case "father", "mother":
                            person_to_store_his_relative.addRelation(person_to_be_related, relation);
                            break;
                        default:
                            System.out.println("Something is wrong with the relations please check your .csv file.");
                    }
                    // Update the person objects in the hashmap.
                    persons_map.put(person_to_be_relative_str, person_to_be_related);
                    persons_map.put(person_to_store_his_relative_str, person_to_store_his_relative);
                } else {
                    System.out.println("One or more persons are null!");
                }
            }
        }

        System.out.println("Successfully added every person from the CSV file\n to the Family Tree.");
        ask_to_continue();
    }

    // TODO MORE LATIONSHIPS
    public static void find_relation(String firstPersonName, String secondPersonName) {
        if (persons_map.isEmpty()) {
            System.out.println("You need to input data from .csv file before we proceed with relationships.");
        } else {
            if (persons_map.containsKey(firstPersonName) && persons_map.containsKey(secondPersonName)) {
                Person firstPerson = persons_map.get(firstPersonName);
                Person secondPerson = persons_map.get(secondPersonName);

                String result = check_relationships(firstPerson, secondPerson);

                if (result != null) {
                    System.out.println(result);
                } else {
                    System.out.println(firstPersonName + " and " + secondPersonName + " are not related.");
                }
            } else {
                System.out.println("One or more person names you have entered");
                System.out.println("don't exist in the .csv file.");
            }
        }
        ask_to_continue();
    }

    private static String check_relationships(Person firstPerson, Person secondPerson) {
        String result = check_child_relation(firstPerson, secondPerson);

        if (result == null) {
            result = check_mother_relation(firstPerson, secondPerson);
        }

        if (result == null) {
            result = check_father_relation(firstPerson, secondPerson);
        }

        if (result == null) {
            result = check_spouse_relation(firstPerson, secondPerson);
        }

        if (result == null) {
            result = check_grandfather_relation(firstPerson, secondPerson);
        }

        if (result == null) {
            result = check_grandmother_relation(firstPerson, secondPerson);
        }

        if (result == null) {
            result = check_grand_child_relation(firstPerson, secondPerson);
        }

        // Add more relationship checks as needed

        return result;
    }

    // Checks if the first person is the father
    private static String check_father_relation(Person firstPerson, Person secondPerson) {
        String first_person_name = firstPerson.getName();
        String second_person_name = secondPerson.getName();
        // Get the father or mother of the second person
        Person father_of_second_person = (secondPerson.getFather() != null) ? secondPerson.getFather() : null;

        if (father_of_second_person != null) {
            if (father_of_second_person.getName().equals(first_person_name)) {
                return first_person_name + " is the Father of " + second_person_name;
            }
        }

        return null;
    }
    // Checks if the first person is the mother
    private static String check_mother_relation(Person firstPerson, Person secondPerson) {
        String first_person_name = firstPerson.getName();
        String second_person_name = secondPerson.getName();
        // Get the father or mother of the first person
        Person mother_of_second_person = (secondPerson.getMother() != null) ? secondPerson.getMother() : null;

        if (secondPerson.getMother() != null) {
            if (mother_of_second_person != null && mother_of_second_person.getName().equals(first_person_name)) {
                return first_person_name + " is the Mother of " + second_person_name;
            }
        }

        return null;
    }
    // Checks if the first person is the son or daughter
    private static String check_child_relation(Person firstPerson, Person secondPerson) {
        String first_person_name = firstPerson.getName();
        String second_person_name = secondPerson.getName();
        // Get the father or mother of the first person
        Person first_person_father = (firstPerson.getFather() != null) ? firstPerson.getFather() : null;
        Person first_person_mother = (firstPerson.getMother() != null) ? firstPerson.getMother() : null;

        // Check if the second person is the grandparent of the first person
        if ((first_person_father != null && first_person_father.getName().equals(second_person_name)) ||
                (first_person_mother != null && first_person_mother.getName().equals(second_person_name))) {
            String genderRelation = (firstPerson.getGender() == Gender.Male) ? "Son" : "Daughter";
            return first_person_name + " is the " + genderRelation + " of " + second_person_name;
        }

        return null;
    }


    private static String check_spouse_relation(Person firstPerson, Person secondPerson) {
        String personName = firstPerson.getName();
        String otherPersonName = secondPerson.getName();

        Person spouse = (firstPerson.getSpouse() != null) ? firstPerson.getSpouse() : null;

        if (spouse != null && spouse.getName().equals(otherPersonName)) {
            String genderRelation = (firstPerson.getGender() == Gender.Male) ? "Husband" : "Wife (Husband in input)";
            return otherPersonName + " is " + genderRelation + " of " + personName;
        }

        return null;
    }
    // Checks if the first person is the Grandfather
    private static String check_grandfather_relation(Person firstPerson, Person secondPerson) {
        String first_person_name = firstPerson.getName();
        String second_person_name = secondPerson.getName();

        Person grandfather_of_second_person = (secondPerson.getFather().getFather() != null) ? secondPerson.getFather().getFather() : null;

        if (grandfather_of_second_person != null) {
            if (grandfather_of_second_person.getName().equals(first_person_name)) {
                return first_person_name + " is the Grandfather of " + second_person_name;
            }
        }

        return null;
    }
    // Checks if the first person is the Grandmother
    private static String check_grandmother_relation(Person firstPerson, Person secondPerson) {
        String first_person_name = firstPerson.getName();
        String second_person_name = secondPerson.getName();
        // Get the father or mother of the first person
        Person grandmother_of_second_person = (secondPerson.getMother().getMother() != null) ? secondPerson.getMother().getMother() : null;

        if (secondPerson.getMother() != null) {
            if (grandmother_of_second_person != null && grandmother_of_second_person.getName().equals(first_person_name)) {
                return first_person_name + " is the Grandmother of " + second_person_name;
            }
        }

        return null;
    }
    // Checks if the first person is granddaughter or grandson
    private static String check_grand_child_relation(Person firstPerson, Person secondPerson) {
        String first_person_name = firstPerson.getName();
        String second_person_name = secondPerson.getName();
        // Get the father or mother of the first person
        Person grandfather_of_first_person = (firstPerson.getFather().getFather() != null) ? firstPerson.getFather().getFather() : null;
        Person grandmother_of_first_person = (firstPerson.getMother().getMother() != null) ? firstPerson.getMother().getMother() : null;

        // Check if the second person is the grandparent of the first person
        if ((grandfather_of_first_person != null && grandfather_of_first_person.getName().equals(second_person_name)) ||
                (grandmother_of_first_person != null && grandmother_of_first_person.getName().equals(second_person_name))) {
            String genderRelation = (firstPerson.getGender() == Gender.Male) ? "Grandson" : "Granddaughter";
            return first_person_name + " is the " + genderRelation + " of " + second_person_name;
        }

        return null;
    }

}