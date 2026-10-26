package com.mrbysco.forcecraft.item.tool;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;

public record EnderData(GlobalPos globalPos, String name) {
	public static final Codec<EnderData> MAP_CODEC = RecordCodecBuilder.create(
			i -> i.group(
							GlobalPos.CODEC.fieldOf("globalPos").forGetter(o -> o.globalPos),
							Codec.STRING.fieldOf("name").forGetter(o -> o.name)
					)
					.apply(i, EnderData::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, EnderData> STREAM_CODEC = StreamCodec.composite(
			GlobalPos.STREAM_CODEC,
			o -> o.globalPos,
			ByteBufCodecs.STRING_UTF8,
			o -> o.name,
			EnderData::new
	);

	public static EnderData getDefault() {
		return new EnderData(GlobalPos.of(Level.OVERWORLD, BlockPos.ZERO), "");
	}

	public double x() {
		return this.globalPos.pos().getX();
	}

	public double y() {
		return this.globalPos.pos().getY();
	}

	public double z() {
		return this.globalPos.pos().getZ();
	}
}
