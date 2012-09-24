import java.util.Stack;

public class FixPositionBinaryTree { 
  // Root node pointer. Will be null for an empty tree. 
  private static Node root; 
 
  public static void main(String[] args) {
	  root = new Node(25);
	  insert(12);
	  insert(30);
	  insert(2);
	  insert(50);
	  printBST(root);
	  
	  System.out.println();
	  
	  root.left.data = 30;
	  root.right.data = 12;
	  printBST(root);
	  
	  correctTree(root);
	  System.out.println();
	  printBST(root);
  }

  private static void correctTree(Node node) {
	// TODO Auto-generated method stub
	  if(node==null)
		  return;
	  Stack st = new Stack<Node>();
	  st.push(node);
	  Node wnode1=null;
	  Node wnode2=null;
	  while(!st.isEmpty()){
		  Node tmp = (Node) st.pop();
		  st.push(tmp.left);
		  st.push(tmp.right);
		  int data = tmp.data;
		  if(!(data > tmp.left.data)){
			  if(wnode1==null){
				  wnode1=  tmp.left;
			  }else{
				  wnode2=  tmp.left;
				  break;
			  }
			
		  }
		  if(!(data < tmp.right.data)){
			  if(wnode1==null){
				  wnode1=  tmp.right;
			  }else{
				  wnode2=  tmp.right;
				  break;
			  }
		  }
			  
	  }
	  System.out.println(wnode1.data+ ", " + wnode2.data);
	  int tmpdata = wnode1.data;
	  wnode1.data = wnode2.data;
	  wnode2.data = tmpdata;
	  return;
}

/* 
   --Node-- 
   The binary tree is built using this nested node class. 
   Each node stores one data element, and has left and right 
   sub-tree pointer which may be null. 
   The node is a "dumb" nested class -- we just use it for 
   storage; it does not have any methods. 
  */ 
  private static class Node { 
    Node left; 
    Node right; 
    int data;

    Node(int newData) { 
      left = null; 
      right = null; 
      data = newData; 
    } 
  }

  /** 
   Creates an empty binary tree -- a null root pointer. 
  */ 
  public void BinaryTree() { 
    root = null; 
  } 
 

  /** 
   Returns true if the given target is in the binary tree. 
   Uses a recursive helper. 
  */ 
  public boolean lookup(int data) { 
    return(lookup(root, data)); 
  } 
 

  /** 
   Recursive lookup  -- given a node, recur 
   down searching for the given data. 
  */ 
  private boolean lookup(Node node, int data) { 
    if (node==null) { 
      return(false); 
    }

    if (data==node.data) { 
      return(true); 
    } 
    else if (data<node.data) { 
      return(lookup(node.left, data)); 
    } 
    else { 
      return(lookup(node.right, data)); 
    } 
  } 
 

  /** 
   Inserts the given data into the binary tree. 
   Uses a recursive helper. 
  */ 
  public static void insert(int data) { 
    root = insert(root, data); 
  } 
 
  public static void printBST(Node node) { 
	     if(node==null)
	    	 return;
	     else{
	    	 System.out.println(node.data);
	    	 printBST(node.left);
	    	 printBST(node.right);
	     }
  } 

  /** 
   Recursive insert -- given a node pointer, recur down and 
   insert the given data into the tree. Returns the new 
   node pointer (the standard way to communicate 
   a changed pointer back to the caller). 
  */ 
  private static Node insert(Node node, int data) { 
    if (node==null) { 
      node = new Node(data); 
    } 
    else { 
      if (data <= node.data) { 
        node.left = insert(node.left, data); 
      } 
      else { 
        node.right = insert(node.right, data); 
      } 
    }

    return(node); // in any case, return the new pointer to the caller 
  } 
}