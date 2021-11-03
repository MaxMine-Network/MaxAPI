package net.maxmine.api.module.kit;

import lombok.Getter;
import net.maxmine.api.common.adapter.PurchaseAdapter;
import net.maxmine.api.common.utility.FormatingStringUtils;
import net.maxmine.api.common.utility.ItemUtil;
import net.maxmine.api.common.utility.StringUtil;
import net.maxmine.api.gamer.Gamer;
import net.maxmine.api.game.Game;
import net.maxmine.api.game.enums.GameType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class GameItem {

    public String name;
    public GameType type;
    public ItemStack item;

    private int minLevel = Integer.MAX_VALUE;
    private int cost;

    private int discount = 1;

    public GameItem(String name, GameType type, ItemStack item) {
        this.name = name;
        this.type = type;
        this.item = item;
    }

    public boolean buy(Gamer gamer) {
        PurchaseAdapter purcheses = gamer.getPurchaseAdapter();
        Player player = gamer.getPlayer();

        if (purcheses.hasItem(name)) {
            player.sendMessage(Game.getInstance().getGameType().getPrefix() + "Вы уже приобрели данную вещь!");
            return false;
        }

        int discountCost = cost * discount;

        if (gamer.getMoney() < discountCost) {
            player.sendMessage(Game.getInstance().getGameType().getPrefix() + "У вас недостаточно монет. Необходимо ещё §c" + StringUtil.formatingToTime(discountCost - gamer.getMoney(), FormatingStringUtils.MONEY));
            return false;
        }

        gamer.changeMoney((-1) * discountCost);
        purcheses.addItem(type, name);
        player.sendMessage(Game.getInstance().getGameType().getPrefix() + "Вы успешно приобрели набор '" + getDisplayName() + "§f'");
        return true;
    }

    public ItemStack getItem(Gamer gamer) {
        ItemUtil itemBuilder = ItemUtil.getBuilder(item.clone());
        int discountCost = cost * discount;

        if (gamer.getMoney() < discountCost && !isAvailable(gamer)) {
            itemBuilder.addLore(
                    "",
                    "§cВы не можете купиь данный набор!",
                    "§cДля того, чтобы купить, Вам необходимо:",
                    "§6* §c" + StringUtil.formatingToTime(discountCost, FormatingStringUtils.MONEY) + " (не хватает " + (discountCost - gamer.getMoney()) + ")");

        } else if (gamer.getMoney() > discountCost && !isAvailable(gamer)) {
            itemBuilder.addLore("", "§aЖми, чтобы купить кит за " + StringUtil.formatingToTime(discountCost, FormatingStringUtils.MONEY));
        } else if (isAvailable(gamer)) itemBuilder.addLore("", "§aЖми, чтобы выбрать кит!");


        return itemBuilder.build().clone();
    }

    public String getDisplayName() {
        return item.getItemMeta().getDisplayName();
    }

    public boolean isAvailable(Gamer gamer) {
        PurchaseAdapter purcheses = gamer.getPurchaseAdapter();

        if (gamer.getGroup().getLevel() >= minLevel)
            return true;

        return purcheses.hasItem(name);
    }

    public void addDiscount(int percent) {
        if (percent > 100 || percent < 1) {
            throw new NumberFormatException("Discont percent must be 1-100");
        }

        this.discount = percent / 100;
    }

    public void freeFor(int level) {
        this.minLevel = level;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
