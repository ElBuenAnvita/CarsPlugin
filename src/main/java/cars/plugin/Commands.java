package cars.plugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String arg, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(args[0].equals("create")){
                if(p.hasPermission("car.create")){
                    Car car = Car.getCar(p);
                    p.sendMessage("§9§lBOT §fAnviBot §8» §7Se ha colocado tu carro");
                }
            }
            else if(args[0].equals("delete")){
                if(p.hasPermission("car.delete")){
                    Car car = Car.getCar(p);
                    car.removeCar(p);
                    p.sendMessage("§9§lBOT §fAnviBot §8» §7Tu carro ha desaparecido");
                }
            }
            else if(args[0].isEmpty()){
                p.sendMessage("§9§lBOT §fAnviBot §8» §7Incorrecto uso del comando");
                p.sendMessage("§7Usa §e/car create§7 para crear tu propio carro");
                p.sendMessage("§7Usa §e/car delete§7 para eliminar tu carro");
            }

        }


        return true;
    }
}
