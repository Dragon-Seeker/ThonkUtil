package com.jab125.thonkutil.mixin;

import com.jab125.thonkutil.api.CapeItem;
import net.minecraft.resource.ResourceManager;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagGroupLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(TagGroupLoader.class)
public class TagGroupLoaderMixin {

    @Shadow @Final private String dataType;

    @Inject(method = "loadTags", at = @At("RETURN"), cancellable = true)
    private void loadTagInject(ResourceManager manager, CallbackInfoReturnable<Map<Identifier, Tag.Builder>> cir) {
        Tag.Builder a = Tag.Builder.create();
        Registry.ITEM.forEach((item -> {
            if (item instanceof CapeItem) a.add(Registry.ITEM.getId(item), "thonkutil:migration_cape");
        }));
        Map<Identifier, Tag.Builder> b = cir.getReturnValue();
        b.put(Identifier.tryParse("trinkets:chest/cape"), a);
        cir.setReturnValue(b);
        cir.getReturnValue().forEach(((identifier, builder) -> {
            System.out.println(identifier.toString() + ", " +  builder.toJson().toString() + ", " + this.dataType);
        }));
    }
}
