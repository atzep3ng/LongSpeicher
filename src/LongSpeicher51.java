import java.util.Arrays;

// Datei LongSpeicher51.java
/* ------------------------------------------------------------------------
 Jedes Objekt der Klasse LongSpeicher51 ist ein Speicher, in dem man
 long-Werte sammeln (einfuegen, entfernen, suchen) kann.
 Vereinfachte Version: Doppelgaenger sind NICHT erlaubt (d.h. werden
 nicht eingefuegt).
 TERMINOLOGIE: Als "n-Knoten" wird hier ein Knoten bezeichnet, der in seinem
 data-Attribut n enthaelt. Welcher (long-) Wert mit n gemeint ist, muss aus
 dem Kontext folgen. Dummy-Knoten zaehlen nicht als n-Knoten (auch wenn sie
 n enthalten).
 ---------------------------------------------------------------------------
 Implementierung: Als binaerer Baum
 Jedes Knoten-Objekt enthaelt ein long Attribut und zwei Knoten[]-Attribute.
 Die Knoten[]-Attribute zeigen auf Reihungen der Laenge 1.
 Uebergibt man einer Methode eine solche Reihung r, so kann die Methode den
 Wert der Variable r[0] veraendern (z.B. auf einen anderen Knoten zeigen
 lassen). Mit diesem "Trick" wird eine Parameteruebergabe per Referenz
 (die es in Java offiziell nicht gibt) nachgeahmt.
 ------------------------------------------------------------------------ */
class LongSpeicher51 extends AbstractLongSpeicher {
   // ---------------------------------------------------------------------
   // Zum Ein-/Ausschalten von Testbefehlen:
   static final boolean TST1 = false;

   // ---------------------------------------------------------------------
   // Eine (statische) geschachtelte Klasse (nested static class),
   // keine innere Klasse!
   // Jedes Objekt dieser Klasse kann als Knoten eines binaeren Baums
   // verwendet werden.
   static protected class Knoten {
      // Die Reihungen lub und rub werden immer die Laenge 1 haben.
      // Sie ermöglichen es, die Referenzen von Knoten per Referenz
      // (auf die Reihung) an Methoden zu uebergeben.
      long     data;
      Knoten[] lub; // lub[0] ist der linke  Unterbaum
      Knoten[] rub; // rub[0] ist der rechte Unterbaum

      Knoten(long data, Knoten lub, Knoten rub) { // Konstruktor
         this.data = data;
         this.lub = new Knoten[] { lub };
         this.rub = new Knoten[] { rub };
      }
   } // class Knoten
     // ---------------------------------------------------------------------
     // Ein leerer Baum besteht aus einem End-Dummy-Knoten EDK und einer
     // Knoten-Reihung AR (der Laenge 1). AR[0] ist anfangs gleich dem EDK
     // und ist spaeter der erste "richtige Knoten" (die Wurzel) des Baums

   private final Knoten   EDK = new Knoten(0, null, null);
   private final Knoten[] AR  = new Knoten[] { EDK };

   // ---------------------------------------------------------------------
   Knoten[] vorgaenger(long n) {
      // Liefert eine Knoten-Reihung r (der Laenge 1), die Teil eines
      // Knotens dieser Sammlung ist, und fuer die gilt:
      // r[0] ist der n-Knoten (falls es einen solchen gibt) und sonst
      // der EDK.
      // Die gesuchte Zahl n in den End-Dummy-Knoten eintragen
      // (spaetestens dort wird sie dann gefunden)
      EDK.data = n;
      return vorgaengerR(n, AR);
   }

   private Knoten[] vorgaengerR(long n, Knoten[] hier) {
      // Eine rekursive Methode. Erledigt, was vorgaenger (ohne R)
      // versprochen hat (sollte nur von vorgaenger aufgerufen werden).

      if (lt(n, hier[0].data)) return vorgaengerR(n, hier[0].lub);
      if (gt(n, hier[0].data)) return vorgaengerR(n, hier[0].rub);
      return hier;
   }

