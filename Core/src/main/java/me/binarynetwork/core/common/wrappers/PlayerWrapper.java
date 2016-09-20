package me.binarynetwork.core.common.wrappers;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import java.net.InetSocketAddress;
import java.util.*;

/**
 * Created by Bench on 9/20/2016.
 */
public class PlayerWrapper implements Player {

    private Player wrappedPlayer;

    public PlayerWrapper(Player wrappedPlayer)
    {
        this.wrappedPlayer = wrappedPlayer;
    }

    @Override
    public String getDisplayName()
    {
        return wrappedPlayer.getDisplayName();
    }

    @Override
    public void setDisplayName(String s)
    {
        wrappedPlayer.setDisplayName(s);
    }

    @Override
    public String getPlayerListName()
    {
        return wrappedPlayer.getPlayerListName();
    }

    @Override
    public void setPlayerListName(String s)
    {
        wrappedPlayer.setPlayerListName(s);
    }

    @Override
    public void setCompassTarget(Location location)
    {
        wrappedPlayer.setCompassTarget(location);
    }

    @Override
    public Location getCompassTarget()
    {
        return wrappedPlayer.getCompassTarget();
    }

    @Override
    public InetSocketAddress getAddress()
    {
        return wrappedPlayer.getAddress();
    }

    @Override
    public void sendRawMessage(String s)
    {
        wrappedPlayer.sendRawMessage(s);
    }

    @Override
    public void kickPlayer(String s)
    {
        wrappedPlayer.kickPlayer(s);
    }

    @Override
    public void chat(String s)
    {
        wrappedPlayer.chat(s);
    }

    @Override
    public boolean performCommand(String s)
    {
        return wrappedPlayer.performCommand(s);
    }

    @Override
    public boolean isSneaking()
    {
        return wrappedPlayer.isSneaking();
    }

    @Override
    public void setSneaking(boolean b)
    {
        wrappedPlayer.setSneaking(b);
    }

    @Override
    public boolean isSprinting()
    {
        return wrappedPlayer.isSprinting();
    }

    @Override
    public void setSprinting(boolean b)
    {
        wrappedPlayer.setSprinting(b);
    }

    @Override
    public void saveData()
    {
        wrappedPlayer.saveData();
    }

    @Override
    public void loadData()
    {
        wrappedPlayer.loadData();
    }

    @Override
    public void setSleepingIgnored(boolean b)
    {
        wrappedPlayer.setSleepingIgnored(b);
    }

    @Override
    public boolean isSleepingIgnored()
    {
        return wrappedPlayer.isSleepingIgnored();
    }

    @Override
    @Deprecated
    public void playNote(Location location, byte b, byte b1)
    {
        wrappedPlayer.playNote(location, b, b1);
    }

    @Override
    public void playNote(Location location, Instrument instrument, Note note)
    {
        wrappedPlayer.playNote(location, instrument, note);
    }

    @Override
    public void playSound(Location location, Sound sound, float v, float v1)
    {
        wrappedPlayer.playSound(location, sound, v, v1);
    }

    @Override
    public void playSound(Location location, String s, float v, float v1)
    {
        wrappedPlayer.playSound(location, s, v, v1);
    }

    @Override
    @Deprecated
    public void playEffect(Location location, Effect effect, int i)
    {
        wrappedPlayer.playEffect(location, effect, i);
    }

    @Override
    public <T> void playEffect(Location location, Effect effect, T t)
    {
        wrappedPlayer.playEffect(location, effect, t);
    }

    @Override
    @Deprecated
    public void sendBlockChange(Location location, Material material, byte b)
    {
        wrappedPlayer.sendBlockChange(location, material, b);
    }

    @Override
    @Deprecated
    public boolean sendChunkChange(Location location, int i, int i1, int i2, byte[] bytes)
    {
        return wrappedPlayer.sendChunkChange(location, i, i1, i2, bytes);
    }

    @Override
    @Deprecated
    public void sendBlockChange(Location location, int i, byte b)
    {
        wrappedPlayer.sendBlockChange(location, i, b);
    }

    @Override
    public void sendSignChange(Location location, String[] strings) throws IllegalArgumentException
    {
        wrappedPlayer.sendSignChange(location, strings);
    }

