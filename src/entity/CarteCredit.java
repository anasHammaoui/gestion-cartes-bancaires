package entity;

public final class CarteCredit extends Carte{
    private double plafond_mensuel;
    private float taux_interet;

    public float getTaux_interet() {
        return taux_interet;
    }

    public void setTaux_interet(float taux_interet) {
        this.taux_interet = taux_interet;
    }

    public double getPlafond_mensuel() {
        return plafond_mensuel;
    }

    public void setPlafond_mensuel(double plafond_mensuel) {
        this.plafond_mensuel = plafond_mensuel;
    }
}
