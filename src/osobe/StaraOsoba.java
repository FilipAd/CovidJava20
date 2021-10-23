package osobe;

import java.awt.Color;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Random;

import grad.Grad;
import objekti.Kuca;

public class StaraOsoba extends PunoljetnaOsoba implements Serializable {

public StaraOsoba() 
	{
		this.boja = Color.GRAY;
		this.odredisteX = 28;
		this.odredisteY = 5;
	}

public StaraOsoba(int x, int y, int dx, int dy,String ime,Kuca kuca) 
{
	this.tjelesnaTemperatura=new Random().nextInt(6)+35;
	this.boja = Color.GRAY;
	this.pozicijaX = x;
	this.pozicijaY = y;
	this.odredisteX = dx;
	this.odredisteY = dy;
	this.ime=ime;
	this.kuca=kuca;
	this.sirinaKvadrata = 7;
	this.duzinaKvadrata = 7;
	int kucaX = kuca.getKoordinataX();
	int kucaY = kuca.getKoordinataY();
	if(kucaX < 4) this.gornjiLijeviCosakX = 1;
	else {
		this.gornjiLijeviCosakX = kucaX - 3;
		if(Grad.getDimenzijaGrada() - kucaX < 4) this.gornjiLijeviCosakX -= kucaX + 5 - Grad.getDimenzijaGrada();
	}
	if(kucaY < 4) this.gornjiLijeviCosakY = 1;
	else {
		this.gornjiLijeviCosakX = kucaY - 3;
		if(Grad.getDimenzijaGrada() - kucaY < 4) this.gornjiLijeviCosakY -= kucaY + 5 - Grad.getDimenzijaGrada();
	}
	postaviNovoOdrediste();
}

public StaraOsoba(String ime, String prezime, Pol pol, int idKuce, int id,Grad grad) 
	{
		super(ime, prezime, Calendar.getInstance().get(Calendar.YEAR)-new Random().nextInt(35)+65, pol, idKuce, id,grad);
		this.boja = Color.GRAY;
	}


	public StaraOsoba(String ime, Kuca kuca, Grad grad) {
		Random random = new Random();
		this.tjelesnaTemperatura = random.nextInt(6) + 35;
		this.boja = Color.GRAY;
		this.ime = ime;
		this.kuca = kuca;
		this.grad = grad;
		this.godinaRodjenja=Calendar.getInstance().get(Calendar.YEAR)-(new Random().nextInt(30)+65);
		this.starost=Calendar.getInstance().get(Calendar.YEAR)-godinaRodjenja;
		this.sirinaKvadrata = 7;
		this.duzinaKvadrata = 7;
		int kucaX = kuca.getKoordinataX();
		int kucaY = kuca.getKoordinataY();
		if (kucaX < 4)
			this.gornjiLijeviCosakX = 1;
		else {
			this.gornjiLijeviCosakX = kucaX - 3;
			if (Grad.getDimenzijaGrada() - kucaX <= 4)
				this.gornjiLijeviCosakX -= kucaX + 5 - Grad.getDimenzijaGrada();
		}
		if (kucaY < 4)
			this.gornjiLijeviCosakY = 1;
		else {
			this.gornjiLijeviCosakY = kucaY - 3;
			if (Grad.getDimenzijaGrada() - kucaY <= 4)
				this.gornjiLijeviCosakY -= kucaY + 5 - Grad.getDimenzijaGrada();
		}
		do {
			this.pozicijaX = gornjiLijeviCosakX + random.nextInt(sirinaKvadrata);
			this.pozicijaY = gornjiLijeviCosakY + random.nextInt(duzinaKvadrata);
			System.out.println("starac se postavlja");
			}
			while(grad.daLiJeZauzeto(pozicijaX, pozicijaY) || grad.daLiJeInficirano(pozicijaX, pozicijaY));
		grad.zauzmiOblastUOdgovarajucojMatrici(this, pozicijaX, pozicijaY);
		postaviNovoOdrediste();
	}
}
