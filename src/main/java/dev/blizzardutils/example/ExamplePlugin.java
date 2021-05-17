package dev.blizzardutils.example;

import dev.blizzardutils.builder.ItemBuilder;
import dev.blizzardutils.string.StringUtils;
import dev.blizzardutils.task.kronos.KronosLibrary;
import dev.blizzardutils.task.time.BenchMarkingUtilities;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExamplePlugin extends JavaPlugin {


    private final List<String> lore = Arrays.asList("&eThis is a fire snowball", "&c1, 2, 3");
    private final KronosLibrary<Void> kronosLibrary = new KronosLibrary<>();
    private final Map<Enchantment, Integer> enchantMap = new ConcurrentHashMap<>();


    public void onEnable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println("Only players can use this command");
            return true;
        }

        BenchMarkingUtilities.initStartTime();
        if (command.getName().equalsIgnoreCase("iceutilsexample")) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                player.sendMessage(StringUtils.format("&cOnly OP Players can use this command!"));
                return true;
            }

            enchantMap.put(Enchantment.FIRE_ASPECT, 2);
            enchantMap.put(Enchantment.DURABILITY, 5);
            player.getInventory().addItem(ItemBuilder.Builder.getInstance().itemType(Material.SNOW_BALL).itemAmount(1).itemName(StringUtils.format("&6Snowball"))
                    .itemEnchant(enchantMap).itemLore(lore).buildWithEnchants());
            kronosLibrary.createNewChain(null).runAsync(() -> player.sendMessage("This is an Async Task of Kronos Chain")).whenCompleteAsync((aVoid, throwable) -> kronosLibrary.callBukkitSyncTask(this, () -> player.sendMessage("Now Running Bukkit Sync Task After Kronos Chain running async")));
            BenchMarkingUtilities.getFinalResults(player);
        }
        return true;
    }
}
