package com.stunslam;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class StunSlamScreen extends Screen {

    public StunSlamScreen() {
        super(Text.literal("Stun Slam Config"));
    }

    private Text delayText() {
        int d = StunSlamConfig.INSTANCE.delayTicks;
        String label = d == 0 ? "Instant" : d + (d == 1 ? " Tick" : " Ticks");
        return Text.literal("Stun Slam Delay: " + label + " (click to change)");
    }

    @Override
    protected void init() {
        addDrawableChild(ButtonWidget.builder(delayText(), b -> {
            StunSlamConfig.INSTANCE.delayTicks = (StunSlamConfig.INSTANCE.delayTicks + 1) % 11;
            StunSlamConfig.save();
            b.setMessage(delayText());
        }).dimensions(width / 2 - 125, height / 2 - 10, 250, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), b -> close())
                .dimensions(width / 2 - 100, height / 2 + 20, 200, 20).build());
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        ctx.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 2 - 40, 0xFFFFFF);
    }
}
