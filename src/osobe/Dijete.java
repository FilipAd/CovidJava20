package osobe;

import java.awt.Color;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Random;

import grad.Grad;
import objekti.Kuca;
import util.Util;

public class Dijete extends Stanovnik implements BezopasniUcesnik,Serializable
{

public Dijete()
{
	
}
public Dijete(int x, int y, int dx, int dy,String ime, Grad grad) 
{
	this.tjelesnaTemperatura=new Random().nextInt(18)+1;
	this.boja = Color.GREEN;
	this.pozicijaX = x;
	this.pozicijaY = y;
	this.odredisteX = dx;
	this.odredisteY = dy;
	this.ime=ime;
	this.grad = grad;
	this.gornjiLijeviCosakX = 1;
	this.gornjiLijeviCosakY = 1;
	this.duzinaKvadrata = grad.getDimenzijaGrada()- 2;
	this.sirinaKvadrata = grad.getDimenzijaGrada() - 2;
}
public Dijete(String ime,String prezime,Pol pol,int idKuce,int id,Grad grad)
{
	super(ime,prezime,Calendar.getInstance().get(Calendar.YEAR)-new Random().nextInt(19),pol,idKuce,id,grad);
	this.boja=Color.PINK;
}

public Dijete(String ime, Kuca kuca, Grad grad) {
	Random random = new Random();
	this.tjelesnaTemperatura=random.nextInt(6)+35;
	this.boja = Color.GREEN;
	this.ime=ime;
	this.kuca = kuca;
	this.grad = grad;
	this.gornjiLijeviCosakX = 1;
	this.gornjiLijeviCosakY = 1;
	this.duzinaKvadrata = Grad.getDimenzijaGrada() - 2;
	this.sirinaKvadrata = Grad.getDimenzijaGrada() - 2;
	this.godinaRodjenja=Calendar.getInstance().get(Calendar.YEAR)-(new Random().nextInt(17)+1);
	this.starost=Calendar.getInstance().get(Calendar.YEAR)-godinaRodjenja;
	do {
		this.pozicijaX = gornjiLijeviCosakX + random.nextInt(sirinaKvadrata);
		this.pozicijaY = gornjiLijeviCosakY + random.nextInt(duzinaKvadrata);
		System.out.println("dijete se postavlja");
		}
		while(grad.daLiJeZauzeto(pozicijaX, pozicijaY));
		grad.zauzmiOblastUOdgovarajucojMatrici(this, pozicijaX, pozicijaY);
	postaviNovoOdrediste();
}
public void zauzmiOblast()
{
	
}

public void oslobodiOblast(int a,int b,int c)
{
	
}

@Override
protected boolean skreni()
{
	int []pomKord=Util.pomjeri(pozicijaX,pozicijaY,smjer);
	if (Util.daLiJeLegalno(pomKord[0], pomKord[1])) {
	synchronized(grad.mapa[pomKord[0]][pomKord[1]])
	{
	boolean[] fizInfo = { true, true, true };
	fizInfo = grad.pribaviInfoZaKretanjeFizickih(pozicijaX, pozicijaY, smjer, this.kuca.getKoordinataX(), this.kuca.getKoordinataY());
	//System.out.println(smjer + "{" + fizInfo[0] + fizInfo[1] + fizInfo[2]);
		if (fizInfo[1]) {
			if (!fizInfo[2])
			{
				smjer = (smjer + 1) % 8;
			}
			else if (!fizInfo[0])
			{
				smjer = (smjer + 7) % 8;
			}
			else {
				smjer = (smjer + 2) % 8;
				return false;
				}
				
		}
		zauzmiUnaprijed();
		idi();
		
		return true;
	}
	} else {
		smjer = (smjer + 1) % 8;
		return false;
	}
}


public void zauzmiUnaprijed()
{
	int pomK[]=Util.pomjeri(pozicijaX, pozicijaY, smjer);
	grad.zauzmiOblastUOdgovarajucojMatrici(this,pomK[0],pomK[1]);
	
}
/*@Override
public void postaviNovoOdrediste() 
{
	
}*/
}

