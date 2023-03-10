package org.zeith.botanicadds.client.particle.lightning;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

import java.util.*;

public class BoltCore
{
	ArrayList<Segment> segments;
	ThVector3 start;
	ThVector3 end;
	HashMap splitparents;
	public float multiplier;
	public float length;
	public int numsegments0;
	public int increment;
	private int numsplits;
	private boolean finalized;
	private boolean canhittarget;
	private Random rand;
	public long seed;
	public int particleAge;
	public int particleMaxAge;
	private AABB boundingBox;
	public static final float speed = 3.0f;
	public static final int fadetime = 20;
	
	public BoltCore(final ThVector3 jammervec, final ThVector3 targetvec, final long seed)
	{
		segments = new ArrayList();
		splitparents = new HashMap();
		canhittarget = true;
		start = jammervec;
		end = targetvec;
		this.seed = seed;
		rand = new Random(seed);
		numsegments0 = 1;
		increment = 1;
		length = end.copy().sub(start).length();
		particleMaxAge = 3 + rand.nextInt(3) - 1;
		multiplier = 1.0f;
		particleAge = -(int) (length * 3.0f);
		boundingBox = new AABB(Math.min(start.x, end.x), Math.min(start.y, end.y), Math.min(start.z, end.z), Math.max(start.x, end.x), Math.max(start.y, end.y), Math.max(start.z, end.z))
				.move(length / 2.0f, length / 2.0f, length / 2.0f);
		segments.add(new Segment(start, end));
	}
	
	public BoltCore(Entity detonator, Entity target, final long seed)
	{
		this(new ThVector3(detonator), new ThVector3(target), seed);
	}
	
	public BoltCore(Entity detonator, Entity target, final long seed, final int speed)
	{
		this(new ThVector3(detonator), new ThVector3(target.getX(), target.getY() + target.getEyeHeight() - 0.699999988079071, target.getZ()), seed);
		increment = speed;
		multiplier = 0.4f;
	}
	
	public BoltCore(final double x1, final double y1, final double z1, final double x, final double y, final double z, final long seed, final int duration, final float multi, final int speed)
	{
		this(new ThVector3(x1, y1, z1), new ThVector3(x, y, z), seed);
		particleMaxAge = duration + rand.nextInt(duration) - duration / 2;
		multiplier = multi;
		increment = speed;
	}
	
	public void setMultiplier(final float m)
	{
		multiplier = m;
	}
	
	public void fractal(final int splits, final float amount, final float splitchance, final float splitlength, final float splitangle)
	{
		if(finalized)
			return;
		final ArrayList<Segment> oldsegments = segments;
		segments = new ArrayList<>();
		Segment prev = null;
		for(final Segment segment : oldsegments)
		{
			prev = segment.prev;
			final ThVector3 subsegment = segment.diff.copy().scale(1.0f / splits);
			final BoltPoint[] newpoints = new BoltPoint[splits + 1];
			final ThVector3 startpoint = segment.startpoint.point;
			newpoints[0] = segment.startpoint;
			newpoints[splits] = segment.endpoint;
			for(int i = 1; i < splits; ++i)
			{
				final ThVector3 randoff = ThVector3.getPerpendicular(segment.diff).rotate(rand.nextFloat() * 360.0f, segment.diff);
				randoff.scale((rand.nextFloat() - 0.5f) * amount);
				final ThVector3 basepoint = startpoint.copy().add(subsegment.copy().scale(i));
				newpoints[i] = new BoltPoint(basepoint, randoff);
			}
			for(int i = 0; i < splits; ++i)
			{
				final Segment next = new Segment(newpoints[i], newpoints[i + 1], segment.light, segment.segmentno * splits + i, segment.splitno);
				if((next.prev = prev) != null)
					prev.next = next;
				if(i != 0 && rand.nextFloat() < splitchance)
				{
					final ThVector3 splitrot = ThVector3.xCrossProduct(next.diff).rotate(rand.nextFloat() * 360.0f, next.diff);
					final ThVector3 diff = next.diff.copy().rotate((rand.nextFloat() * 0.66f + 0.33f) * splitangle, splitrot).scale(splitlength);
					++numsplits;
					splitparents.put(numsplits, next.splitno);
					final Segment split = new Segment(newpoints[i], new BoltPoint(newpoints[i + 1].basepoint, newpoints[i + 1].offsetvec.copy().add(diff)), segment.light / 2.0f, next.segmentno, numsplits);
					split.prev = prev;
					segments.add(split);
				}
				prev = next;
				segments.add(next);
			}
			if(segment.next != null)
				segment.next.prev = prev;
		}
		numsegments0 *= splits;
	}
	
