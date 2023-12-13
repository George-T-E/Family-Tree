import java.util.ArrayList;

enum Gender {
    Male, Female
}
class Node {
    private String name;
    private Gender gender;
    private Node[] Parents;
    private ArrayList<Node> Relations = new ArrayList<Node>();

    /* Overload constructor method to instantiate empty node to
     * return in Node.getParents() incase of null Parents node. */
    public Node() {
        this.name = null;
        this.gender = null;
        this.Parents = new Node[2];
    }
    // Main Constructor to use
    public Node(String name, Gender gender) {
        this.name = name;
        this.gender = gender;
        this.Parents = new Node[2];
        System.out.println(this.name + ", " + this.gender);
    }

    // Logic Functions
    public void addRelation(Node person) {
        person.addParents(this);
        this.Relations.add(person);
    }
    private void addParents(Node parent) {
        if(parent.getGender() == Gender.Female) {
            this.Parents[1] = parent;
        } else {
            this.Parents[0] = parent;
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
    public Node getParent(Gender gender) {
        if(this.Parents[1] == null && this.Parents[0] == null) {
            return new Node();
        }
        if(gender == Gender.Female) {
            return this.Parents[1];
        } else if(gender == Gender.Male) {
            return this.Parents[0];
        } else return new Node();

    }
    public ArrayList<Node> getRelationships() {
        return this.Relations;
    }
}
class App {
    public static void main(String[] args) {
        //Simulating already read file with manually setting graph
        Node Connor = new Node("Connor Baratheon", Gender.Male);
        Node Ashley = new Node("Ashley Baratheon", Gender.Female);
        Node Aaron = new Node("Aaron Baratheon", Gender.Male);
        Connor.addRelation(Ashley);
        Connor.addRelation(Aaron);

        System.out.println("Get Connor's Parents Node: "+Connor.getParent(Gender.Male).getName());
        System.out.println("Get Ashley's Parents Node: "+Ashley.getParent(Gender.Male).getName());
        System.out.print(Connor.getName()+" relationships: ");
        for (int i = 0; i < Connor.getRelationships().size(); i++) {
            Node Obj = Connor.getRelationships().get(i);
            System.out.print(Obj.getName()+", ");
        }
        System.out.println();
    }
}