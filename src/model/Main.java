package model;

import java.util.Random;

public class Main {

	static BinaryTree drzewo = new BinaryTree();

	static String raport = "";

	public static void main(String[] args) {

		int iloscX = 8;
		int iloscA = 6;
		// Tabele ze sprawozdania
		boolean[][][] tablicaOptymalna1 = optymalna1();
		boolean[][][] tablicaNieOptymalna1 = nieoptymalna1();

		Random random = new Random();
		// tablica[nr obiektu X][nr atrybutu//ostatni jest decyzj¹][wartoœæ atrybutu]
		boolean[][][] tablica = new boolean[iloscX][iloscA + 1][1];

		// Wype³niam tablicê losowo
		for (int i = 0; i < tablica.length; i++) {
			for (int j = 0; j < tablica[i].length; j++) {
				tablica[i][j][0] = random.nextBoolean();
			}
		}

		// Twórz drzewo dla tablicy losowej
		System.out.println("Drzewo dla tablicy losowej...");
		System.out.println(naglowek(tablica));
		drukuj(tablica);
		classifier(false, null, tablica);
		System.out.println("\nRAPORT:\n" + raport);
		System.out.print(drzewo.prettyPrint(drzewo.root, 0, BinaryTree.wysokosc(drzewo.root)));
		raport = "";

	}