   // ---------------------------------------------------------------------
   @Override
   public String toString() {
      // Liefert eine String-Darstellung dieses Speichers. Beispiele:
      //
      // Zahl(en) im      Ergebnis von:
      // Speicher         toString
      //   --               "[]"
      //   10               "[10]"
      //   10, 20, 30       "[10, 20, 30]"
      if (AR[0] == EDK) return "[]";
      StringBuilder sb = new StringBuilder("[");
      toStringR(AR, sb);
      sb.replace(sb.length() - 2, sb.length(), "]");
      return sb.toString();
   }

   private void toStringR(Knoten[] hier, StringBuilder sb) {
      // Rekurisve Hilfsmethode fuer toString.
      if (hier[0] == EDK) return;
      toStringR(hier[0].lub, sb);
      sb.append(hier[0].data);
      sb.append(", ");
      toStringR(hier[0].rub, sb);
   }

   // ---------------------------------------------------------------------
   @Override
   public boolean fuegeEin(long n) {
      // Liefert false, wenn n bereits in dieser Sammlung vorkommt.
      // Fuegt sonst n in diesen Speicher ein und liefert true.

      Knoten[] vorgaengerReferenz = vorgaenger(n);
      // Wenn die Zahl schon vorhanden ist, dann füge sie nicht ein
      if (vorgaengerReferenz[0] != EDK) return false;

      Knoten neuerKnoten = new Knoten(n, EDK, EDK);
      vorgaengerReferenz[0] = neuerKnoten;
      return true;
   }

   // ---------------------------------------------------------------------
   @Override
   public boolean loesche(long n) {
      // Liefert false, wenn n in diesem Speicher nicht vorkommt.
      // Loescht sonst den Knoten, der n enthaelt und liefert true.

      Knoten[] knotenReferenz = vorgaenger(n);

      //Falls n nicht im Speicher vorkommt
      if (knotenReferenz[0] == EDK) return false;
      
      // Falls der Knoten knoten[0] keinen linken Unterbaum hat
      if (knotenReferenz[0].lub[0] == EDK) {
         knotenReferenz[0] = knotenReferenz[0].rub[0];
         return true;
      }
      
      // Falls der Knoten knoten[0] keinen rechten Unterbaum hat
      if (knotenReferenz[0].rub[0] == EDK) {
         knotenReferenz[0] = knotenReferenz[0].lub[0];
         return true;
      }
      
      // Falls der Knoten knoten[0] zwei (nicht-leere) Unterbaeume hat
      Knoten[] maxKnoten = knotenReferenz[0].lub;
      while (maxKnoten[0].rub[0] != EDK)
         maxKnoten = maxKnoten[0].rub;
      
      // Überschreiben
      knotenReferenz[0].data = maxKnoten[0].data;
      
      // Rechten Knoten Maximum löschen
      maxKnoten[0] = maxKnoten[0].lub[0];
      return true;

   }

   // ---------------------------------------------------------------------
   @Override
   public boolean istDrin(long n) {
      // Liefert true wenn n in diesem Speicher vorkommt, sonst false.
      return vorgaenger(n)[0] != EDK;
   }

   // ---------------------------------------------------------------------
   // Zum Testen
   private void print() {
      // Gibt den Baum, auf den AR zeigt, in lesbarer Form aus:
      // Pro Zeile ein Knoten, Unterbaeume sind eingerueckt, z.B. so
      //
      //         90
      //      80
      //         80
      //   60
      //      50
      //         40
      //            30
      //
      // Diese Darstellungen sollte man sich um 90 Grad nach rechts gedreht
      // vorstellen (so dass die Wurzel 60 ganz oben ist).
      printf("+--------------------------%n"); // Anfangs-Zeile
      if (AR[0] == EDK) {
         printf("| Leerer Baum!%n");
      } else {
         printR(AR, "| "); // Alle normalen Zeilen beginnen mit "| "
      }
      printf("+--------------------------%n"); // Ende-Zeile
   }

