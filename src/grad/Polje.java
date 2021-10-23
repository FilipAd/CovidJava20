package grad;

import java.io.Serializable;

import objekti.Kuca;
import osobe.BezopasniUcesnik;
import osobe.PunoljetnaOsoba;
import osobe.RizicniUcesnik;
import osobe.Stanovnik;
import osobe.Ucesnik;

public class Polje implements Serializable
{
private volatile boolean dijeteIAmbulantnoVozilo;
private boolean punoljetnaOsoba;
private boolean punkt;
private boolean ambulanta;

public Polje()
{
	this.dijeteIAmbulantnoVozilo=false;
	this.punoljetnaOsoba=false;
	this.punkt=false;
	this.ambulanta=false;
	
}
public Polje(boolean dijeteAmbulantnoVozilo,boolean punoljetnaOsoba)
{
	this.dijeteIAmbulantnoVozilo=dijeteAmbulantnoVozilo;
	this.punoljetnaOsoba=punoljetnaOsoba;
}

public synchronized void zauzmiZaRizicnu()
{
	this.punoljetnaOsoba=true;
}

public synchronized void zauzmiZaPunkt()
{
	this.punkt=true;
}
public synchronized void zauzmiZaAmbulantu()
{
	this.ambulanta=true;
}

public synchronized void zauzmiBezopasnoj()
{
	this.dijeteIAmbulantnoVozilo=true;
}
 public synchronized void oslobodiZaPunoljetnu()
 {
	 this.punoljetnaOsoba=false;
 }
public synchronized void oslobodiZaBezopasne()
{
	this.dijeteIAmbulantnoVozilo=false;
}
public synchronized boolean provjeriZaPunoljetne()
{
	return this.punoljetnaOsoba;
}

public synchronized boolean provjeriZaBezopasne()
{
	return this.dijeteIAmbulantnoVozilo;
}

public synchronized boolean provjeriZaPunkt()
{
	return this.punkt;
}
public synchronized boolean provjeriZaAmbulantu()
{
	return this.ambulanta;
}
}