    @Override
    public void sendMap(MapView mapView)
    {
        wrappedPlayer.sendMap(mapView);
    }

    @Override
    public void updateInventory()
    {
        wrappedPlayer.updateInventory();
    }

    @Override
    public void awardAchievement(Achievement achievement)
    {
        wrappedPlayer.awardAchievement(achievement);
    }

    @Override
    public void removeAchievement(Achievement achievement)
    {
        wrappedPlayer.removeAchievement(achievement);
    }

    @Override
    public boolean hasAchievement(Achievement achievement)
    {
        return wrappedPlayer.hasAchievement(achievement);
    }

    @Override
    public void incrementStatistic(Statistic statistic) throws IllegalArgumentException
    {
        wrappedPlayer.incrementStatistic(statistic);
    }

    @Override
    public void decrementStatistic(Statistic statistic) throws IllegalArgumentException
    {
        wrappedPlayer.decrementStatistic(statistic);
    }

    @Override
    public void incrementStatistic(Statistic statistic, int i) throws IllegalArgumentException
    {
        wrappedPlayer.incrementStatistic(statistic, i);
    }

    @Override
    public void decrementStatistic(Statistic statistic, int i) throws IllegalArgumentException
    {
        wrappedPlayer.decrementStatistic(statistic, i);
    }

    @Override
    public void setStatistic(Statistic statistic, int i) throws IllegalArgumentException
    {
        wrappedPlayer.setStatistic(statistic, i);
    }

