// Datei LongSpeicher10.java
/* ------------------------------------------------------------------------
 Jedes Objekt der Klasse LongSpeicher10 ist ein Speicher, in dem man
 long-Werte sammeln (einfuegen, entfernen, suchen) kann.
 Doppelgaenger sind erlaubt.
 ---------------------------------------------------------------------------
 Implementierung: Als unsortierte Reihung.
 ------------------------------------------------------------------------ */
import static java.lang.String.format;

import java.util.Arrays;

class LongSpeicher20 extends AbstractLongSpeicher {
	// ---------------------------------------------------------------------
	// Zum Ein-/Ausschalten von Testbefehlen:
	static final boolean TST1 = true;
	// ---------------------------------------------------------------------
	private long[] speicher;
	private int lbi = -1; // letzter belegter Index

	public LongSpeicher20(int length) {
		
		speicher = new long[length];
	
	}

	// ---------------------------------------------------------------------
	private int index(long n) {

		// Liefert (wenn moeglich) einen Index i, an dem ein n im speicher
		// steht (d.h. fuer den gilt: speicher[i] == n).
		// Falls n nicht im speicher steht, wird der Index geliefert, an dem
		// n eingefuegt werden sollte (falls man es einfuegen moechte).
		// Achtung:
		// Das Ergebnis von index ist moeglicherweise gleich speicher.length
		// (und somit kein Index von speicher), und zwar wenn
		// 1. der speicher voll ist (d.h. lbi == speicher.length-1 gilt) und
		// 2. n groesser ist als alle long-Werte in diesem Speicher.
		// Binaer gesucht wird in der Teilreihung speicher[von..bis]:
	   // lbi zeigt jetzt immer auf die größte zahl, die im array ist
	   
		int von = 0;
		int bis = lbi;
		while (von <= bis) {
			int mitte = von + (bis - von) / 2;
			if (gt(n, speicher[mitte])) {
				von = mitte + 1; // rechts weitersuchen
			} else if (lt(n, speicher[mitte])) {
				bis = mitte - 1;// links weitersuchen
			} else {
				return mitte; // hier gilt: eq(n, speicher[mitte])
			}
		}
		return von; // n steht nicht im speicher
	}

	@Override
	public String toString() {

		// Liefert eine String-Darstellung dieses Speichers. Beispiele:
		// // Anzahl der long-Werte im Speicher:
		// "[]" // 0
		// "[10]" // 1
		// "[20, 30, 10]" // 3

		if (lbi == -1) return "[]";
		StringBuilder sb = new StringBuilder();
		sb.append("[" + speicher[0]);
		for (int i = 1; i <= lbi; i++) {
			sb.append(", " + speicher[i]);
		}
		sb.append("]");

		return sb.toString();
	}

	// ---------------------------------------------------------------------
	@Override
	public boolean fuegeEin(long n) {

		// Liefert false, falls dieser Speicher bereits voll ist.
		// Fuegt sonst n in diesen Speicher ein und liefert true.

		if (lbi >= speicher.length - 1) return false;
		int index = index(n); // liefert index i, an dem n im Speicher steht

		// lbi zeigt auf die größte Zahl die im Speicher ist
		// Schleife durchläuft von hinten (lbi) mit Bedingung, dass
		// dass lbi immer größer oder gleich dem Index sein muss 
		// Bis der Index gefunden wurde, an dem der neue Eintrag zahlenmäßig passt.
		// Dann wird dort eine Lücke geschaffen, und die neue Zahl wird eingetragen.
		for (int i = lbi; i >= index; i--) {
			speicher[i + 1] = speicher[i];
		}
		
		speicher[index] = n; // der neue Index ist jetzt n, hier steht er jetzt im Speicher
		lbi++;
		
		return true;

	}
	
	

	// ---------------------------------------------------------------------
	@Override
	public boolean loesche(long n) {

		// Entfernt ein n aus diesem Speicher, und liefert true.
		// Liefert false falls n in diesem Speicher nicht vorkommt.

		int index = index(n);
		
		if (index > lbi || index >= speicher.length || speicher[index] != n) return false; 
	
		for (int i = index; i < lbi; i++) {
			if(i+1 <= lbi) speicher[i] = speicher[i + 1];
		}
		
		lbi--;
		return true;
	}

