import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

enum Gender {
    Man, Woman
}
class Node {
    private String name;
    private Gender gender;
    private Node spouse;
    private Node father;
    private Node mother;
    private ArrayList<Node> Relations = new ArrayList<Node>();


    /* Overload constructor method to instantiate empty node to
     * return in Node.getParents() incase of null Parents node. */
    public Node() {}
    // Main Constructor to use
    public Node(String name, Gender gender) {
        this.name = name;
        this.gender = gender;
        this.spouse = new Node();
        this.father = new Node();
        this.mother = new Node();
        System.out.println(this.name + ", " + this.gender);
    }

    // Logic Functions
    public void addRelation(Node person) {
        person.addParent(this);
        this.Relations.add(person);
    }
    public void addSpouse(Node Spouse) {
        Spouse.addSpouse(this);
        this.spouse = Spouse;
    }
    private void addParent(Node parent) {
        if(parent.getGender() == Gender.Man) {
            this.father = parent;
        } else {
            this.mother = parent;
        }
    }

    // Getters for data
    public String getName() {
        if(name != null && !name.isEmpty()) {
            return this.name;
        }
        return "No Name";
    }
    public Gender getGender() {
        return this.gender;
    }
    public Node getSpouse() {
        return this.spouse;
    }
    public Node getParent(Gender gender) {
        if(gender == Gender.Man) {
            return this.father;
        } else return this.mother;
    }
    public ArrayList<Node> getRelationships() {
        return this.Relations;
    }
}
class App {
    public static Node arrayFindNode(ArrayList<Node> Arr, String nodeName) {
        for (Node node : Arr) {
            if (node.getName() == nodeName) {
                return node;
            }
        }
        return new Node();
    }
    public static void main(String[] args) {
        try(BufferedReader br = new BufferedReader(new FileReader("input.csv"))) {
            ArrayList<Node> Nodes = new ArrayList<Node>();
            String line;
            String[] splitLine;
            Node parentNode;
            Node connectNode;
            while ((line = br.readLine()) != null) {
                line = line.replace(", ", ",");
                splitLine = line.split(",");
                switch (splitLine.length) {
                    case 2:
                        /* Create a new node according to the file, add it to a list for
                        * further processing during the reading stage. */
                        Nodes.add(new Node(splitLine[0], Gender.valueOf(splitLine[1])));
                    default:
                        parentNode = arrayFindNode(Nodes, splitLine[0]);
                        connectNode = arrayFindNode(Nodes, splitLine[2]);
                        switch (splitLine[1]) {
                            case "Husband": parentNode.addSpouse(connectNode);
                            default: parentNode.addRelation(connectNode);
                        }
                }
            }
            br.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        /*
            //Simulating already read file with manually setting graph
            Node Connor = new Node("Connor Baratheon", Gender.Man);
            Node Ashley = new Node("Ashley Baratheon", Gender.Woman);
            Node Aaron = new Node("Aaron Baratheon", Gender.Man);
            Connor.addRelation(Ashley);
            Connor.addRelation(Aaron);

            System.out.println("Get Connor's Parents Node: "+Connor.getParent(Gender.Man).getName());
            System.out.println("Get Ashley's Parents Node: "+Ashley.getParent(Gender.Man).getName());
            System.out.print(Connor.getName()+" relationships: ");
            for (int i = 0; i < Connor.getRelationships().size(); i++) {
                Node Obj = Connor.getRelationships().get(i);
                System.out.print(Obj.getName()+", ");
            }
            System.out.println();
         */
    }
}