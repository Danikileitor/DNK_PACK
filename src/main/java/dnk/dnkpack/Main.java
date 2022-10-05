package dnk.dnkpack;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class Main extends JavaPlugin implements Listener{
	
	Logger log = Bukkit.getLogger();
    private static Economy econ = null;
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
    @Override
    public void onEnable() {

    	log.info("[DNK] Mierda varia loaded");
    	
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
    	log.info("[DNK] Desgraciao pa que me deshabilitas?!?!?!?!?");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
    	Player p = e.getPlayer();
    	p.getWorld().playSound(p.getLocation(), "custom.specialsummon", 1, 1);
    	if (p.getDisplayName().contentEquals("1234")){p.getWorld().spawnEntity(p.getLocation(), EntityType.PRIMED_TNT);}
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
    	Player p = e.getPlayer();
    	ItemStack offhand = new ItemStack(p.getInventory().getItemInOffHand().getType());
    	if (p.getInventory().getItemInOffHand().getType().equals(Material.TNT)){p.getWorld().spawnEntity(p.getLocation(), EntityType.PRIMED_TNT);}
    	else if(p.getInventory().getItemInOffHand().getType().equals(Material.SHIELD)){p.teleport(p.getLocation()); e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);}
    	else if(offhand.getType().equals(Material.AIR)){}
    	else {p.getWorld().dropItemNaturally(p.getLocation(), offhand);}
    }
    
    @EventHandler
    public void onCreatureNew(CreatureSpawnEvent e){
    	Entity c = e.getEntity();
    	c.getWorld().playSound(c.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
//    	c.getWorld().playSound(c.getLocation(), "custom.specialsummon", 1, 1);
    }
    
    @EventHandler
    public void onDisparo(EntityShootBowEvent e){
    	Entity flecha = e.getProjectile();
//    	flecha.addPassenger(flecha.getWorld().spawnEntity(flecha.getLocation(), EntityType.ARROW));
//    	flecha.getPassengers().get(0).addPassenger(flecha.getWorld().spawnEntity(flecha.getLocation(), EntityType.COW));
//    	flecha.getPassengers().get(0).getPassengers().get(0).addPassenger(flecha.getWorld().spawnEntity(flecha.getLocation(), EntityType.CHICKEN));
    	
    	Entity p = e.getEntity();
    	if(p.getType() == EntityType.PLAYER){
    	flecha.addPassenger(p);
    	flecha.setGlowing(true);
    	flecha.setCustomName("Nos vamos pa madrid");
    	flecha.setCustomNameVisible(true);
    }}

   @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {
       Player p = (Player) sender;
        if (command.getName().equalsIgnoreCase("dnk")) {
            sender.sendMessage("Escribiste /dnk!");
            EconomyResponse r = econ.depositPlayer(p, 1.00);
            if(r.transactionSuccess()) {
                sender.sendMessage(String.format("You were given %s and now have %s", econ.format(r.amount), econ.format(r.balance)));
            } else {
                sender.sendMessage(String.format("An error occured: %s", r.errorMessage));
            }
            return true;
        }
        if (command.getName().equalsIgnoreCase("mierda")) {
            sender.sendMessage("§b[DNK] Mierda pa ti");
            ItemStack mierda1 = new ItemStack(Material.CAMPFIRE, 1);
            ItemStack mierda2 = new ItemStack(Material.WHEAT_SEEDS, 4);
            p.getInventory().addItem(mierda1);
            p.getInventory().addItem(mierda2);
            return true;
        }
        return false;
    }

   public static Economy getEconomy() {
       return econ;
   }
}