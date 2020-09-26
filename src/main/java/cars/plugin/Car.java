package cars.plugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Car {
    private Plugin plugin = Plugin.getPlugin(Plugin.class);
    private static ConfigManager config;
    private Player player;
    private Minecart carro;
    private boolean reversa = false;
    private boolean turbo = false;
    public static final HashMap<Player,Car> cars = new HashMap<Player,Car>();
    public static ItemStack reverseItem_ON;
    public static ItemStack reverseItem_OFF;
    public static ItemStack boostItem_ON;
    public static ItemStack boostItem_OFF;
    private ItemStack[] inventario;
    private ItemStack[] Armadura;
    public Car(Player p){
        this.player = p;
        this.carro = Bukkit.getWorld(p.getWorld().getName()).spawn(p.getLocation(), Minecart.class);
        cars.put(p,this);
        carro.setInvulnerable(plugin.vulnerable);

    }
    public Boolean HasTurbo(){
        return turbo;
    }
    public void setTurbo(Boolean bol) { this.turbo = bol;}
    public Boolean HasReverse(){
        return reversa;
    }
    public void setReversa(Boolean bol) {this.reversa = bol;}
    public void removeCar(Player p){
        if(cars.containsKey(p)){
            cars.get(p).carro.remove();
            cars.remove(p, cars.get(p));
            reversa = false;
            turbo = false;
        }
    }
    public static Boolean hasCar(Player p) {
        return cars.containsKey(p);
    }
    public void setNewInventory(){
        this.inventario = player.getInventory().getContents();
        this.Armadura = player.getInventory().getArmorContents();
        player.getInventory().clear();
        player.getInventory().setItem(0, boostItem_OFF);
        player.getInventory().setItem(8, reverseItem_OFF);
        player.updateInventory();
    }

    public static void loadConfig() {
        loadBoostOFF();
        loadBoostON();
        loadReverseOFF();
        loadReverseON();

    }

    public static void loadBoostOFF(){
        config = new ConfigManager();
        String Boositem = "config.Inventory.Boost.itemOFF";
        String itemID = config.getConfig().getString(Boositem + ".item");
        if(itemID.contains(":")){
            Integer id = Integer.parseInt( itemID.split(":")[0]);
            Short sht = Short.parseShort(itemID.split(":")[1]);
            boostItem_OFF = new ItemStack(Material.getMaterial(id), 1 , (Short) sht);
        } else {
            boostItem_OFF = new ItemStack(Material.getMaterial(Integer.parseInt(itemID)), 1);
        }
        ItemMeta boostMeta = boostItem_OFF.getItemMeta();
        boostMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&' , config.getConfig().getString(Boositem + ".name")));
        List<String> lore = new ArrayList<String>();
        for(int x=0; x<config.getConfig().getStringList(Boositem + ".lore").size(); x++){
            lore.add(ChatColor.translateAlternateColorCodes('&', config.getConfig().getStringList(Boositem + ".lore").get(x)));
        }
        boostMeta.setLore(lore);
        boostItem_OFF.setItemMeta(boostMeta);
        /*********************/
    }

    public static void loadBoostON(){
        config = new ConfigManager();
        String Boositem = "config.Inventory.Boost.itemON";
        String itemID = config.getConfig().getString(Boositem + ".item");
        if(itemID.contains(":")){
            Integer id = Integer.parseInt( itemID.split(":")[0]);
            Short sht = Short.parseShort(itemID.split(":")[1]);
            boostItem_ON = new ItemStack(Material.getMaterial(id), 1 , (Short) sht);
        } else {
            boostItem_ON = new ItemStack(Material.getMaterial(Integer.parseInt(itemID)), 1);
        }
        ItemMeta boostMeta = boostItem_ON.getItemMeta();
        boostMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&' , config.getConfig().getString(Boositem + ".name")));
        List<String> lore = new ArrayList<String>();
        for(int x=0; x<config.getConfig().getStringList(Boositem + ".lore").size(); x++){
            lore.add(ChatColor.translateAlternateColorCodes('&', config.getConfig().getStringList(Boositem + ".lore").get(x)));
        }
        boostMeta.setLore(lore);
        boostItem_ON.setItemMeta(boostMeta);
        /*********************/
    }

    public static void loadReverseOFF(){
        config = new ConfigManager();
        String Boositem = "config.Inventory.Reverse.itemOFF";
        String itemID = config.getConfig().getString(Boositem + ".item");
        if(itemID.contains(":")){
            Integer id = Integer.parseInt( itemID.split(":")[0]);
            Short sht = Short.parseShort(itemID.split(":")[1]);
            reverseItem_OFF = new ItemStack(Material.getMaterial(id), 1 , (Short) sht);
        } else {
            reverseItem_OFF = new ItemStack(Material.getMaterial(Integer.parseInt(itemID)), 1);
        }
        ItemMeta boostMeta = reverseItem_OFF.getItemMeta();
        boostMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&' , config.getConfig().getString(Boositem + ".name")));
        List<String> lore = new ArrayList<String>();
        for(int x=0; x<config.getConfig().getStringList(Boositem + ".lore").size(); x++){
            lore.add(ChatColor.translateAlternateColorCodes('&', config.getConfig().getStringList(Boositem + ".lore").get(x)));
        }
        boostMeta.setLore(lore);
        reverseItem_OFF.setItemMeta(boostMeta);
        /*********************/
    }
    public static void loadReverseON(){
        config = new ConfigManager();
        String Boositem = "config.Inventory.Reverse.itemON";
        String itemID = config.getConfig().getString(Boositem + ".item");
        if(itemID.contains(":")){
            Integer id = Integer.parseInt( itemID.split(":")[0]);
            Short sht = Short.parseShort(itemID.split(":")[1]);
            reverseItem_ON = new ItemStack(Material.getMaterial(id), 1 , (Short) sht);
        } else {
            reverseItem_ON = new ItemStack(Material.getMaterial(Integer.parseInt(itemID)), 1);
        }
        ItemMeta boostMeta = reverseItem_ON.getItemMeta();
        boostMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&' , config.getConfig().getString(Boositem + ".name")));
        List<String> lore = new ArrayList<String>();
        for(int x=0; x<config.getConfig().getStringList(Boositem + ".lore").size(); x++){
            lore.add(ChatColor.translateAlternateColorCodes('&', config.getConfig().getStringList(Boositem + ".lore").get(x)));
        }
        boostMeta.setLore(lore);
        reverseItem_ON.setItemMeta(boostMeta);
        /*********************/
    }



    public void PlayersetInventario(){
        player.getInventory().clear();
        player.getInventory().setArmorContents(Armadura);
        player.getInventory().setContents(inventario);
        player.updateInventory();
    }
    public static Car getCar(Player p){
        if(cars.containsKey(p)){
            return cars.get(p);
        } else {
            return new Car(p);
        }
    }
}
