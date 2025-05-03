package Entitati;

import java.util.ArrayList;
import java.util.List;

/** Oferta unui curs într-un an/semestru anume */
public class CourseOffering {
    private final int  idOferta;
    private final Course curs;
    private final int semestru;
    private final int an;
    private Professor profesor;
    private final List<Classroom> sali = new ArrayList<>();

    public CourseOffering(int idOferta, Course curs, int semestru, int an) {
        this.idOferta = idOferta;
        this.curs     = curs;
        this.semestru = semestru;
        this.an       = an;
    }

    public int       getIdOferta() { return idOferta; }
    public Course    getCurs()     { return curs;     }
    public int       getSemestru() { return semestru; }
    public int       getAn()       { return an;       }
    public Professor getProfesor() { return profesor; }

    public void setProfesor(Professor p) { this.profesor = p; }
    public void adaugaSala(Classroom c)  { sali.add(c); }

    @Override public String toString() {
        return "Oferta " + idOferta + " – " + curs.getDenumire() +
                " (S" + semestru + ", " + an + ")";
    }
}
