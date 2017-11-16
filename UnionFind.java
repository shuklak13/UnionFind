// @author Karan

import java.util.*;
import java.io.*; 

class KASHP3{

	public static void main(String [] args) throws IOException {
		//the "throws" statement is for reading from a named file
		Scanner sc = new Scanner(System.in);
		
                int n = sc.nextInt();   //num set elements
		UnionFind uf = new UnionFind(n);
                
		String line = sc.nextLine();

                //inputs: uNION, fIND, pRINT, sIZE, cONNECTIVITYLIST, eND
		while(!(line.split(" ")[0].equals("e"))){	//E ends the program

			String[] input = line.split(" ");

			//union
			if(input[0].equals("u")){
                            int x = Integer.parseInt(input[1]);
                            int y = Integer.parseInt(input[2]);
                            int root = uf.union(x,y);
                            if(root == -1) System.out.println(x + " and " + y + " are already in the same set");
                            else System.out.println("Root of union(" + x + ", " + y + ") is " + root);
			}
			//find 
			else if(input[0].equals("f")){
                            int x = Integer.parseInt(input[1]);
                            System.out.println("Root of find(" + x + ") is " + uf.findRoot(x));
			}
			//print
			else if(input[0].equals("p")){
                            uf.printArray();
			}
			//print size of set rooted at x
			else if(input[0].equals("s")){
                            int x = Integer.parseInt(input[1]);
                            System.out.println(uf.size(x));
			}
			//output connectivity list
			else if(input[0].equals("c")){
                            uf.connectivityList();
			}
			line = sc.nextLine();
		}
	}
}

class UnionFind {
    
        int[] array;
    
	UnionFind(int n){
            array = new int[n];
            
            //each element initially is a root
            //so it contains a -1, indicating a tree of size 1
            for(int i=0; i<n; i++)
                array[i] = -1;
        }
        
        //union by size
        int union(int x, int y){
            int rootX = findRoot(x);
            int rootY = findRoot(y);
            
            //if same root, then same tree
            if(rootX==rootY)
                return -1;
            //otherwise...
            else{
                if(array[rootX]<=array[rootY]){      //if X is bigger tree (negative, so we flip sign)
                    array[rootX] += array[rootY];   //add Y's size to X
                    array[rootY] = rootX;                  //make Y point to X
                    return rootX;                   //X is the new root
                }
                else{
                    array[rootY] += array[rootX];
                    array[rootX] = rootY;
                    return rootY;
                }
            }
        }
        
        //find root of x (path compression: makes x point to root if not already)
        //recursive
        int findRoot(int x){
            if(array[x]>=0)         //if x is not a root
                x = findRoot(array[x]); //make x point to the root
            return x;   //return x's pointer (which points to the root)
        }
        
        int size(int x){
            return -1 * array[findRoot(x)];
        }
        
        //returns number of sets
        int sets(){
            int numSets = 0;
            for(int pointer: array)
                if(pointer<0)   numSets++;
            return numSets;
        }
        
        void printArray(){
            for(int pointer: array)
                System.out.print(pointer + " ");
        }
        
        void clear(){
            for(int i=0; i<array.length; i++)
                array[i] = -1;
        }
        
        void connectivityList(){
            //tree.get(i) is the i'th tree
            //tree.get(i).get(j) is the j'th node of the i'th tree
            LinkedList<LinkedList<Integer>> trees = new LinkedList<LinkedList<Integer>>();
            
            //iterate through every element
            for(int i=0; i<array.length; i++){
                int j=0;
                //find which tree the root belongs to
                //look for first tree whose root = i's root)
                while(j<trees.size() && findRoot(trees.get(j).peek()) != findRoot(i))
                    j++;
                //we have not exhausted the trees; a node with the same root was found
                if(j < trees.size())
                    trees.get(j).add(i);
                //i's root was not in a tree. We must add a new tree
                else{
                    trees.add(new LinkedList<Integer>());
                    trees.getLast().add(new Integer(i));
                }
            }
            
            //print out the connectivity list
            for(LinkedList<Integer> tree: trees){
                for(Object element: tree)
                    System.out.print(element + " ");
                System.out.println();
            }
        }
}
