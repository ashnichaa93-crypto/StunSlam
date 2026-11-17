package com.stunslam;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class StunSlamClient implements ClientModInitializer {
    private static final KeyBinding.Category CATEGORY =
            KeyBinding.Category.create(Identifier.of("stunslam", "main"));

    private static KeyBinding toggleKey;
    private static KeyBinding configKey;

    private static boolean enabled = false;
    private static boolean running = false; // guards against re-triggering from our own attacks

    private static int waitTicks = -1;
    private static Entity pendingTarget;
    private static int pendingMaceSlot;

    @Override
    public void onInitializeClient() {
        StunSlamConfig.load();

        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.stunslam.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_J, CATEGORY));
        configKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.stunslam.config", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_BRACKET, CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);

        AttackEntityCallback.EVENT.register((player, world, hand, entity, hit) -> {
            if (!world.isClient() || !enabled || running || waitTicks >= 0) return ActionResult.PASS;
            if (!(entity instanceof PlayerEntity)) return ActionResult.PASS;

            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null || mc.interactionManager == null) return ActionResult.PASS;

            int axe = -1, mace = -1;
            for (int i = 0; i < 9; i++) {
                ItemStack s = mc.player.getInventory().getStack(i);
                if (axe == -1 && s.isIn(ItemTags.AXES)) axe = i;
                if (mace == -1 && s.isOf(Items.MACE)) mace = i;
            }
            if (axe == -1 || mace == -1) return ActionResult.PASS; // missing item -> normal attack

            // 1) axe hit
            hit(mc, axe, entity);

            // 2) mace hit
            int delay = StunSlamConfig.INSTANCE.delayTicks;
            if (delay <= 0) {
                hit(mc, mace, entity);
            } else {
                pendingTarget = entity;
                pendingMaceSlot = mace;
                waitTicks = delay;
            }
            return ActionResult.FAIL; // cancel the original attack
        });
    }

    private void onTick(MinecraftClient mc) {
        while (toggleKey.wasPressed()) {
            enabled = !enabled;
            if (mc.player != null) {
                mc.player.sendMessage(Text.literal("Stun Slam: " + (enabled ? "§aON" : "§cOFF")), true);
            }
        }
        while (configKey.wasPressed()) {
            mc.setScreen(new StunSlamScreen());
        }

        if (mc.player == null || mc.world == null) {
            waitTicks = -1;
            pendingTarget = null;
            return;
        }

        if (waitTicks >= 0) {
            if (waitTicks == 0) {
                if (pendingTarget != null && pendingTarget.isAlive() && mc.player.distanceTo(pendingTarget) < 6.0f) {
                    hit(mc, pendingMaceSlot, pendingTarget);
                }
                waitTicks = -1;
                pendingTarget = null;
            } else {
                waitTicks--;
            }
        }
    }

    private static void hit(MinecraftClient mc, int slot, Entity target) {
        mc.player.getInventory().setSelectedSlot(slot); // synced to server by attackEntity
        running = true;
        try {
            mc.interactionManager.attackEntity(mc.player, target);
            mc.player.swingHand(Hand.MAIN_HAND);
        } finally {
            running = false;
        }
    }
}
