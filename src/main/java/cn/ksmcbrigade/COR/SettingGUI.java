package cn.ksmcbrigade.COR;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.CycleOption;
import net.minecraft.client.ProgressOption;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.io.IOException;

public class SettingGUI extends Screen {

    private OptionsList optionsList;

    public SettingGUI() {
        super(new TranslatableComponent("gui.cor.title"));
    }

    @Override
    protected void init() {

        this.optionsList = new OptionsList(
                this.minecraft, this.width, this.height,
                24,
                this.height - 32,
                25
        );

        this.optionsList.addBig(CycleOption.createOnOff("gui.cor.in_server",(p_168280_) -> {
            return Connector.manager.isIN_SERVER();
        }, (p_168282_, p_168283_, p_168284_) -> {
            Connector.manager.setIN_SERVER(p_168284_);
        }));

        this.optionsList.addBig(CycleOption.createOnOff("gui.cor.left",(p_168280_) -> {
            return Connector.manager.isLEFT();
        }, (p_168282_, p_168283_, p_168284_) -> {
            Connector.manager.setLEFT(p_168284_);
        }));

        this.optionsList.addBig(new ProgressOption("gui.cor.sleep",0.0F,5000.0F,1F,(p_168123_) -> {
            return (double) Connector.manager.getSLEEP();
        }, (p_168226_, p_168227_) -> {
            Connector.manager.setSLEEP((int) p_168227_.doubleValue());
        }, (p_168274_, p_168275_) -> {
            double d0 = p_168275_.get(p_168274_);
            return (Component)(d0 == 0.0D ? CommonComponents.optionStatus(this.title, false) : new TranslatableComponent("options.generic_value", new TranslatableComponent("gui.cor.sleep"), d0));
        }));

        this.optionsList.addBig(new ProgressOption("gui.cor.sleep_skew",0.0F,100.0F,1F,(p_168123_) -> {
            return (double) Connector.manager.getSLEEP_SKEW();
        }, (p_168226_, p_168227_) -> {
            Connector.manager.setSleepSkew((int) p_168227_.doubleValue());
        }, (p_168274_, p_168275_) -> {
            double d0 = p_168275_.get(p_168274_);
            return (Component)(d0 == 0.0D ? CommonComponents.optionStatus(this.title, false) : new TranslatableComponent("options.generic_value", new TranslatableComponent("gui.cor.sleep_skew"), d0));
        }));

        this.addWidget(this.optionsList);

        this.addRenderableWidget(new Button((this.width - 200) / 2,this.height - 25,200,20, CommonComponents.GUI_DONE,(p_96057_) -> {
            this.onClose();
        }));
        super.init();
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks){
        this.renderBackground(poseStack);
        this.optionsList.render(poseStack,mouseX,mouseY,partialTicks);
        drawCenteredString(poseStack, font,this.title.getString(),this.width / 2, 8, 16777215);
        super.render(poseStack,mouseX,mouseY,partialTicks);
    }

    @Override
    public void onClose() {
        try {
            Connector.manager.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.onClose();
    }
}
