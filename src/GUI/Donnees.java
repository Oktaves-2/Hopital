package GUI;

import javax.sql.rowset.CachedRowSet;

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
