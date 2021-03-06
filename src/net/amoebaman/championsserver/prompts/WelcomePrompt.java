package net.amoebaman.championsserver.prompts;

import java.util.Map;

import net.amoebaman.championsserver.ChampionsServer;
import net.amoebaman.utils.JsonReader;
import net.amoebaman.utils.JsonWriter;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class WelcomePrompt extends MessagePrompt{
	
	public Player getPlayer(ConversationContext context){
		return (Player) context.getSessionData("player");
	}
	
	@SuppressWarnings("resource")
    public String getPromptText(ConversationContext context) {
		Player player = getPlayer(context);
		Map<String, String> data = new JsonReader(ChampionsServer.sql.loadDataString(player)).readMap();
		if(data.get("doneconvotutorial") == null || data.get("doneconvotutorial").equals("false")){
			player.sendRawMessage(ChatColor.RED + "Chat with the NPC in chat, just like normal");
			player.sendRawMessage(ChatColor.GOLD + "Other players can't hear what you say to him");
			player.sendRawMessage(ChatColor.YELLOW + "You can end the conversation by typing " + ChatColor.RED + "SHUT UP" + ChatColor.YELLOW + " in chat");
			data.put("doneconvotutorial", "true");
			ChampionsServer.sql.saveDataString(player, new JsonWriter().writeMap(data).closeOut());
		}
		return "Whassup dawg?";
	}
	
	protected Prompt getNextPrompt(ConversationContext context) { return new ActionPrompt(); }
	
}
