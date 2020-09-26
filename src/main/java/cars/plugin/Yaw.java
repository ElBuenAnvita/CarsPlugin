package cars.plugin;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

    public enum Yaw {
        NORTH, SOUTH, EAST, WEST;
        public static Yaw getYaw(Player p) {
            float yaw = p.getLocation().getYaw();
            yaw = (yaw % 360 + 360) % 360; // true modulo, as javas modulo is weird for negative values
            if (yaw > 135 || yaw < -135) {
                return Yaw.NORTH;
            } else if (yaw < -45) {
                return Yaw.EAST;
            } else if (yaw > 45) {
                return Yaw.WEST;
            } else {
                return Yaw.SOUTH;
            }
        }

        public static Vector obtenerYaw(Player p) {
            double radians = Math.toRadians(p.getLocation().getYaw());
            double x = Math.sin(radians);
            double z = Math.cos(radians);
            if(p.getLocation().add(0,1,-0.7).getBlock().getType() != Material.AIR || p.getLocation().add(0,1,+0.7).getBlock().getType() != Material.AIR || p.getLocation().add(-0.7,1,0).getBlock().getType() != Material.AIR || p.getLocation().add(+0.7,1,0).getBlock().getType() != Material.AIR) {
                return new Vector(-x, 1, z);
            } else if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
                return new Vector (-x, -1, z);
            } else {
                return new Vector(-x, 0, z);
            }
        }
    }