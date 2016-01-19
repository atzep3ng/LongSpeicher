// Datei LongSpeicher50.java
/* ------------------------------------------------------------------------
 Jedes Objekt der Klasse LongSpeicher50 ist ein Speicher, in dem man
 long-Werte sammeln (einfuegen, entfernen, suchen) kann.
 Doppelgaenger sind erlaubt.
 TERMINOLOGIE: Als "n-Knoten" wird hier ein Knoten bezeichnet, der in seinem
 data-Attribut n enthaelt. Welcher (long-) Wert mit n gemeint ist, muss aus
 dem Kontext folgen. Dummy-Knoten zaehlen nicht als n-Knoten (auch wenn sie
 n enthalten).
 ---------------------------------------------------------------------------
 Implementierung: Als binaerer Baum
 Jedes Knoten-Objekt enthaelt ein long Attribut und zwei Knoten[]-Attribute.
 Die Knoten[]-Attribute zeigen auf Reihungen der Laenge 1.
 Uebergibt man einer Methode eine solche Reihung r, so kann diese den
 Wert der Variable r[0] veraendern (z.B. auf einen anderen Knoten zeigen
 lassen). Mit diesem "Trick" wird eine Parameteruebergabe per Referenz
 (die es in Java offiziell nicht gibt) nachgeahmt.
 ------------------------------------------------------------------------ */
class LongSpeicher50 extends AbstractLongSpeicher {
   // ---------------------------------------------------------------------
   // Zum Ein-/Ausschalten von Testbefehlen:
   static final boolean NAM  = false;
   static final boolean TST1 = false;
   static final boolean TST2 = false;

   // ---------------------------------------------------------------------
   // Eine (statische) geschachtelte Klasse (nested static class),
   // keine innere Klasse!
   // Jedes Objekt dieser Klasse kann als Knoten eines binaeren Baums
   // verwendet werden.
   static private class Knoten {
      // Die Reihungen lub und rub werden immer die Laenge 1 haben.
      // Sie ermöglichen es, die Referenzen von Knoten per Referenz
      // an Methoden zu uebergeben.
      long     data;
      Knoten[] lub; // lub[0] ist der linke  Unterbaum
      Knoten[] rub; // rub[0] ist der rechte Unterbaum
      char     name; // Nur zum Testen: Um Doppelgaenger zu unterscheiden

      Knoten(long data, Knoten lub, Knoten rub) { // Konstruktor
         this.data = data;
         this.lub = new Knoten[] { lub };
         this.rub = new Knoten[] { rub };
         if (NAM) this.name = naechsterName++;
      }

      // Der EDK bekommt 'A' als Name,
      // die richtigen Knoten bekommen 'B', 'C', 'D', ... als name
      static char naechsterName = 'A';
   } // class Knoten
     // ---------------------------------------------------------------------
     // Ein leerer Baum besteht aus einem End-Dummy-Knoten EDK und einer
     // Knoten-Reihung AR (der Laenge 1). AR[0] ist anfangs gleich dem EDK
     // und ist spaeter der erste "richtige Knoten" (die Wurzel) des Baums

   private final Knoten   EDK = new Knoten(0, null, null);
   private final Knoten[] AR  = new Knoten[] { EDK };

   // ---------------------------------------------------------------------
   private Knoten[] vorgaenger(long n) {
      // Liefert eine Knoten-Reihung r (der Laenge 1), die Teil eines
      // Knotens dieser Sammlung ist, und fuer die gilt:
      // r[0] ist ein n-Knoten (falls es einen solchen gibt) und sonst
      // der EDK. Falls es mehrere n-Knoten gibt, ist r[0] der
      // erste ("oberste") dieser Knoten.
      // Die gesuchte Zahl n in den End-Dummy-Knoten eintragen
      // (spaetestens dort wird sie dann gefunden)
      EDK.data = n;
      return vorgaengerR(n, AR);
   }

   private Knoten[] vorgaengerR(long n, Knoten[] hier) {
      // Eine rekursive Methode. Erledigt, was vorgaenger (ohne R)
      // versprochen hat (sollte nur von vorgaenger aufgerufen werden).
      if(lt(n, hier[0].data)) return vorgaengerR(n, hier[0].lub);
      if(gt(n, hier[0].data)) return vorgaengerR(n, hier[0].rub);
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
      //
      // Falls NAM den Wert true hat, erscheint vor jeder long-Zahl
      // der name des Knoten, z.B. [B10, D20, C30] statt [10, 20, 30].

      if (AR[0] == EDK) return "[]"; // Wenn dieser Speicher leer ist
      StringBuilder sb = new StringBuilder("[");
      toStringR(AR, sb);      
      // Die letzten Trennzeichen ", " werden durch "]" ersetzt:
      sb.replace(sb.length()-2, sb.length(), "]");
      return sb.toString();
   }

   private void toStringR(Knoten[] hier, StringBuilder sb) {
      // Rekurisve Hilfsmethode fuer toString.
      if (hier[0] == EDK) return; // hier[0] leerer Unterbaum?
      toStringR(hier[0].lub, sb); // Linken Unterbaum bearbeiten
      if (NAM) sb.append(hier[0].name);
      sb.append(hier[0].data);
      sb.append(", ");
      toStringR(hier[0].rub, sb); // Rechten Unterbaum bearbeiten
   }

