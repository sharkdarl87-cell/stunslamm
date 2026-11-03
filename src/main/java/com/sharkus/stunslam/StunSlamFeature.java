package com.sharkus.stunslam;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Stun Slam: Axt bricht den Schild, danach Mace-Smash.
 *
 * Ablauf:
 *  1. Du zielst auf einen Spieler, der blockt, und hast die Axt in der Hand.
 *     Sobald die Axt voll aufgeladen ist, wird automatisch zugeschlagen.
 *  2. Danach wird automatisch auf die Mace gewechselt.
 *  3. Bist du im Fall (fallDistance > MIN_FALL) und zielst auf das Ziel,
 *     schlaegt die Mace automatisch zu, sobald sie bereit ist.
 *
 * Springen und Zielen machst du selbst. Wird nur im Menue ("Ü") ueber
 * StunSlamFeature.enabled ein- und ausgeschaltet.
 */
public final class StunSlamFeature {

    public static boolean enabled = true;

    private static final double MIN_FALL = 1.5;      // Mindest-Fallhoehe fuer den Smash
    private static final float  AXE_READY = 1.0f;    // Axt voll aufgeladen
    private static final float  MACE_READY = 0.9f;   // Mace-Cooldown fast voll

    private enum State { IDLE, SWITCH_TO_MACE, WAIT_FOR_SMASH }
    private static State state = State.IDLE;
    private static int timeout = 0;

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(StunSlamFeature::tick);
    }

    private static void tick(Minecraft mc) {
        Player me = mc.player;
        if (!enabled || me == null || mc.gameMode == null || mc.screen != null) {
            state = State.IDLE;
            return;
        }

        Entity target = targetedPlayer(mc);

        switch (state) {
            case IDLE -> {
                if (target == null || !isAxe(me.getMainHandItem())) return;
                if (!(target instanceof Player p) || !p.isBlocking()) return;
                if (me.getAttackStrengthScale(0f) < AXE_READY) return;

                attack(mc, me, target);
                state = State.SWITCH_TO_MACE;
                timeout = 40; // 2 Sekunden Zeitfenster
            }
            case SWITCH_TO_MACE -> {
                if (--timeout <= 0) { state = State.IDLE; return; }
                int slot = findMaceSlot(me);
                if (slot < 0) { state = State.IDLE; return; }
                me.getInventory().setSelectedSlot(slot);
                state = State.WAIT_FOR_SMASH;
            }
            case WAIT_FOR_SMASH -> {
                if (--timeout <= 0) { state = State.IDLE; return; }
                if (!me.getMainHandItem().is(Items.MACE)) return;
                if (target == null) return;
                if (me.fallDistance < MIN_FALL) return;
                if (me.getAttackStrengthScale(0f) < MACE_READY) return;

                attack(mc, me, target);
                state = State.IDLE;
            }
        }
    }

    private static Entity targetedPlayer(Minecraft mc) {
        HitResult hit = mc.hitResult;
        if (hit instanceof EntityHitResult ehr && ehr.getEntity() instanceof Player) {
            return ehr.getEntity();
        }
        return null;
    }

    private static void attack(Minecraft mc, Player me, Entity target) {
        mc.gameMode.attack(me, target);
        me.swing(InteractionHand.MAIN_HAND);
    }

    private static boolean isAxe(ItemStack s) {
        return s.is(ItemTags.AXES);
    }

    private static int findMaceSlot(Player me) {
        for (int i = 0; i < 9; i++) {
            if (me.getInventory().getItem(i).is(Items.MACE)) return i;
        }
        return -1;
    }
}
