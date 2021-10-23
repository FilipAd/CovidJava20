package filewatcher;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import ekran.Ekran;

public class Watcher extends Thread
{
	private WatchService watcher;
	private Path direktorijum;
	private WatchKey key;
	private static FileHandler filehandler;


	static
	{
	try
	{
		filehandler=new FileHandler(System.getProperty("user.dir")+File.separatorChar+"logger"+File.separatorChar+"Watcher.log",true);
		SimpleFormatter format=new SimpleFormatter();
		filehandler.setFormatter(format);
	}
	catch(IOException ex)
	{
		log(ex);
	}
	}
public Watcher(String putanja)
{
	try
	{
		watcher=FileSystems.getDefault().newWatchService();
		direktorijum=Paths.get(putanja);
		direktorijum.register(watcher,ENTRY_CREATE,ENTRY_MODIFY);
	}
	catch (IOException ex) 
	{
		log(ex);
	}
}
@Override
public void run()
{
	while(true)
	{
		try
		{
		key=watcher.take();
		}
		catch (InterruptedException ex) 
		{
			log(ex);
			return;
		}
		for(WatchEvent<?> dogadjaj:key.pollEvents())
		{
			WatchEvent.Kind<?> kind=dogadjaj.kind();
			WatchEvent<Path> dog=(WatchEvent<Path>)dogadjaj;
			Path nazivfajla=dog.context();
			if((nazivfajla.toString().trim().endsWith("txt")) && (kind.equals(ENTRY_MODIFY)||kind.equals(ENTRY_CREATE)))
				{
				azuriraj(nazivfajla);
				}
			boolean ispravan=key.reset();
			if(!ispravan)
			{
				break;
			}
			
		}
	}
}
public void azuriraj(Path nazivfajla)
{
	File f=new File(System.getProperty("user.dir")+File.separatorChar+"src"+File.separatorChar+"podaci"+File.separatorChar+nazivfajla.toString());
	synchronized(f)
	{
		Ekran.prikazBrojaZO(f.toString());
	}
	
}
public static void log(Exception ex)
{
	Logger logger=Logger.getLogger(Watcher.class.getName());
	logger.addHandler(filehandler);
	logger.log(Level.WARNING,Watcher.class.getName(),ex);
}
}
