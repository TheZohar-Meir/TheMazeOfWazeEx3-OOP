package gameClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * # KML
#### * This class create a new KML file for our game.
#### * After creating this file we save it in our project KML_Auto_Scenario folder double clicking it will lunch GoogleEarth and display the movement of our robots and graphs for each level you played. 
#### * © authors: Zohar and Lidor.
 */
public class KML
{
	private static int Current_time;
	static File gamePath;
	
	/**
	 * This class create a new KML file for our game.
	 * @param recivedName - receive the file name.
	 * @param Senerio_number - the number of the scenario.
	 * @return a string that represent the file name.
	 * */
	public static String CreatNewKMLFile(String recivedName, int Senerio_number)
	{
		if(Senerio_number % 2 == 1) {
			Current_time = 60000;
		}
		else {
			Current_time = 30000;
		}
		File file = new File(recivedName);
		if(!file.exists())
		{
			System.out.println("file Name: "+recivedName);
			String output = "";
			try 
			{
				file.createNewFile();
				BufferedWriter br = new BufferedWriter(new FileWriter(file));
				
				//First 3 lines
				output += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"; 
				output += "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\r\n";
				output += "<Document>\r\n";
				
				//Fruit1
				output += "<Style id=\"Fruit1\">\r\n";
				output += "<Icon>\r\n";
				output += "<href>https://i.imgur.com/xh6wLVK.png</href>\r\n";
				output += "</Icon>\r\n";	
				output += "</Style>\r\n";
				
				//Fruit2
				output += "<Style id=\"Fruit2\">\r\n";
				output += "<Icon>\r\n";
				output += "<href>https://i.imgur.com/9fW4PkQ.png</href>\r\n";
				output += "</Icon>\r\n";	
				output += "</Style>\r\n";
				
				//Robot
				output += "<Style id=\"Robot\">\r\n";
				output += "<Icon>\r\n";
				output += "<href>https://i.imgur.com/0QBAYfL.png</href>\r\n";
				output += "</Icon>\r\n";	
				output += "</Style>\r\n\r\n";
				
				br.write(output);
				br.close();
			} 
			catch (IOException e) {e.printStackTrace();}
			return recivedName;
		}
		else
		{
			int i = recivedName.indexOf(".");
			System.out.println("You already have a file with the same name.");
			return CreatNewKMLFile(recivedName.substring(0, i) + i + ".kml",Senerio_number);
		}
	}
	
	
	/**
	 * This function writes to an existing KML file.
	 * @param recivedName - receive the file name.
	 * @param x - Object x value.
	 * @param y - Object y value.
	 * @param Object_type - the written object type: in order to give specific icon to every type of object.
	 * @param timeToEnd - helps to write the correct timeSpan for this object.
	 * @throws FileNotFoundException - if "recivedName" does not exists
	 * */
	public static void Write_Data(String recivedName, double x, double y, String Object_type, long TimeToEnd)throws FileNotFoundException
	{
		long  time = (Current_time - TimeToEnd)/1000;
		String output = "";
		File f = new File(recivedName);
		if(!f.exists())
			throw new FileNotFoundException("Error this file does not exist...");
		try 
		{
			BufferedWriter br = new BufferedWriter(new FileWriter(recivedName,true));
			
			output += "<Placemark>\r\n";
			output += "<name>" + Object_type + "</name>\r\n";
			output += "<TimeSpan>\r\n<begin>" + time + "Z</begin>\r\n<end>" + (time+0.1) + "</end>\r\n</TimeSpan>\r\n";
			switch(Object_type)
			{
			case "Fruit1":
			{
				output += "<styleUrl>#Fruit1</styleUrl>\r\n";
				break;
			}
			case "Fruit2":
			{
				output += "<styleUrl>#Fruit2</styleUrl>\r\n";
				break;
			}
			case "Robot":
			{
				output += "<styleUrl>#Robot</styleUrl>\r\n";
				break;
			}
			}
			output += "<TimeStamp>\r\n<when>"+LocalDateTime.now()+"Z</when>\r\n</TimeStamp>\r\n";
			output += "<Point>\r\n";
			output += "<coordinates>"+ x + ", " + y + ", 0</coordinates>\r\n</Point>\r\n";
			output += "</Placemark>\r\n\r\n";
			br.write(output);
			br.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	
	
	/**
	 * This function close the file "recivedName" as KML file.
	 * @param recivedName - To know which KML file to close.
	 * @throws IOException if file "recivedName" does not start with the proper KML file deceleration.
	 * */
	public static String close_KML(String recivedName) throws IOException 
	{
		System.out.println("close KML file");
		File f = new File(recivedName);
		if(!f.exists())throw new FileNotFoundException("error this file does not exist!");
		BufferedReader br = new BufferedReader(new FileReader(f));
		String s = br.readLine();
		
		if(!s.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"))
		{
			br.close();
			throw new IOException("wrong file format ");
		}
		
		s = br.readLine();
		if(!s.equals("<kml xmlns=\"http://www.opengis.net/kml/2.2\">"))
		{
			br.close();
			throw new IOException("wrong file format ");
		}
		
		s = br.readLine();
		if(!s.equals("<Document>"))
		{
			br.close();
			throw new IOException("wrong file format ");
		}
		
		br.close();
		BufferedWriter bw = new BufferedWriter(new FileWriter(recivedName,true));
		String output = "";
		output += "</KML>\r\n";
		//		out += "</Folder>\r\n";
		output += "</kml>";
		bw.write(output);
		bw.close();
		f.setWritable(false);
		return f.toString();
	}
	
}