   // ---------------------------------------------------------------------
   @Override
   public boolean fuegeEin(long n) {
      // Fuegt n in diesen Speicher ein und liefert true.
      
      Knoten[] vorgaengerReferenz = vorgaenger(n);
      
      // Single Knoten einfügen
      if(vorgaengerReferenz[0] == EDK) {
         vorgaengerReferenz[0] = new Knoten(n, EDK, EDK);
         return true;
      }
      
      // Doppelgänger einfügen
      if(vorgaengerReferenz[0] != EDK) {
         Knoten doppelgaenger = new Knoten(n, vorgaengerReferenz[0].lub[0], EDK);
         vorgaengerReferenz[0].lub[0] = doppelgaenger;
         return true;
      }
      
      return true;
   }

   // ---------------------------------------------------------------------
   @Override
   public boolean loesche(long n) {
      // Liefert false, wenn diese Sammlung keinen n-Knoten enthaelt.
      // Loescht sonst einen n-Knoten und liefert true.
      // Falls diese Sammlung mehrere n-Knoten enthaelt, wird der geloescht,
      // der direkt links unter dem ersten ("obersten") n-Knoten haengt.
      Knoten[] vorgaengerReferenz = vorgaenger(n);
      if(vorgaengerReferenz[0] == EDK) return false; // n ist nicht im Speicher vorhanden
      
      // Falls es im Baum mehrere n-Knoten gibt
      // Doppelgänger haben keine rechten Unterbäume -> nicht beachten
      if(vorgaengerReferenz[0].lub[0].data == n && vorgaengerReferenz[0].lub[0] != EDK) {
         vorgaengerReferenz[0].lub[0] = vorgaengerReferenz[0].lub[0].lub[0];
         return true;
      }
      
      // Kein linker Unterbaum? Kein Doppelgänger!
      if(vorgaengerReferenz[0].lub[0] == EDK) {
         vorgaengerReferenz[0] = vorgaengerReferenz[0].rub[0];
         return true;
      }
      
      // Kein rechter Unterbaum?
      if(vorgaengerReferenz[0].rub[0] == EDK) {
         vorgaengerReferenz[0] = vorgaengerReferenz[0].lub[0];
         return true;
      }
      
      // Komplizierter Fall: es gibt linken und rechten Unterbaum und keine Doppelgänger
      Knoten[] rechtesterKnoten = vorgaengerReferenz[0].lub;
      while(rechtesterKnoten[0].rub[0] != EDK) {
         rechtesterKnoten = rechtesterKnoten[0].rub;
      }
      
      vorgaengerReferenz[0].data = rechtesterKnoten[0].data;
      rechtesterKnoten[0] = rechtesterKnoten[0].lub[0];
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
      // (links wenn NAM gleich true,  rechts wenn NAM gleich false ist):
      //
      //          H90                            90
      //       D80                            80
      //          G80                            80
      //    B60                            60
      //       E50                            50
      //          C40                            40
      //             F30                            30
      //
      // Diese Darstellungen sollte man sich um 90 Grad nach rechts gedreht
      // vorstellen (so dass die Wurzel B60 bzw. 60 ganz oben ist).
      // Die Buchstaben B, C, D etc. dienen zur Unterscheidung von
      // Doppelgaengern (z.B. D80 und G80). Sie erscheinen nur, wenn
      // NAM den Wert true hat.
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
      final String EINRUECK = "   ";
      // Ein Ende des Baumes erreicht?
      if (hier[0] == EDK) return;
      // Gib den linken Unterbaum aus:
      printR(hier[0].rub, einrueck + EINRUECK);
      // Gib den aktuellen Knoten aus:
      printf("%s", einrueck);
      if (NAM) printf("%c", hier[0].name);
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
      printf("LongSpeicher50: Jetzt geht es los!%n");
      printf("A ------------------------------- A%n");
      printf("Positive Tests mit fuegeEin:%n%n");
      LongSpeicher50 lsa = new LongSpeicher50();
      lsa.print();
      printf("lsa.fuegeEin(60): %-5b%n", lsa.fuegeEin(60));
      lsa.print();
      printf("lsa.fuegeEin(50): %-5b%n", lsa.fuegeEin(50));
      lsa.print();
      printf("lsa.fuegeEin(70): %-5b%n", lsa.fuegeEin(70));
      lsa.print();
      printf("lsa.fuegeEin(60): %-5b%n", lsa.fuegeEin(60));
      lsa.print();
      printf("lsa.fuegeEin(50): %-5b%n", lsa.fuegeEin(50));
      lsa.print();
      printf("lsa.fuegeEin(70): %-5b%n", lsa.fuegeEin(70));
      lsa.print();
      printf("lsa.fuegeEin(60): %-5b%n", lsa.fuegeEin(60));
      lsa.print();
      printf("lsa.fuegeEin(50): %-5b%n", lsa.fuegeEin(50));
      lsa.print();
      printf("lsa.fuegeEin(70): %-5b%n", lsa.fuegeEin(70));
      lsa.print();
      printf("lsa.istDrin(50): %-5b%n", lsa.istDrin(50));
      printf("lsa.istDrin(60): %-5b%n", lsa.istDrin(60));
      printf("lsa.istDrin(70): %-5b%n", lsa.istDrin(70));
      printf("lsa.istDrin(45): %-5b%n", lsa.istDrin(45));
      printf("lsa.istDrin(55): %-5b%n", lsa.istDrin(55));
      printf("lsa.istDrin(65): %-5b%n", lsa.istDrin(65));
      printf("lsa.istDrin(75): %-5b%n", lsa.istDrin(75));
      printf("%n");
      printf("B ------------------------------- B%n");
      // Hier sollen Sie weitere aehnliche Testbefehle einfuegen
      printf("H ------------------------------- H%n");
      printf("LongSpeicher50: Das war's erstmal!%n%n");
   } // main
     // ---------------------------------------------------------------------
} // class LongSpeicher50
