package net.minecraftmurder.commands;

import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.minecraftmurder.main.MPlayer;
import net.minecraftmurder.tools.ChatContext;
import net.minecraftmurder.tools.MLogger;
import net.minecraftmurder.tools.Paths;
import net.minecraftmurder.tools.SimpleFile;

public class CoinCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		if (args.length < 1) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatContext.PREFIX_WARNING + "Only players may execute this command.");
				return true;
			}
			Player player = (Player) sender;
			int coins = 0;
			if (SimpleFile.exists(Paths.FOLDER_PLAYERS + player.getName() + ".yml")) {
				coins = MPlayer.getCoins(player.getName());
			}
			sender.sendMessage(ChatContext.PREFIX_PLUGIN + "You have " + ChatContext.COLOR_HIGHLIGHT + coins + ChatContext.COLOR_LOWLIGHT  + (coins != 1?" coins":" coins") + "!");
			return true;
		} else {
			if (!sender.hasPermission("murder.coins.manage"))  {
				sender.sendMessage(ChatContext.PREFIX_PLUGIN + "You're not allowed to use arguments on this command.");
				return true;
			}
			if (args[0].equalsIgnoreCase("add")) {
				if (args.length != 3) {
					sender.sendMessage(ChatContext.ERROR_ARGUMENTS);
					sender.sendMessage(ChatContext.PREFIX_PLUGIN + "/coins add <player> <count>");
					return true;
				}
				int count;
				try {
					count = Integer.parseInt(args[2]);
				} catch (Exception e) {
					sender.sendMessage(ChatContext.COLOR_WARNING + args[2] + " is not a valid number.");
					return true;
				}
				MLogger.log(Level.INFO, sender.getName() + " gave " + count + " coins to " + args[1] + ".");
				MPlayer.addCoins(args[1], count, true, true);
				return true;
			} else if (args[0].equalsIgnoreCase("set")) {
				if (args.length != 3) {
					sender.sendMessage(ChatContext.ERROR_ARGUMENTS);
					sender.sendMessage(ChatContext.PREFIX_PLUGIN + "/coins set <player> <count>");
					return true;
				}
				int count;
				try {
					count = Integer.parseInt(args[2]);
				} catch (Exception e) {
					sender.sendMessage(ChatContext.COLOR_WARNING + args[2] + " is not a valid number.");
					return true;
				}
				MPlayer.setCoins(args[1], count, true);
				return true;
			} else if (args[0].equalsIgnoreCase("get")) {
					if (args.length != 2) {
						sender.sendMessage(ChatContext.ERROR_ARGUMENTS);
						sender.sendMessage(ChatContext.PREFIX_PLUGIN + "/coins get <player>");
						return true;
					}
					if (SimpleFile.exists(Paths.FOLDER_PLAYERS + args[1] + ".yml"))
						sender.sendMessage(ChatContext.PREFIX_PLUGIN + args[1] + " has " + MPlayer.getCoins(args[1]) + " coins.");
					else
						sender.sendMessage(ChatContext.PREFIX_WARNING + args[1] + " doesn't exist.");
				}
			return false;
		}
	}
}