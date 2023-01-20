package org.zeith.botanicadds.particle;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import org.zeith.botanicadds.client.particle.lightning.Bolt;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public class BoltParticleType
		extends ParticleType<Bolt>
{
	@RegistryName("bolt")
	public static final BoltParticleType TYPE = new BoltParticleType(true);
	
	public BoltParticleType(boolean overrideLimiter)
	{
		super(overrideLimiter, Bolt.DESERIALIZER);
	}
	
	@Override
	public Codec<Bolt> codec()
	{
		return Bolt.CODEC;
	}
}