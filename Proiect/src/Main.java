import Entitati.*;
import Servicii.CatalogService;
import Utile.IdGenerator;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        CatalogService catalog = CatalogService.getInstance();

        Department info = new Department(1,"Informatica","FMI");
        Course cProg  = new Course("CS101","Programare",6, info);
        Course cAlgo  = new Course("CS102","Algoritmi",5,  info);
        catalog.adaugaMaterie(cProg);
        catalog.adaugaMaterie(cAlgo);

        Professor prof = new Professor(IdGenerator.generareId(),
                "Dr. Vasile Pop", "Lect. univ. dr.", info);
        catalog.adaugaProfesor(prof);

        CourseOffering offProg = new CourseOffering(IdGenerator.generareId(),
                cProg,2,2025);
        catalog.adaugaOferta(offProg);
        catalog.atribuireProfesorLaCurs(prof.getId(), offProg.getIdOferta());

        Student sAna = new Student(IdGenerator.generareId(),
                "Popescu Ana",
                LocalDate.of(2004,5,10),
                "Informatică");
        catalog.adaugaStudent(sAna);

        Enrollment eProg = catalog.inscriereStudentLaCurs(sAna.getId(), offProg.getIdOferta());
        catalog.inregistrareNota(eProg.getIdInscriere(), 9.5);
        catalog.inregistrareNota(eProg.getIdInscriere(), 8.0);

        System.out.println("Media Anei: " + catalog.medieStudent(sAna.getId()));
        System.out.println("Studenți la Programare: " + catalog.vizualizareStudentiCurs(offProg.getIdOferta()));
    }
}
