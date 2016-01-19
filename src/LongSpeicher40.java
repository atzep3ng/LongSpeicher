

// Datei LongSpeicher40.java
/* ------------------------------------------------------------------------
Jedes Objekt der Klasse LongSpeicher40 ist ein Speicher, in dem man
long-Werte sammeln (einfuegen, entfernen, suchen) kann.
Doppelgaenger sind erlaubt.
---------------------------------------------------------------------------
Implementierung: Als sortierte einfach verkettete Liste
------------------------------------------------------------------------ */
class LongSpeicher40 extends AbstractLongSpeicher {
   // ---------------------------------------------------------------------
   // Zum Ein-/Ausschalten von Testbefehlen:
   static final boolean TST1 = false;

   // ---------------------------------------------------------------------
   // Eine (statische) geschachtelte Klasse (nested static class).
   // Jedes Objekt dieser Klasse kann als Knoten einer einfach
   // verketteten Liste verwendet werden:
   static protected class Knoten {
      Knoten next;
      long   data;

      Knoten(Knoten next, long data) { // Konstruktor
         this.next = next;
         this.data = data;
      }
   } // class Knoten
     // ---------------------------------------------------------------------
     // Eine leere Liste besteht aus 2 Dummy-Knoten:
     // einem End-Dummy-Knoten EDK und einem Anfangs-Dummy-Knoten ADK. Die
     // "richtigen Knoten" werden spaeter zwischen die 2 Dummies gehaengt.

   final Knoten EDK = new Knoten(null, 0); // End-Dummy-Knoten
   final Knoten ADK = new Knoten(EDK, 0); // Anfangs-Dummy-Knoten

   // ---------------------------------------------------------------------
   private Knoten vorgaenger(long n) {
      // Liefert den ersten Knoten k in dieser sortierten Liste fuer den
      // gilt: ge(k.next.data, n)
      // (d.h. k ist der Vorgaenger des ersten Knotens,
      // dessen data-Komponente groesser oder gleich n ist).
      // Falls n in dieser Sammlung nicht vorkommt, ist k der
      // Vorgaenger des EDK.

      EDK.data = n;

      Knoten k = ADK; 
      while (k.next.data < n) k = k.next; 
      
      return k; 

   }

   // ---------------------------------------------------------------------
   @Override
   public String toString() {
      // Liefert eine String-Darstellung dieses Speichers. Beispiele:
      //                    // Anzahl der long-Werte im Speicher:
      // "[]"               //  0
      // "[10]"             //  1
      // "[20, 30, 10]"     //  3

      if (ADK.next == EDK) return "[]";

      StringBuilder sb = new StringBuilder("[" + ADK.next.data);
      Knoten aktuellerKnoten = ADK.next.next;
      while (aktuellerKnoten != EDK) {
         sb.append(", " + aktuellerKnoten.data);
         aktuellerKnoten = aktuellerKnoten.next;
      }
      sb.append("]");
      return sb.toString();
   }

   // ---------------------------------------------------------------------
   @Override
   public boolean fuegeEin(long n) {
      // Fuegt n in diesen Speicher ein und liefert true.

      Knoten k = vorgaenger(n);
      k.next = new Knoten(k.next, n);
      
      return true;

   }

   // ---------------------------------------------------------------------
   @Override
   public boolean loesche(long n) {
      // Loescht ein Vorkommen von n in diesem Speicher, und liefert true.
      // Liefert false falls n nicht in diesem Speicher vorkommt.

      Knoten knoten = vorgaenger(n);
      if (knoten.next == EDK || knoten.next.data != n) return false;

      knoten.next = knoten.next.next;
      return true;

   }

   // ---------------------------------------------------------------------
   @Override
   public boolean istDrin(long n) {
      // Liefert true wenn n in diesem Speicher vorkommt, und sonst false.
      
      Knoten knoten = vorgaenger(n);
      return (knoten.next != EDK && knoten.next.data == n);
   }

   // ---------------------------------------------------------------------
   // Zum Testen:
   private void print(String name) {
      // Gibt den name und diese Sammlung in lesbarer Form
      // zur Standardausgabe aus:
      printf("%s.toString(): %s%n", name, this.toString());
   }

   private String nach_vor(long n) { // Nachfolger des Vorgaengers
      // Zum Testen der Methode vorgaenger.
      // Liefert eine String-Repraesentation des Nachfolgers
      // des Knotens vorgaenger(n). Diese String-Repraesentation
      // stellt n dar oder ist gleich "EDK".

      Knoten nv = vorgaenger(n).next;

      if (nv == ADK) return "ADK"; // Sollte nie benoetigt werden
      if (nv == EDK) return "EDK";
      return "" + nv.data;
   }

