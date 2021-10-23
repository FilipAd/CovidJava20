package util;

import osobe.Dijete;
import osobe.OdraslaOsoba;
import osobe.Pol;
import osobe.Stanovnik;

public class Informacija
{

public static int ukupnoStanovnika=0;
public static int ukupanBrojZarazenih=0;
public static int ukupanBrojOporavljenih=0;

public static int ukupanBrojZarazenihTrenutno=0;

 
public static int brojZarazenihMuskaraca=0;
public static int brojZarazenihMuskaracaTrenutno=0;
public static int brojZarazenihZena=0;
public static int brojZarazenihZenaTrenutno=0;
public static int brojZarazeneDjece=0;
public static int brojZarazeneDjeceTrenutno=0;
public static int brojZarazenihStaraca=0;
public static int brojZarazenihStaracaTrenutno=0;
public static int brojZarazenihOdraslihOsoba=0;
public static int brojZarazenihOdraslihOsobaTrenutno=0;


 
public static int brojOporavljenihMuskaraca=0;
public static int brojOporavljenihZena=0;
public static int brojOporavljenihDjece=0;
public static int brojOporavljenihStaraca=0;
public static int brojOporavljenihOdraslihOsoba=0;

public static void azurirajInformacijePriUlasku(Stanovnik pacijent)
{
	if(pacijent.getPol()==Pol.MUSKI)
	{
		brojZarazenihMuskaracaTrenutno++;
		brojZarazenihMuskaraca++;
		if(pacijent instanceof Dijete)
		{
			brojZarazeneDjece++;
			brojZarazeneDjeceTrenutno++;
		}
		else if(pacijent instanceof OdraslaOsoba)
		{
			brojZarazenihOdraslihOsoba++;
			brojZarazenihOdraslihOsobaTrenutno++;
		}
		else
		{
			brojZarazenihStaraca++;
			brojZarazenihStaracaTrenutno++;
		}
	}
	else
	{
		brojZarazenihZena++;
		brojZarazenihZenaTrenutno++;
		if(pacijent instanceof Dijete)
		{
			brojZarazeneDjece++;
			brojZarazeneDjeceTrenutno++;
		}
		else if(pacijent instanceof OdraslaOsoba)
		{
			brojZarazenihOdraslihOsoba++;
			brojZarazenihOdraslihOsobaTrenutno++;
		}
		else
		{
			brojZarazenihStaraca++;
			brojZarazenihStaracaTrenutno++;
		}
	}
	ukupanBrojZarazenihTrenutno++;
	ukupanBrojZarazenih++;
}
public static void azurirajInformacijePriIzlasku(Stanovnik pacijent)
{
	if(pacijent.getPol()==Pol.MUSKI)
	{
		brojZarazenihMuskaracaTrenutno--;
		brojOporavljenihMuskaraca++;
		if(pacijent instanceof Dijete)
		{
			brojZarazeneDjeceTrenutno--;
			brojOporavljenihDjece++;
		}
		else if(pacijent instanceof OdraslaOsoba)
		{
			brojZarazenihOdraslihOsobaTrenutno--;
			brojOporavljenihOdraslihOsoba++;
		}
		else
		{
			brojZarazenihStaracaTrenutno--;
			brojOporavljenihStaraca++;
		}
	}
	else
	{
		brojZarazenihZenaTrenutno--;
		brojOporavljenihZena++;
		if(pacijent instanceof Dijete)
		{
			brojZarazeneDjeceTrenutno--;
			brojOporavljenihDjece++;
		}
		else if(pacijent instanceof OdraslaOsoba)
		{
			brojZarazenihOdraslihOsobaTrenutno--;
			brojOporavljenihOdraslihOsoba++;
		}
		else
		{
			brojZarazenihStaracaTrenutno--;
			brojOporavljenihStaraca++;
		}
	}
	ukupanBrojZarazenihTrenutno--;
	ukupanBrojOporavljenih++;
}
public  static double procenatZarazenihMuskarci()
{
 double rez=0;
 if(ukupanBrojZarazenih!=0)
 {
	 rez=(double)brojZarazenihMuskaraca/(double)ukupanBrojZarazenih;
 }
 return rez*100.0;
}

public  static double procenatZarazenihZene()
{
	 double rez=0;
	 if(ukupanBrojZarazenih!=0)
	 {
		 rez=(double)brojZarazenihZena/(double)ukupanBrojZarazenih;
	 }
	 return rez*100.0;
}

public  static double procenatZarazeneDjece()
{
	 double rez=0;
	 if(ukupanBrojZarazenih!=0)
	 {
		 rez=(double)brojZarazeneDjece/(double)ukupanBrojZarazenih;
	 }
	 return rez*100.0;
}

public static double procenatZarazenihStarih()
{
	 double rez=0;
	 if(ukupanBrojZarazenih!=0)
	 {
		 rez=(double)brojZarazenihStaraca/(double)ukupanBrojZarazenih;
	 }
	 return rez*100.0;
}

public  static double procenatZarazenihOdraslihOsoba()
{
	 double rez=0;
	 if(ukupanBrojZarazenih!=0)
	 {
		 rez=(double)brojZarazenihOdraslihOsoba/(double)ukupanBrojZarazenih;
	 }
	 return rez*100.0;
}
public  static double procenatOporavljenihMuskarci()
{
 double rez=0;
 if(ukupanBrojOporavljenih!=0)
 {
	 rez=(double)brojOporavljenihMuskaraca/(double)ukupanBrojOporavljenih;
 }
 return rez*100.0;
}

public  static double procenatOporavljenihZena()
{
	 double rez=0;
	 if(ukupanBrojOporavljenih!=0)
	 {
		 rez=(double)brojOporavljenihZena/(double)ukupanBrojOporavljenih;
	 }
	 return rez*100.0;
}

public  static double procenatOpooravljeneDjece()
{
	 double rez=0;
	 if(ukupanBrojOporavljenih!=0)
	 {
		 rez=(double)brojOporavljenihDjece/(double)ukupanBrojOporavljenih;
	 }
	 return rez*100.0;
}

public  static double procenatOporavljenihStaraca()
{
	 double rez=0;
	 if(ukupanBrojOporavljenih!=0)
	 {
		 rez=(double)brojOporavljenihStaraca/(double)ukupanBrojOporavljenih;
	 }
	 return rez*100.0;
}

public  static double procenatOporavljenihOdraslihOsoba()
{
	 double rez=0;
	 if(ukupanBrojOporavljenih!=0)
	 {
		 rez=(double)brojOporavljenihOdraslihOsoba/(double)ukupanBrojOporavljenih;
	 }
	 return rez*100.0;
}
public static int sumaTrenutnoZarazenih()
{
	 return brojZarazenihMuskaracaTrenutno+brojZarazenihZenaTrenutno;
}
public static int sumaUkupnoZarazenih()
{
	return ukupanBrojZarazenih;
}

public static int sumaUkupnoOporavljenih()
{
	return ukupanBrojOporavljenih;
}


}
