package com.stunslam;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class StunSlamScreen extends Screen {
    private ButtonWidget delayButton;

    public StunSlamScreen() {
        super(Text.literal("Stun Slam Config"));
    }

    private Text delayText() {
        return Text.literal("Stun Slam Delay: " + (StunSlamConfig.INSTANCE.delayTicks == 0 ? "Instant" : "10 Ticks"));
    }

    @Override
    protected void init() {
        delayButton = ButtonWidget.builder(delayText(), b -> {
            StunSlamConfig.INSTANCE.delayTicks = StunSlamConfig.INSTANCE.delayTicks == 0 ? 10 : 0;
            StunSlamConfig.save();
            b.setMessage(delayText());
        }).dimensions(width / 2 - 100, height / 2 - 10, 200, 20).build();
        addDrawableChild(delayButton);

        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), b -> close())
                .dimensions(width / 2 - 100, height / 2 + 20, 200, 20).build());
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        ctx.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 2 - 40, 0xFFFFFF);
    }
}
