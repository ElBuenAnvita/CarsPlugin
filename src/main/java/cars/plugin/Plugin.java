package cars.plugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

import static cars.plugin.Yaw.obtenerYaw;


public class Plugin extends JavaPlugin implements Listener {
    private Plugin plugin;
    private static Plugin instance;
    private ConfigManager config;
    private Integer normal_speed;
    private Integer boost_speed;
    private Integer reverce_speed;
    public Boolean vulnerable;
    public Vector v;

    @Override
    public void onEnable() {
        instance = this;
        RegisterConfig();
        LoadConfiguration();
        Car.loadConfig();
        getCommand("car").setExecutor((CommandExecutor)new Commands());
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Cars has been enable.");

    }
    public static Plugin get() {
        return instance;
    }
    public void RegisterConfig() {
        config = new ConfigManager();
       config.registerConfig();
    }
    public void LoadConfiguration(){
        normal_speed = Integer.parseInt(config.getConfig().getString("config.normal-speed"));
        boost_speed = Integer.parseInt(config.getConfig().getString("config.boost-speed"));
        reverce_speed = Integer.parseInt(config.getConfig().getString("config.reverce-speed"));
        vulnerable = Boolean.parseBoolean(config.getConfig().getString("config.vulnerable"));
    }

    @Override
    public void onDisable() {
      for(Player p : Bukkit.getOnlinePlayers()){
          if(p.isInsideVehicle()){
              if(!Car.hasCar(p)){ return;}
              Car car = Car.getCar(p);
              p.getInventory().clear();
              car.PlayersetInventario();
          }
      }
    }

    @EventHandler
    public void CarDestroy(VehicleDestroyEvent e){
        if(vulnerable){
            e.setCancelled(false);
        } else {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void BlockPlace(BlockPlaceEvent e){
        if(e.getPlayer().isInsideVehicle() && Car.hasCar(e.getPlayer())){
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void ItemsInteract(PlayerInteractEvent e){
       if(e.getPlayer().isInsideVehicle()){
           if(!Car.hasCar(e.getPlayer())){ return; }
           Car car = Car.getCar(e.getPlayer());
           if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
               if(e.getPlayer().getItemInHand().getType() == Car.boostItem_ON.getType()){
                   car.setTurbo(false);
                   car.setReversa(false);
                   e.getPlayer().setItemInHand(Car.boostItem_OFF);
                   return;
               }
               if(e.getPlayer().getItemInHand().getType() == Car.boostItem_OFF.getType()){
                    car.setTurbo(true);
                   car.setReversa(false);
                   e.getPlayer().setItemInHand( Car.boostItem_ON);
                   return;
               }
               if(e.getPlayer().getItemInHand().getType() == Car.reverseItem_OFF.getType()){
                   car.setReversa(true);
                   car.setTurbo(false);
                   e.getPlayer().setItemInHand(Car.reverseItem_ON);
                   return;
               }
               if(e.getPlayer().getItemInHand().getType() == Car.reverseItem_ON.getType()){
                   car.setReversa(false);
                   car.setTurbo(false);
                   e.getPlayer().setItemInHand(Car.reverseItem_OFF);
                   return;
               }
               e.getPlayer().updateInventory();



           }

       }
    }


    @EventHandler
    public void InteractVehicle(PlayerInteractAtEntityEvent e){
        if(e.getRightClicked() instanceof Minecart){
            if(!e.isCancelled()){
                Player p = (Player) e.getPlayer();
                if(!Car.hasCar(p)){ return; }
                Car car = Car.getCar(p);
                car.setNewInventory();
                p.sendMessage("§9§lBOT §fAnviBot §8» §7Utiliza los controles para acelerar o retroceder. ¡Buen viaje!");
            }
        }
    }

    @EventHandler
    public void quitEvent(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if(p.isInsideVehicle()){
            if(!Car.hasCar(p)){ return;}
            Car car = Car.getCar(p);
                p.getInventory().clear(); // Se obtiene el inventario y lo elimina
                car.PlayersetInventario(); // Se coloca el inventario que tenía
                car.removeCar(p); // Remueve el carro si el usuario se desconecta
        } else if (!p.isInsideVehicle()) {
            if(!Car.hasCar(p)){ return;}
            Car car = Car.getCar(p);
                car.removeCar(p); // Remueve el carro, mas no se elimina el inventario
        }

    }

    @EventHandler
    public void DamageEvent(EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if(p.isInsideVehicle()){
                if(!Car.hasCar(p)){ return;}
                Car car = Car.getCar(p);
                if(p.getHealth() - e.getFinalDamage() > 0) {
                    p.getInventory().clear();
                    car.PlayersetInventario();
                }
            }
        }
    }


    @EventHandler
    public void onPlayerMove(VehicleMoveEvent event) {
        Entity passenger = event.getVehicle().getPassenger();
        if (passenger instanceof Player) {
            Player p = (Player)passenger;
            if(!Car.hasCar(p)){ return; }
            Car car = Car.getCar(p);
            Vehicle vee = event.getVehicle();
            /* if(p.isSneaking()){
                car.PlayersetInventario();
            } */
            if(car.HasTurbo()) {
                v = p.getLocation().getDirection().multiply(boost_speed);
            } else if (car.HasReverse()) {
                v = p.getLocation().getDirection().multiply(reverce_speed);
            } else {
                v = p.getLocation().getDirection().multiply(normal_speed);
            }

            vee.getLocation().setDirection(v);
            p.getLocation().getYaw();

            vee.setVelocity(obtenerYaw(p));
        }
    }
    @EventHandler
    public void onVehicleExitEvent(VehicleExitEvent event) {
        Vehicle vehicle = event.getVehicle();
        Entity passenger = vehicle.getPassenger();
        if(!(passenger instanceof Player)) {
            return;
        }
        Player p = (Player)passenger;
        if(!Car.hasCar(p)) { return; }
        Car car = Car.getCar(p);

        car.removeCar(p);
        car.PlayersetInventario();
        p.sendMessage("§9§lBOT §fAnviBot §8» §7Tu carro ha desaparecido");
    }

    @EventHandler
    public void onVehicleUpdate(VehicleUpdateEvent event) {
        Vehicle vehicle = event.getVehicle();
        Entity passenger = vehicle.getPassenger();
        if (!(passenger instanceof Player)) {
            return;
        }
        Player p = (Player)passenger;
        if(!Car.hasCar(p)){ return; }
        Car carr = Car.getCar(p);
        if (vehicle instanceof Minecart) {
            Minecart car = (Minecart)vehicle;
            if(v == null){
                this.v = p.getLocation().getDirection();
            }
            Vector dir;
            if(carr.HasTurbo()){
                dir = this.v.multiply(boost_speed);
            } else if(carr.HasReverse()){
                dir = this.v.multiply(reverce_speed);
            } else {
                dir = this.v.multiply(normal_speed);
            }

            Vector dir_ = new Vector(dir.getX(), 1.0E-4D, dir.getZ());
            car.setVelocity(dir_);
        }
    }


}
