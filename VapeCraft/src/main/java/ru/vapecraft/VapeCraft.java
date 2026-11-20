
package ru.vapecraft;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class VapeCraft implements ModInitializer {
    public static final String MOD_ID = "vapecraft";

    public static final Item CIGARETTE = register("cigarette", new SmokeItem(32, 1.0f, 1, 1, 40));
    public static final Item DISPOSABLE = register("disposable_vape", new SmokeItem(48, 2.0f, 2, 2, 60));
    public static final Item POD = register("pod_system", new SmokeItem(96, 3.0f, 3, 3, 80));
    public static final Item PRO_VAPE = register("pro_vape", new SmokeItem(160, 5.0f, 4, 4, 100));
    public static final Item LEGEND_VAPE = register("legend_vape", new SmokeItem(240, 8.0f, 6, 5, 120));

    public static final Item EMPTY_CARTRIDGE = register("empty_cartridge", new Item(new Item.Settings()));
    public static final Item CARTRIDGE = register("filled_cartridge", new Item(new Item.Settings()));
    public static final Item E_LIQUID = register("e_liquid", new Item(new Item.Settings()));
    public static final Item BATTERY = register("battery", new Item(new Item.Settings()));
    public static final Item COIL = register("coil", new Item(new Item.Settings()));

    public static final ItemGroup VAPECRAFT_GROUP = Registry.register(
            Registries.ITEM_GROUP,
            new Identifier(MOD_ID, "main"),
            FabricItemGroup.builder()
                    .displayName(Text.literal("VapeCraft"))
                    .icon(() -> new ItemStack(CIGARETTE))
                    .entries((context, entries) -> {
                        entries.add(CIGARETTE);
                        entries.add(DISPOSABLE);
                        entries.add(POD);
                        entries.add(PRO_VAPE);
                        entries.add(LEGEND_VAPE);
                        entries.add(EMPTY_CARTRIDGE);
                        entries.add(CARTRIDGE);
                        entries.add(E_LIQUID);
                        entries.add(BATTERY);
                        entries.add(COIL);
                    })
                    .build()
    );

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(MOD_ID, name), item);
    }

    @Override
    public void onInitialize() {
    }
}
