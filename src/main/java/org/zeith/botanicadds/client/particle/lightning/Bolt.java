package org.zeith.botanicadds.client.particle.lightning;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import org.zeith.botanicadds.particle.BoltParticleType;

public record Bolt(long seed, int age, float angleMult, int speed, Fractal fractal, Layer inner, Layer outer)
		implements ParticleOptions
{
	public static final Codec<Bolt> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					Codec.LONG.fieldOf("seed").forGetter(Bolt::seed),
					Codec.INT.fieldOf("age").forGetter(Bolt::age),
					Codec.FLOAT.fieldOf("angle").forGetter(Bolt::angleMult),
					Codec.INT.fieldOf("speed").forGetter(Bolt::speed),
					Fractal.CODEC.fieldOf("fractal").forGetter(Bolt::fractal),
					Layer.CODEC.fieldOf("inner").forGetter(Bolt::inner),
					Layer.CODEC.fieldOf("outer").forGetter(Bolt::outer)
			).apply(instance, Bolt::new)
	);
	
	public static final ParticleOptions.Deserializer<Bolt> DESERIALIZER = new Deserializer<Bolt>()
	{
		@Override
		public Bolt fromCommand(ParticleType<Bolt> p_123733_, StringReader p_123734_) throws CommandSyntaxException
		{
			var msg = Component.literal("Bolt particle deserialization is not supported.");
			throw new CommandSyntaxException(new SimpleCommandExceptionType(msg), msg);
		}
		
		@Override
		public Bolt fromNetwork(ParticleType<Bolt> type, FriendlyByteBuf buf)
		{
			return new Bolt(buf.readLong(), buf.readInt(), buf.readFloat(), buf.readInt(), Fractal.fromNetwork(buf), Layer.fromNetwork(buf), Layer.fromNetwork(buf));
		}
	};
	
	@Override
	public void writeToNetwork(FriendlyByteBuf buf)
	{
		buf.writeLong(seed);
		buf.writeInt(age);
		buf.writeFloat(angleMult);
		buf.writeInt(speed);
		fractal.toNetwork(buf);
		inner.toNetwork(buf);
		outer.toNetwork(buf);
	}
	
	@Override
	public ParticleType<?> getType()
	{
		return BoltParticleType.TYPE;
	}
	
	@Override
	public String writeToString()
	{
		return "";
	}
	
	public record Layer(int blendFunc, int color, boolean active)
	{
		public static final Codec<Layer> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(
						Codec.INT.fieldOf("func").forGetter(Layer::blendFunc),
						Codec.INT.fieldOf("color").forGetter(Layer::color),
						Codec.BOOL.fieldOf("active").forGetter(Layer::active)
				).apply(instance, Layer::new)
		);
		
		private static long counter = 0L;
		
		public Layer
		{
			++counter;
		}
		
		public Layer(boolean active)
		{
			this(771, counter % 2 == 1 ? 0xFF00FF : 0x0, active);
		}
		
		public Layer()
		{
			this(true);
		}
		
		public static Layer fromNetwork(FriendlyByteBuf buf)
		{
			return new Layer(buf.readInt(), buf.readInt(), buf.readBoolean());
		}
		
		public void toNetwork(FriendlyByteBuf buf)
		{
			buf.writeInt(blendFunc);
			buf.writeFloat(color);
			buf.writeBoolean(active);
		}
	}
	
	public record Fractal(int splits, float baseAngle)
	{
		public static final Codec<Fractal> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(
						Codec.INT.fieldOf("splits").forGetter(Fractal::splits),
						Codec.FLOAT.fieldOf("angle").forGetter(Fractal::baseAngle)
				).apply(instance, Fractal::new)
		);
		
		public static final Fractal DEFAULT_FRACTAL = new Fractal();
		
		public Fractal()
		{
			this(2, 45F);
		}
		
		public static Fractal fromNetwork(FriendlyByteBuf buf)
		{
			return new Fractal(buf.readInt(), buf.readFloat());
		}
		
		public void toNetwork(FriendlyByteBuf buf)
		{
			buf.writeInt(splits);
			buf.writeFloat(baseAngle);
		}
		
		public void apply(BoltCore core)
		{
			core.defaultFractal(splits, baseAngle);
		}
	}
}