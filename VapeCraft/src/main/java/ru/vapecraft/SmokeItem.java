
package ru.vapecraft;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.player.PlayerEntity;

public class SmokeItem extends Item {
    private final float damage;
    private final int radius;
    private final int poisonSeconds;
    private final int cooldown;
    private final int smokeAmount;

    public SmokeItem(int durability, float damage, int radius,
                     int poisonSeconds, int cooldown, int smokeAmount) {
        super(new Item.Settings().maxCount(1).maxDamage(durability));
        this.damage = damage;
        this.radius = radius;
        this.poisonSeconds = poisonSeconds;
        this.cooldown = cooldown;
        this.smokeAmount = smokeAmount;
    }

    @Override
    public TypedActionResult<ItemStack> use(
            World world, PlayerEntity user, Hand hand) {

        ItemStack stack = user.getStackInHand(hand);

        if (stack.getDamage() >= stack.getMaxDamage()) {
            return TypedActionResult.fail(stack);
        }

        user.getItemCooldownManager().set(this, cooldown);

        if (!world.isClient) {
            var center = user.getPos()
                    .add(user.getRotationVec(1.0f).multiply(1.4));

            ServerWorld serverWorld = (ServerWorld) world;

            // Густое облако дыма
            serverWorld.spawnParticles(
                    ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    center.x, center.y, center.z,
                    smokeAmount,
                    0.8, 0.7, 0.8,
                    0.035
            );

            serverWorld.spawnParticles(
                    ParticleTypes.CLOUD,
                    center.x, center.y, center.z,
                    smokeAmount / 2,
                    0.7, 0.5, 0.7,
                    0.025
            );

            Box area = new Box(
                    center.x - radius, center.y - radius, center.z - radius,
                    center.x + radius, center.y + radius, center.z + radius
            );

            for (Entity entity : world.getOtherEntities(
                    user, area, e -> e instanceof LivingEntity)) {

                if (entity.squaredDistanceTo(center) <= radius * radius) {
                    LivingEntity target = (LivingEntity) entity;

                    target.damage(world.getDamageSources().magic(), damage);

                    target.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.POISON,
                            poisonSeconds * 20,
                            1
                    ));
                }
            }

            stack.damage(1, user, p -> p.sendToolBreakStatus(hand));
        }

        return TypedActionResult.success(stack, world.isClient());
    }
}
