package com.glatinis.basecraft.economy.bank.npc;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class NPCSpawner {

    static String bankTellerSignature = "Gtp5KPLbIZJh3lLHzvrscsCPKptVZsqaWdSKwtub/nEEc0uC7srQxNfvYneGBbbZPRYxWFOjvqk3Nopy6omxO8FN83LAuvAB+YM5RXo8tvksl8sF/KIuzIRgr1peplkMXI+uDZhIl9jzmLo5s6ibAzKOlgNPzm6dLIV1DFx0tccN6y7BoFUah00uis4ecTXKfAhnqsJC84nHlREJ7EWmOBdqQSgo1PvLLaCgh7NF1c5/6lX7zxag591NfXPCR0K5+mjXULATLQ5wVVCwhxEtWhmj26di9JrITPlOnz0meLeC6+Cy64/eHHZmlJWmlRd8aASC7BwoGOJ6SUkq8Eh1NJe6zCyeR0wv8YOBPVvyzTgTmkQjkrgrJXyxeqqRS/TdC/+7h9oQSnW3LYTPKLR5iROSImjdEJv7Cxm9ecUHRK79JBOz9zv1m3JZKTV6wN8zcHkFoJhXK6wcUUHM2txIlEfq+xjB1rU3w44+CtdemfIbwbtcm+ktSngPBuLaUEIJ8ZL7Brmw+8hnw+GsDOZ4tv0pMY76ERrqOLR9ntptaAlFo9vg9FhS/E9ig8G3OtXJ4YJ+IKzPwv7hE99heetLFkazoj6gwyD1LEZA+4OyYL1olgwSBEi+Mqy9DypHVXW4l7aeeZ1hQzXgvaixczrBT0SqMDZeo16HpSwF4RxxNbI=";
    static String bankTellerTexture = "eyJ0aW1lc3RhbXAiOjE1ODU3NDQxODUwMTUsInByb2ZpbGVJZCI6IjJkYzc3YWU3OTQ2MzQ4MDI5NDI4MGM4NDIyNzRiNTY3IiwicHJvZmlsZU5hbWUiOiJzYWR5MDYxMCIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjI1OWNiMzY1YTA2NTQ1ZjIyZjQ5NGM0OGNjNzg0NmRkZWQ5N2QzZDQ0MjMxMjgxZjMwMzdjZTVkODNiNzg1NSJ9fX0=";

    public static void spawnBankTeller(Location loc) {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Bank Teller");
        SkinTrait skinTrait = npc.getOrAddTrait(SkinTrait.class);
        skinTrait.setSkinPersistent("bankteller", bankTellerSignature, bankTellerTexture);

        npc.spawn(loc);
        npc.addTrait(BankTrait.class);
    }
}
