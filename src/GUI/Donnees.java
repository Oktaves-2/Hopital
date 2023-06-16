package GUI;

import javax.sql.rowset.CachedRowSet;

/**
 * Cette classe permet simplement l'instanciation comme objets des deux types de
 * cachedrowset qui sont associés au stage fmxl au cours de l'utilisation du programme, 
 * une liste ou un tableau pourrait tout aussi bien etre utilisés
 */
public class Donnees {
    private CachedRowSet rwLogin;
    private CachedRowSet rwPat;

    public Donnees() {

    }

    public void setrwLogin(CachedRowSet rwLogin) {
        this.rwLogin = rwLogin;
    }

    public void setrwPat(CachedRowSet rwPat) {
        this.rwPat = rwPat;
    }

    public CachedRowSet getrwLogin() {
        return this.rwLogin;
    }

    public CachedRowSet getrwPat() {
        return this.rwPat;
    }
}
