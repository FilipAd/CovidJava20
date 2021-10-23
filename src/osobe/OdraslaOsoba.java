package osobe;

import java.awt.Color;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Random;

import grad.Grad;
import objekti.Kuca;

public class OdraslaOsoba extends PunoljetnaOsoba implements RizicniUcesnik,Serializable
{
	
public OdraslaOsoba()
{
		
}

public OdraslaOsoba(String ime, Kuca kuca, Grad grad) 
{
	super();
	Random random = new Random();
	this.tjelesnaTemperatura=random.nextInt(6)+35;
	this.boja = Color.BLUE;
	this.ime=ime;
	this.kuca=kuca;
	this.godinaRodjenja=Calendar.getInstance().get(Calendar.YEAR)-(new Random().nextInt(47)+18);
	this.starost=Calendar.getInstance().get(Calendar.YEAR)-godinaRodjenja;
	this.grad = grad;
	int pom = (int) Math.round(Grad.getDimenzijaGrada()*0.25);
	this.sirinaKvadrata = this.duzinaKvadrata = 2 * pom + 1;
	int kucaX = kuca.getKoordinataX();
	int kucaY = kuca.getKoordinataY();
	if(kucaX < pom + 1) this.gornjiLijeviCosakX = 1;
	else {
		this.gornjiLijeviCosakX = kucaX - pom;
		if(Grad.getDimenzijaGrada() - kucaX <= pom + 1) this.gornjiLijeviCosakX -= kucaX + pom + 2 - Grad.getDimenzijaGrada();
	}
	if(kucaY < pom + 1) this.gornjiLijeviCosakY = 1;
	else {
		this.gornjiLijeviCosakY = kucaY - pom;
		if(Grad.getDimenzijaGrada()- kucaY <= pom + 1) this.gornjiLijeviCosakY -= kucaY + pom + 2 - Grad.getDimenzijaGrada();
	}
	do {
	this.pozicijaX = gornjiLijeviCosakX + random.nextInt(sirinaKvadrata);
	this.pozicijaY = gornjiLijeviCosakY + random.nextInt(duzinaKvadrata);
	System.out.println("odrasli se postavlja");
	}
	while(grad.daLiJeZauzeto(pozicijaX, pozicijaY) || grad.daLiJeInficirano(pozicijaX, pozicijaY));
	grad.zauzmiOblastUOdgovarajucojMatrici(this, pozicijaX, pozicijaY);
	postaviNovoOdrediste();
}

public OdraslaOsoba(String ime,String prezime,Pol pol,int idKuce,int id,Grad grad)
{
	super(ime,prezime,new Random().nextInt(47)+18,pol,idKuce,id,grad);
	int pom = (int) Math.round(Grad.getDimenzijaGrada()*0.25);
	this.sirinaKvadrata = this.duzinaKvadrata = 2 * pom + 1;
	int kucaX = kuca.getKoordinataX();
	int kucaY = kuca.getKoordinataY();
	if(kucaX < pom + 1) this.gornjiLijeviCosakX = 1;
	else {
		this.gornjiLijeviCosakX = kucaX - pom;
		if(Grad.getDimenzijaGrada()- kucaX < pom + 1) this.gornjiLijeviCosakX -= kucaX + pom + 2 - Grad.getDimenzijaGrada();
	}
	if(kucaY < pom + 1) this.gornjiLijeviCosakY = 1;
	else {
		this.gornjiLijeviCosakX = kucaY - pom;
		if(Grad.getDimenzijaGrada() - kucaY < pom + 1) this.gornjiLijeviCosakY -= kucaY + pom + 2 - Grad.getDimenzijaGrada();
	}
}

}