	public void defaultFractal()
	{
		fractal(2, length * multiplier / 8.0f, 0.7f, 0.1f, 45.0f);
		fractal(2, length * multiplier / 12.0f, 0.5f, 0.1f, 50.0f);
		fractal(2, length * multiplier / 17.0f, 0.5f, 0.1f, 55.0f);
		fractal(2, length * multiplier / 23.0f, 0.5f, 0.1f, 60.0f);
		fractal(2, length * multiplier / 30.0f, 0.0f, 0.0f, 0.0f);
		fractal(2, length * multiplier / 34.0f, 0.0f, 0.0f, 0.0f);
		fractal(2, length * multiplier / 40.0f, 0.0f, 0.0f, 0.0f);
	}
	
	public void defaultFractal(int splits, float baseAngle)
	{
		fractal(splits, length * multiplier / 8.0f, 0.7f, 0.1f, baseAngle);
		fractal(splits, length * multiplier / 12.0f, 0.5f, 0.1f, baseAngle + 5);
		fractal(splits, length * multiplier / 17.0f, 0.5f, 0.1f, baseAngle + 10);
		fractal(splits, length * multiplier / 23.0f, 0.5f, 0.1f, baseAngle + 15);
		fractal(splits, length * multiplier / 30.0f, 0.0f, 0.0f, baseAngle - 45);
		fractal(splits, length * multiplier / 34.0f, 0.0f, 0.0f, baseAngle - 45);
		fractal(splits, length * multiplier / 40.0f, 0.0f, 0.0f, baseAngle - 45);
	}
	
	private void calculateCollisionAndDiffs()
	{
		final HashMap<Integer, Integer> lastactivesegment = new HashMap<>();
		segments.sort(new SegmentSorter());
		int lastsplitcalc = 0;
		int lastactiveseg = 0;
		float splitresistance = 0.0f;
		for(final Segment segment : segments)
		{
			if(segment.splitno > lastsplitcalc)
			{
				lastactivesegment.put(lastsplitcalc, lastactiveseg);
				lastsplitcalc = segment.splitno;
				lastactiveseg = lastactivesegment.get(splitparents.get(segment.splitno));
				splitresistance = ((lastactiveseg >= segment.segmentno) ? 0.0f : 50.0f);
			}
			if(splitresistance < 40.0f * segment.light)
				lastactiveseg = segment.segmentno;
		}
		lastactivesegment.put(lastsplitcalc, lastactiveseg);
		lastsplitcalc = 0;
		lastactiveseg = lastactivesegment.get(0);
		final Iterator<Segment> iterator2 = segments.iterator();
		while(iterator2.hasNext())
		{
			final Segment segment2 = iterator2.next();
			if(lastsplitcalc != segment2.splitno)
			{
				lastsplitcalc = segment2.splitno;
				lastactiveseg = lastactivesegment.get(segment2.splitno);
			}
			if(segment2.segmentno > lastactiveseg)
				iterator2.remove();
			segment2.calcEndDiffs();
		}
		if(lastactivesegment.get(0) + 1 < numsegments0)
			canhittarget = false;
	}
	
	public void finalizeBolt()
	{
		if(finalized)
			return;
		finalized = true;
		calculateCollisionAndDiffs();
		segments.sort(new SegmentLightSorter());
	}
	
	public void onUpdate()
	{
		particleAge += increment;
		if(particleAge > particleMaxAge)
			particleAge = particleMaxAge;
	}
	