   private void printR(Knoten[] hier, String einrueck) {
      // Rekursive Hilfsmethode fuer print
      // Eine Einrueck-Stufe:
      final String EINRUECK = "   "; // Eine Stufe gleich 3 Leerzeichen
      // Ein Ende des Baumes erreicht?
      if (hier[0] == EDK) return;
      // Gib den linken Unterbaum aus:
      printR(hier[0].rub, einrueck + EINRUECK);
      // Gib den aktuellen Knoten aus:
      printf("%s", einrueck);
      printf("%2d%n", hier[0].data);
      // Gib den rechten Unterbaum aus:
      printR(hier[0].lub, einrueck + EINRUECK);
   }

   // ---------------------------------------------------------------------
   // Eine alternative print-Methode printB:
   private void printB(String name) {
      // Gibt den name und den Baum, auf den AR zeigt, in lesbarer Form aus
      // (alle Komponenten auf einer Zeile, durch Kommas getrennt in
      // eckige Klammern eingeschlossen).
      printf("%s: %s%n", name, this.toString());
   }

   // ---------------------------------------------------------------------
   static public void main(String[] sonja) {
      printf("LongSpeicher51: Jetzt geht es los!%n");
      printf("A ------------------------------ A%n");
      printf("Positive Tests mit fuegeEin:%n%n");
      LongSpeicher51 fsa = new LongSpeicher51();
      fsa.print();
      printf("fsa.fuegeEin(65): %-5b%n", fsa.fuegeEin(65));
      fsa.print();
      printf("fsa.fuegeEin(45): %-5b%n", fsa.fuegeEin(45));
      fsa.print();
      printf("fsa.fuegeEin(35): %-5b%n", fsa.fuegeEin(35));
      fsa.print();
      printf("fsa.fuegeEin(85): %-5b%n", fsa.fuegeEin(85));
      fsa.print();
      printf("fsa.fuegeEin(75): %-5b%n", fsa.fuegeEin(75));
      fsa.print();
      printf("fsa.fuegeEin(55): %-5b%n", fsa.fuegeEin(55));
      fsa.print();
      printf("fsa.fuegeEin(95): %-5b%n", fsa.fuegeEin(95));
      fsa.print();
      fsa.printB("fsa");
      printf("B ------------------------------ B%n");

      // Doppelgänger sind nicht erlaubt. Dieser Befehl muss false liefern.
      printf("fsa.fuegeEin(95): %-5b%n", fsa.fuegeEin(95));
      fsa.print();

      // Die 95 wird gelöscht und dann wieder eingefügt, muss true liefern.
      printf("fsa.loesche(95): %-5b%n", fsa.loesche(95));
      fsa.print();
      printf("fsa.fuegeEin(95): %-5b%n", fsa.fuegeEin(95));
      fsa.print();

      // Die 95 wurde hinzugefügt, der Befehl muss true liefern.
      printf("fsa.istDrin(95): %-5b%n", fsa.istDrin(95));
      fsa.print();

      // Test istDrin mit einer Zahl, die nicht im Speicher ist.
      printf("fsa.istDrin(97): %-5b%n", fsa.istDrin(97));
      fsa.print();

      // Wurzelknoten löschen
      printf("fsa.loesche(65): %-5b%n", fsa.loesche(65));
      fsa.print();

      // Vorgaenger testen am Wurzelknoten
      printf("fsa.vorgaenger(55): %s%n", fsa.vorgaenger(55)[0].data);
      fsa.print();

      // Vorgaenger testen bei vorhandenen Einträgen
      printf("fsa.vorgaenger(35): %s%n", fsa.vorgaenger(35)[0].data);
      fsa.print();

      // Vorgaenger testen bei nicht vorhandenen Einträgen
      printf("fsa.vorgaenger(100): %s%n", fsa.vorgaenger(100)[0].data);
      fsa.print();

      printf("H ------------------------------ H%n");
      printf("LongSpeicher51: Das war's erstmal!%n%n");
   } // main
     // ---------------------------------------------------------------------
} // class LongSpeicher51