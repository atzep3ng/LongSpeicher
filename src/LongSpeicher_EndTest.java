/* ------------------------------------------------------------------------
 * LongSpeicher_EndTest.java
 * Mit diesem JUnit 4 Testprogramm kann man die Abnahmetests für die
 * Klassen LongSpeicher10 bis LongSpeicher60 durchführen.
 *
 * DIESES TESTPROGRAMM SOLLTE NICHT SCHON WAEHREND DER ENTWICKLUNG EINER
 * KLASSE BENUTZT WERDEN (DAZU IST ES KAUM GEEIGNET, DA ES VIEL ZU WENIG
 * HILFREICHE MELDUNGEN AUSGIBT).
 *
 * Waehrend der Entwicklung einer Klasse K sollten Sie K durch geeignete
 * Befehle in der main-Methode testen. Diese Befehle sollten moeglichst
 * viele hilfreiche Meldungen ausgeben. Erst wenn Sie sicher sind, dass K
 * korrekt ist (und Sie viele hilfreiche Meldungen gesehen haben, die
 * das bestaetigen), sollten Sie einen Endtest mit dem vorliegenden
 * Testprogramm durchführen.
 *
 * Um dieses Testprogramm auf eine Klasse K anzuwenden, muss man den
 * vollen Namen der Klasse K als Zielwert der final-Variablen KLASSEN_NAME
 * vereinbaren (siehe ca. 23 Zeilen weiter unten).
 *
 * Dieses Test-Programm wendet auf die Klasse LongSpeicher51 keine Tests
 * an, in denen Doppelgaenger vorkommen (denn LongSpeicher51 darf und
 * sollte, als einzige Ausnahme, Doppelgaenger ablehnen).
 * ---------------------------------------------------------------------*/
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.Random;

public class LongSpeicher_EndTest {
   // ---------------------------------------------------------------------
   // Der volle Name der zu testenden Klasse ("mit allen Paketen davor").
   // Wenn die zu testende Klasse zum namenlosen Paket gehoert, ist
   // ihr voller Name gleich ihrem einfachen Namen, ohne Pakete davor.

   final static String KLASSEN_NAME = "LongSpeicher60";
   // ---------------------------------------------------------------------
   static final boolean TST1 = false;
   // ---------------------------------------------------------------------
   // Der einzige Konstruktor dieser Klasse
   // ---------------------------------------------------------------------
   public LongSpeicher_EndTest() {
      // Prueft folgende Bedingungen:
      // 1. Existiert eine Klasse namens KLASSEN_NAME?
      // 2. Erweitert sie die Klasse AbstractLongSpeicher?
      // 3. Hat sie genau einen Konstruktor?
      // 4. Hat dieser Konstruktor 0 oder 1 Parameter?
      // 5. Wenn er 1 Parameter hat: Ist der vom Typ int?
      // Beendet dieses Programm, falls nicht alle Bedingungen erfuellt
      // sind. Initialisiert sonst die Variablen kob und kon.

      try {
         // kob soll auf das Class-Objekt der zu testenden Klasse zeigen:
         kob = Class.forName(KLASSEN_NAME);

         // Erweitert die zu testende Klasse die Klasse
         // AbstractLongSpeicher?
         if (!AbstractLongSpeicher.class.isAssignableFrom(kob)) {
            printf("Die Klasse %s erweitert nicht "
               + "AbstractLongSpeicher!%n", KLASSEN_NAME);
            printf("Sollte sie aber!%n");
            printf("-------------------------------------------------%n");
            System.exit(1);
         }

         // Enthaelt die zu testende Klasse genau einen Konstruktor?
         Constructor<?>[] kons = kob.getDeclaredConstructors();
         if (kons.length > 1) {
            printf("Error: Die Klasse %s enthaelt %d Konstruktoren!%n",
               KLASSEN_NAME, kons.length);
            printf("Sie sollte nur EINEN Konstruktor enthalten!%n");
            printf("-------------------------------------------------%n");
            System.exit(2);
         }

         // kon soll auf den einzigen Konstruktor der
         // zu testenden Klasse zeigen:
         kon = kons[0];

         // Hat der Konstruktor kon genau einen Parameter?
         Class<?>[] paramTypen = kon.getParameterTypes();
         if (paramTypen.length > 1) {
            printf("Error: Der Konstruktor %s hat %d Parameter!%n",
               KLASSEN_NAME, paramTypen.length);
            printf("Er sollte EINEN Parameter vom Typ int haben!%n");
            printf("-------------------------------------------------%n");
            System.exit(3);
         }

         // Ist der (einzige) Parameter von kon vom Typ int?
         if (paramTypen.length == 1 && paramTypen[0] != Integer.TYPE) {
            printf("Error: Konstruktor %s hat Param vom Typ %s%n",
               KLASSEN_NAME, paramTypen[0]);
            printf("Der Param sollte vom Typ int sein!%n");
            printf("-------------------------------------------------%n");
            System.exit(4);
         }
         
      } catch (ClassNotFoundException ex) {
         printf("Eine Klasse namens %s%n", KLASSEN_NAME);
         printf("konnte nicht gefunden werden!%n");
         printf("-------------------------------------------------%n");
         System.exit(1);
      } catch (Exception ex) {
         printf("Ausnahme im Konstruktor LongSpeicher_EndTest:%n");
         printf("%s%n", ex.toString());
         printf("-------------------------------------------------%n");
         System.exit(5);
      }
   }
   // ---------------------------------------------------------------------
   // Attribute dieses Testprogramms
   // ---------------------------------------------------------------------
   // @formatter:off
   // Die zu testende Klasse:
   static Class<?> kob;
   // Der einzige Konstruktor von kob:
   static Constructor<?> kon;
   // Sind Sammlungen vom Typ kob begrenzt?
   static boolean istBegrenzt =
      KLASSEN_NAME.endsWith("LongSpeicher10") ||
      KLASSEN_NAME.endsWith("LongSpeicher20");
   // Sollte der Konstruktor der zu testenden Klasse einen
   // Parameter (vom Typ int) haben?
   static boolean konstruktorHatParam =
      istBegrenzt || KLASSEN_NAME.endsWith("LongSpeicher60");
   // Bei LongSpeicher60 (Hash-Tabelle) hat der Konstruktor zwar einen
   // int-Parameter, die Sammlungen sind aber nicht begrenzt.
   