	public static String classifier(boolean flaga, Node node, boolean[][][] tablica, int... args) {

		System.out.print("Wykluczone atrybuty: ");
		for (int i = 0; i < args.length; i++) {
			System.out.print("a" + (1 + args[i]) + " ");
		}
		System.out.println();
		int tmp = 0;
		boolean dominacjajedynek = false;
		for (int i = 0; i < tablica.length; i++) {
			if (tablica[i][tablica[i].length - 1][0]) {
				tmp++;
			}
			if (tmp >= (tablica.length / 2) + 1)
				dominacjajedynek = true;

			if (tmp == tablica.length) {
				// Zwróæ wierzcho³ek z etykiet¹ 1
				raport += "Same jedynki, koñczê z wierzcho³kiem 1\n";
				if (node == null) {
					drzewo.addNode(null, "1", true);
				} else {
					drzewo.addNode(node, "1", flaga);
				}
				return "1";
			}
			if (i == tablica.length - 1 && tmp == 0) {
				// Zwróæ wierzcho³ek z etykiet¹ 0
				raport += "Same zera, koñczê z wierzcho³kiem 0\n";
				if (node == null) {
					drzewo.addNode(null, "0", false);
				} else {
					drzewo.addNode(node, "0", flaga);
				}
				return "0";
			}
			if (args.length == (tablica[0].length - 1) && dominacjajedynek) {
				raport += "Brak atrybutów, koñczê z wierzcho³kiem 1\n";
				if (node == null) {
					drzewo.addNode(null, "1", true);
				} else {
					drzewo.addNode(node, "1", flaga);
				}
				return "1";
			}
			if (args.length == (tablica[0].length - 1) && !dominacjajedynek) {
				raport += "Brak atrybutów, koñczê z wierzcho³kiem 0\n";
				if (node == null) {
					drzewo.addNode(null, "0", false);
				} else {
					drzewo.addNode(node, "0", flaga);
				}
				return "0";
			}

		}
		tmp = 0;
		int tmp2 = 0;
		int tmp3 = 1000;
		int b = 0;
		for (int i = 0; i < tablica[0].length - 1; i++) {
			// Wykliczenie atrybutów nie bior¹cych udzia³u
			if (!zawiera(args, i)) {
				for (int j = 0; j < tablica.length; j++) {
					// b - atrybut najlepiej dziel¹cy zbiór X
					if (tablica[j][i][0])
						tmp++;
					else
						tmp2++;
				}
				tmp = tmp2 - tmp;
				if (tmp < 0)
					tmp *= (-1);
				if (tmp < tmp3) {
					tmp3 = tmp;
					b = i;
				}
				tmp = 0;
				tmp2 = 0;
			}
		}
		System.out.println("Atrybut najlepiej dziel¹cy zbiór: a" + (1 + b));
		int[] args2 = new int[args.length + 1];
		for (int i = 0; i < args.length; i++) {
			args2[i] = args[i];
		}
		args2[args.length] = b;
		int wierszeL = 0;
		int wierszeP = 0;
		for (int i = 0; i < tablica.length; i++) {
			if (tablica[i][b][0]) { // dla a(x)=1
				wierszeP++;
			}
			if (!tablica[i][b][0]) { // dla a(x)=0
				wierszeL++;
			}
		}
		if (node != null) {
			if (node.name.equals("1") || node.name.equals("0"))
				return "Liœæ";
		}
		System.out.println("Wiersze lewe: " + wierszeL + " Wiersze prawe: " + wierszeP);
		boolean[][][] tablicaL = new boolean[wierszeL][tablica[0].length][1];
		if (tablicaL.length != 0 && tablicaL[0].length != 0) {

			int x = 0;
			for (int i = 0; i < tablica.length; i++) {
				if (!tablica[i][b][0]) { // dla a(x)=0
					for (int j = 0; j < tablicaL[0].length; j++) {
						if (!zawiera(args, j)) {
							// Przepisanie atrybutów i decyzji
							tablicaL[x][j][0] = tablica[i][j][0];
						} else {
							// nie rób nic
						}
					}
					x++;
					if (x > (wierszeL - 1))
						break;
				}
			}
			System.out.println("\nLewe drzewo:");
			System.out.println(naglowek(tablica));
			for (int i = 0; i < tablicaL.length; i++) {
				System.out.print((i + 1) + " | ");
				for (int j = 0; j < tablicaL[i].length; j++) {
					tmp = tablicaL[i][j][0] ? 1 : 0;
					System.out.print(tmp + "  | ");
				}
				System.out.println();
			}
			if (node != null)
				raport += "Aktualny wêze³: " + node.name + "\n";
			if (node == null) {
				raport += "Dodajê wierzcho³ek: a" + (1 + b) + " jako korzeñ\n";
				drzewo.addNode(null, "a" + (b + 1), false);
				node = drzewo.root;
				classifier(false, node, tablicaL, args2);
			} else if (node.name.equals("a" + (b + 1))) {
				raport += "Wierzcho³ek " + (1 + b) + " ju¿ istnieje\n";
				raport += "Wywo³ujê classifier(false, node, tablicaL, args2)\n";
				classifier(false, node, tablicaL, args2);
			} else if (node.name.equals("1") || node.name.equals("0")) {
				raport += "Ups, koniec drzewa!\n";
			} else if (node.leftChild == null) {
				raport += "Dodajê a" + (1 + b) + " z lewej strony\n";
				drzewo.addNode(node, "a" + (b + 1), false);
				node = node.leftChild;
				classifier(false, node, tablicaL, args2);
			}
		}

		boolean[][][] tablicaP = new boolean[wierszeP][tablica[0].length][1];
		if (tablicaP.length != 0 && tablicaP[0].length != 0) {
			int x = 0;
			for (int i = 0; i < tablica.length; i++) {
				if (tablica[i][b][0]) { // dla a(x)=1
					for (int j = 0; j < tablicaP[0].length; j++) {
						if (!zawiera(args, j)) {
							// Przepisanie atrybutów i decyzji
							tablicaP[x][j][0] = tablica[i][j][0];
						} else {
							// nie rób nic
						}
					}
					x++;
					if (x > (wierszeP - 1))
						break;
				}
			}
			System.out.println("\nPrawe drzewo:");
			System.out.println(naglowek(tablica));
			for (int i = 0; i < tablicaP.length; i++) {
				System.out.print((i + 1) + " | ");
				for (int j = 0; j < tablicaP[i].length; j++) {
					tmp = tablicaP[i][j][0] ? 1 : 0;
					System.out.print(tmp + "  | ");
				}
				System.out.println();
			}
			if (node != null)
				raport += "Aktualny wêze³: " + node.name + "\n";
			if (node == null) {
				raport += "Dodajê wierzcho³ek: a" + (1 + b) + " jako korzeñ\n";
				drzewo.addNode(null, "a" + (b + 1), true);
				node = drzewo.root;
				classifier(true, node, tablicaP, args2);
			} else if (node.name.equals("a" + (b + 1))) {
				raport += "Wierzcho³ek a" + (1 + b) + " ju¿ istnieje\n";
				raport += "Wywo³ujê classifier(true, node, tablicaP, args2)\n";
				classifier(true, node, tablicaP, args2);
			} else if (node.name.equals("1") || node.name.equals("0")) {
				raport += "Ups, koniec drzewa!\n";
			} else if (node.rightChild == null) {
				raport += "Dodajê a" + (1 + b) + " z prawej strony\n";
				drzewo.addNode(node, "a" + (b + 1), true);
				node = node.rightChild;
				classifier(true, node, tablicaP, args2);
			}

		}
		raport += "Koniec operacji, wêze³:" + node.name + "\n";
		if (node.name.equals("1") || node.name.equals("0"))
			return "";
		if (node.leftChild == null) {
			if (tablicaL.length != 0) {
				classifier(false, node, tablicaL, args2);
			} else if (dominacjajedynek) {
				drzewo.addNode(node, "1", false);
			} else {
				drzewo.addNode(node, "0", false);
			}
		}
		if (node.rightChild == null) {
			if (tablicaP.length != 0) {
				classifier(true, node, tablicaP, args2);
			} else if (dominacjajedynek) {
				drzewo.addNode(node, "1", true);
			} else {
				drzewo.addNode(node, "0", true);
			}
		}

		return "";
	}

