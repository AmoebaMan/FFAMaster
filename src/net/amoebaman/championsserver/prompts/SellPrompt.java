package net.amoebaman.championsserver.prompts;

import net.amoebaman.championsserver.TradeHandler;
import net.amoebaman.championsserver.objects.Trade;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SellPrompt extends StringPrompt{
	
	public Player getPlayer(ConversationContext context){
		return (Player) context.getSessionData("player");
	}
	
	public Trade getTrade(ConversationContext context){
		return (Trade) context.getSessionData("trade");
	}
	
	public String getPromptText(ConversationContext context) {
		if(getTrade(context).item != null)
			return "Anything else you were lookin' to offload, bro?";
		else
			return "I'll take anything useful off your hands for a fair price, bro.  Whatcha got?";
	}
	
	public Prompt acceptInput(ConversationContext context, String input) {
		ItemStack item = TradeHandler.getItem(input);
		
		Player player = getPlayer(context);
		Trade trade = getTrade(context);
		trade.item = item;
		context.setSessionData("trade", trade);
		
		if(item == null){
			player.sendRawMessage(TradeHandler.PREFIX + "Dang man, you tripped up on something strong.  I ain't never heard of nothin' like that before.");
			return new SellPrompt();
		}
		
		if(TradeHandler.getAmountInInventory(item, player.getInventory()) == 0){
			player.sendRawMessage(TradeHandler.PREFIX + "Hey, c'mon homie, don't try to scam a bro.  I know you ain't got none of that stuff with you.");
			return new SellPrompt();
		}
		
		int price = TradeHandler.getBuyPrice(item) / 5;
		trade.eachAt = price;
		context.setSessionData("trade", trade);
		
		if(price <= 0){
			player.sendRawMessage(TradeHandler.PREFIX + "Sorry man, but even I won't take that kind of junk off your hands.");
			return new SellPrompt();
		}
		
		player.sendRawMessage(TradeHandler.PREFIX + "Hey, nice find man!  I'll take " + ChatColor.GREEN + TradeHandler.getName(item) + "s" + ChatColor.RESET + " off your hands for " + ChatColor.GREEN + price + "LVL" + ChatColor.RESET + " each.");
		return new HowManyPrompt();
	}
	
}