   // Der Parameter fuer den Konstruktor, falls der Konstruktor einen
   // Parameter (vom Typ int) hat:
   static final int ARGA =   5; // fuer lsa
   static final int ARGB =  11; // fuer lsb
   static final int ARGC = 100; // fuer lsc

   // Die zu testenden Long-Speicher (die anfangs alle leer sind):
   AbstractLongSpeicher lsa;
   AbstractLongSpeicher lsb;
   AbstractLongSpeicher lsc;
   
   // Eine Reihungen mit ARGA vielen nicht-sortierten long-Werten:
   static final long[] lra = {30, 10, 50, 20, 40};
   
   static void fuelle(AbstractLongSpeicher ls, long[] lr) {
      // Fuegt die long-Werte aus lr in ls ein
      for (int i=0; i<lr.length; i++) ls.fuegeEin(lr[i]);
   }
   
   // formatter:on
   // ---------------------------------------------------------------------
   // Eine @BeforeClass-Methode (wird nur einmal am Anfang ausgefuehrt)
   // ---------------------------------------------------------------------
   @BeforeClass
   static public void welcheKlasse() {
      printf("+---------------------------------------%n");
      printf("| Getestet wir die Klasse %s%n", KLASSEN_NAME);
      printf("+---------------------------------------%n");
   }
   // ---------------------------------------------------------------------
   // Eine @Before-Methode (wird vor jeder @Test-Methode ausgefuehrt)
   // ---------------------------------------------------------------------
   @Before
   public void setUp() {
      // Initialisiert die Variablen lsa, lsb, lsc so, dass sie auf
      // Objekte des Typs AbstractLongSpeicher zeigen:
      try {
         // Drei Objekte der zu testenden Klasse erzeugen, entweder
         // mit Parameter oder ohne:
         if (konstruktorHatParam) {  // mit Parameter
           lsa = (AbstractLongSpeicher) kon.newInstance(ARGA);
           lsb = (AbstractLongSpeicher) kon.newInstance(ARGB);
           lsc = (AbstractLongSpeicher) kon.newInstance(ARGC);
         } else {                    // ohne Parameter
           lsa = (AbstractLongSpeicher) kon.newInstance();
           lsb = (AbstractLongSpeicher) kon.newInstance();
           lsc = (AbstractLongSpeicher) kon.newInstance();
         }
      } catch (Throwable t) {
         printf("Ausnahme in Methode setUp!%n");
         printf("%s%n", t);
      }
   }
   // ---------------------------------------------------------------------
   // Eine Hilfsmethode zum Testen der Methode toString
   static public String remove(String str, String[] sr) {
      // Liefert das String-Objekt welches entsteht, wenn man aus
      // str alle String-Objekte entfernt, die sich in sr befinden.
      // Beispiel:
      // str: "[20, 30, 10]"
      // sr:  {"10", "20", "30"}
      // Ergebnis von remove: "[, , ]"
      
      for (String s : sr) str = str.replaceFirst(s,  "");
      return str;
   }
   // ---------------------------------------------------------------------
   // Die @Test-Methoden (Testfaelle, test cases):
   // ---------------------------------------------------------------------
   @Test
   public void test_01() {
      // lsa, lsb, lsc sollten hier auf Objekte zeigen:
      assertNotNull("lsa ist gleich null!", lsa);
      assertNotNull("lsb ist gleich null!", lsb);
      assertNotNull("lsc ist gleich null!", lsc);
   }
   // ---------------------------------------------------------------------
   @Test
   public void test_02() {
      // Wird 0 zur richtigen Zeit gefunden?
      assertEquals("lsa.istDrin (0)", false, lsa.istDrin (0));
      assertEquals("lsa.fuegeEin(0)", true,  lsa.fuegeEin(0));
      assertEquals("lsa.istDrin (0)", true,  lsa.istDrin (0));
      assertEquals("lsa.loesche (0)", true,  lsa.loesche (0));
      assertEquals("lsa.istDrin (0)", false, lsa.istDrin (0));
   }
   // ---------------------------------------------------------------------
   @Test
   public void test_03() {
      // Systematische Tests mit istDrin und fuegeEin.
      // Es werden ARGA long-Werte in den Speicher lsa eingefuegt.
      // Falls lsa begrenzt ist, ist lsa damit voll.

      assertEquals("lsa.istDrin (10)", false, lsa.istDrin (10));
      assertEquals("lsa.istDrin (20)", false, lsa.istDrin (20));
      assertEquals("lsa.istDrin (30)", false, lsa.istDrin (30));
      assertEquals("lsa.istDrin (40)", false, lsa.istDrin (40));
      assertEquals("lsa.istDrin (50)", false, lsa.istDrin (50));

      assertEquals("lsa.fuegeEin(30)", true,  lsa.fuegeEin(30));

      assertEquals("lsa.istDrin (10)", false, lsa.istDrin (10));
      assertEquals("lsa.istDrin (20)", false, lsa.istDrin (20));
      assertEquals("lsa.istDrin (30)", true,  lsa.istDrin (30));
      assertEquals("lsa.istDrin (40)", false, lsa.istDrin (40));
      assertEquals("lsa.istDrin (50)", false, lsa.istDrin (50));

      assertEquals("lsa.fuegeEin(10)", true,  lsa.fuegeEin(10));

      assertEquals("lsa.istDrin (10)", true,  lsa.istDrin (10));
      assertEquals("lsa.istDrin (20)", false, lsa.istDrin (20));
      assertEquals("lsa.istDrin (30)", true,  lsa.istDrin (30));
      assertEquals("lsa.istDrin (40)", false, lsa.istDrin (40));
      assertEquals("lsa.istDrin (50)", false, lsa.istDrin (50));

      assertEquals("lsa.fuegeEin(50)", true,  lsa.fuegeEin(50));

      assertEquals("lsa.istDrin (10)", true,  lsa.istDrin (10));
      assertEquals("lsa.istDrin (20)", false, lsa.istDrin (20));
      assertEquals("lsa.istDrin (30)", true,  lsa.istDrin (30));
      assertEquals("lsa.istDrin (40)", false, lsa.istDrin (40));
      assertEquals("lsa.istDrin (50)", true,  lsa.istDrin (50));

      assertEquals("lsa.fuegeEin(20)", true,  lsa.fuegeEin(20));

      assertEquals("lsa.istDrin (10)", true,  lsa.istDrin (10));
      assertEquals("lsa.istDrin (20)", true,  lsa.istDrin (20));
      assertEquals("lsa.istDrin (30)", true,  lsa.istDrin (30));
      assertEquals("lsa.istDrin (40)", false, lsa.istDrin (40));
      assertEquals("lsa.istDrin (50)", true,  lsa.istDrin (50));

      assertEquals("lsa.fuegeEin(40)", true,  lsa.fuegeEin(40));

      assertEquals("lsa.istDrin (10)", true,  lsa.istDrin (10));
      assertEquals("lsa.istDrin (20)", true,  lsa.istDrin (20));
      assertEquals("lsa.istDrin (30)", true,  lsa.istDrin (30));
      assertEquals("lsa.istDrin (40)", true,  lsa.istDrin (40));
      assertEquals("lsa.istDrin (50)", true,  lsa.istDrin (50));

      // Die folgenden Tests sind nur fuer begrenzte Sammlungen sinnvoll:
      if (!istBegrenzt) return;

      // lsa ist voll. Einfuegen sollte nicht mehr moeglich sein:
      assertEquals("lsa.fuegeEin(60)", false, lsa.fuegeEin(60));
      // Eine nicht eingefuegte Komponente loeschen sollte auch nicht gehn:
      assertEquals("lsa.loesche (60)", false, lsa.loesche (60));
      // Eine eingefuegte Komponente loeschen sollte gehen:
      assertEquals("lsa.loesche (50)", true,  lsa.loesche (50));
   }
   // ---------------------------------------------------------------------
   @Test
   public void test_04() {
      // Erkennt eine beschraenkte Sammlung ihre Beschraenktheit?
      
      // Die Sammlung lsa wird mit den Werten aus lra gefuellt.
      // Falls lsa begrenzt ist, ist lsa damit voll.
      fuelle(lsa, lra);
      
      // Fuer den Fall, dass lsb sortiert ist, wird versucht,
      // VOR allen schon vorhandenen long-Werten, ZWISCHEN je zwei Werten
      // und NACH allen vorhandenen Werten einen weiteren Wert einzufuegen.
      // Falls die Sammlung lsa begrenzt ist, sollte das Einfuegen an
      // keiner Stelle gelingen. Falls lsa nicht begrenzt ist, sollte das
      // ueberall gelingen.

      final boolean SOLL = !istBegrenzt;

      assertEquals("lsa.fuegeEin( 5)", SOLL,  lsa.fuegeEin( 5));
      assertEquals("lsa.istDrin ( 5)", SOLL,  lsa.istDrin ( 5));

      assertEquals("lsa.fuegeEin(15)", SOLL,  lsa.fuegeEin(15));
      assertEquals("lsa.istDrin (15)", SOLL,  lsa.istDrin (15));

      assertEquals("lsa.fuegeEin(25)", SOLL,  lsa.fuegeEin(25));
      assertEquals("lsa.istDrin (25)", SOLL,  lsa.istDrin (25));

      assertEquals("lsa.fuegeEin(35)", SOLL,  lsa.fuegeEin(35));
      assertEquals("lsa.istDrin (35)", SOLL,  lsa.istDrin (35));

      assertEquals("lsa.fuegeEin(45)", SOLL,  lsa.fuegeEin(45));
      assertEquals("lsa.istDrin (45)", SOLL,  lsa.istDrin (45));

      assertEquals("lsa.fuegeEin(55)", SOLL,  lsa.fuegeEin(55));
      assertEquals("lsa.istDrin (55)", SOLL,  lsa.istDrin (55));
   }
   // ---------------------------------------------------------------------
   @Test
   public void test_05() {
      // Erkennt eine beschraenkte Sammlung ihre Beschraenktheit?
      
      // Die Sammlung lsb wird mit den Werten aus lra gefuellt.
      // Falls lsb begrenzt ist, ist lsb damit etwa halb voll.
      fuelle(lsb, lra);
      
      // Fuer den Fall, dass lsb sortiert ist, wird versucht,
      // VOR allen schon vorhandenen long-Werten, ZWISCHEN je zwei Werten
      // und NACH allen vorhandenen Werten einen weiteren Wert einzufuegen.
      // Das sollte bei beschraenkten und unbeschraenkten Sammlungen
      // ueberall gelingen.

      final boolean SOLL = true;

      assertEquals("lsb.fuegeEin( 5)", SOLL,  lsb.fuegeEin( 5));
      assertEquals("lsb.istDrin ( 5)", SOLL,  lsb.istDrin ( 5));

      assertEquals("lsb.fuegeEin(15)", SOLL,  lsb.fuegeEin(15));
      assertEquals("lsb.istDrin (15)", SOLL,  lsb.istDrin (15));

      assertEquals("lsb.fuegeEin(25)", SOLL,  lsb.fuegeEin(25));
      assertEquals("lsb.istDrin (25)", SOLL,  lsb.istDrin (25));

      assertEquals("lsb.fuegeEin(35)", SOLL,  lsb.fuegeEin(35));
      assertEquals("lsb.istDrin (35)", SOLL,  lsb.istDrin (35));

      assertEquals("lsb.fuegeEin(45)", SOLL,  lsb.fuegeEin(45));
      assertEquals("lsb.istDrin (45)", SOLL,  lsb.istDrin (45));

      assertEquals("lsb.fuegeEin(55)", SOLL,  lsb.fuegeEin(55));
      assertEquals("lsb.istDrin (55)", SOLL,  lsb.istDrin (55));
   }
   // ---------------------------------------------------------------------
   @Test
   public void test_06() {
      // Systematische Tests mit istDrin und loesche.
      
      // Die Sammlung lsa wird mit den Werten aus lra gefuellt.
      // Falls lsa begrenzt ist, ist lsa damit voll.
      fuelle(lsa, lra);

      // Sind alle drin?
      assertEquals("lsa.istDrin (10)", true,  lsa.istDrin (10));
      assertEquals("lsa.istDrin (20)", true,  lsa.istDrin (20));
      assertEquals("lsa.istDrin (30)", true,  lsa.istDrin (30));
      assertEquals("lsa.istDrin (40)", true,  lsa.istDrin (40));
      assertEquals("lsa.istDrin (50)", true,  lsa.istDrin (50));

      assertEquals("lsa.loesche (30)", true,  lsa.loesche (30));

      // Sind alle bis auf 30 drin?
      assertEquals("lsa.istDrin (10)", true,  lsa.istDrin (10));
      assertEquals("lsa.istDrin (20)", true,  lsa.istDrin (20));
      assertEquals("lsa.istDrin (30)", false, lsa.istDrin (30));
      assertEquals("lsa.istDrin (40)", true,  lsa.istDrin (40));
      assertEquals("lsa.istDrin (50)", true,  lsa.istDrin (50));

      assertEquals("lsa.loesche (30)", false, lsa.loesche (30));
      assertEquals("lsa.loesche (10)", true,  lsa.loesche (10));

      // Sind alle bis auf 10 und 30 drin?
      assertEquals("lsa.istDrin (10)", false, lsa.istDrin (10));
      assertEquals("lsa.istDrin (20)", true,  lsa.istDrin (20));
      assertEquals("lsa.istDrin (30)", false, lsa.istDrin (30));
      assertEquals("lsa.istDrin (40)", true,  lsa.istDrin (40));
      assertEquals("lsa.istDrin (50)", true,  lsa.istDrin (50));

      assertEquals("lsa.loesche (30)", false, lsa.loesche (30));
      assertEquals("lsa.loesche (10)", false, lsa.loesche (10));
      assertEquals("lsa.loesche (50)", true,  lsa.loesche (50));

      // Sind alle bis auf 10, 30 und 50 drin?
      assertEquals("lsa.istDrin (10)", false, lsa.istDrin (10));
      assertEquals("lsa.istDrin (20)", true,  lsa.istDrin (20));
      assertEquals("lsa.istDrin (30)", false, lsa.istDrin (30));
      assertEquals("lsa.istDrin (40)", true,  lsa.istDrin (40));
      assertEquals("lsa.istDrin (50)", false, lsa.istDrin (50));
      
      assertEquals("lsa.loesche (30)", false, lsa.loesche (30));
      assertEquals("lsa.loesche (10)", false, lsa.loesche (10));
      assertEquals("lsa.loesche (50)", false, lsa.loesche (50));
      assertEquals("lsa.loesche (20)", true,  lsa.loesche (20));

      // Jetzt sollte nur noch 40 drin sein:
      assertEquals("lsa.istDrin (10)", false, lsa.istDrin (10));
      assertEquals("lsa.istDrin (20)", false, lsa.istDrin (20));
      assertEquals("lsa.istDrin (30)", false, lsa.istDrin (30));
      assertEquals("lsa.istDrin (40)", true,  lsa.istDrin (40));
      assertEquals("lsa.istDrin (50)", false, lsa.istDrin (50));

      assertEquals("lsa.loesche (30)", false, lsa.loesche (30));
      assertEquals("lsa.loesche (10)", false, lsa.loesche (10));
      assertEquals("lsa.loesche (50)", false, lsa.loesche (50));
      assertEquals("lsa.loesche (20)", false, lsa.loesche (20));
      assertEquals("lsa.loesche (40)", true,  lsa.loesche (40));

      // Jetzt sollte keine Zahl mehr drin sein:
      assertEquals("lsa.istDrin (10)", false, lsa.istDrin (10));
      assertEquals("lsa.istDrin (20)", false, lsa.istDrin (20));
      assertEquals("lsa.istDrin (30)", false, lsa.istDrin (30));
      assertEquals("lsa.istDrin (40)", false, lsa.istDrin (40));
      assertEquals("lsa.istDrin (50)", false, lsa.istDrin (50));

      // Wo nichts drin ist kann man nichts raus-loeschen:
      assertEquals("lsa.loesche (30)", false, lsa.loesche (30));
      assertEquals("lsa.loesche (10)", false, lsa.loesche (10));
      assertEquals("lsa.loesche (50)", false, lsa.loesche (50));
      assertEquals("lsa.loesche (20)", false, lsa.loesche (20));
      assertEquals("lsa.loesche (40)", false, lsa.loesche (40));
   }
   // ---------------------------------------------------------------------
   @Test
   public void test_07() {
      // Dieser Test ist vor allem fuer die Methode loesche bei binaeren
      // Baeumen gedacht (aber loesche-Methoden in anderen Klassen sollten
      // diesen Test auch bestehen).
      // In die Sammlung lsb werden Zahlen so eingefuegt, dass ein
      // interessanter binaerer Baum entsteht, bei dem einige Knoten
      // "schwierig zu loeschen sind" (weil sie 2 nicht-leere Unterbaeume
      // haben).

      // 7 Knoten in einer bestimmten Reihenfolge einfuegen:
      assertEquals("lsb.fuegeEin(50)", true,  lsb.fuegeEin(50));
      assertEquals("lsb.fuegeEin(30)", true,  lsb.fuegeEin(30));
      assertEquals("lsb.fuegeEin(70)", true,  lsb.fuegeEin(70));
      assertEquals("lsb.fuegeEin(20)", true,  lsb.fuegeEin(20));
      assertEquals("lsb.fuegeEin(40)", true,  lsb.fuegeEin(40));
      assertEquals("lsb.fuegeEin(60)", true,  lsb.fuegeEin(60));
      assertEquals("lsb.fuegeEin(80)", true,  lsb.fuegeEin(80));
      
      // Falls lsb ein binaerer Baum ist sollte der jetzt
      // folgende Struktur haben:
      //                          50
      //                         /  \
      //                        /    \
      //                      30      70
      //                     /  \    /  \
      //                   20   40  60   80

      // Sind alle eingefuegten auch wirklich drin?
      assertEquals("lsb.istDrin (50)", true,  lsb.istDrin (50));
      assertEquals("lsb.istDrin (30)", true,  lsb.istDrin (30));
      assertEquals("lsb.istDrin (70)", true,  lsb.istDrin (70));
      assertEquals("lsb.istDrin (20)", true,  lsb.istDrin (20));
      assertEquals("lsb.istDrin (40)", true,  lsb.istDrin (40));
      assertEquals("lsb.istDrin (60)", true,  lsb.istDrin (60));
      assertEquals("lsb.istDrin (80)", true,  lsb.istDrin (80));

      // Sind ein paar nicht-eingefuegte auch wirklich nicht-drin?
      // Von den restlichen ca. 18 Trillionen long-Werten hoffen
      // wir dann, dass sie auch wirklich nicht drin sind.
      assertEquals("lsb.istDrin (15)", false, lsb.istDrin (15));
      assertEquals("lsb.istDrin (55)", false, lsb.istDrin (55));
      assertEquals("lsb.istDrin (35)", false, lsb.istDrin (35));
      assertEquals("lsb.istDrin (75)", false, lsb.istDrin (75));
      assertEquals("lsb.istDrin (25)", false, lsb.istDrin (25));
      assertEquals("lsb.istDrin (45)", false, lsb.istDrin (45));
      assertEquals("lsb.istDrin (65)", false, lsb.istDrin (65));
      assertEquals("lsb.istDrin (85)", false, lsb.istDrin (85));

      // Loesche alle eingefuegten Knoten (in einem binaeren Baum
      // sind 30, 70 uns 50 "schwer zu loeschen):
      assertEquals("lsb.loesche (30)", true,  lsb.loesche (30));
      assertEquals("lsb.loesche (70)", true,  lsb.loesche (70));
      assertEquals("lsb.loesche (50)", true,  lsb.loesche (50));
      assertEquals("lsb.loesche (20)", true,  lsb.loesche (20));
      assertEquals("lsb.loesche (40)", true,  lsb.loesche (40));
      assertEquals("lsb.loesche (80)", true,  lsb.loesche (80));
      assertEquals("lsb.loesche (60)", true,  lsb.loesche (60));

      // Sind alle geloeschten auch wirklich nicht mehr drin?
      assertEquals("lsb.istDrin (50)", false, lsb.istDrin (50));
      assertEquals("lsb.istDrin (30)", false, lsb.istDrin (30));
      assertEquals("lsb.istDrin (70)", false, lsb.istDrin (70));
      assertEquals("lsb.istDrin (20)", false, lsb.istDrin (20));
      assertEquals("lsb.istDrin (40)", false, lsb.istDrin (40));
      assertEquals("lsb.istDrin (60)", false, lsb.istDrin (60));
      assertEquals("lsb.istDrin (80)", false, lsb.istDrin (80));
   }
   // ---------------------------------------------------------------------
   @Test
   public void test_08() {
      // Dieser Test ist vor allem fuer Hash-Tabellen gedacht, bei denen
      // aus den long-Werten Indizes einer Reihung (von Listen) berechnet
      // werden. Das koennte bei sehr grossen oder sehr kleinen long-Werten
      // schief gehen. Alle anderen (nicht-Hash-Tabellen-) Sammlungen
      // sollten diesen Test aber auch bestehen.     

      long n1 = +123_456_789_012L;
      long n2 = -123_456_789_012L;
      long n3 = +234_567_890_123L;
      long n4 = -234_567_890_123L;
      long n5 = +345_678_901_234L;
      long n6 = -345_678_901_234L;
      long n7 = Long.MAX_VALUE;
      long n8 = Long.MIN_VALUE;

      assertEquals("lsb.fuegeEin(n1)", true,  lsb.fuegeEin(n1));
      assertEquals("lsb.fuegeEin(n2)", true,  lsb.fuegeEin(n2));
      assertEquals("lsb.fuegeEin(n3)", true,  lsb.fuegeEin(n3));
      assertEquals("lsb.fuegeEin(n4)", true,  lsb.fuegeEin(n4));
      assertEquals("lsb.fuegeEin(n5)", true,  lsb.fuegeEin(n5));
      assertEquals("lsb.fuegeEin(n6)", true,  lsb.fuegeEin(n6));
      assertEquals("lsb.fuegeEin(n7)", true,  lsb.fuegeEin(n7));
      assertEquals("lsb.fuegeEin(n8)", true,  lsb.fuegeEin(n8));
      
      // Wenn man die großen Zahlen mal sehen moechte:
      if(TST1) printf("%s%n", lsb.toString());

      assertEquals("lsb.istDrin(n1)",  true,  lsb.istDrin(n1));
      assertEquals("lsb.istDrin(n2)",  true,  lsb.istDrin(n2));
      assertEquals("lsb.istDrin(n3)",  true,  lsb.istDrin(n3));
      assertEquals("lsb.istDrin(n4)",  true,  lsb.istDrin(n4));
      assertEquals("lsb.istDrin(n5)",  true,  lsb.istDrin(n5));
      assertEquals("lsb.istDrin(n6)",  true,  lsb.istDrin(n6));
      assertEquals("lsb.istDrin(n7)",  true,  lsb.istDrin(n7));
      assertEquals("lsb.istDrin(n8)",  true,  lsb.istDrin(n8));

      assertEquals("lsb.loesche(n1)",  true,  lsb.loesche(n1));
      assertEquals("lsb.loesche(n2)",  true,  lsb.loesche(n2));
      assertEquals("lsb.loesche(n3)",  true,  lsb.loesche(n3));
      assertEquals("lsb.loesche(n4)",  true,  lsb.loesche(n4));
      assertEquals("lsb.loesche(n5)",  true,  lsb.loesche(n5));
      assertEquals("lsb.loesche(n6)",  true,  lsb.loesche(n6));
      assertEquals("lsb.loesche(n7)",  true,  lsb.loesche(n7));
      assertEquals("lsb.loesche(n8)",  true,  lsb.loesche(n8));
   }
   // ---------------------------------------------------------------------
   @Test
   public void test_09() {
      // Dies ist eine Art "Massentest".
      // anzS mal werden jeweils anzL Zufallszahlen erzeugt und auf jede
      // Zufallszahl n werden die Methoden fuegeEin, istDrin und loesche
      // angewendet. All diese Methodenaufrufe sollten gelingen.
      // Die Methode rand.nextLong liefert erst nach Billionen von
      // Aufrufen einen Doppelgaenger (so oft wird die Methode hier nicht
      // aufgerufen).
   
      Random rand = new Random();
      int    anzS = 100; // Anzahl der seeds (deutsch: Keime)
      int    anzL = 100; // Anzahl der long-Werte (pro seed)
      
      for (int seedNr=1; seedNr<=anzS; seedNr++) {

         // Fuege anzL long-Werte in lsc ein:
         rand.setSeed(seedNr*50);
         for (int i=1; i<=anzL; i++) {
            long   komp = rand.nextLong();
            String msg  = String.format("lsc.fuegeEin(%,d)", komp);
            assertEquals(msg, true,  lsc.fuegeEin(komp));
         }
         
         // Pruefe, ob alle eingefuegten long-Werte in lsc drin sind:
         rand.setSeed(seedNr*50);
         for (int i=1; i<=anzL; i++) {
            long   komp = rand.nextLong();
            String msg  = String.format("lsc.istDrin(%,d)", komp);
            assertEquals(msg, true,  lsc.istDrin(komp));
         }
   
         // Versuche, alle in lsc eingefuegten long-Werte zu loeschen:
         rand.setSeed(seedNr*50);
         for (int i=1; i<=anzL; i++) {
            long   komp = rand.nextLong();
            String msg  = String.format("lsc.loesche(%,d)", komp);
            assertEquals(msg, true,  lsc.loesche(komp));
         }
      }
   }
   // ---------------------------------------------------------------------
   @Test
   public void test_10() {
      // Dies ist eine Art "Massentest" mit Doppelgaengern.
      // anzS mal werden jeweils anzL Zufallszahlen erzeugt und auf jede
      // Zufallszahl n werden die Methoden fuegeEin, istDrin und loesche
      // angewendet. All diese Methodenaufrufe sollten gelingen.
      
      // LongSpeicher51 kann diesen Test nicht bestehen:
      if (KLASSEN_NAME.endsWith("LongSpeicher51")) return;
   
      Random rand = new Random();
      int    anzS = 100; // Anzahl der seeds (deutsch: Keime)
      int    anzL =  10; // Anzahl der long-Werte (pro seed)
      
      for (int seedNr=1; seedNr<=anzS; seedNr++) {

         // Fuege anzL long-Werte zwischen 0 und 9 (einschliesslich)
         // in lsb ein. Darunter werden zahlreiche Doppelgaenger sein.
         rand.setSeed(seedNr*50);
         for (int i=1; i<=anzL; i++) {
            long   komp = rand.nextInt(10);
            String msg  = String.format("lsb.fuegeEin(%,d)", komp);
            assertEquals(msg, true,  lsb.fuegeEin(komp));
         }
         
         // Wenn man die Doppelgaenger sehen moechte:
         if(TST1) printf("%s%n", lsb.toString());

         // Pruefe, ob alle eingefuegten long-Werte in lsb drin sind:
         rand.setSeed(seedNr*50);
         for (int i=1; i<=anzL; i++) {
            long   komp = rand.nextInt(10);
            String msg  = String.format("lsb.istDrin(%,d)", komp);
            assertEquals(msg, true,  lsb.istDrin(komp));
         }
   
         // Versuche, alle in lsb eingefuegten long-Werte zu loeschen:
         rand.setSeed(seedNr*50);
         for (int i=1; i<=anzL; i++) {
            long   komp = rand.nextInt(10);
            String msg  = String.format("lsb.loesche(%,d)", komp);
            assertEquals(msg, true,  lsb.loesche(komp));
         }
      }
   }
   // ---------------------------------------------------------------------
   @Test
   public void test_11() {
      // Einfache Tests der toString-Methode (ohne und mit
      // Doppelgaengern):
      
      // Eine leere Sammlung;
      assertEquals("lsa.toString()", "[]",               lsa.toString());
      
      // Eine Sammlung mit einer Zahl darin:
      assertEquals("lsa.fuegeEin(123)", true,  lsa.fuegeEin(123));
      assertEquals("lsa.toString()", "[123]",            lsa.toString());
 
      // LongSpeicher51 kann die weiteren Tests nicht bestehen:
      if (KLASSEN_NAME.endsWith("LongSpeicher51")) return;      
      
      // Eine Sammlung mit zwei gleichen Zahlen darin:
      assertEquals("lsa.fuegeEin(123)", true,  lsa.fuegeEin(123));
      assertEquals("lsa.toString()", "[123, 123]",       lsa.toString());
      
      // Eine Sammlung mit drei gleichen Zahlen darin:
      assertEquals("lsa.fuegeEin(123)", true,  lsa.fuegeEin(123));
      assertEquals("lsa.toString()", "[123, 123, 123]",  lsa.toString());
   }
   // ---------------------------------------------------------------------
   @Test
   public void test_12() {
      // Ein schwierigerer Test der toString-Methode:
      // Wenn eine Sammlung lsa mehrere verschiedene Zahlen enthaelt,
      // haengt deren Reihenfolge von der Stuktur der Sammlung ab
      // (sortiert oder nicht sortiert, Reihung oder Liste etc.)
      //
      // Achtung: Hier wird nur mit kurzen long-Zahlen getestet
      // (1 bis 3 Ziffern), weil es bei laengeren Zahlen einen
      // Unterschied zwischen "schoen formatiert", z.B. 12.345.678.
      // und nicht formatiert, z.B. 12345678, gibt.
      
      assertEquals("lsa.fuegeEin(10)", true,  lsa.fuegeEin(10));
      assertEquals("lsa.fuegeEin(20)", true,  lsa.fuegeEin(20));
      assertEquals("lsa.fuegeEin(30)", true,  lsa.fuegeEin(30));
      
      // Die folgenden Zahlen muessen jetzt in lsa.toString() enthalten
      // sein, aber nicht notwendig in dieser Reihenfolge:
      String[] sr10 = {"10", "20", "30"};
      
      // Aus s11 alle in sr10 enthaltenen Zahlen entfernen:
      String s11 = lsa.toString();
      String s12 = remove(s11, sr10);
      // Das Ergebnis in s12 sollte so aussehen: "[, , ]"
      
      String t11 = "lsa.toString(): Expected something like ";
      String t12 = "[10, 20, 30],  but got " + s11;
      
      if (!"[, , ]".equals(s12)) fail(t11 + t12);
      
      assertEquals("lsa.fuegeEin(444)", true,  lsa.fuegeEin(444));
      assertEquals("lsa.fuegeEin(  0)", true,  lsa.fuegeEin(   0));      
 
      // Die folgenden Zahlen muessen jetzt in lsa.toString() enthalten
      // sein, aber nicht notwendig in dieser Reihenfolge:
      String[] sr20 = {"10", "20", "30", "444", "0"};
      
      // Aus s21 alle in sr20 enthaltenen Zahlen entfernen:
      String s21 = lsa.toString();
      String s22 = remove(s21, sr20);
      // Das Ergebnis in s22 sollte so aussehen: "[, , , ,]"

      String t21 = "lsa.toString(): Expected something like ";
      String t22 = "[10, 20, 30, 444, 0], but got " + s21;
      
      if (!"[, , , , ]".equals(s22)) fail(t21 + t22);
   }
   // ---------------------------------------------------------------------
   // formatter:off
   // Methoden mit kurzen Namen:
   static void printf(String n, Object... v) {System.out.printf(n, v);}
   static void pln(Object ob)                {System.out.println(ob) ;}
   static void pln()                         {System.out.println()   ;}
   static void p(Object ob)                  {System.out.print(ob)   ;}

} // class LongSpeicher_EndTest