	public static boolean zawiera(int[] tablica, int wartosc) {
		for (int i : tablica) {
			if (wartosc == i) {
				return true;
			}
		}
		return false;
	}

	public static String naglowek(boolean[][][] tab) {
		String naglowek = "X";
		for (int i = 0; i < (tab[0].length - 1); i++) {
			naglowek += " | a" + (i + 1);
		}
		naglowek += " | d  |";
		return naglowek;
	}

	public static boolean[][][] optymalna1() {
		boolean[][][] tablicaOptymalna1 = new boolean[4][4 + 1][1];

		tablicaOptymalna1[0][0][0] = false;
		tablicaOptymalna1[0][1][0] = false;
		tablicaOptymalna1[0][2][0] = true;
		tablicaOptymalna1[0][3][0] = true;
		// Decyzja
		tablicaOptymalna1[0][4][0] = true;

		tablicaOptymalna1[1][0][0] = false;
		tablicaOptymalna1[1][1][0] = false;
		tablicaOptymalna1[1][2][0] = true;
		tablicaOptymalna1[1][3][0] = true;
		// Decyzja
		tablicaOptymalna1[1][4][0] = true;

		tablicaOptymalna1[2][0][0] = false;
		tablicaOptymalna1[2][1][0] = true;
		tablicaOptymalna1[2][2][0] = false;
		tablicaOptymalna1[2][3][0] = true;
		// Decyzja
		tablicaOptymalna1[2][4][0] = true;

		tablicaOptymalna1[3][0][0] = true;
		tablicaOptymalna1[3][1][0] = false;
		tablicaOptymalna1[3][2][0] = false;
		tablicaOptymalna1[3][3][0] = true;
		// Decyzja
		tablicaOptymalna1[3][4][0] = false;

		return tablicaOptymalna1;
	}

	public static boolean[][][] nieoptymalna1() {
		boolean[][][] tablicaNieOptymalna1 = new boolean[8][4 + 1][1];

		tablicaNieOptymalna1[0][0][0] = true;
		tablicaNieOptymalna1[0][1][0] = true;
		tablicaNieOptymalna1[0][2][0] = true;
		tablicaNieOptymalna1[0][3][0] = false;
		// Decyzja
		tablicaNieOptymalna1[0][4][0] = true;

		tablicaNieOptymalna1[1][0][0] = true;
		tablicaNieOptymalna1[1][1][0] = false;
		tablicaNieOptymalna1[1][2][0] = true;
		tablicaNieOptymalna1[1][3][0] = false;
		// Decyzja
		tablicaNieOptymalna1[1][4][0] = false;

		tablicaNieOptymalna1[2][0][0] = true;
		tablicaNieOptymalna1[2][1][0] = true;
		tablicaNieOptymalna1[2][2][0] = true;
		tablicaNieOptymalna1[2][3][0] = true;
		// Decyzja
		tablicaNieOptymalna1[2][4][0] = true;

		tablicaNieOptymalna1[3][0][0] = true;
		tablicaNieOptymalna1[3][1][0] = false;
		tablicaNieOptymalna1[3][2][0] = false;
		tablicaNieOptymalna1[3][3][0] = false;
		// Decyzja
		tablicaNieOptymalna1[3][4][0] = false;

		tablicaNieOptymalna1[4][0][0] = false;
		tablicaNieOptymalna1[4][1][0] = true;
		tablicaNieOptymalna1[4][2][0] = true;
		tablicaNieOptymalna1[4][3][0] = true;
		// Decyzja
		tablicaNieOptymalna1[4][4][0] = true;

		tablicaNieOptymalna1[5][0][0] = false;
		tablicaNieOptymalna1[5][1][0] = false;
		tablicaNieOptymalna1[5][2][0] = false;
		tablicaNieOptymalna1[5][3][0] = true;
		// Decyzja
		tablicaNieOptymalna1[5][4][0] = false;

		tablicaNieOptymalna1[6][0][0] = false;
		tablicaNieOptymalna1[6][1][0] = true;
		tablicaNieOptymalna1[6][2][0] = false;
		tablicaNieOptymalna1[6][3][0] = true;
		// Decyzja
		tablicaNieOptymalna1[6][4][0] = true;

		tablicaNieOptymalna1[7][0][0] = false;
		tablicaNieOptymalna1[7][1][0] = false;
		tablicaNieOptymalna1[7][2][0] = false;
		tablicaNieOptymalna1[7][3][0] = false;
		// Decyzja
		tablicaNieOptymalna1[7][4][0] = false;

		return tablicaNieOptymalna1;
	}

	public static void drukuj(boolean[][][] tablica) {
		int tmp = 0;
		for (int i = 0; i < tablica.length; i++) {
			System.out.print((i + 1) + " | ");
			for (int j = 0; j < tablica[i].length; j++) {
				tmp = tablica[i][j][0] ? 1 : 0;
				System.out.print(tmp + "  | ");
			}
			System.out.println();
		}
	}
}