	// ---------------------------------------------------------------------
	@Override
	public boolean istDrin(long n) {
	   
		// Liefert true wenn n in diesem Speicher vorkommt, und sonst false.
		
	   int index = index(n);
		return (index <= lbi && speicher[index] == n);
	
	}

	// ---------------------------------------------------------------------
	// Zum Testen:
	private void print(String name) {
		// Gibt name, speicher.length, lbi und alle long-Werte dieser
		// Sammlung (in 2 Zeilen) zur Standardausgabe aus:
		printf("%s.length: %d, lbi: %2d:%n", name, speicher.length, lbi);
		printf("%s.toString(): %s%n", name, this.toString());
	}

	// ---------------------------------------------------------------------
	static public void main(String[] sonja) {
		printf("LongSpeicher10: Jetzt geht es los!%n");
		printf("-----------------------------------%n");
		printf("Test Konstruktor und toString:%n%n");
		LongSpeicher20 lsa = new LongSpeicher20(4);
		lsa.print("lsa");
		printf("-----------------------------------%n");
		printf("Positive Tests mit fuegeEin:%n%n");
		printf("lsa.fuegeEin(25): %b%n", lsa.fuegeEin(25));
		lsa.print("lsa");
		printf("lsa.fuegeEin(15): %b%n", lsa.fuegeEin(15));
		lsa.print("lsa");
		printf("lsa.fuegeEin(40): %b%n", lsa.fuegeEin(40));
		lsa.print("lsa");
		printf("lsa.fuegeEin(40): %b%n", lsa.fuegeEin(40));
		lsa.print("lsa");

		printf("-----------------------------------%n");
		printf("Negative Tests mit fuegeEin:%n%n");

		printf("lsa.fuegeEin(50): %b%n", lsa.fuegeEin(50));
		lsa.print("lsa");
		printf("lsa.fuegeEin(60): %b%n", lsa.fuegeEin(60));
		lsa.print("lsa");
		printf("lsa.fuegeEin(15): %b%n", lsa.fuegeEin(15));
      lsa.print("lsa");

		printf("-----------------------------------%n");
		printf("Positive Tests mit istDrin:%n%n");

		printf("lsa.istDrin(25): %b%n", lsa.istDrin(25));
		lsa.print("lsa");

		printf("-----------------------------------%n");
		printf("Negative Tests mit istDrin:%n%n");

		printf("lsa.istDrin(50): %b%n", lsa.istDrin(50));
		lsa.print("lsa");

		printf("-----------------------------------%n");
		printf("Tests mit Index, der existiert:%n%n");

		printf("lsa.index(25): %d%n", lsa.index(25));
		lsa.print("lsa");
		printf("lsa.index(15): %d%n", lsa.index(15));
		lsa.print("lsa");

		printf("-----------------------------------%n");
		printf("Tests mit Index, der nicht existiert:%n%n");

		printf("lsa.index(60): %d%n", lsa.index(60));
		lsa.print("lsa");
		printf("lsa.index(70): %d%n", lsa.index(70));
      lsa.print("lsa");
      printf("lsa.index(10): %d%n", lsa.index(10));
      lsa.print("lsa");
		printf("lsa.index(15): %d%n", lsa.index(15));
      lsa.print("lsa");

		printf("-----------------------------------%n");
		printf("Tests mit Löschen:%n%n");

		printf("lsa.loesche(25): %b%n", lsa.loesche(25));
		lsa.print("lsa");
		printf("lsa.loesche(50): %b%n", lsa.loesche(50));
		lsa.print("lsa");

		printf("-----------------------------------%n");
		printf("-----------------------------------%n");
		printf("LongSpeicher10: Das war's erstmal!%n%n");

		// System.out.println(Integer.MAX_VALUE);

		// long[] r1 = {10, 20, 30};
		// System.out.println(r1);
		// System.out.println(Arrays.toString(r1));

	} // main

	// ---------------------------------------------------------------------
} 