	public AABB getBoundingBox()
	{
		return boundingBox;
	}
	
	public class BoltPoint
	{
		ThVector3 point;
		ThVector3 basepoint;
		ThVector3 offsetvec;
		final BoltCore this$0;
		
		public BoltPoint(final ThVector3 basepoint, final ThVector3 offsetvec)
		{
			this$0 = BoltCore.this;
			point = basepoint.copy().add(offsetvec);
			this.basepoint = basepoint;
			this.offsetvec = offsetvec;
		}
	}
	
	public class Segment
	{
		public BoltPoint startpoint;
		public BoltPoint endpoint;
		public ThVector3 diff;
		public Segment prev;
		public Segment next;
		public ThVector3 nextdiff;
		public ThVector3 prevdiff;
		public float sinprev;
		public float sinnext;
		public float light;
		public int segmentno;
		public int splitno;
		final BoltCore this$0;
		
		public void calcDiff()
		{
			diff = endpoint.point.copy().sub(startpoint.point);
		}
		
		public void calcEndDiffs()
		{
			if(prev != null)
			{
				final ThVector3 prevdiffnorm = prev.diff.copy().normalize();
				final ThVector3 thisdiffnorm = diff.copy().normalize();
				prevdiff = thisdiffnorm.add(prevdiffnorm).normalize();
				sinprev = (float) Math.sin(ThVector3.anglePreNorm(thisdiffnorm, prevdiffnorm.scale(-1.0f)) / 2.0f);
			} else
			{
				prevdiff = diff.copy().normalize();
				sinprev = 1.0f;
			}
			if(next != null)
			{
				final ThVector3 nextdiffnorm = next.diff.copy().normalize();
				final ThVector3 thisdiffnorm = diff.copy().normalize();
				nextdiff = thisdiffnorm.add(nextdiffnorm).normalize();
				sinnext = (float) Math.sin(ThVector3.anglePreNorm(thisdiffnorm, nextdiffnorm.scale(-1.0f)) / 2.0f);
			} else
			{
				nextdiff = diff.copy().normalize();
				sinnext = 1.0f;
			}
		}
		
		@Override
		public String toString()
		{
			return String.valueOf(startpoint.point.toString()) + " " + endpoint.point.toString();
		}
		
		public Segment(final BoltPoint start, final BoltPoint end, final float light, final int segmentnumber, final int splitnumber)
		{
			this$0 = BoltCore.this;
			startpoint = start;
			endpoint = end;
			this.light = light;
			segmentno = segmentnumber;
			splitno = splitnumber;
			calcDiff();
		}
		
		public Segment(final ThVector3 start, final ThVector3 end)
		{
			this(new BoltPoint(start, new ThVector3(0.0, 0.0, 0.0)), new BoltPoint(end, new ThVector3(0.0, 0.0, 0.0)), 1.0f, 0, 0);
		}
	}
	
	public class SegmentLightSorter
			implements Comparator
	{
		final BoltCore this$0;
		
		public int compare(final Segment o1, final Segment o2)
		{
			return Float.compare(o2.light, o1.light);
		}
		
		@Override
		public int compare(final Object obj, final Object obj1)
		{
			return compare((Segment) obj, (Segment) obj1);
		}
		
		public SegmentLightSorter()
		{
			this$0 = BoltCore.this;
		}
	}
	
	public class SegmentSorter
			implements Comparator
	{
		final BoltCore this$0;
		
		public int compare(final Segment o1, final Segment o2)
		{
			final int comp = Integer.valueOf(o1.splitno).compareTo(Integer.valueOf(o2.splitno));
			if(comp == 0)
			{
				return Integer.valueOf(o1.segmentno).compareTo(Integer.valueOf(o2.segmentno));
			}
			return comp;
		}
		
		@Override
		public int compare(final Object obj, final Object obj1)
		{
			return compare((Segment) obj, (Segment) obj1);
		}
		
		public SegmentSorter()
		{
			this$0 = BoltCore.this;
		}
	}
}