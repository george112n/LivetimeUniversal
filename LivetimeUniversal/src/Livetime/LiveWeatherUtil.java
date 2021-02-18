package Livetime;
import org.bukkit.entity.Player;

import OpenWeatherMap.Current;

import java.time.LocalDateTime;

import org.bukkit.Location;

/**
 * @author 14walkerg
 * @date 3 Jan 2021
 * @time 12:11:00
 */

//resetPlayerWeather returns it back to server

public class LiveWeatherUtil
{
	String apiKey;
	static String szURL;
	Player p;
	Location pLocation;
	double[] coords;
	public String szLocation;
	public long lTime;
	public String szWeather;
	
	public LiveWeatherUtil()
	{
		
	}
	public LiveWeatherUtil(Player player)
	{
		reset();
		this.p = player;
		this.coords[0] = 51.50768624686004;
		this.coords[1] = -0.12703404821640815;
		this.apiKey = Main.getInstance().config.getString("apiKey");
	}
	
	public LiveWeatherUtil(Player player, Location location)
	{
		reset();
		this.p = player;
		this.pLocation = location;
		this.apiKey = Main.getInstance().config.getString("apiKey");
	}
	
	private void reset()
	{
		szURL = "";
		pLocation = null;
		coords = new double[2];
		szLocation = "";
		lTime = -1;
		szWeather = "";
	}
	
	public void call(boolean seasonalTime)
	{
		LocalDateTime dSunrise;
		LocalDateTime dSunset;
		long currentTime;
		
		Current current;
		
		//Compiles the request URL
		compileSourceURL();
		
		//Gets the weather
		current = GetWeather.entry(szURL);
				
		if (seasonalTime)
		{
			dSunrise = current.getCity().getSunrise();
			dSunset = current.getCity().getSunset();
			
			currentTime = Main.updateTimeSeasonal(p, dSunrise, dSunset);
			lTime = currentTime;
		}
	}
		
	public void compileSourceURL()
	{
		szURL = "http://api.openweathermap.org/data/2.5/weather?lat=";
		szURL = szURL + coords[1];
		szURL = szURL + "&lon=";
		szURL = szURL + coords[0];
		szURL = szURL + "&appid=" + apiKey;//apiKey;
		szURL = szURL + "&mode=xml";
	}
}
//End Class

//Created by Bluecarpet in London