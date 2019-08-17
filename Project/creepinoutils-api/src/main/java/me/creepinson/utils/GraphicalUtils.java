package me.creepinson.utils;

/**
 * Made By Creepinson
 */
public class GraphicalUtils {
    public static enum AnvilSlot {
        INPUT_LEFT(0), INPUT_RIGHT(1), OUTPUT(2);

        private int slot;

        private AnvilSlot(int slot) {
            this.slot = slot;
        }

        public static AnvilSlot bySlot(int slot) {
            for (AnvilSlot anvilSlot : values()) {
                if (anvilSlot.getSlot() == slot) {
                    return anvilSlot;
                }
            }

            return null;
        }

        public int getSlot() {
            return slot;
        }
    }

    public static interface AnvilClickEventHandler {
        void onAnvilClick(AnvilClickEvent event);
    }

    public static class AnvilClickEvent {
        private AnvilSlot slot;

        private String name;

        private String renameText;

        private boolean close = false;
        private boolean destroy = false;

        public boolean isCanceled;

        public AnvilClickEvent(AnvilSlot slot, String name, String renameText) {
            this.slot = slot;
            this.name = name;
            this.renameText = renameText;
        }

        public String getRenameText() {
            return renameText;
        }

        public AnvilSlot getSlot() {
            return slot;
        }

        public String getName() {
            return name;
        }

        public boolean getWillClose() {
            return close;
        }

        public void setWillClose(boolean close) {
            this.close = close;
        }

        public boolean getWillDestroy() {
            return destroy;
        }

        public void setWillDestroy(boolean destroy) {
            this.destroy = destroy;
        }
    }
}
