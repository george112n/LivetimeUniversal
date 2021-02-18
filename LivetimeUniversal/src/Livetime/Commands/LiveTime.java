package Livetime.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Livetime.WeatherPreference;
import Livetime.LiveWeatherUtil;
import Livetime.Main;

/**
 * @author 14walkerg
 * @date 3 Jan 2021
 * @time 14:08:09
 */

public class LiveTime implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		//Check is command sender is a player
		if (!(sender instanceof Player))
		{
			sender.sendMessage("&cYou cannot add a player to a region!");
			return true;
		}
		
		//Convert sender to player
		Player p = (Player) sender;
		
		if (args.length > 1)
		{
			p.sendMessage(ChatColor.RED + "/livetime [seasonal]");
			return true;
		}
		
		//Get player's preferences
		
		//Initiate player preferences
		Livetime.WeatherPreference wp = new WeatherPreference();

		//Sets the UUID into the wp class
		wp.setUUID(p.getUniqueId().toString());
		
		//Gets weather preferences
		wp.fetchFromUUID();
		
		boolean newState;
		long lCurrentTime;
		
		
		if (args.length > 0)
		{
			if (args[0].equalsIgnoreCase("Seasonal"))
			{
				newState = wp.toogleSeasonal();
				if (newState)
				{
					p.sendMessage(ChatColor.GOLD + "Seasonal time enabled");
					LiveWeatherUtil LWU = new LiveWeatherUtil(p);
					LWU.call(true);
					lCurrentTime = LWU.lTime;
					String szTime = String.format("%02d:%02d", lCurrentTime/100, lCurrentTime %100);
					p.sendMessage(ChatColor.GOLD + "Time updated for "+ChatColor.RED +szTime);
				}
				else
				{
					p.sendMessage(ChatColor.GOLD + "Seasonal time disabled");
					lCurrentTime = Main.updateTime(p);
					String szTime = String.format("%02d:%02d", lCurrentTime/100, lCurrentTime %100);
					p.sendMessage(ChatColor.GOLD + "Time updated for "+ChatColor.RED +szTime);
				}
				return true;
			}
			else
			{
				p.sendMessage(ChatColor.RED + "/livetime [seasonal]");
				return true;
			}
		}
		else
		{
			newState = wp.toogleTime();
			if (newState)
			{
				p.sendMessage(ChatColor.GOLD + "Live time enabled");
				if (wp.getSeasonal())
				{
					LiveWeatherUtil LWU = new LiveWeatherUtil(p);
					LWU.call(true);
					lCurrentTime = LWU.lTime;
					String szTime = String.format("%02d:%02d", lCurrentTime/100, lCurrentTime %100);
					p.sendMessage(ChatColor.GOLD + "Time set to seasonal "+ChatColor.RED +szTime);
				}
				else
				{
					lCurrentTime = Main.updateTime(p);
					String szTime = String.format("%02d:%02d", lCurrentTime/100, lCurrentTime %100);
					p.sendMessage(ChatColor.GOLD + "Time set to non seasonal "+ChatColor.RED +szTime);
				}
			}
			else
			{
				p.sendMessage(ChatColor.GOLD + "Live time disabled");
				p.resetPlayerTime();
			}
		}
		return true;
	}
} //End Class

//Created by Bluecarpet in London