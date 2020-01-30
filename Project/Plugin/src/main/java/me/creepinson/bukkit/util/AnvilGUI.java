package me.creepinson.bukkit.util;

import me.creepinson.api.nms.VersionWrapper;
import me.creepinson.bukkit.core.VersionMatcher;
import me.creepinson.util.GraphicalUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * An anvil gui, used for gathering a user's input
 *
 * @author Wesley Smith
 * @since 1.0
 */
public class AnvilGUI {

    /**
     * The local {@link VersionWrapper} object for the server's version
     */
    private static VersionWrapper WRAPPER = new VersionMatcher().match();

    /**
     * The {@link Plugin} that this anvil GUI is associated with
     */
    private final Plugin plugin;
    /**
     * The player who has the GUI open
     */
    private final Player player;

    private final GraphicalUtils.AnvilClickEventHandler clickHandler;
    private boolean shouldGiveOutputItem = true;
    private boolean shouldCloseOnComplete;

    /**
     * The text that will be displayed to the user
     */
    private String text = "";
    /**
     * A state that decides where the anvil GUI is able to be closed by the user
     */
    private final boolean preventClose;
    /**
     * An {@link Consumer} that is called when the anvil GUI is closed
     */
    private final Consumer<Player> closeListener;
    /**
     * An {@link BiFunction} that is called when the {@link me.creepinson.util.GraphicalUtils.AnvilSlot#OUTPUT} slot has been clicked
     * Return null in the bifunction for no response. Otherwise return {@link AnvilGUI.Response}
     * the second parameter of the bifunction is the clicked item name.
     */
    private final BiFunction<Player, String, Response> completeFunction;

    /**
     * The ItemStack that is in the {@link me.creepinson.util.GraphicalUtils.AnvilSlot#INPUT_LEFT} slot.
     */
    private ItemStack insert;
    /**
     * The container id of the inventory, used for NMS methods
     */
    private int containerId;
    /**
     * The inventory that is used on the Bukkit side of things
     */
    protected Inventory inventory;
    /**
     * The listener holder class
     */
    private final ListenUp listener = new ListenUp();

    /**
     * Represents the state of the inventory being open
     */
    private boolean open;


    /**
     * Create an AnvilGUI and open it for the player.
     *
     * @param plugin     A {@link org.bukkit.plugin.java.JavaPlugin} instance
     * @param holder     The {@link Player} to open the inventory for
     * @param insert     What to have the text already set to
     * @param biFunction A {@link BiFunction} that is called when the player clicks the {@link me.creepinson.util.GraphicalUtils.AnvilSlot#OUTPUT} slot
     * @throws NullPointerException If the server version isn't supported
     * @deprecated As of version 1.2.3, use {@link Builder}
     */
    @Deprecated
    public AnvilGUI(Plugin plugin, Player holder, String insert, BiFunction<Player, String, String> biFunction, GraphicalUtils.AnvilClickEventHandler clickEventHandler, boolean shouldGiveOutputItem, boolean shouldCloseOnComplete) {
        this(plugin, holder, insert, false, null, clickEventHandler, shouldGiveOutputItem, shouldCloseOnComplete, (player, text) -> {
            String response = biFunction.apply(player, text);
            if (response != null) {
                return Response.text(response);
            } else {
                return Response.close();
            }
        });
    }

    /**
     * Create an AnvilGUI and open it for the player.
     *
     * @param plugin           A {@link org.bukkit.plugin.java.JavaPlugin} instance
     * @param player           The {@link Player} to open the inventory for
     * @param text             What to have the text already set to
     * @param preventClose     Whether to prevent the inventory from closing
     * @param closeListener    A {@link Consumer} when the inventory closes
     * @param completeFunction A {@link BiFunction} that is called when the player clicks the {@link me.creepinson.util.GraphicalUtils.AnvilSlot#OUTPUT} slot
     */
    private AnvilGUI(
            Plugin plugin,
            Player player,
            String text,
            boolean preventClose,
            Consumer<Player> closeListener,
            GraphicalUtils.AnvilClickEventHandler clickEventHandler,
            boolean shouldGiveOutputItem,
            boolean shouldCloseOnComplete,
            BiFunction<Player, String, Response> completeFunction
    ) {
        this.plugin = plugin;
        this.player = player;
        this.text = text;
        this.preventClose = preventClose;
        this.closeListener = closeListener;
        this.completeFunction = completeFunction;
        this.clickHandler = clickEventHandler;
        this.shouldGiveOutputItem = shouldGiveOutputItem;
        this.shouldCloseOnComplete = shouldCloseOnComplete;
        openInventory();
    }

