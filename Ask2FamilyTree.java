import java.util.ArrayList;

enum Gender {
    Male, Female
}
class Node {
    private String name;
    private Gender gender;
    private Node Parent;
    private ArrayList<Node> Relations = new ArrayList<Node>();

    /* Overload constructor method to instantiate empty node to 
     * return in Node.getParent() incase of null parent node. */
    public Node() {
        this.name = null;
        this.gender = null;
        this.Parent = null;
    }
    // Main Constructor to use 
    public Node(String name, Gender gender) {
        this.name = name;
        this.gender = gender;
        this.Parent = null;
    }

    // Logic Functions
    public void addRelation(Node Person) {
        Person.addParent(this);
        this.Relations.add(Person);
    }
    private void addParent(Node Parent) {
        this.Parent = Parent;
    }

    // Getters for data
    public String getName() {
        return this.name;
    }
    public Gender getGender() {
        return this.gender;
    }
    public Node getParent() {
        if (this.Parent == null) {
            /* Garbage collector will eventually deallocate memory for non-referenced
             * object, should manually run Runtime.GetRuntime().gc() to make sure. */
            return new Node();
        } else {
            return this.Parent;
        }
    }
    public ArrayList<Node> getRelationships() {
        return this.Relations;
    }
}
class Ask2FamilyTree {
    public static void main(String[] args) {
        //Simulating already read file with manually setting graph
        Node Connor = new Node("Connor Baratheon", Gender.Male);
        Node Ashley = new Node("Ashley Baratheon", Gender.Female);
        Node Aaron = new Node("Aaron Baratheon", Gender.Male);
        Connor.addRelation(Ashley);
        Connor.addRelation(Aaron);
        System.out.println("Get Connor's Parent Node: "+Connor.getParent().getName());
        System.out.println("Get Ashley's Parent Node: "+Ashley.getParent().getName());
        System.out.print(Connor.getName()+" relationships: ");
        for (int i = 0; i < Connor.getRelationships().size(); i++) {
            Node Obj = Connor.getRelationships().get(i);
            System.out.print(Obj.getName()+", ");
        }
        System.out.println();
    }
}