    @Override
    public int getStatistic(Statistic statistic) throws IllegalArgumentException
    {
        return wrappedPlayer.getStatistic(statistic);
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException
    {
        wrappedPlayer.incrementStatistic(statistic, material);
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException
    {
        wrappedPlayer.decrementStatistic(statistic, material);
    }

    @Override
    public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException
    {
        return wrappedPlayer.getStatistic(statistic, material);
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException
    {
        wrappedPlayer.incrementStatistic(statistic, material, i);
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException
    {
        wrappedPlayer.decrementStatistic(statistic, material, i);
    }

    @Override
    public void setStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException
    {
        wrappedPlayer.setStatistic(statistic, material, i);
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException
    {
        wrappedPlayer.incrementStatistic(statistic, entityType);
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException
    {
        wrappedPlayer.decrementStatistic(statistic, entityType);
    }

    @Override
    public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException
    {
        return wrappedPlayer.getStatistic(statistic, entityType);
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType, int i) throws IllegalArgumentException
    {
        wrappedPlayer.incrementStatistic(statistic, entityType, i);
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType, int i)
    {
        wrappedPlayer.decrementStatistic(statistic, entityType, i);
    }

    @Override
    public void setStatistic(Statistic statistic, EntityType entityType, int i)
    {
        wrappedPlayer.setStatistic(statistic, entityType, i);
    }

    @Override
    public void setPlayerTime(long l, boolean b)
    {
        wrappedPlayer.setPlayerTime(l, b);
    }

    @Override
    public long getPlayerTime()
    {
        return wrappedPlayer.getPlayerTime();
    }

    @Override
    public long getPlayerTimeOffset()
    {
        return wrappedPlayer.getPlayerTimeOffset();
    }

    @Override
    public boolean isPlayerTimeRelative()
    {
        return wrappedPlayer.isPlayerTimeRelative();
    }

    @Override
    public void resetPlayerTime()
    {
        wrappedPlayer.resetPlayerTime();
    }

    @Override
    public void setPlayerWeather(WeatherType weatherType)
    {
        wrappedPlayer.setPlayerWeather(weatherType);
    }

    @Override
    public WeatherType getPlayerWeather()
    {
        return wrappedPlayer.getPlayerWeather();
    }

    @Override
    public void resetPlayerWeather()
    {
        wrappedPlayer.resetPlayerWeather();
    }

    @Override
    public void giveExp(int i)
    {
        wrappedPlayer.giveExp(i);
    }

    @Override
    public void giveExpLevels(int i)
    {
        wrappedPlayer.giveExpLevels(i);
    }

    @Override
    public float getExp()
    {
        return wrappedPlayer.getExp();
    }

    @Override
    public void setExp(float v)
    {
        wrappedPlayer.setExp(v);
    }

    @Override
    public int getLevel()
    {
        return wrappedPlayer.getLevel();
    }

    @Override
    public void setLevel(int i)
    {
        wrappedPlayer.setLevel(i);
    }

    @Override
    public int getTotalExperience()
    {
        return wrappedPlayer.getTotalExperience();
    }

    @Override
    public void setTotalExperience(int i)
    {
        wrappedPlayer.setTotalExperience(i);
    }

    @Override
    public float getExhaustion()
    {
        return wrappedPlayer.getExhaustion();
    }

    @Override
    public void setExhaustion(float v)
    {
        wrappedPlayer.setExhaustion(v);
    }

    @Override
    public float getSaturation()
    {
        return wrappedPlayer.getSaturation();
    }

    @Override
    public void setSaturation(float v)
    {
        wrappedPlayer.setSaturation(v);
    }

    @Override
    public int getFoodLevel()
    {
        return wrappedPlayer.getFoodLevel();
    }

    @Override
    public void setFoodLevel(int i)
    {
        wrappedPlayer.setFoodLevel(i);
    }

    @Override
    public Location getBedSpawnLocation()
    {
        return wrappedPlayer.getBedSpawnLocation();
    }

    @Override
    public void setBedSpawnLocation(Location location)
    {
        wrappedPlayer.setBedSpawnLocation(location);
    }

    @Override
    public void setBedSpawnLocation(Location location, boolean b)
    {
        wrappedPlayer.setBedSpawnLocation(location, b);
    }

    @Override
    public boolean getAllowFlight()
    {
        return wrappedPlayer.getAllowFlight();
    }

    @Override
    public void setAllowFlight(boolean b)
    {
        wrappedPlayer.setAllowFlight(b);
    }

    @Override
    public void hidePlayer(Player player)
    {
        wrappedPlayer.hidePlayer(player);
    }

    @Override
    public void showPlayer(Player player)
    {
        wrappedPlayer.showPlayer(player);
    }

    @Override
    public boolean canSee(Player player)
    {
        return wrappedPlayer.canSee(player);
    }

    @Override
    @Deprecated
    public boolean isOnGround()
    {
        return wrappedPlayer.isOnGround();
    }

    @Override
    public boolean isFlying()
    {
        return wrappedPlayer.isFlying();
    }

    @Override
    public void setFlying(boolean b)
    {
        wrappedPlayer.setFlying(b);
    }

    @Override
    public void setFlySpeed(float v) throws IllegalArgumentException
    {
        wrappedPlayer.setFlySpeed(v);
    }

    @Override
    public void setWalkSpeed(float v) throws IllegalArgumentException
    {
        wrappedPlayer.setWalkSpeed(v);
    }

    @Override
    public float getFlySpeed()
    {
        return wrappedPlayer.getFlySpeed();
    }

    @Override
    public float getWalkSpeed()
    {
        return wrappedPlayer.getWalkSpeed();
    }

    @Override
    @Deprecated
    public void setTexturePack(String s)
    {
        wrappedPlayer.setTexturePack(s);
    }

    @Override
    public void setResourcePack(String s)
    {
        wrappedPlayer.setResourcePack(s);
    }

    @Override
    public Scoreboard getScoreboard()
    {
        return wrappedPlayer.getScoreboard();
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException
    {
        wrappedPlayer.setScoreboard(scoreboard);
    }

    @Override
    public boolean isHealthScaled()
    {
        return wrappedPlayer.isHealthScaled();
    }

    @Override
    public void setHealthScaled(boolean b)
    {
        wrappedPlayer.setHealthScaled(b);
    }

    @Override
    public void setHealthScale(double v) throws IllegalArgumentException
    {
        wrappedPlayer.setHealthScale(v);
    }

    @Override
    public double getHealthScale()
    {
        return wrappedPlayer.getHealthScale();
    }

    @Override
    public Entity getSpectatorTarget()
    {
        return wrappedPlayer.getSpectatorTarget();
    }

    @Override
    public void setSpectatorTarget(Entity entity)
    {
        wrappedPlayer.setSpectatorTarget(entity);
    }

    @Override
    @Deprecated
    public void sendTitle(String s, String s1)
    {
        wrappedPlayer.sendTitle(s, s1);
    }

    @Override
    @Deprecated
    public void resetTitle()
    {
        wrappedPlayer.resetTitle();
    }

    @Override
    public Spigot spigot()
    {
        return wrappedPlayer.spigot();
    }

    @Override
    public String getName()
    {
        return wrappedPlayer.getName();
    }

    @Override
    public PlayerInventory getInventory()
    {
        return wrappedPlayer.getInventory();
    }

    @Override
    public Inventory getEnderChest()
    {
        return wrappedPlayer.getEnderChest();
    }

    @Override
    public boolean setWindowProperty(InventoryView.Property property, int i)
    {
        return wrappedPlayer.setWindowProperty(property, i);
    }

    @Override
    public InventoryView getOpenInventory()
    {
        return wrappedPlayer.getOpenInventory();
    }

    @Override
    public InventoryView openInventory(Inventory inventory)
    {
        return wrappedPlayer.openInventory(inventory);
    }

    @Override
    public InventoryView openWorkbench(Location location, boolean b)
    {
        return wrappedPlayer.openWorkbench(location, b);
    }

    @Override
    public InventoryView openEnchanting(Location location, boolean b)
    {
        return wrappedPlayer.openEnchanting(location, b);
    }

    @Override
    public void openInventory(InventoryView inventoryView)
    {
        wrappedPlayer.openInventory(inventoryView);
    }

    @Override
    public void closeInventory()
    {
        wrappedPlayer.closeInventory();
    }

    @Override
    public ItemStack getItemInHand()
    {
        return wrappedPlayer.getItemInHand();
    }

    @Override
    public void setItemInHand(ItemStack itemStack)
    {
        wrappedPlayer.setItemInHand(itemStack);
    }

    @Override
    public ItemStack getItemOnCursor()
    {
        return wrappedPlayer.getItemOnCursor();
    }

    @Override
    public void setItemOnCursor(ItemStack itemStack)
    {
        wrappedPlayer.setItemOnCursor(itemStack);
    }

    @Override
    public boolean isSleeping()
    {
        return wrappedPlayer.isSleeping();
    }

    @Override
    public int getSleepTicks()
    {
        return wrappedPlayer.getSleepTicks();
    }

    @Override
    public GameMode getGameMode()
    {
        return wrappedPlayer.getGameMode();
    }

    @Override
    public void setGameMode(GameMode gameMode)
    {
        wrappedPlayer.setGameMode(gameMode);
    }

    @Override
    public boolean isBlocking()
    {
        return wrappedPlayer.isBlocking();
    }

    @Override
    public int getExpToLevel()
    {
        return wrappedPlayer.getExpToLevel();
    }

    @Override
    public double getEyeHeight()
    {
        return wrappedPlayer.getEyeHeight();
    }

    @Override
    public double getEyeHeight(boolean b)
    {
        return wrappedPlayer.getEyeHeight(b);
    }

    @Override
    public Location getEyeLocation()
    {
        return wrappedPlayer.getEyeLocation();
    }

    @Override
    @Deprecated
    public List<Block> getLineOfSight(HashSet<Byte> hashSet, int i)
    {
        return wrappedPlayer.getLineOfSight(hashSet, i);
    }

    @Override
    public List<Block> getLineOfSight(Set<Material> set, int i)
    {
        return wrappedPlayer.getLineOfSight(set, i);
    }

    @Override
    @Deprecated
    public Block getTargetBlock(HashSet<Byte> hashSet, int i)
    {
        return wrappedPlayer.getTargetBlock(hashSet, i);
    }

    @Override
    public Block getTargetBlock(Set<Material> set, int i)
    {
        return wrappedPlayer.getTargetBlock(set, i);
    }

    @Override
    @Deprecated
    public List<Block> getLastTwoTargetBlocks(HashSet<Byte> hashSet, int i)
    {
        return wrappedPlayer.getLastTwoTargetBlocks(hashSet, i);
    }

    @Override
    public List<Block> getLastTwoTargetBlocks(Set<Material> set, int i)
    {
        return wrappedPlayer.getLastTwoTargetBlocks(set, i);
    }

    @Override
    @Deprecated
    public Egg throwEgg()
    {
        return wrappedPlayer.throwEgg();
    }

    @Override
    @Deprecated
    public Snowball throwSnowball()
    {
        return wrappedPlayer.throwSnowball();
    }

    @Override
    @Deprecated
    public Arrow shootArrow()
    {
        return wrappedPlayer.shootArrow();
    }

    @Override
    public int getRemainingAir()
    {
        return wrappedPlayer.getRemainingAir();
    }

    @Override
    public void setRemainingAir(int i)
    {
        wrappedPlayer.setRemainingAir(i);
    }

    @Override
    public int getMaximumAir()
    {
        return wrappedPlayer.getMaximumAir();
    }

    @Override
    public void setMaximumAir(int i)
    {
        wrappedPlayer.setMaximumAir(i);
    }

    @Override
    public int getMaximumNoDamageTicks()
    {
        return wrappedPlayer.getMaximumNoDamageTicks();
    }

    @Override
    public void setMaximumNoDamageTicks(int i)
    {
        wrappedPlayer.setMaximumNoDamageTicks(i);
    }

    @Override
    public double getLastDamage()
    {
        return wrappedPlayer.getLastDamage();
    }

    @Override
    public void setLastDamage(double v)
    {
        wrappedPlayer.setLastDamage(v);
    }

    @Override
    public int getNoDamageTicks()
    {
        return wrappedPlayer.getNoDamageTicks();
    }

    @Override
    public void setNoDamageTicks(int i)
    {
        wrappedPlayer.setNoDamageTicks(i);
    }

    @Override
    public Player getKiller()
    {
        return wrappedPlayer.getKiller();
    }

    @Override
    public boolean addPotionEffect(PotionEffect potionEffect)
    {
        return wrappedPlayer.addPotionEffect(potionEffect);
    }

    @Override
    public boolean addPotionEffect(PotionEffect potionEffect, boolean b)
    {
        return wrappedPlayer.addPotionEffect(potionEffect, b);
    }

    @Override
    public boolean addPotionEffects(Collection<PotionEffect> collection)
    {
        return wrappedPlayer.addPotionEffects(collection);
    }

    @Override
    public boolean hasPotionEffect(PotionEffectType potionEffectType)
    {
        return wrappedPlayer.hasPotionEffect(potionEffectType);
    }

    @Override
    public void removePotionEffect(PotionEffectType potionEffectType)
    {
        wrappedPlayer.removePotionEffect(potionEffectType);
    }

    @Override
    public Collection<PotionEffect> getActivePotionEffects()
    {
        return wrappedPlayer.getActivePotionEffects();
    }

    @Override
    public boolean hasLineOfSight(Entity entity)
    {
        return wrappedPlayer.hasLineOfSight(entity);
    }

    @Override
    public boolean getRemoveWhenFarAway()
    {
        return wrappedPlayer.getRemoveWhenFarAway();
    }

    @Override
    public void setRemoveWhenFarAway(boolean b)
    {
        wrappedPlayer.setRemoveWhenFarAway(b);
    }

    @Override
    public EntityEquipment getEquipment()
    {
        return wrappedPlayer.getEquipment();
    }

    @Override
    public void setCanPickupItems(boolean b)
    {
        wrappedPlayer.setCanPickupItems(b);
    }

    @Override
    public boolean getCanPickupItems()
    {
        return wrappedPlayer.getCanPickupItems();
    }

    @Override
    public boolean isLeashed()
    {
        return wrappedPlayer.isLeashed();
    }

    @Override
    public Entity getLeashHolder() throws IllegalStateException
    {
        return wrappedPlayer.getLeashHolder();
    }

    @Override
    public boolean setLeashHolder(Entity entity)
    {
        return wrappedPlayer.setLeashHolder(entity);
    }

    @Override
    public Location getLocation()
    {
        return wrappedPlayer.getLocation();
    }

    @Override
    public Location getLocation(Location location)
    {
        return wrappedPlayer.getLocation(location);
    }

    @Override
    public void setVelocity(Vector vector)
    {
        wrappedPlayer.setVelocity(vector);
    }

    @Override
    public Vector getVelocity()
    {
        return wrappedPlayer.getVelocity();
    }

    @Override
    public World getWorld()
    {
        return wrappedPlayer.getWorld();
    }

    @Override
    public boolean teleport(Location location)
    {
        return wrappedPlayer.teleport(location);
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause teleportCause)
    {
        return wrappedPlayer.teleport(location, teleportCause);
    }

    @Override
    public boolean teleport(Entity entity)
    {
        return wrappedPlayer.teleport(entity);
    }

    @Override
    public boolean teleport(Entity entity, PlayerTeleportEvent.TeleportCause teleportCause)
    {
        return wrappedPlayer.teleport(entity, teleportCause);
    }

    @Override
    public List<Entity> getNearbyEntities(double v, double v1, double v2)
    {
        return wrappedPlayer.getNearbyEntities(v, v1, v2);
    }

    @Override
    public int getEntityId()
    {
        return wrappedPlayer.getEntityId();
    }

    @Override
    public int getFireTicks()
    {
        return wrappedPlayer.getFireTicks();
    }

    @Override
    public int getMaxFireTicks()
    {
        return wrappedPlayer.getMaxFireTicks();
    }

    @Override
    public void setFireTicks(int i)
    {
        wrappedPlayer.setFireTicks(i);
    }

    @Override
    public void remove()
    {
        wrappedPlayer.remove();
    }

    @Override
    public boolean isDead()
    {
        return wrappedPlayer.isDead();
    }

    @Override
    public boolean isValid()
    {
        return wrappedPlayer.isValid();
    }

    @Override
    public Server getServer()
    {
        return wrappedPlayer.getServer();
    }

    @Override
    public Entity getPassenger()
    {
        return wrappedPlayer.getPassenger();
    }

    @Override
    public boolean setPassenger(Entity entity)
    {
        return wrappedPlayer.setPassenger(entity);
    }

    @Override
    public boolean isEmpty()
    {
        return wrappedPlayer.isEmpty();
    }

    @Override
    public boolean eject()
    {
        return wrappedPlayer.eject();
    }

    @Override
    public float getFallDistance()
    {
        return wrappedPlayer.getFallDistance();
    }

    @Override
    public void setFallDistance(float v)
    {
        wrappedPlayer.setFallDistance(v);
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent entityDamageEvent)
    {
        wrappedPlayer.setLastDamageCause(entityDamageEvent);
    }

    @Override
    public EntityDamageEvent getLastDamageCause()
    {
        return wrappedPlayer.getLastDamageCause();
    }

    @Override
    public UUID getUniqueId()
    {
        return wrappedPlayer.getUniqueId();
    }

    @Override
    public int getTicksLived()
    {
        return wrappedPlayer.getTicksLived();
    }

    @Override
    public void setTicksLived(int i)
    {
        wrappedPlayer.setTicksLived(i);
    }

    @Override
    public void playEffect(EntityEffect entityEffect)
    {
        wrappedPlayer.playEffect(entityEffect);
    }

    @Override
    public EntityType getType()
    {
        return wrappedPlayer.getType();
    }

    @Override
    public boolean isInsideVehicle()
    {
        return wrappedPlayer.isInsideVehicle();
    }

    @Override
    public boolean leaveVehicle()
    {
        return wrappedPlayer.leaveVehicle();
    }

    @Override
    public Entity getVehicle()
    {
        return wrappedPlayer.getVehicle();
    }

    @Override
    public void setCustomName(String s)
    {
        wrappedPlayer.setCustomName(s);
    }

    @Override
    public String getCustomName()
    {
        return wrappedPlayer.getCustomName();
    }

    @Override
    public void setCustomNameVisible(boolean b)
    {
        wrappedPlayer.setCustomNameVisible(b);
    }

    @Override
    public boolean isCustomNameVisible()
    {
        return wrappedPlayer.isCustomNameVisible();
    }

    @Override
    public void setMetadata(String s, MetadataValue metadataValue)
    {
        wrappedPlayer.setMetadata(s, metadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String s)
    {
        return wrappedPlayer.getMetadata(s);
    }

    @Override
    public boolean hasMetadata(String s)
    {
        return wrappedPlayer.hasMetadata(s);
    }

    @Override
    public void removeMetadata(String s, Plugin plugin)
    {
        wrappedPlayer.removeMetadata(s, plugin);
    }

    @Override
    public void sendMessage(String s)
    {
        wrappedPlayer.sendMessage(s);
    }

    @Override
    public void sendMessage(String[] strings)
    {
        wrappedPlayer.sendMessage(strings);
    }

    @Override
    public boolean isPermissionSet(String s)
    {
        return wrappedPlayer.isPermissionSet(s);
    }

    @Override
    public boolean isPermissionSet(Permission permission)
    {
        return wrappedPlayer.isPermissionSet(permission);
    }

    @Override
    public boolean hasPermission(String s)
    {
        return wrappedPlayer.hasPermission(s);
    }

    @Override
    public boolean hasPermission(Permission permission)
    {
        return wrappedPlayer.hasPermission(permission);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b)
    {
        return wrappedPlayer.addAttachment(plugin, s, b);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin)
    {
        return wrappedPlayer.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b, int i)
    {
        return wrappedPlayer.addAttachment(plugin, s, b, i);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int i)
    {
        return wrappedPlayer.addAttachment(plugin, i);
    }

    @Override
    public void removeAttachment(PermissionAttachment permissionAttachment)
    {
        wrappedPlayer.removeAttachment(permissionAttachment);
    }

    @Override
    public void recalculatePermissions()
    {
        wrappedPlayer.recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions()
    {
        return wrappedPlayer.getEffectivePermissions();
    }

    @Override
    public boolean isOp()
    {
        return wrappedPlayer.isOp();
    }

    @Override
    public void setOp(boolean b)
    {
        wrappedPlayer.setOp(b);
    }

    @Override
    public void damage(double v)
    {
        wrappedPlayer.damage(v);
    }

    @Override
    public void damage(double v, Entity entity)
    {
        wrappedPlayer.damage(v, entity);
    }

    @Override
    public double getHealth()
    {
        return wrappedPlayer.getHealth();
    }

    @Override
    public void setHealth(double v)
    {
        wrappedPlayer.setHealth(v);
    }

    @Override
    public double getMaxHealth()
    {
        return wrappedPlayer.getMaxHealth();
    }

    @Override
    public void setMaxHealth(double v)
    {
        wrappedPlayer.setMaxHealth(v);
    }

    @Override
    public void resetMaxHealth()
    {
        wrappedPlayer.resetMaxHealth();
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> aClass)
    {
        return wrappedPlayer.launchProjectile(aClass);
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> aClass, Vector vector)
    {
        return wrappedPlayer.launchProjectile(aClass, vector);
    }

    @Override
    public boolean isConversing()
    {
        return wrappedPlayer.isConversing();
    }

    @Override
    public void acceptConversationInput(String s)
    {
        wrappedPlayer.acceptConversationInput(s);
    }

    @Override
    public boolean beginConversation(Conversation conversation)
    {
        return wrappedPlayer.beginConversation(conversation);
    }

    @Override
    public void abandonConversation(Conversation conversation)
    {
        wrappedPlayer.abandonConversation(conversation);
    }

    @Override
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent conversationAbandonedEvent)
    {
        wrappedPlayer.abandonConversation(conversation, conversationAbandonedEvent);
    }

    @Override
    public boolean isOnline()
    {
        return wrappedPlayer.isOnline();
    }

    @Override
    public boolean isBanned()
    {
        return wrappedPlayer.isBanned();
    }

    @Override
    @Deprecated
    public void setBanned(boolean b)
    {
        wrappedPlayer.setBanned(b);
    }

    @Override
    public boolean isWhitelisted()
    {
        return wrappedPlayer.isWhitelisted();
    }

    @Override
    public void setWhitelisted(boolean b)
    {
        wrappedPlayer.setWhitelisted(b);
    }

    @Override
    public Player getPlayer()
    {
        return wrappedPlayer.getPlayer();
    }

    @Override
    public long getFirstPlayed()
    {
        return wrappedPlayer.getFirstPlayed();
    }

    @Override
    public long getLastPlayed()
    {
        return wrappedPlayer.getLastPlayed();
    }

    @Override
    public boolean hasPlayedBefore()
    {
        return wrappedPlayer.hasPlayedBefore();
    }

    @Override
    public Map<String, Object> serialize()
    {
        return wrappedPlayer.serialize();
    }

    @Override
    public void sendPluginMessage(Plugin plugin, String s, byte[] bytes)
    {
        wrappedPlayer.sendPluginMessage(plugin, s, bytes);
    }

    @Override
    public Set<String> getListeningPluginChannels()
    {
        return wrappedPlayer.getListeningPluginChannels();
    }
}