   // ---------------------------------------------------------------------
   static public void main(String[] sonja) {
      printf("LongSpeicher30: Jetzt geht es los!%n");
      printf("-----------------------------------%n");
//      printf("Test vorgaenger (ohne fuegeEin!):%n%n");
      LongSpeicher40 lsc = new LongSpeicher40();
      lsc.print("lsc");
      //Einfuegen ohne fuegeEin: Reihenfolge falschrum
//      Knoten adk = lsc.ADK;
//      adk.next = new Knoten(adk.next, 25);
//      adk.next = new Knoten(adk.next, 35);
//      adk.next = new Knoten(adk.next, 15);
//      System.out.println(adk.next.data);

      printf("%n");

//      printf("lsc.nach_vor(15): %s%n", lsc.nach_vor(15)); // sollte die Zahl selbst ausgeben
//      printf("lsc.nach_vor(25): %s%n", lsc.nach_vor(25));
//      printf("lsc.nach_vor(35): %s%n", lsc.nach_vor(35));
//      printf("lsc.nach_vor(30): %s%n", lsc.nach_vor(30));
//      printf("%n");
//      lsc.print("lsc");
      printf("-----------------------------------%n");

      printf("Test fuegeEin:%n%n");

      printf("lsc.fuegeEin(80): %s%n", lsc.fuegeEin(80));
      printf("lsc.fuegeEin(60): %s%n", lsc.fuegeEin(60));
      printf("lsc.fuegeEin(50): %s%n", lsc.fuegeEin(50));
      printf("lsc.fuegeEin(60): %s%n", lsc.fuegeEin(60));
      printf("lsc.fuegeEin(70): %s%n", lsc.fuegeEin(70));
      printf("lsc.fuegeEin(40): %s%n", lsc.fuegeEin(40));
      printf("lsc.fuegeEin(0): %s%n", lsc.fuegeEin(0));
      printf("lsc.fuegeEin(1_000_000): %s%n", lsc.fuegeEin(1_000_000));

      lsc.print("lsc");
      printf("-----------------------------------%n");

      printf("Test vorgaenger:%n%n");

      printf("lsc.vorgaenger(60): %s%n", lsc.vorgaenger(60).data);
      printf("lsc.vorgaenger(50): %s%n", lsc.vorgaenger(50).data);
      printf("lsc.vorgaenger(40): %s%n", lsc.vorgaenger(40).data);
      printf("lsc.vorgaenger(0): %s%n", lsc.vorgaenger(0).data);
      printf("lsc.vorgaenger(1_000_000): %s%n", lsc.vorgaenger(1_000_000).data);
      lsc.print("lsc");
      printf("lsc.loesche(0): %s%n", lsc.loesche(0));
      lsc.print("lsc");
      printf("lsc.vorgaenger(0): %s%n", lsc.vorgaenger(0).data);
      printf("lsc.vorgaenger(1_000_000): %s%n", lsc.vorgaenger(1_000_000).data);
      printf("lsc.vorgaenger(2_000_000): %s%n", lsc.vorgaenger(2_000_000).data);

      printf("-----------------------------------%n");
      printf("Test istDrin:%n%n");

      printf("lsc.istDrin(60): %s%n", lsc.istDrin(60));
      printf("lsc.istDrin(60): %s%n", lsc.istDrin(70));
      printf("lsc.istDrin(60): %s%n", lsc.istDrin(80));
      printf("lsc.istDrin(60): %s%n", lsc.istDrin(50));
      printf("lsc.istDrin(60): %s%n", lsc.istDrin(40));
      printf("lsc.istDrin(60): %s%n", lsc.istDrin(0));
      printf("lsc.istDrin(60): %s%n", lsc.istDrin(80));
      printf("lsc.istDrin(60): %s%n", lsc.istDrin(99));
      printf("lsc.istDrin(60): %s%n", lsc.istDrin(5_000));

      printf("-----------------------------------%n");
      printf("Test mit loesche bei doppeltem Eintrag, wieder einfuegen, istDrin:%n%n");

      printf("lsc.loesche(60): %s%n", lsc.loesche(60));
      printf("lsc.istDrin(60): %s%n", lsc.istDrin(60));
      printf("lsc.loesche(60): %s%n", lsc.loesche(60));
      printf("lsc.istDrin(60): %s%n", lsc.istDrin(60));
      printf("lsc.fuegeEin(60): %s%n", lsc.fuegeEin(60));
      printf("lsc.istDrin(60): %s%n", lsc.istDrin(60));

      printf("-----------------------------------%n");
      printf("Test mit loesche, wenn Eintrag nicht vorhanden:%n%n");
      printf("lsc.loesche(880): %s%n", lsc.loesche(880));

      printf("-----------------------------------%n");
      printf("Test mit loesche, wenn Eintrag vorhanden:%n%n");
      printf("lsc.loesche(40): %s%n", lsc.loesche(40));

      printf("-----------------------------------%n");
      printf("Test toString:%n%n");
      lsc.print("lsc");

      printf("-----------------------------------%n");
      printf("Test loeschen, dann toString:%n%n");

      printf("lsc.loesche(60): %s%n", lsc.loesche(60));
      printf("lsc.loesche(1_000_000): %s%n", lsc.loesche(1_000_000));
      printf("lsc.loesche(0): %s%n", lsc.loesche(0));
      lsc.print("lsc");
      printf("lsc.loesche(80): %s%n", lsc.loesche(80));
      printf("lsc.loesche(70): %s%n", lsc.loesche(70));
      printf("lsc.loesche(50): %s%n", lsc.loesche(50));
      lsc.print("lsc");
      printf("lsc.loesche(15): %s%n", lsc.loesche(15));
      printf("lsc.loesche(35): %s%n", lsc.loesche(35));
      printf("lsc.loesche(25): %s%n", lsc.loesche(25));
      lsc.print("lsc");

      printf("-----------------------------------%n");
      printf("Test loeschen, wenn kein Eintrag vorhanden:%n%n");
      printf("lsc.loesche(25): %s%n", lsc.loesche(25));

      printf("-----------------------------------%n");
      printf("LongSpeicher30: Das war's erstmal!%n%n");
   } // main
     // ---------------------------------------------------------------------
}