    /**
     * Opens the anvil GUI
     */
    private void openInventory() {
        final ItemStack paper = new ItemStack(Material.PAPER);
        final ItemMeta paperMeta = paper.getItemMeta();
        paperMeta.setDisplayName(text);
        paper.setItemMeta(paperMeta);
        //this.insert = paper;

        WRAPPER.handleInventoryCloseEvent(player);
        WRAPPER.setActiveContainerDefault(player);

        Bukkit.getPluginManager().registerEvents(listener, plugin);

        final Object container = WRAPPER.newContainerAnvil(player);

        inventory = WRAPPER.toBukkitInventory(container);
        //inventory.setItem(GraphicalUtils.AnvilSlot.INPUT_LEFT.getSlot(), this.insert);

        containerId = WRAPPER.getNextContainerId(player);
        WRAPPER.sendPacketOpenWindow(player, containerId);
        WRAPPER.setActiveContainer(player, container);
        WRAPPER.setActiveContainerId(container, containerId);
        WRAPPER.addActiveContainerSlotListener(container, player);
        open = true;
    }

    /**
     * Closes the inventory if it's open.
     *
     * @throws IllegalArgumentException If the inventory isn't open
     */
    public void closeInventory() {
        Validate.isTrue(open, "You can't close an inventory that isn't open!");
        open = false;

        WRAPPER.handleInventoryCloseEvent(player);
        WRAPPER.setActiveContainerDefault(player);
        WRAPPER.sendPacketCloseWindow(player, containerId);

        HandlerList.unregisterAll(listener);

        if (closeListener != null) {
            closeListener.accept(player);
        }
    }

    /**
     * Returns the Bukkit inventory for this anvil gui
     *
     * @return the {@link Inventory} for this anvil gui
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Simply holds the listeners for the GUI
     */
    private class ListenUp implements Listener {

        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            if (event.getInventory().equals(inventory) && event.getRawSlot() < 3) {
                final Player clicker = (Player) event.getWhoClicked();
                GraphicalUtils.AnvilClickEvent clickEvent = null;
                try {
                    String renameText = (String) inventory.getClass().getMethod("getRenameText").invoke(inventory);
                    clickEvent = new GraphicalUtils.AnvilClickEvent(GraphicalUtils.AnvilSlot.bySlot(event.getRawSlot()), "Repair & Rename", renameText);
                    clickHandler.onAnvilClick(clickEvent);
                    event.setCancelled(clickEvent.isCanceled);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (event.getRawSlot() == GraphicalUtils.AnvilSlot.OUTPUT.getSlot() && completeFunction != null) {
                    final ItemStack clicked = inventory.getItem(GraphicalUtils.AnvilSlot.OUTPUT.getSlot());
                    if (clicked == null || clicked.getType() == Material.AIR) return;

                    final Response response = completeFunction.apply(clicker, clicked.hasItemMeta() ? clicked.getItemMeta().getDisplayName() : "");
                    if (response != null && response.getText() != null) {
                        final ItemMeta meta = clicked.getItemMeta();
                        assert meta != null;
                        meta.setDisplayName(response.getText());
                        clicked.setItemMeta(meta);
                        inventory.setItem(GraphicalUtils.AnvilSlot.INPUT_LEFT.getSlot(), clicked);
                    } else {
                        if (shouldGiveOutputItem) {
                            clicker.getInventory().addItem(inventory.getItem(GraphicalUtils.AnvilSlot.OUTPUT.getSlot()));
                            inventory.clear();
                        }

                        if (shouldCloseOnComplete) {
                            closeInventory();
                        }
                    }
                }
            }
        }

        @EventHandler
        public void onInventoryClose(InventoryCloseEvent event) {
            if (open && event.getInventory().equals(inventory)) {
                closeInventory();
                if (preventClose) {
                    Bukkit.getScheduler().runTask(plugin, AnvilGUI.this::openInventory);
                }
            }
        }

    }

    /**
     * A builder class for an {@link AnvilGUI} object
     */
    public static class Builder {

        /**
         * An {@link Consumer} that is called when the anvil GUI is closed
         */
        private Consumer<Player> closeListener;
        /**
         * A state that decides where the anvil GUI is able to be closed by the user
         */
        private boolean preventClose = false;
        /**
         * An {@link BiFunction} that is called when the anvil output slot has been clicked
         */
        private BiFunction<Player, String, Response> completeFunction;
        /**
         * The {@link Plugin} that this anvil GUI is associated with
         */
        private Plugin plugin;
        /**
         * The text that will be displayed to the user
         */
        private String text = "";

