import java.io.*;
import java.util.StringTokenizer;

class FastReader {
    BufferedReader br;
    StringTokenizer st;

    public FastReader(){
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    String next(){
        while (st == null || !st.hasMoreElements()) {
            try {
                st = new StringTokenizer(br.readLine());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return st.nextToken();
    }

    int nextInt(){ return Integer.parseInt(next()); }

    long nextLong(){ return Long.parseLong(next()); }

    double nextDouble(){
        return Double.parseDouble(next());
    }

    String nextLine(){
        String str = "";
        try {
            if(st.hasMoreTokens()){
                str = st.nextToken("\n");
            }
            else{
                str = br.readLine();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}

class Node {
    public int data;
    public Node left;
    public Node right;
    public Node parent;

    public Node(int data) {
        this.data = data;
        this.parent = null;
        this.left = null;
        this.right = null;
    }
}

public class SplayTree {
    public Node root;

    public SplayTree() {
        this.root = null;
    }

    public Node maximum(Node x) {   // Log n
        while(x.right != null)
            x = x.right;
        return x;
    }

    public void leftRotate(Node x){     // O(1)
        Node y = x.right;
        x.right = y.left;
        if(y.left != null) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if(x.parent == null) {          //x was root
            this.root = y;
        }
        else if(x == x.parent.left) {   //x is left child
            x.parent.left = y;
        }
        else {                          //x is right child
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    public void rightRotate(Node x) {   // O(1)
        Node y = x.left;
        x.left = y.right;
        if(y.right != null) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if(x.parent == null) { //x is root
            this.root = y;
        }
        else if(x == x.parent.right) { //x is left child
            x.parent.right = y;
        }
        else { //x is right child
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    public void splay(Node n) {                 // Logn * 2O(rightRotate)   ==      Logn
        while(n.parent != null) {               //  node is not root

            if(n.parent == this.root) {         //  node is child of root, one rotation
                if(n == n.parent.left)
                    this.rightRotate(n.parent);
                else
                    this.leftRotate(n.parent);
            }

            else {                              // two rotation
                Node p = n.parent;
                Node g = p.parent;

                if(n.parent.left == n && p.parent.left == p) {
                    this.rightRotate(g);
                    this.rightRotate(p);
                }
                else if(n.parent.right == n && p.parent.right == p) {
                    this.leftRotate(g);
                    this.leftRotate(p);
                }
                else if(n.parent.right == n && p.parent.left == p) {
                    this.leftRotate(p);
                    this.rightRotate(g);
                }
                else if(n.parent.left == n && p.parent.right == p) {
                    this.rightRotate(p);
                    this.leftRotate(g);
                }
            }
        }
    }

    public void add(int num) {      // Logn + O(splay)  == logn
        if (find(num))
            return;
        Node n = new Node(num);
        Node y = null;
        Node temp = this.root;
        while(temp != null) {
            y = temp;
            if(n.data < temp.data)
                temp = temp.left;
            else
                temp = temp.right;
        }
        n.parent = y;

        if(y == null)           //newly added node is root
            this.root = n;
        else if(n.data < y.data)
            y.left = n;
        else
            y.right = n;

        this.splay(n);
    }

    public boolean find(int x) {    // Logn + O(Splay)      == logn
        Node n = root;
        while (true){
            if (n == null)
                return false;
            if (n.data == x) {
                splay(n);
                return true;
            }

            if (n.data > x){
                if (n.left == null && n.right == null)
                    break;
                n = n.left;
            }

            else{
                if (n.left == null && n.right == null)
                    break;
                n = n.right;
            }
        }
        splay(n);
        return false;
    }

    public void del(int num) {      // Logn + Logn + Logn + O(splay) ==    logn
        if (find(num)){
            SplayTree leftSubtree = new SplayTree();
            leftSubtree.root = this.root.left;

            if(leftSubtree.root != null)
                leftSubtree.root.parent = null;

            SplayTree rightSubtree = new SplayTree();
            rightSubtree.root = this.root.right;
            if(rightSubtree.root != null)
                rightSubtree.root.parent = null;

            if(leftSubtree.root != null) {
                Node m = leftSubtree.maximum(leftSubtree.root);
                leftSubtree.splay(m);
                leftSubtree.root.right = rightSubtree.root;
                if (rightSubtree.root != null)
                    rightSubtree.root.parent = leftSubtree.root;
                this.root = leftSubtree.root;
            }
            else {
                this.root = rightSubtree.root;
            }
        }
    }

    public long sum(int l, int r){
        long sum = 0;
        return sumInRangeHelper(this.root, l, r, sum);
    }

    private long sumInRangeHelper(Node node, int x, int y, long sum) {    // O(n)
        if (node == null) {
            return sum;
        }

        if (node.data >= x && node.data <= y) {
            sum += node.data;
        }

        if (node.data > x) {
            sum = sumInRangeHelper(node.left, x, y, sum);
        }

        if (node.data < y) {
            sum = sumInRangeHelper(node.right, x, y, sum);
        }

        return sum;
    }

    public void inorder(Node n) {
        if(n != null) {
            inorder(n.left);
            System.out.println(n.data);
            inorder(n.right);
        }
    }

    public void preOrder(Node node) {
        if (node != null) {
            System.out.println(node.data);
            preOrder(node.left);
            preOrder(node.right);
        }
    }

    public static void main(String[] args) {
        FastReader fastReader = new FastReader();
        int n = fastReader.nextInt();
        String [][] arr = new String[n][3];

        for(int i = 0; i < n; i++){
            arr[i] = fastReader.nextLine().split(" ");
        }

        SplayTree t = new SplayTree();

        for (int i = 0; i < n; i++) {
            switch (arr[i][0]){
                case "add":{
                    t.add(Integer.parseInt(arr[i][1]));
                    break;
                }
                case "find":{
                    System.out.println(t.find(Integer.parseInt(arr[i][1])));
                    break;
                }
                case "del":{
                    t.del(Integer.parseInt(arr[i][1]));
                    break;
                }
                case "sum":{
                    System.out.println(t.sum(Integer.parseInt(arr[i][1]), Integer.parseInt(arr[i][2])));
                    break;
                }
            }
        }
    }
}
