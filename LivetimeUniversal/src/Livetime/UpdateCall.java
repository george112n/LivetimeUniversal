package Livetime;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author 14walkerg
 * @date 4 Jan 2021
 * @time 18:20:43
 */

public class UpdateCall extends BukkitRunnable
{
	private final JavaPlugin plugin;
	private Player player;
	
	public UpdateCall(JavaPlugin plugin, PlayerJoinEvent event)
	{
		this.plugin = plugin;
		this.player = event.getPlayer();
	}
	
	public UpdateCall(JavaPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	public UpdateCall(JavaPlugin plugin, Player player)
	{
		this.plugin = plugin;
		this.player = player;
	}

	@Override
	public void run()
	{
		final Collection<? extends Player> players = Bukkit.getOnlinePlayers();

		//Initiates wp
		WeatherPreference wp = new WeatherPreference();
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "LivetimeUniversal: Running livetime update");
		
		for (Player player: players)
		{
			boolean bSeasonalTime = false;
			
			//Gets UUID of player
			String UUID = player.getUniqueId().toString();
			
			//Sets UUID into wp
			wp.setUUID(UUID);

			//If there isn't a wp record for this UUID, create one
			if (!wp.exists())
			{
				wp.createNewPref();
			}
			//If this didn't work, skip them
			if (!wp.exists())
			{
				continue;
			}
			//Load preferences into wp
			wp.fetchFromUUID();
			
			//Updates time
			if (wp.getTime())
			{
				bSeasonalTime = wp.getSeasonal();
				if (bSeasonalTime)
				{
					LiveWeatherUtil LWU = new LiveWeatherUtil(player);
					LWU.call(bSeasonalTime);
				}
				else
				{
					Main.updateTime(player);
				}
			}
		}
	}
	
	public void initialRun()
	{
		Player player = this.player;
		
		boolean bSeasonalTime = false;
		
		long lCurrentTime;
		
		String szTime = "";
		
		//Gets UUID of player joined
		String UUID = player.getUniqueId().toString();
		
		//Initiates wp
		WeatherPreference wp = new WeatherPreference();
		
		//Sets UUID into wp
		wp.setUUID(UUID);
		
		//If there isn't a wp record for this UUID, create one
		if (!wp.exists())
		{
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "No weather preference record exists so creating one for "+player.getUniqueId().toString());
			boolean bCreated;
			bCreated = wp.createNewPref();
			if (bCreated)
				Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Created new live weather preference record for "+player.getUniqueId().toString());
			else
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Unable to create new live weather preference record for "+player.getUniqueId().toString());
		}
		
		//Gets weather preferences
		wp.fetchFromUUID();
		
		//Updates time
		if (wp.getTime())
		{
			//Gets whether seasonal or not
			bSeasonalTime = wp.getSeasonal();
			
			if (bSeasonalTime)
			{
				LiveWeatherUtil LWU = new LiveWeatherUtil(player);
				LWU.call(bSeasonalTime);
				lCurrentTime = LWU.lTime;
				szTime = String.format("%02d:%02d", lCurrentTime/100, lCurrentTime %100);
			}
			else
			{
				lCurrentTime = Main.updateTime(player);
				szTime = String.format("%02d:%02d", lCurrentTime/100, lCurrentTime %100);
				player.sendMessage(ChatColor.GOLD + "Time set to non seasonal "+ChatColor.RED +szTime);
			}
		}
	}
} //End Class

//Created by Bluecarpet in London