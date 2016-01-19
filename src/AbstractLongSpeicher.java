	
	/* ------------------------------------------------------------------------
	 * Datei AbstractLongSpeicher.java
	 *
	 * Von dieser abstrakten Klasse sollen mehrere konkrete Unterklassen
	 * (LongSpeicher10, LongSpeicher20, ...) entwickelt werden.
	 * Jedes Objekt einer sollchen konkreten Unterklasse ist ein long-Speicher,
	 * d.h. ein Speicher
	 * in  den man long-Werte einfuegen kann (mit fuegeEin),
	 * aus dem man long-Werte entfernen kann (mit loesche)
	 * und in dem man die Anwesenheit eines bestimmten long-Wertes pruefen
	 * kann (mit istDrin).
	 *
	 * Alle long-Speicher sollen es zulassen, dass man einen bestimmten
	 * long-Wert mehrmals einfuegt (d.h. Doppelgaenger sollen erlaubt sein).
	------------------------------------------------------------------------ */
	public abstract class AbstractLongSpeicher {
	
	   abstract public String toString();
	   // Liefert eine String-Darstellung dieses Speichers. Beispiele:
	   //                    // Anzahl der long-Werte im Speicher:
	   // "[]"               //  0
	   // "[10]"             //  1
	   // "[20, 30, 10]"     //  3
	
	   abstract public boolean fuegeEin(long n);
	   // Fuegt n in diesen Speicher ein und liefert true.
	   // Liefert false, wenn n nicht eingefuegt werden konnte.
	
	   abstract public boolean loesche(long n);
	   // Loescht ein Vorkommen von n in diesem Speicher, und liefert true.
	   // Liefert false falls n nicht in diesem Speicher vorkommt.
	
	   abstract public boolean istDrin(long n);
	   // Liefert true wenn n in diesem Speicher vorkommt, und sonst false.
	
	   // Methoden mit kurzen Namen:
	   // @formatter:off   Hier ist nur eine Zeile pro Methode vorteilhaft
	   static void printf(String s, Object... ob) {System.out.printf (s, ob);}
	   static void pln(Object ob)                 {System.out.println(   ob);}
	   static void p  (Object ob)                 {System.out.print  (   ob);}
	   static void pln()                          {System.out.println(     );}
	
	   // long-Werte, die in einen long-Speicher eingefuegt werden, sollen
	   // nicht mit den Operatoren <, <=, == etc, verglichen werden, sondern
	   // mit den folgenden Funktionen lt, le, eq etc.
	   // Dadurch wird es leichter, den Typ long (spaeter einmal) durch einen
	   // anderen Typ (z.B. int oder String oder ...) zu ersetzen.
	   static boolean lt(long a, long b) {return a <  b;} // less then
	   static boolean le(long a, long b) {return a <= b;} // less or equal
	   static boolean eq(long a, long b) {return a == b;} // equal
	   static boolean ge(long a, long b) {return a >= b;} // greater or equal
	   static boolean gt(long a, long b) {return a >  b;} // greater
	   static boolean ne(long a, long b) {return a != b;} // not equal
	} // AbstractLongSpeicher


