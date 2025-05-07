import Entitati.*;
import Servicii.CatalogService;
import Utile.IdGenerator;

import java.time.LocalDate;
import java.util.*;


public class Main {

    /* ============ colecții locale cu ID-uri valide ============ */
    private static final Set<Integer> studentIds   = new HashSet<>();
    private static final Set<Integer> professorIds = new HashSet<>();
    private static final Set<Integer> offeringIds  = new HashSet<>();
    private static final Set<Integer> enrollmentIds= new HashSet<>();

    private static final CatalogService catalog = CatalogService.getInstance();
    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        populateDemo();

        while (true) {
            afiseazaMeniu();
            int opt = citesteInt("Alegeți opțiunea: ");

            switch (opt) {
                case 0  -> { System.out.println("La revedere!"); return; }
                case 1  -> actionInscriereStudent();
                case 2  -> actionRetragereStudent();
                case 3  -> actionListareCursuriSemestru();
                case 4  -> actionAtribuireProfesor();
                case 5  -> actionAdaugareMaterie();
                case 6  -> actionModificareMaterie();
                case 7  -> actionVizualizareStudenti();
                case 8  -> actionInregistrareNota();
                case 9  -> actionNoteSiMedieStudent();
                case 10 -> actionCautareCursuriDept();
                default -> System.out.println("Opțiune invalidă!");
            }
            System.out.println("\n──────────────────────────────────────\n");
        }
    }

    /* ===================== MENIU  ============================= */
    private static void afiseazaMeniu() {
        System.out.println("""
                ========= Catalog Universitar =========
                0. Ieșire
                1. Înscriere student la curs
                2. Retragere student din curs
                3. Listare cursuri disponibile (semestru + an)
                4. Atribuire profesor la curs
                5. Adăugare materie în catalog
                6. Modificare informații materie
                7. Vizualizare studenți înscriși la un curs
                8. Înregistrare notă pentru student
                9. Listare note și medie student
               10. Căutare cursuri după departament
               =========================================""");
    }

    /* ================== ACȚIUNI  ============================== */

    // ---- 1
    private static void actionInscriereStudent() {
        int idStud = citesteIdValid("Id student: ", studentIds, "Nu există acest student! Introduceți alt ID: ");
        int idOf   = citesteIdValid("Id ofertă curs: ", offeringIds, "Nu există oferta! Introduceți alt ID: ");

        Enrollment e = catalog.inscriereStudentLaCurs(idStud, idOf);
        enrollmentIds.add(e.getIdInscriere());            // memorăm noul ID
        System.out.println("Înscriere realizată: id=" + e.getIdInscriere());
    }

    // ---- 2
    private static void actionRetragereStudent() {
        int idIns = citesteIdValid("Id înscriere: ", enrollmentIds, "Nu există înscrierea! Introduceți alt ID: ");
        catalog.retragereStudentDinCurs(idIns);
        enrollmentIds.remove(idIns);                       // scoatem din set
        System.out.println("Înscriere anulată.");
    }

    // ---- 3
    private static void actionListareCursuriSemestru() {
        int sem = citesteInt("Semestru (1/2): ");
        int an  = citesteInt("An universitar (ex. 2025): ");
        List<CourseOffering> list = catalog.listareCursuri(sem, an);
        list.forEach(System.out::println);
    }

    // ---- 4
    private static void actionAtribuireProfesor() {
        int idProf = citesteIdValid("Id profesor: ", professorIds, "Nu există profesorul! Introduceți alt ID: ");
        int idOf   = citesteIdValid("Id ofertă curs: ", offeringIds, "Nu există oferta! Introduceți alt ID: ");
        catalog.atribuireProfesorLaCurs(idProf, idOf);
        System.out.println("Profesor atribuit.");
    }

    // ---- 5
    private static void actionAdaugareMaterie() {
        String cod  = citesteStr("Cod materie: ");
        String nume = citesteStr("Denumire: ");
        int credite = citesteInt("Credite: ");
        String dept = citesteStr("Departament: ");
        Department d = new Department(IdGenerator.generareId(), dept, "FMI");
        Course c = new Course(cod, nume, credite, d);
        catalog.adaugaMaterie(c);
        System.out.println("Materie adăugată.");
    }

    // ---- 6
    private static void actionModificareMaterie() {
        String cod = citesteStr("Cod materie: ");
        int credite = citesteInt("Număr credite nou: ");
        catalog.modificaMaterie(cod, credite);
        System.out.println("Actualizat.");
    }

    // ---- 7
    private static void actionVizualizareStudenti() {
        int idOf = citesteIdValid("Id ofertă curs: ", offeringIds, "Nu există oferta! Introduceți alt ID: ");
        catalog.vizualizareStudentiCurs(idOf).forEach(System.out::println);
    }

    // ---- 8
    private static void actionInregistrareNota() {
        int idIns = citesteIdValid("Id înscriere: ", enrollmentIds, "Nu există înscrierea! Introduceți alt ID: ");
        double nota = citesteDouble("Valoare notă: ");
        catalog.inregistrareNota(idIns, nota);
        System.out.println("Notă adăugată.");
    }

    // ---- 9
    private static void actionNoteSiMedieStudent() {
        int idStud = citesteIdValid("Id student: ", studentIds, "Nu există acest student! Introduceți alt ID: ");
        catalog.noteStudent(idStud).forEach(g ->
                System.out.println("Notă: " + g.getValoareNota()));
        System.out.printf("Media: %.2f%n", catalog.medieStudent(idStud));
    }

    // ---- 10
    private static void actionCautareCursuriDept() {
        String dept = citesteStr("Nume departament: ");
        catalog.cautaCursuriDepartament(dept).forEach(System.out::println);
    }

    /* =========== HELPER PENTRU VALIDAREA ID-ului ============= */
    private static int citesteIdValid(String msg, Set<Integer> set,
                                      String msgNuExista) {
        int id = citesteInt(msg);
        while (!set.contains(id)) {
            System.out.print(msgNuExista);
            id = citesteInt("");          // citește din nou
        }
        return id;
    }

    /* ============ ALTE HELPER I/O ============================ */
    private static int citesteInt(String msg) {
        System.out.print(msg);
        while (!in.hasNextInt()) { in.next(); System.out.print("Introduceți un număr întreg: "); }
        return in.nextInt();
    }
    private static double citesteDouble(String msg) {
        System.out.print(msg);
        while (!in.hasNextDouble()) { in.next(); System.out.print("Introduceți un număr: "); }
        return in.nextDouble();
    }
    private static String citesteStr(String msg) {
        System.out.print(msg);
        in.nextLine();
        return in.nextLine();
    }

    /* ============ DATĂ DEMO + populare seturi ================= */
    private static void populateDemo() {
        Department info = new Department(1,"Informatica","FMI");

        Course cProg = new Course("CS101","Programare",6, info);
        Course cAlgo = new Course("CS102","Algoritmi",5, info);
        catalog.adaugaMaterie(cProg);
        catalog.adaugaMaterie(cAlgo);

        Professor prof = new Professor(IdGenerator.generareId(),
                "Dr. Vasile Pop","Lect. univ. dr.",info);
        catalog.adaugaProfesor(prof);
        professorIds.add(prof.getId());

        CourseOffering offProg = new CourseOffering(IdGenerator.generareId(),
                cProg,2,2025);
        catalog.adaugaOferta(offProg);
        offeringIds.add(offProg.getIdOferta());

        Student sAna = new Student(IdGenerator.generareId(),
                "Popescu Ana", LocalDate.of(2004,5,10),
                "Informatică");
        catalog.adaugaStudent(sAna);
        studentIds.add(sAna.getId());

        Enrollment e = catalog.inscriereStudentLaCurs(sAna.getId(), offProg.getIdOferta());
        enrollmentIds.add(e.getIdInscriere());
        catalog.inregistrareNota(e.getIdInscriere(), 9.5);
        catalog.inregistrareNota(e.getIdInscriere(), 8.0);
    }
}
