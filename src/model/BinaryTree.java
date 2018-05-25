package model;

import java.util.Scanner;

public class BinaryTree {

	Node root;
	private Scanner rightScanner;
	private Scanner leftScanner;

	public void addNode(Node parent, String name, boolean flaga) {
		
		// Inicjalizacja wêz³a
		Node newNode = new Node(parent, name, flaga);
		// Jeœli korzeñ nie istnieje nowy wêze³ zostaje korzeniem
		if (parent == null) {
			root = newNode;
		} else {
			// W innym przypadku zacznij od korzenia
			Node currentNode = parent;
			// Zaininicjuj rodzica dla nowego wêz³a


			while (true) {
				// root is the top parent so we start there
				parent = currentNode;
				// Flaga - lewa strona
				if (!flaga) {
					// Przejdz do lewego dziecka
					currentNode = currentNode.leftChild;
					// Jeœli nie ma wiêcej dzieci
					if (currentNode == null) {
						// Wstaw nowy wêze³ po lewej
						parent.leftChild = newNode;
						return;
					}
				} else { // flaga - prawa strona
					currentNode = currentNode.rightChild;
					if (currentNode == null) {
						parent.rightChild = newNode;
						return;
					}
				}
			}
		}
	}

	public String getRootName() {
		return root.name;
	}

	public Node getRoot() {
		return root;
	}

	public String getLeftChildName(Node root) {
		return root.leftChild.name;
	}

	public String getRightChildName(Node root) {
		return root.rightChild.name;
	}

	public Node getLeftChild(Node root) {
		return root.leftChild;
	}

	public Node getRightChild(Node root) {
		return root.rightChild;
	}

	public void drukuj(Node root) {

		while (root != null) {
			if (root.leftChild != null) {
				System.out.println(root.leftChild.name + "  ");
				drukuj(root.leftChild);
				break;
			}
			if (root.rightChild != null) {
				System.out.println("  " + root.rightChild.name);
				drukuj(root.rightChild);
				break;
			}
			break;
		}

	}

	void drukujPoziom(Node root, int level) {

		if (root == null)
			return;
		if (level == 1) {

			System.out.print(" " + root.name);
			// System.out.print("\n / \\ ");
		} else if (level > 1) {
			drukujPoziom(root.leftChild, level - 1);
			drukujPoziom(root.rightChild, level - 1);
		}
	}

	void printTree(Node node) {
		if (node == null) {
			return;
		}

		System.out.print(node.name + "  ");
		printTree(node.leftChild);
		printTree(node.rightChild);

	}

	public static int wysokosc(Node node) {
		if (node == null) {
			return 0;
		} else {
			return 1 + Math.max(wysokosc(node.leftChild), wysokosc(node.rightChild));
		}
	}

	public StringBuilder prettyPrint(Node root, int currentHeight, int totalHeight) {
		StringBuilder sb = new StringBuilder();
		int spaces = getSpaceCount(totalHeight - currentHeight + 1);
		if (root == null) {
			// create a 'spatial' block and return it
			String row = String.format("%" + (2 * spaces + 1) + "s%n", "");
			// now repeat this row space+1 times
			String block = new String(new char[spaces + 1]).replace("\0", row);
			return new StringBuilder(block);
		}
		if (currentHeight == totalHeight)
			return new StringBuilder(root.name + "");
		int slashes = getSlashCount(totalHeight - currentHeight + 1);
		sb.append(String.format("%" + (spaces + 1) + "s%" + spaces + "s", root.name + "", ""));
		sb.append("\n");
		// now print / and \
		// but make sure that left and right exists
		char leftSlash = root.leftChild == null ? ' ' : '/';
		char rightSlash = root.rightChild == null ? ' ' : '\\';
		int spaceInBetween = 1;
		for (int i = 0, space = spaces - 1; i < slashes; i++, space--, spaceInBetween += 2) {
			for (int j = 0; j < space; j++)
				sb.append(" ");
			sb.append(leftSlash);
			for (int j = 0; j < spaceInBetween; j++)
				sb.append(" ");
			sb.append(rightSlash + "");
			for (int j = 0; j < space; j++)
				sb.append(" ");
			sb.append("\n");
		}
		// sb.append("\n");

		// now get string representations of left and right subtrees
		StringBuilder leftTree = prettyPrint(root.leftChild, currentHeight + 1, totalHeight);
		StringBuilder rightTree = prettyPrint(root.rightChild, currentHeight + 1, totalHeight);
		leftScanner = new Scanner(leftTree.toString());
		rightScanner = new Scanner(rightTree.toString());
		// spaceInBetween+=1;
		while (leftScanner.hasNextLine()) {
			if (currentHeight == totalHeight - 1) {
				sb.append(String.format("%-2s %2s", leftScanner.nextLine(), rightScanner.nextLine()));
				sb.append("\n");
				spaceInBetween -= 2;
			} else {
				sb.append(leftScanner.nextLine());
				sb.append(" ");
				sb.append(rightScanner.nextLine() + "\n");
			}
		}

		return sb;

	}

	public int getSpaceCount(int height) {
		return (int) (3 * Math.pow(2, height - 2) - 1);
	}

	public int getSlashCount(int height) {
		if (height <= 3)
			return height - 1;
		return (int) (3 * Math.pow(2, height - 3) - 1);
	}

}

class Node {

	String name;
	boolean flaga;

	Node root;
	Node leftChild;
	Node rightChild;

	Node(Node root, String name, boolean flaga) {

		this.root = root;
		this.name = name;
		this.flaga = flaga;

	}

	Node(String name) {

		this.name = name;

	}

	public String toString() {

		return "to string do zrobienia";

	}

}