        //TODO: add multiple click handlers
        private GraphicalUtils.AnvilClickEventHandler clickEventHandler;
        private boolean shouldGiveOutputItem = true;
        private boolean shoudCloseOnComplete;

        /**
         * Prevents the closing of the anvil GUI by the user
         *
         * @return The {@link Builder} instance
         */
        public Builder preventClose() {
            preventClose = true;
            return this;
        }

        /**
         * Sets whether the gui should give the output slot item back to the player when it is clicked.
         * Also sets whether the gui should close when clicked
         *
         * @return The {@link Builder} instance
         */
        public Builder outputOptions(boolean shouldGiveOutputItem, boolean shouldCloseOnComplete) {
            this.shouldGiveOutputItem = shouldGiveOutputItem;
            this.shoudCloseOnComplete = shouldCloseOnComplete;
            return this;
        }


        /**
         * Listens for when the inventory is closed
         *
         * @param closeListener An {@link Consumer} that is called when the anvil GUI is closed
         * @return The {@link Builder} instance
         * @throws IllegalArgumentException when the closeListener is null
         */
        public Builder onClose(Consumer<Player> closeListener) {
            Validate.notNull(closeListener, "closeListener cannot be null");
            this.closeListener = closeListener;
            return this;
        }

        /**
         * Handles the inventory output slot when it is clicked
         * Return null in the BiFunction for no response. Otherwise return {@link AnvilGUI.Response}.
         * The second parameter of the BiFunction is the clicked item name.
         *
         * @param completeFunction An {@link BiFunction} that is called when the user clicks the output slot
         * @return The {@link Builder} instance
         */
        public Builder onComplete(BiFunction<Player, String, Response> completeFunction) {
            this.completeFunction = completeFunction;
            return this;
        }

        /**
         * Sets the plugin for the {@link AnvilGUI}
         *
         * @param plugin The {@link Plugin} this anvil GUI is associated with
         * @return The {@link Builder} instance
         * @throws IllegalArgumentException if the plugin is null
         */
        public Builder plugin(Plugin plugin) {
            Validate.notNull(plugin, "Plugin cannot be null");
            this.plugin = plugin;
            return this;
        }

        /**
         * Sets the text that is to be displayed to the user
         *
         * @param text The text that is to be displayed to the user
         * @return The {@link Builder} instance
         * @throws IllegalArgumentException if the text is null
         */
        public Builder text(String text) {
            Validate.notNull(text, "Text cannot be null");
            this.text = text;
            return this;
        }

        public Builder addClickHandler(GraphicalUtils.AnvilClickEventHandler handler) {
            Validate.notNull(handler, "Click handler cannot be null");
            this.clickEventHandler = handler;
            return this;
        }

        /**
         * Creates the anvil GUI and opens it for the player
         *
         * @param player The {@link Player} the anvil GUI should open for
         * @return The {@link AnvilGUI} instance from this builder
         * @throws IllegalArgumentException when the onComplete function, plugin, or player is null
         */
        public AnvilGUI open(Player player) {
            Validate.notNull(plugin, "Plugin cannot be null");
            Validate.notNull(completeFunction, "Complete function cannot be null");
            Validate.notNull(player, "Player cannot be null");
            Validate.notNull(clickEventHandler, "Click handler cannot be null");
            return new AnvilGUI(plugin, player, text, preventClose, closeListener, clickEventHandler, shouldGiveOutputItem, shoudCloseOnComplete, completeFunction);
        }

    }

    /**
     * Represents a response when the player clicks the output item in the anvil GUI
     */
    public static class Response {

        /**
         * The text that is to be displayed to the user
         */
        private final String text;

        /**
         * Creates a response to the user's input
         *
         * @param text The text that is to be displayed to the user, which can be null to close the inventory
         */
        private Response(String text) {
            this.text = text;
        }

        /**
         * Gets the text that is to be displayed to the user
         *
         * @return The text that is to be displayed to the user
         */
        public String getText() {
            return text;
        }

        /**
         * Returns an {@link Response} object for when the anvil GUI is to close
         *
         * @return An {@link Response} object for when the anvil GUI is to close
         */
        public static Response close() {
            return new Response(null);
        }

        /**
         * Returns an {@link Response} object for when the anvil GUI is to display text to the user
         *
         * @param text The text that is to be displayed to the user
         * @return An {@link Response} object for when the anvil GUI is to display text to the user
         */
        public static Response text(String text) {
            return new Response(text);
        }

    }

}
